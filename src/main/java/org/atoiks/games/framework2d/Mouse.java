package org.atoiks.games.framework2d;

import java.util.Arrays;
import java.awt.MouseInfo;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseWheelEvent;

public final class Mouse extends MouseAdapter implements IMouse<MouseAdapter> {

    public static final int BTN_PRESSED = -1;

    private final int[] btnbuf = new int[MouseInfo.getNumberOfButtons()];

    private int localX, localY;
    private int globalX, globalY;
    private int wheelRot;

    @Override
    public void reset() {
        Arrays.fill(btnbuf, 0);
        localX = localY = -1;
        globalX = globalY = -1;
        wheelRot = 0;
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

    private void defaultMouseEventHandler(final MouseEvent e) {
        localX = e.getX();
        localY = e.getY();
        globalX = e.getXOnScreen();
        globalY = e.getYOnScreen();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        defaultMouseEventHandler(e);
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
    public void mouseWheelMoved(MouseWheelEvent e) {
        wheelRot = e.getWheelRotation();
    }

    @Override
    public MouseAdapter getRawInputDevice() {
        return this;
    }
}