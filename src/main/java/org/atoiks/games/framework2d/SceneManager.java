package org.atoiks.games.framework2d;

import java.util.Map;
import java.util.HashMap;

public final class SceneManager {

    private final Map<String, Object> res = new HashMap<>();
    private final Map<String, SceneHolder> scenes = new HashMap<>();

    private boolean skipCycle;

    private String currentId;
    private SceneHolder currentScene;

    /* package */ IFrame frame;

    public SceneManager(FrameInfo info) {
        this.skipCycle = false;
        this.res.putAll(info.res);

        for (final Scene scene : info.getScenes()) {
            loadScene(scene, false);
        }

        switchToScene(info.getFirstScene());
    }

    /**
     * If the scene being loaded has the same ID as the current scene, then
     * the current one will be exited then unloaded first. Afterwards, the
     * scene being loaded will be initialized and entered.
     *
     * @param scene - The new scene being loaded
     * @param replaceOld - Will replace previously loaded scene with same ID
     *                     if true
     */
    public void loadScene(Scene scene, boolean replaceOld) {
        scene.attachSceneManager(this);

        final String id = scene.getId();
        final SceneHolder old = scenes.get(id);
        if (old != null) {
            if (!replaceOld) {
                throw new RuntimeException("Duplicate scene ID of " + id);
            }

            final boolean replCurrent = old == currentScene;

            // the old game scene will be replaced,
            // call leave if needed then call deinit
            if (replCurrent) {
                old.callLeave();
            }
            old.tryDeinit();

            // swap the old game scene with the new one
            old.scene = scene;

            if (replCurrent) {
                // then call enter
                old.callEnter(id);
            }
        } else {
            scenes.put(id, new SceneHolder(scene));
        }
    }

    /**
     * Deinitializes and removes the scene from the list of scenes
     *
     * will crash if you try to unload current scene
     *
     * @param id - the scene being unloaded
     */
    public void unloadScene(final String id) {
        final SceneHolder holder = scenes.get(id);
        if (holder == currentScene) {
            throw new IllegalStateException("Cannot unload currently active scene");
        }

        holder.tryDeinit();
        scenes.remove(id);
    }

    public boolean switchToScene(final String id) {
        if (currentScene != null) {
            currentScene.callLeave();
        }

        if ((currentScene = scenes.get(id)) != null) {
            currentScene.callEnter(currentId);
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
        return currentScene != null ? currentScene.scene : null;
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
        if (currentScene != null) {
            currentScene.callRender(g);
        }
    }

    public void resizeCurrentScene(final int x, final int y) {
        if (currentScene != null) {
            currentScene.callResize(x, y);
        }
    }

    public boolean updateCurrentScene(final float dt) {
        if (currentScene != null) {
            return currentScene.callUpdate(dt);
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

    /* package */ void callSceneDeinit() {
        scenes.forEach((k, v) -> v.tryDeinit());
        scenes.clear();
    }
}
