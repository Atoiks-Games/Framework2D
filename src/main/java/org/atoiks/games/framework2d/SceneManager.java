package org.atoiks.games.framework2d;

import java.util.Map;
import java.util.HashMap;

public final class SceneManager {

    private static final Map<String, Object> res = new HashMap<>();
    private static final Map<String, SceneHolder> scenes = new HashMap<>();

    private static boolean skipCycle;

    private static String currentId;
    private static SceneHolder currentScene;

    private static IFrame frame;

    private SceneManager() {
    }

    public static void setFrameContext(IFrame frame) {
        SceneManager.frame = frame;
        SceneManager.skipCycle = false;
    }

    public static void loadScenes(final Scene... scenes) {
        for (final Scene scene : scenes) {
            loadScene(scene, false);
        }
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
    public static void loadScene(Scene scene, boolean replaceOld) {
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
    public static void unloadScene(final String id) {
        final SceneHolder holder = scenes.get(id);
        if (holder == currentScene) {
            throw new IllegalStateException("Cannot unload currently active scene");
        }

        holder.tryDeinit();
        scenes.remove(id);
    }

    public static boolean switchToScene(final String id) {
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

    public static boolean restartCurrentScene() {
        return switchToScene(currentId);
    }

    public static String getCurrentSceneId() {
        return currentId;
    }

    public static Scene getCurrentScene() {
        return currentScene != null ? currentScene.scene : null;
    }

    public static boolean shouldSkipCycle() {
        if (skipCycle) {
            // No more cycles to skip
            skipCycle = false;
            return true;
        }
        return false;
    }

    public static void renderCurrentScene(final IGraphics g) {
        if (currentScene != null) {
            currentScene.callRender(g);
        }
    }

    public static void resizeCurrentScene(final int x, final int y) {
        if (currentScene != null) {
            currentScene.callResize(x, y);
        }
    }

    public static boolean updateCurrentScene(final float dt) {
        if (currentScene != null) {
            return currentScene.callUpdate(dt);
        }

        // Even though there is no frame, the app is still running
        return true;
    }

    public static IFrame frame() {
        return frame;
    }

    public static Map<String, Object> resources() {
        return res;
    }

    /* package */ static void callSceneDeinit() {
        scenes.forEach((k, v) -> v.tryDeinit());
        scenes.clear();
    }
}
