package org.atoiks.games.framework2d.swing;

import java.util.Arrays;
import java.awt.MouseInfo;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseWheelEvent;

import org.atoiks.games.framework2d.IMouse;

/* package */ class Mouse extends MouseAdapter implements IMouse {

    private enum MouseState {
        RELEASED, HELD, PRESSED;
    }

    private final boolean[] state;
    private final MouseState[] poll;

    private int localX, localY;
    private int globalX, globalY;
    private int wheelRot;
    private boolean inFrame;
    private boolean moved;

    public Mouse() {
        final int btns = MouseInfo.getNumberOfButtons();
        state = new boolean[btns];
        poll = new MouseState[btns];
        Arrays.fill(poll, MouseState.RELEASED);
    }

    @Override
    public void reset() {
        Arrays.fill(poll, MouseState.RELEASED);
        wheelRot = 0;
        moved = false;

        localX = localY = globalX = globalY = 0;
        inFrame = false;
    }

    @Override
    public synchronized void update() {
        wheelRot = 0;
        moved = false;

        final int size = poll.length;
        for (int i = 0; i < size; ++i) {
            if (state[i]) {
                poll[i] = poll[i] == MouseState.RELEASED ? MouseState.PRESSED : MouseState.HELD;
            } else {
                poll[i] = MouseState.RELEASED;
            }
        }
    }

    @Override
    public int getLocalX() {
        return localX;
    }

    @Override
    public int getLocalY() {
        return localY;
    }

    @Override
    public int getGlobalX() {
        return globalX;
    }

    @Override
    public int getGlobalY() {
        return globalY;
    }

    @Override
    public int getWheelRotation() {
        return wheelRot;
    }

    @Override
    public boolean isButtonDown(int btn) {
        if (btn < poll.length) {
            final MouseState st = poll[btn];
            return st == MouseState.HELD || st == MouseState.PRESSED;
        }
        return false;
    }

    @Override
    public boolean isButtonUp(int btn) {
        if (btn < poll.length) {
            return poll[btn] == MouseState.RELEASED;
        }
        return false;
    }

    @Override
    public boolean isButtonClicked(int btn) {
        if (btn < poll.length) {
            return poll[btn] == MouseState.PRESSED;
        }
        return false;
    }

    @Override
    public boolean isInFrame() {
        return inFrame;
    }

    @Override
    public boolean mouseMoved() {
        return moved;
    }

    private synchronized void defaultMouseEventHandler(final MouseEvent e) {
        localX = e.getX();
        localY = e.getY();
        globalX = e.getXOnScreen();
        globalY = e.getYOnScreen();
        moved = true;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        defaultMouseEventHandler(e);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        defaultMouseEventHandler(e);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // click status is set in update method
    }

    @Override
    public void mousePressed(MouseEvent e) {
        state[e.getButton()] = true;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        state[e.getButton()] = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        inFrame = true;
    }

    @Override
    public void mouseExited(MouseEvent e) {
        inFrame = false;
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        wheelRot = e.getWheelRotation();
    }
}
