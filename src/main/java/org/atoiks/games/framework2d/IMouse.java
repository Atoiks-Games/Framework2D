package org.atoiks.games.framework2d;

public interface IMouse<T> extends IInputDevice<T> {

    /**
     * Returns X-coordinate of mouse relative to frame
     *
     * @return x relative to frame
     */
    public int getLocalX();
    
    /**
     * Returns Y-coordinate of mouse relative to frame
     *
     * @return y relative to frame
     */
    public int getLocalY();

    /**
     * Returns X-coordinate of mouse relative to screen
     *
     * @return x relative to screen
     */
    public int getGlobalX();
    
    /**
     * Returns Y-coordinate of mouse relative to screen
     *
     * @return y relative to screen
     */
    public int getGlobalY();

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
     * Checks is the button was clicked a certain amount of times
     *
     * @param btn - The mouse button
     * @param clicks - The amount of clicks
     *
     * @return if button was clicked n-times
     */
    public boolean isButtonClicked(int btn, int clicks);

    /**
     * Get the amount of clicks of a button
     *
     * @param btn - The mouse button
     *
     * @return clicks of a button, has to be non-negative
     */
    public int getButtonClicks(int btn);
}