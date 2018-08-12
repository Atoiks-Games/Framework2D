package org.atoiks.games.framework2d.swing;

import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;

import org.atoiks.games.framework2d.IKeyboard;

/* package */ class Keyboard extends KeyAdapter implements IKeyboard<KeyAdapter> {

    private final boolean[] keybuf = new boolean[256];
    private final StringBuilder sb = new StringBuilder();

    private boolean captureChars = false;

    private int lastKey = KeyEvent.VK_UNDEFINED;

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() < keybuf.length) {
            this.keybuf[lastKey = e.getKeyCode()] = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() < keybuf.length) {
            this.keybuf[e.getKeyCode()] = false;
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
        final int ret = lastKey;
        lastKey = KeyEvent.VK_UNDEFINED;
        return ret;
    }

    @Override
    public void reset() {
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

    @Override
    public KeyAdapter getRawInputDevice() {
        return this;
    }
}