package org.atoiks.games.framework2d;

import java.io.Serializable;

public abstract class GameScene extends Scene implements Serializable {

    private static final long serialVersionUID = 1225562906466876179L;

    public final String id;

    protected GameScene() {
        this.id = this.getClass().getSimpleName();
    }

    public GameScene(final String name) {
        if (name == null) {
            throw new IllegalArgumentException("Scene name cannot be null");
        }

        this.id = name;
    }

    /**
     * Called during a scene transition, after the leave method of the previous
     * scene.
     *
     * @param previousSceneId the scene id of the previous scene
     */
    public void enter(String previousSceneId) {
        // Does nothing
    }
}
