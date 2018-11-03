package org.atoiks.games.framework2d;

public final class Input {

    // This is the *global* input manager
    // Most of these methods are just going
    // to be delegates to the respected
    // IInputDevice components

    private static IKeyboard compKeyboard;
    private static IMouse compMouse;

    private Input() {
        // static only singleton
    }

    /**
     * Resets the internal state of the input devices
     */
    public static void resetInputDevices() {
        compKeyboard.reset();
        compMouse.reset();
    }

    /**
     * You almost never use this method because the the game loop will call
     * this for you!
     */
    public static void invokeFrameUpdate() {
        compKeyboard.update();
        compMouse.update();
    }

    /**
     * Sets the internal keyboard component.
     *
     * @param kb - new keyboard component
     * @throws IllegalArgumentException if parameter {@code kb} is null
     * @throws IllegalStateException if internal keyboard component is already initalized
     */
    public static void provideKeyboard(IKeyboard kb) {
        if (kb == null) {
            throw new IllegalArgumentException("Keyboard is null");
        }

        if (compKeyboard == null) {
            compKeyboard = kb;
        } else {
            throw new IllegalStateException("Keyboard is already initialized");
        }
    }

    /**
     * Sets the internal mouse component.
     *
     * @param mouse - new mouse component
     * @throws IllegalArgumentException if parameter {@code mouse} is null
     * @throws IllegalStateException if internal mouse component is already initalized
     */
    public static void provideMouse(IMouse mouse) {
        if (mouse == null) {
            throw new IllegalArgumentException("Mouse is null");
        }

        if (compMouse == null) {
            compMouse = mouse;
        } else {
            throw new IllegalStateException("Mouse is already initialized");
        }
    }

    // ------ Feature-checking methods ------

    public static boolean isKeyboardSupported() {
        return compKeyboard != null;
    }

    public static boolean isMouseSupported() {
        return compMouse != null;
    }

    // ------ Keyboard-delegate methods ------

    public static boolean isKeyUp(int kc) {
        return compKeyboard.isKeyUp(kc);
    }

    public static boolean isKeyDown(int kc) {
        return compKeyboard.isKeyDown(kc);
    }

    public static boolean isKeyPressed(int kc) {
        return compKeyboard.isKeyPressed(kc);
    }

    public static int getLastDownKey() {
        return compKeyboard.getLastDownKey();
    }

    public static void captureTypedChars(boolean flag) {
        compKeyboard.captureTypedChars(flag);
    }

    public static String getTypedChars() {
        return compKeyboard.getTypedChars();
    }

    // ------ Mouse-delegate methods ------

    public static int getLocalX() {
        return compMouse.getLocalX();
    }

    public static int getLocalY() {
        return compMouse.getLocalY();
    }

    public static int getGlobalX() {
        return compMouse.getGlobalX();
    }

    public static int getGlobalY() {
        return compMouse.getGlobalY();
    }

    public static int getWheelRotation() {
        return compMouse.getWheelRotation();
    }

    public static boolean isMouseButtonUp(int btn) {
        return compMouse.isButtonUp(btn);
    }

    public static boolean isMouseButtonDown(int btn) {
        return compMouse.isButtonDown(btn);
    }

    public static boolean isMouseButtonClicked(int btn) {
        return compMouse.isButtonClicked(btn);
    }

    public static boolean isMouseButtonClicked(int btn, int clicks) {
        return compMouse.isButtonClicked(btn, clicks);
    }

    public static int getMouseButtonClicks(int btn) {
        return compMouse.getButtonClicks(btn);
    }

    public static boolean isMouseInFrame() {
        return compMouse.isInFrame();
    }

    public static boolean mouseMoved() {
        return compMouse.mouseMoved();
    }
}
