package org.atoiks.games.framework2d;

public abstract class Scene {

    protected transient SceneManager scene;

    final void attachSceneManager(SceneManager mgr) {
        this.scene = mgr;
    }

    /**
     * Renders on the screen
     */
    public abstract void render(IGraphics g);

    /**
     * Updates the entities in the scene
     *
     * @param dt elapsed time measured in seconds
     * @return true if game should continue
     */
    public abstract boolean update(float dt);

    /**
     * Called when screen is resized
     *
     * @param x new width in pixels
     * @param y new height in pixels
     */
    public abstract void resize(int x, int y);

    /**
     * Should stay constant during an object's lifetime!
     */
    public abstract String getId();

    /**
     * The initializer of the scene. Guarantee called before the scene is
     * entered
     */
    public void init() {
        // Does nothing
    }

    /**
     * The clean up method of the scene. Guarantee called when the game is
     * shutting down
     */
    public void deinit() {
        // Does nothing
    }

    /**
     * Called during a scene transition, after leaving the previous scene
     *
     * @param from - the scene id of thr previous scene
     */
    public void enter(String from) {
        // Does nothing
    }

    /**
     * Called during a scene transition, before entering the next scene.
     */
    public void leave() {
        // Does nothing
    }
}
