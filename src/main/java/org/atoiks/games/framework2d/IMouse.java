package org.atoiks.games.framework2d;

public interface IMouse extends IInputDevice {

    /**
     * Returns X-coordinate of mouse relative to frame
     *
     * @return x relative to frame
     */
    public int getX();

    /**
     * Returns Y-coordinate of mouse relative to frame
     *
     * @return y relative to frame
     */
    public int getY();

    /**
     * Returns the amount of rotating on the mouse wheel
     *
     * @return the amount of rotating
     */
    public int getWheelRotation();

    /**
     * Checks if the mouse button is not clicked or held down
     *
     * @param btn - The mouse button
     *
     * @return if button is up
     */
    public boolean isButtonUp(int btn);

    /**
     * Checks if the mouse button is held down
     *
     * @param btn - The mouse button
     *
     * @return if button is down
     */
    public boolean isButtonDown(int btn);

    /**
     * Checks is the button was clicked
     *
     * @param btn - The mouse button
     *
     * @return if button was clicked
     */
    public boolean isButtonClicked(int btn);

    /**
     * Checks if mouse is inside frame
     *
     * @return true if mouse is inside frame
     */
    public boolean isInFrame();

    /**
     * @return true if mouse moved during this update
     */
    public boolean mouseMoved();
}
