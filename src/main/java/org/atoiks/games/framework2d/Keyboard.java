package org.atoiks.games.framework2d;

import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;

public final class Keyboard extends KeyAdapter implements IKeyboard<KeyAdapter> {

    private final boolean[] keybuf = new boolean[256];

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() < keybuf.length) {
            this.keybuf[e.getKeyCode()] = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() < keybuf.length) {
            this.keybuf[e.getKeyCode()] = false;
        }
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
    public KeyAdapter getRawInputDevice() {
        return this;
    }
}