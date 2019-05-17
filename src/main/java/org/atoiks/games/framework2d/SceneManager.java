package org.atoiks.games.framework2d;

import java.util.Map;
import java.util.HashMap;

public final class SceneManager {

    public static final String LOADER_SCENE_ID = null;

    private final Map<String, Object> res = new HashMap<>();
    private final Map<String, GameSceneHolder> scenes = new HashMap<>();

    private Scene loader;
    private boolean skipCycle;

    private String currentId;
    private GameSceneHolder currentGameScene;

    /* package */ IFrame frame;

    public SceneManager(FrameInfo info) {
        this.currentId = LOADER_SCENE_ID;
        this.skipCycle = false;
        this.res.putAll(info.res);

        this.loader = info.getLoader();
        if (loader != null) loader.attachSceneManager(this);

        for (final GameScene gs : info.getGameScenes()) {
            loadGameScene(gs, false);
        }
    }

    /**
     * Loads in a game scene. If the scene being loaded has the same ID as the
     * current scene, then the current one will be exited then unloaded first.
     * Afterwards, the scene being loaded will be initialized and entered.
     *
     * @param gs - The new game scene being loaded
     * @param replaceOld - Will replace previously loaded scene with same ID
     *                     if true
     */
    public void loadGameScene(GameScene gs, boolean replaceOld) {
        gs.attachSceneManager(this);

        final GameSceneHolder old = scenes.get(gs.id);
        if (old != null) {
            if (!replaceOld) {
                throw new RuntimeException("Duplicate scene ID of " + gs.id);
            }

            final boolean replCurrent = old == currentGameScene;

            // the old game scene will be replaced,
            // call leave if needed then call deinit
            if (replCurrent) {
                old.callLeave();
            }
            old.tryDeinit();

            // swap the old game scene with the new one
            old.gs = gs;

            if (replCurrent) {
                // then call enter
                old.callEnter(gs.id);
            }
        } else {
            scenes.put(gs.id, new GameSceneHolder(gs));
        }
    }

    public boolean switchToScene(final String id) {
        if (currentId == LOADER_SCENE_ID) {
            loader.leave();
        } else {
            currentGameScene.callLeave();
        }

        if ((currentGameScene = scenes.get(id)) != null) {
            currentGameScene.callEnter(currentId);
            currentId = id;
            return (skipCycle = true);
        }

        return false;
    }

    public boolean restartCurrentScene() {
        return switchToScene(currentId);
    }

    public String getCurrentSceneId() {
        return currentId;
    }

    public Scene getCurrentScene() {
        if (currentGameScene == null) {
            return currentId == null ? loader : null;
        }
        return currentGameScene.gs;
    }

    public boolean shouldSkipCycle() {
        if (skipCycle) {
            // No more cycles to skip
            skipCycle = false;
            return true;
        }
        return false;
    }

    public void renderCurrentScene(final IGraphics g) {
        if (currentId == LOADER_SCENE_ID) {
            loader.render(g);
            return;
        }

        if (currentGameScene != null) {
            currentGameScene.callRender(g);
        }
    }

    public void resizeCurrentScene(final int x, final int y) {
        if (currentId == LOADER_SCENE_ID) {
            loader.resize(x, y);
            return;
        }

        if (currentGameScene != null) {
            currentGameScene.callResize(x, y);
        }
    }

    public boolean updateCurrentScene(final float dt) {
        if (currentId == LOADER_SCENE_ID) {
            return loader.update(dt);
        }

        if (currentGameScene != null) {
            return currentGameScene.callUpdate(dt);
        }

        // Even though there is no frame, the app is still running
        return true;
    }

    public IFrame frame() {
        return frame;
    }

    public Map<String, Object> resources() {
        return res;
    }

    /* package */ void callGameSceneDeinit() {
        scenes.forEach((k, v) -> v.tryDeinit());
        scenes.clear();
    }

    /* package */ void callLoaderInit() {
        loader.init();
    }

    /* package */ void callLoaderDeinit() {
        loader.deinit();
    }
}
