package org.atoiks.games.framework2d;

import java.util.Map;
import java.util.HashMap;
import java.awt.Graphics;

public final class SceneManager {

    private final Map<String, Object> res = new HashMap<>();
    private final Keyboard kbHandle;
    private final Mouse mouseHandle;

    private Scene[] scenes;
    private int sceneId;
    private boolean skipCycle;

    public SceneManager(Scene... scenes) {
        this(new Keyboard(), new Mouse(), scenes);
    }

    public SceneManager(Keyboard kb, Mouse m, Scene... scenes) {
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

    public void renderCurrentScene(final Graphics g) {
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

    public Keyboard keyboard() {
        return kbHandle;
    }

    public Mouse mouse() {
        return mouseHandle;
    }

    public Map<String, Object> resources() {
        return res;
    }
}