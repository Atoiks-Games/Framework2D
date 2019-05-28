package org.atoiks.games.framework2d;

public interface IScene {

    /**
     * Renders the screen
     */
    public void render(IGraphics g);

    /**
     * Updates the entities in the scene
     *
     * @param dt elapsed time measured in seconds
     * @return true if game should continue, false if game should stop
     */
    public boolean update(float dt);

    /**
     * Called when screen is resized
     *
     * @param w new width in pixels
     * @param h new height in pixels
     */
    public void resize(int w, int h);

    /**
     * ID of the scene, used for scene switching.
     *
     * Should stay constant during an object's lifetime!
     *
     * @return simple name of the class by default
     */
    public default String getId() {
        return this.getClass().getSimpleName();
    }

    /**
     * The initializer of the scene. Guarantee called before the scene is
     * entered
     */
    public void init();

    /**
     * The clean up method of the scene. Guarantee called when the scene is
     * unloaded
     */
    public void deinit();

    /**
     * Called during a scene transition, after leaving the previous scene
     *
     * @param from - the scene id of thr previous scene
     */
    public void enter(String from);

    /**
     * Called during a scene transition, before entering the next scene.
     */
    public void leave();
}
