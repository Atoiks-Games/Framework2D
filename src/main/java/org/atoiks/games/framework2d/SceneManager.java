package org.atoiks.games.framework2d;

import java.util.Map;
import java.util.HashMap;
import java.util.ArrayDeque;

public final class SceneManager {

    private static final Map<String, Object> res = new HashMap<>();
    private static final ArrayDeque<Scene> sceneStack = new ArrayDeque<>();

    private static boolean skipCycle;

    private static String currentId;

    private static IFrame frame;

    private SceneManager() {
    }

    public static void setFrameContext(IFrame frame) {
        SceneManager.frame = frame;
        SceneManager.skipCycle = false;
    }

    public static Scene getCurrentScene() {
        return sceneStack.peekFirst();
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
        getCurrentScene().render(g);
    }

    public static void resizeCurrentScene(final int w, final int h) {
        getCurrentScene().resize(w, h);
    }

    public static boolean updateCurrentScene(final float dt) {
        return getCurrentScene().update(dt);
    }

    public static IFrame frame() {
        return frame;
    }

    public static Map<String, Object> resources() {
        return res;
    }

    public static void pushScene(final Scene next) {
        final Scene current = sceneStack.peekFirst();
        if (current != null) {
            current.suspend();
        }

        next.enter(current);
        sceneStack.addFirst(next);
    }

    public static void popScene() {
        final Scene oldCurrent = sceneStack.removeFirst();
        oldCurrent.leave();

        final Scene newCurrent = sceneStack.getFirst();
        newCurrent.resume(oldCurrent);
    }

    public static void swapScene(final Scene repl) {
        final Scene oldCurrent = sceneStack.removeFirst();
        oldCurrent.leave();
        repl.enter(oldCurrent);
        sceneStack.addFirst(repl);
    }

    public static void unwindToScene(final Scene repl) {
        clearAllScenes();
        pushScene(repl);
    }

    public static int getNumberOfLoadedScenes() {
        return sceneStack.size();
    }

    /* package */ static void clearAllScenes() {
        while (!sceneStack.isEmpty()) {
            sceneStack.removeFirst().leave();
        }
    }
}
