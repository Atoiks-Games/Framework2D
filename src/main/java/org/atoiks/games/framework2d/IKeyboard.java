package org.atoiks.games.framework2d;

public interface IKeyboard<T> extends IInputDevice<T> {

    /**
     * Checks if key is not being pressed
     *
     * @param kc - The keycode of the key
     *
     * @return true if not being pressed
     */
    public boolean isKeyUp(int kc);

    /**
     * Checks if key is being held down
     *
     * @param kc - The keycode of the key
     *
     * @return true if being held down
     */
    public boolean isKeyDown(int kc);

    /**
     * Checks if key is typed
     *
     * @param kc - The keycode of the key
     *
     * @return true if being held down
     */
    public boolean isKeyPressed(int kc);
}