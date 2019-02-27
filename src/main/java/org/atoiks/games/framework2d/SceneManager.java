package org.atoiks.games.framework2d;

import java.util.Map;
import java.util.HashMap;

public final class SceneManager<G> {

    public static final int LOADER_SCENE_ID = -1;
    public static final int UNKNOWN_SCENE_ID = -2;

    private final Map<String, Object> res = new HashMap<>();

    private Scene loader;
    private GameScene[] scenes;
    private int sceneId;
    private boolean skipCycle;

    /* package */ IFrame frame;

    public SceneManager(FrameInfo info) {
        this.loader = info.getLoader();
        this.scenes = info.getGameScenes();
        this.sceneId = LOADER_SCENE_ID;
        this.skipCycle = false;
        this.res.putAll(info.res);

        if (loader != null) loader.attachSceneManager(this);
        for (final Scene s : scenes) {
            s.attachSceneManager(this);
        }
    }

    public boolean switchToScene(final int id) {
        if (sceneId == LOADER_SCENE_ID) {
            // leave the loader and trigger init of game scenes
            this.loader.leave();
            for (final GameScene s : scenes) {
                s.init();
            }
        } else if (sceneId >= 0 && sceneId < scenes.length) {
            scenes[sceneId].leave();
        }

        if (id >= 0 && id < scenes.length) {
            scenes[id].enter(sceneId);
            sceneId = id;
            skipCycle = true;
            return true;
        }

        return false;
    }

    public boolean gotoNextScene() {
        return switchToScene(sceneId + 1);
    }

    public boolean restartCurrentScene() {
        return switchToScene(sceneId);
    }

    public int getCurrentSceneId() {
        return sceneId;
    }

    public boolean shouldSkipCycle() {
        if (skipCycle) {
            // No more cycles to skip
            skipCycle = false;
            return true;
        }
        return false;
    }

    public void renderCurrentScene(final IGraphics<? extends G> g) {
        if (sceneId == LOADER_SCENE_ID) {
            loader.render(g);
            return;
        }

        if (sceneId >= 0 && sceneId < scenes.length) {
            this.scenes[sceneId].render(g);
        }
    }

    public void resizeCurrentScene(final int x, final int y) {
        if (sceneId == LOADER_SCENE_ID) {
            loader.resize(x, y);
            return;
        }

        if (sceneId >= 0 && sceneId < scenes.length) {
            this.scenes[sceneId].resize(x, y);
        }
    }

    public boolean updateCurrentScene(final float dt) {
        if (sceneId == LOADER_SCENE_ID) {
            return loader.update(dt);
        }

        if (sceneId >= 0 && sceneId < scenes.length) {
            return this.scenes[sceneId].update(dt);
        }
        // Even though there is no frame, the app is still running
        return true;
    }

    public IFrame frame() {
        return frame;
    }

    public Map<String, ? extends Object> resources() {
        return res;
    }

    /* package */ void callGameSceneDeinit() {
        for (final GameScene s : scenes) {
            s.deinit();
        }
    }

    /* package */ void callLoaderInit() {
        loader.init();
    }

    /* package */ void callLoaderDeinit() {
        loader.deinit();
    }
}
