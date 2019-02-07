package org.atoiks.games.framework2d.java2d;

import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;

import java.util.Arrays;

import org.atoiks.games.framework2d.IKeyboard;

/* package */ class Keyboard extends KeyAdapter implements IKeyboard {

    private final boolean[] keybuf = new boolean[256];
    private final StringBuilder sb = new StringBuilder();

    private boolean captureChars = false;

    private int lastKey = KeyEvent.VK_UNDEFINED;

    @Override
    public void reset() {
        Arrays.fill(keybuf, false);
        captureTypedChars(false);

        lastKey = KeyEvent.VK_UNDEFINED;
    }

    @Override
    public void update() {
        // Do nothing
    }

    @Override
    public void keyPressed(KeyEvent e) {
        final int k = e.getKeyCode();
        if (k < keybuf.length) {
            this.keybuf[lastKey = k] = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        final int k = e.getKeyCode();
        if (k < keybuf.length) {
            this.keybuf[k] = false;
            if (lastKey == k) {
                lastKey = KeyEvent.VK_UNDEFINED;
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (captureChars) {
            sb.append(e.getKeyChar());
        }
    }

    @Override
    public int getLastDownKey() {
        return lastKey;
    }

    @Override
    public boolean isKeyDown(int keycode) {
        if (keycode < keybuf.length) {
            return keybuf[keycode];
        }
        return false;
    }

    @Override
    public boolean isKeyUp(int keycode) {
        if (keycode < keybuf.length) {
            return !keybuf[keycode];
        }
        return true;
    }

    @Override
    public boolean isKeyPressed(int keycode) {
        if (isKeyDown(keycode)) {
            keybuf[keycode] = false;
            return true;
        }
        return false;
    }

    @Override
    public void captureTypedChars(boolean flag) {
        if (!(captureChars = flag)) {
            // if flag is false, discard the current char buffer
            sb.setLength(0);
        }
    }

    @Override
    public String getTypedChars() {
        final String ret = sb.toString();
        // Clear buffer
        sb.setLength(0);
        return ret;
    }
}
