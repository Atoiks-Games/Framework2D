package org.atoiks.games.framework2d;

import java.util.Map;
import java.util.HashMap;

public final class SceneManager<K, M, G> {

    private final Map<String, ? extends Object> res = new HashMap<>();

    private Scene[] scenes;
    private int sceneId;
    private boolean skipCycle;

    private final IKeyboard<K> kbHandle;
    private final IMouse<M> mouseHandle;

    public SceneManager(IKeyboard<K> kb, IMouse<M> m, Scene... scenes) {
        this.kbHandle = kb;
        this.mouseHandle = m;
        this.scenes = scenes;
        this.sceneId = -1;
        this.skipCycle = false;

        for (final Scene s : scenes) {
            s.attachSceneManager(this);
        }
    }

    public void switchToScene(final int id) {
        if (sceneId >= 0 && sceneId < scenes.length) scenes[sceneId].leave();
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
        if (sceneId >= 0 && sceneId < scenes.length) {
            this.scenes[sceneId].render(g);
        }
    }

    public void resizeCurrentScene(final int x, final int y) {
        if (sceneId >= 0 && sceneId < scenes.length) {
            this.scenes[sceneId].resize(x, y);
        }
    }

    public boolean updateCurrentScene(final float dt) {
        if (sceneId >= 0 && sceneId < scenes.length) {
            return this.scenes[sceneId].update(dt);
        }
        // Even though there is no frame, the app is still running
        return true;
    }

    public void resetInputDevices() {
        kbHandle.reset();
        mouseHandle.reset();
    }

    public IKeyboard<? extends K> keyboard() {
        return kbHandle;
    }

    public IMouse<? extends M> mouse() {
        return mouseHandle;
    }

    public Map<String, ? extends Object> resources() {
        return res;
    }
}