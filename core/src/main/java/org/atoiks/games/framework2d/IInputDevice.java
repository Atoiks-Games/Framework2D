package org.atoiks.games.framework2d;

public interface IInputDevice {

    /**
     * Resets the internal state of the input device
     */
    public void reset();

    /**
     * Called once per update frame
     */
    public void update();
}
