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

    public void loadGameScene(GameScene gs, boolean replaceOld) {
        final GameSceneHolder old = scenes.get(gs.id);
        if (old != null) {
            if (!replaceOld) {
                throw new RuntimeException("Duplicate scene ID of " + gs.id);
            }

            // the old game scene will be replaced, have to leave and deinit!
            old.callLeave();
            old.tryDeinit();
        }

        scenes.put(gs.id, new GameSceneHolder(gs));
        gs.attachSceneManager(this);
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
