package org.atoiks.games.framework2d;

import java.awt.Graphics;

public abstract class Scene {

    protected SceneManager scene;

    final void attachSceneManager(SceneManager mgr) {
        this.scene = mgr;
    }

    /**
     * Renders on the screen
     */
    public abstract void render(Graphics g);

    /**
     * Updates the entities in the scene
     *
     * @return true if game should continue
     */
    public abstract boolean update(float dt);

    public abstract void resize(int x, int y);

    public void leave() {
        // Does nothing
    }

    public void enter(int previousSceneId) {
        // Does nothing
    }
}