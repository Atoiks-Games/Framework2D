package org.atoiks.games.framework2d;

import java.util.Map;
import java.util.HashMap;

public final class SceneManager<G> {

    public static final int LOADER_SCENE_ID = -1;

    private final Map<String, Object> res = new HashMap<>();

    private Scene loader;
    private GameScene[] scenes;
    private int sceneId;
    private boolean skipCycle;

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

    public void switchToScene(final int id) {
        if (sceneId == LOADER_SCENE_ID) {
            // loaders do not have a leave method
            // but doing so will trigger init
            for (GameScene s : scenes) s.init();
            // In addition, we will detach the loader scene
            // in case someone decides to do switchToScene(LOADER_SCENE)
            // (we would not want the loader to reload data)
            this.loader = null;
        } else if (sceneId >= 0 && sceneId < scenes.length) {
            scenes[sceneId].leave();
        }

        if (id >= 0 && id < scenes.length) scenes[id].enter(sceneId);
        sceneId = id;
        skipCycle = true;
    }

    public void gotoNextScene() {
        switchToScene(sceneId + 1);
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
        if (sceneId == LOADER_SCENE_ID && loader != null) {
            loader.render(g);
            return;
        }

        if (sceneId >= 0 && sceneId < scenes.length) {
            this.scenes[sceneId].render(g);
        }
    }

    public void resizeCurrentScene(final int x, final int y) {
        if (sceneId == LOADER_SCENE_ID && loader != null) {
            loader.resize(x, y);
            return;
        }

        if (sceneId >= 0 && sceneId < scenes.length) {
            this.scenes[sceneId].resize(x, y);
        }
    }

    public boolean updateCurrentScene(final float dt) {
        if (sceneId == LOADER_SCENE_ID && loader != null) {
            return loader.update(dt);
        }

        if (sceneId >= 0 && sceneId < scenes.length) {
            return this.scenes[sceneId].update(dt);
        }
        // Even though there is no frame, the app is still running
        return true;
    }

    public Map<String, ? extends Object> resources() {
        return res;
    }

    /* package */ void callDeinit() {
        for (GameScene s : scenes) {
            s.deinit();
        }
    }
}
