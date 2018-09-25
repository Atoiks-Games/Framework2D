package org.atoiks.games.framework2d;

public abstract class GameScene extends Scene {

    /**
     * The runtime guarantee calls it only once and guaranteed called after
     * the loader scenes. Call order (relative to other game scenes) is
     * undefined.
     */
    public void init() {
        // Does nothing
    }

    /**
     * The runtime guarantee calls it only once. Call order (relative to other
     * game scenes) is undefined.
     */
    public void deinit() {
        // Does nothing
    }

    /**
     * Called during a scene transition, after the leave method of the previous
     * scene.
     *
     * @param previousSceneId the scene id of the previous scene
     */
    public void enter(int previousSceneId) {
        // Does nothing
    }

    /**
     * Called during a scene transition, before the enter method of the next
     * scene.
     */
    public void leave() {
        // Does nothing
    }
}
