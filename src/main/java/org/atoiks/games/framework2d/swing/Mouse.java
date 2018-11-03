package org.atoiks.games.framework2d.swing;

import java.util.Arrays;
import java.awt.MouseInfo;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseWheelEvent;

import org.atoiks.games.framework2d.IMouse;

/* package */ class Mouse extends MouseAdapter implements IMouse {

    public static final int BTN_PRESSED = -1;

    private final int[] btnbuf = new int[MouseInfo.getNumberOfButtons()];

    private int localX, localY;
    private int globalX, globalY;
    private int wheelRot;
    private boolean inFrame;
    private boolean moved;

    @Override
    public void reset() {
        Arrays.fill(btnbuf, 0);
        wheelRot = 0;
        moved = false;

        localX = localY = globalX = globalY = 0;
        inFrame = false;
    }

    @Override
    public void update() {
        Arrays.fill(btnbuf, 0);
        wheelRot = 0;
        moved = false;
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
        if (btn < btnbuf.length) {
            return btnbuf[btn] == BTN_PRESSED;
        }
        return false;
    }

    @Override
    public boolean isButtonUp(int btn) {
        if (btn < btnbuf.length) {
            return btnbuf[btn] == 0;
        }
        return false;
    }

    @Override
    public boolean isButtonClicked(int btn) {
        if (btn < btnbuf.length) {
            return btnbuf[btn] > 0;
        }
        return false;
    }

    @Override
    public boolean isButtonClicked(int btn, int clicks) {
        if (btn < btnbuf.length) {
            return btnbuf[btn] == clicks;
        }
        return false;
    }

    @Override
    public int getButtonClicks(int btn) {
        if (btn < btnbuf.length) {
            return Math.max(0, btnbuf[btn]);
        }
        return 0;
    }

    @Override
    public boolean isInFrame() {
        return inFrame;
    }

    @Override
    public boolean positionChanged() {
        return moved;
    }

    private void defaultMouseEventHandler(final MouseEvent e) {
        localX = e.getX();
        localY = e.getY();
        globalX = e.getXOnScreen();
        globalY = e.getYOnScreen();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        defaultMouseEventHandler(e);
        moved = true;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        defaultMouseEventHandler(e);
        btnbuf[e.getButton()] = BTN_PRESSED;
        moved = true;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        defaultMouseEventHandler(e);
        btnbuf[e.getButton()] = e.getClickCount();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        defaultMouseEventHandler(e);
        btnbuf[e.getButton()] = BTN_PRESSED;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        defaultMouseEventHandler(e);
        btnbuf[e.getButton()] = 0;
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
