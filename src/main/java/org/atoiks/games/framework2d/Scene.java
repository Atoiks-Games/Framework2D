package org.atoiks.games.framework2d;

public interface Scene {

    /**
     * Renders the screen
     */
    public default void render(IGraphics g) {
    }

    /**
     * Updates the entities in the scene
     *
     * @param dt elapsed time measured in seconds
     * @return true if game should continue, false if game should stop
     */
    public default boolean update(float dt) {
        return true;
    }

    /**
     * Called when screen is resized
     *
     * @param w new width in pixels
     * @param h new height in pixels
     */
    public default void resize(int w, int h) {
    }

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
    public default void init() {
    }

    /**
     * The clean up method of the scene. Guarantee called when the scene is
     * unloaded
     */
    public default void deinit() {
    }

    /**
     * Called during a scene transition, after leaving the previous scene
     *
     * @param from - the scene id of thr previous scene
     */
    public default void enter(String from) {
    }

    /**
     * Called during a scene transition, before entering the next scene.
     */
    public default void leave() {
    }
}
