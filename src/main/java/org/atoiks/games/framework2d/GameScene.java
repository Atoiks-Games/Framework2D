package org.atoiks.games.framework2d;

public abstract class GameScene extends Scene {

    /**
     * Called during a scene transition, after the leave method of the previous
     * scene.
     *
     * @param previousSceneId the scene id of the previous scene
     */
    public void enter(int previousSceneId) {
        // Does nothing
    }
}
