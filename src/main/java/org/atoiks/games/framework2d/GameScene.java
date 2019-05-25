package org.atoiks.games.framework2d;

import java.io.Serializable;

public abstract class GameScene extends Scene implements Serializable {

    private static final long serialVersionUID = 1225562906466876179L;

    private final String id;

    protected GameScene() {
        this.id = this.getClass().getSimpleName();
    }

    public GameScene(final String name) {
        if (name == null) {
            throw new IllegalArgumentException("Scene name cannot be null");
        }

        this.id = name;
    }

    @Override
    public final String getId() {
        return id;
    }
}
