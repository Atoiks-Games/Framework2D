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
            case VK_COMMA: return KeyCode.KEY_COMMA;
            case VK_PERIOD: return KeyCode.KEY_PERIOD;
            case VK_OPEN_BRACKET: return KeyCode.KEY_LBRACKET;
            case VK_CLOSE_BRACKET: return KeyCode.KEY_RBRACKET;
            case VK_SLASH: return KeyCode.KEY_SLASH;
            case VK_BACK_SLASH: return KeyCode.KEY_BACKSLASH;
            case VK_BACK_QUOTE: return KeyCode.KEY_GRAVE_ACCENT;
            case VK_QUOTE: return KeyCode.KEY_APOSTROPHE;
            case VK_MINUS: return KeyCode.KEY_MINUS;

            case VK_SPACE: return KeyCode.KEY_SPACE;
            case VK_TAB: return KeyCode.KEY_TAB;
            case VK_ESCAPE: return KeyCode.KEY_ESCAPE;
            case VK_BACK_SPACE: return KeyCode.KEY_BACKSPACE;
            case VK_DELETE: return KeyCode.KEY_DELETE;

            case VK_DECIMAL: return KeyCode.KEY_KP_DECIMAL;
            case VK_ADD: return KeyCode.KEY_KP_ADD;
            case VK_SUBTRACT: return KeyCode.KEY_KP_SUBTRACT;
            case VK_MULTIPLY: return KeyCode.KEY_KP_MULTIPLY;
            case VK_DIVIDE: return KeyCode.KEY_KP_DIVIDE;

            case VK_F1: return KeyCode.KEY_F1;
            case VK_F2: return KeyCode.KEY_F2;
            case VK_F3: return KeyCode.KEY_F3;
            case VK_F4: return KeyCode.KEY_F4;
            case VK_F5: return KeyCode.KEY_F5;
            case VK_F6: return KeyCode.KEY_F6;
            case VK_F7: return KeyCode.KEY_F7;
            case VK_F8: return KeyCode.KEY_F8;
            case VK_F9: return KeyCode.KEY_F9;
            case VK_F10: return KeyCode.KEY_F10;
            case VK_F11: return KeyCode.KEY_F11;
            case VK_F12: return KeyCode.KEY_F12;
            case VK_F13: return KeyCode.KEY_F13;
            case VK_F14: return KeyCode.KEY_F14;
            case VK_F15: return KeyCode.KEY_F15;
            case VK_F16: return KeyCode.KEY_F16;
            case VK_F17: return KeyCode.KEY_F17;
            case VK_F18: return KeyCode.KEY_F18;
            case VK_F19: return KeyCode.KEY_F19;
            case VK_F20: return KeyCode.KEY_F20;
            case VK_F21: return KeyCode.KEY_F21;
            case VK_F22: return KeyCode.KEY_F22;
            case VK_F23: return KeyCode.KEY_F23;
            case VK_F24: return KeyCode.KEY_F24;

            case VK_UP: return KeyCode.KEY_UP;
            case VK_DOWN: return KeyCode.KEY_DOWN;
            case VK_LEFT: return KeyCode.KEY_LEFT;
            case VK_RIGHT: return KeyCode.KEY_RIGHT;

            case VK_PAGE_UP: return KeyCode.KEY_PAGE_UP;
            case VK_PAGE_DOWN: return KeyCode.KEY_PAGE_DOWN;
            case VK_HOME: return KeyCode.KEY_HOME;
            case VK_END: return KeyCode.KEY_END;
            case VK_INSERT: return KeyCode.KEY_INSERT;
            case VK_CONTEXT_MENU: return KeyCode.KEY_MENU;
            case VK_PAUSE: return KeyCode.KEY_PAUSE;
            case VK_PRINTSCREEN: return KeyCode.KEY_PRINTSCREEN;

            case VK_CAPS_LOCK: return KeyCode.KEY_CAPS_LOCK;
            case VK_SCROLL_LOCK: return KeyCode.KEY_SCROLL_LOCK;
            case VK_NUM_LOCK: return KeyCode.KEY_NUM_LOCK;

            // keys that need a bit more work
            case VK_SHIFT: return e.getKeyLocation() == KEY_LOCATION_LEFT
                    ? KeyCode.KEY_LSHIFT : KeyCode.KEY_RSHIFT;

            case VK_CONTROL: return e.getKeyLocation() == KEY_LOCATION_LEFT
                    ? KeyCode.KEY_LCTRL : KeyCode.KEY_RCTRL;

            case VK_ALT: return e.getKeyLocation() == KEY_LOCATION_LEFT
                    ? KeyCode.KEY_LALT : KeyCode.KEY_RALT;

            case VK_WINDOWS: // Treat windows key as if it was the meta key!
            case VK_META: return e.getKeyLocation() == KEY_LOCATION_LEFT
                    ? KeyCode.KEY_LSUPER : KeyCode.KEY_RSUPER;

            case VK_EQUALS: return e.getKeyLocation() == KEY_LOCATION_STANDARD
                    ? KeyCode.KEY_EQUAL : KeyCode.KEY_KP_EQUAL;
            case VK_ENTER: return e.getKeyLocation() == KEY_LOCATION_STANDARD
                    ? KeyCode.KEY_ENTER : KeyCode.KEY_KP_ENTER;

            // keys that we do not recognize
            case VK_UNDEFINED:
            default:
                return KeyCode.KEY_UNDEFINED;
        }
    }
}
