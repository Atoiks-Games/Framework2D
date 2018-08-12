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
     * @return true if typed
     */
    public boolean isKeyPressed(int kc);

    /**
     * @return the last held key
     */
    public int getLastDownKey();

    /**
     * Decides whether or not typed characters are handled. In order for
     * {@link this#getTypedChars()} to return meaningful data, this
     * method must be called with {@code true} at least an update frame
     * earlier.
     *
     * @param flag - captures typed chars if true, ignores otherwise
     */
    public void captureTypedChars(boolean flag);

    /**
     * Returns string with the characters typed in. Calling this method will
     * only retrieve the characters since last call. If
     * {@link this#captureTypedChars()} is called with false, empty string is
     * returned.
     *
     * @return string with typed characters
     */
    public String getTypedChars();
}