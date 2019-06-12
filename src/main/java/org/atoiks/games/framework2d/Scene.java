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
     * Called when the current scene is being pushed or swapped in.
     *
     * @param from - The scene that issued the push
     *
     * @see org.atoiks.games.framework2d.SceneManager#pushScene(Scene)
     * @see org.atoiks.games.framework2d.SceneManager#swapScene(Scene)
     */
    public default void enter(Scene from) {
    }

    /**
     * Called when the current scene is being popped or swapped out.
     *
     * @see org.atoiks.games.framework2d.SceneManager#popScene()
     * @see org.atoiks.games.framework2d.SceneManager#swapScene(Scene)
     */
    public default void leave() {
    }

    /**
     * Called when scene re-enters due to a pop
     *
     * @see org.atoiks.games.framework2d.SceneManager#popScene(Scene)
     */
    public default void resume(Scene from) {
    }

    /**
     * Called when scene issues a push
     *
     * @see org.atoiks.games.framework2d.SceneManager#pushScene(Scene)
     */
    public default void suspend() {
    }
}
