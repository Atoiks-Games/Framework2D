package org.atoiks.games.framework2d.java2d;

import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;

import java.util.EnumSet;

import org.atoiks.games.framework2d.KeyCode;
import org.atoiks.games.framework2d.IKeyboard;

import static java.awt.event.KeyEvent.*;

/* package */ final class Keyboard extends KeyAdapter implements IKeyboard {

    private final EnumSet<KeyCode> keybuf = EnumSet.noneOf(KeyCode.class);
    private final StringBuilder sb = new StringBuilder();

    private boolean captureChars = false;

    private KeyCode lastKey = KeyCode.KEY_UNDEFINED;

    @Override
    public void reset() {
        this.keybuf.clear();
        captureTypedChars(false);

        this.lastKey = KeyCode.KEY_UNDEFINED;
    }

    @Override
    public void update() {
        // Do nothing
    }

    @Override
    public void keyPressed(KeyEvent e) {
        final KeyCode kc = getKeyCodeFromEvent(e);
        this.keybuf.add(kc);
        this.lastKey = kc;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        final KeyCode kc = getKeyCodeFromEvent(e);
        this.keybuf.remove(kc);
        if (this.lastKey == kc) {
            this.lastKey = KeyCode.KEY_UNDEFINED;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (captureChars) {
            sb.append(e.getKeyChar());
        }
    }

    @Override
    public KeyCode getLastDownKey() {
        return this.lastKey;
    }

    @Override
    public boolean isKeyDown(KeyCode keycode) {
        return this.keybuf.contains(keycode);
    }

    @Override
    public boolean isKeyUp(KeyCode keycode) {
        return !this.keybuf.contains(keycode);
    }

    @Override
    public boolean isKeyPressed(KeyCode keycode) {
        // Returns true only if set contained such keycode, test for key-down)
        // Then will remove this entry from set, achieving key-pressed effect)
        return this.keybuf.remove(keycode);
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

    private static KeyCode getKeyCodeFromEvent(final KeyEvent e) {
        switch (e.getKeyCode()) {
            // 1-to-1 mapping keys
            case VK_0: return KeyCode.KEY_0;
            case VK_1: return KeyCode.KEY_1;
            case VK_2: return KeyCode.KEY_2;
            case VK_3: return KeyCode.KEY_3;
            case VK_4: return KeyCode.KEY_4;
            case VK_5: return KeyCode.KEY_5;
            case VK_6: return KeyCode.KEY_6;
            case VK_7: return KeyCode.KEY_7;
            case VK_8: return KeyCode.KEY_8;
            case VK_9: return KeyCode.KEY_9;

            case VK_NUMPAD0: return KeyCode.KEY_KP0;
            case VK_NUMPAD1: return KeyCode.KEY_KP1;
            case VK_NUMPAD2: return KeyCode.KEY_KP2;
            case VK_NUMPAD3: return KeyCode.KEY_KP3;
            case VK_NUMPAD4: return KeyCode.KEY_KP4;
            case VK_NUMPAD5: return KeyCode.KEY_KP5;
            case VK_NUMPAD6: return KeyCode.KEY_KP6;
            case VK_NUMPAD7: return KeyCode.KEY_KP7;
            case VK_NUMPAD8: return KeyCode.KEY_KP8;
            case VK_NUMPAD9: return KeyCode.KEY_KP9;

            case VK_A: return KeyCode.KEY_A;
            case VK_B: return KeyCode.KEY_B;
            case VK_C: return KeyCode.KEY_C;
            case VK_D: return KeyCode.KEY_D;
            case VK_E: return KeyCode.KEY_E;
            case VK_F: return KeyCode.KEY_F;
            case VK_G: return KeyCode.KEY_G;
            case VK_H: return KeyCode.KEY_H;
            case VK_I: return KeyCode.KEY_I;
            case VK_J: return KeyCode.KEY_J;
            case VK_K: return KeyCode.KEY_K;
            case VK_L: return KeyCode.KEY_L;
            case VK_M: return KeyCode.KEY_M;
            case VK_N: return KeyCode.KEY_N;
            case VK_O: return KeyCode.KEY_O;
            case VK_P: return KeyCode.KEY_P;
            case VK_Q: return KeyCode.KEY_Q;
            case VK_R: return KeyCode.KEY_R;
            case VK_S: return KeyCode.KEY_S;
            case VK_T: return KeyCode.KEY_T;
            case VK_U: return KeyCode.KEY_U;
            case VK_V: return KeyCode.KEY_V;
            case VK_W: return KeyCode.KEY_W;
            case VK_X: return KeyCode.KEY_X;
            case VK_Y: return KeyCode.KEY_Y;
            case VK_Z: return KeyCode.KEY_Z;

            case VK_SEMICOLON: return KeyCode.KEY_SEMICOLON;
            case VK_EQUALS: return KeyCode.KEY_EQUAL;
            case VK_OPEN_BRACKET: return KeyCode.KEY_LBRACKET;
            case VK_CLOSE_BRACKET: return KeyCode.KEY_RBRACKET;

            case VK_TAB: return KeyCode.KEY_TAB;
            case VK_ESCAPE: return KeyCode.KEY_ESCAPE;
            case VK_ENTER: return KeyCode.KEY_ENTER;
            case VK_UP: return KeyCode.KEY_UP;
            case VK_DOWN: return KeyCode.KEY_DOWN;
            case VK_LEFT: return KeyCode.KEY_LEFT;
            case VK_RIGHT: return KeyCode.KEY_RIGHT;

            // keys that need a bit more work
            case VK_SHIFT: return e.getKeyLocation() == KEY_LOCATION_LEFT
                    ? KeyCode.KEY_LSHIFT : KeyCode.KEY_RSHIFT;

            // keys that we do not recognize
            case VK_UNDEFINED:
            default:
                return KeyCode.KEY_UNDEFINED;
        }
    }
}
