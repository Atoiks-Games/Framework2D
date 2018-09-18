package org.atoiks.games.framework2d;

public abstract class Scene {

    protected SceneManager scene;

    final void attachSceneManager(SceneManager mgr) {
        this.scene = mgr;
    }

    /**
     * Renders on the screen
     */
    public abstract <T> void render(IGraphics<T> g);

    /**
     * Updates the entities in the scene
     *
     * @return true if game should continue
     */
    public abstract boolean update(float dt);

    public abstract void resize(int x, int y);
}