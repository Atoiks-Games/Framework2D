package org.atoiks.games.framework2d.lwjgl3;

import java.util.EnumMap;

import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWCharCallback;

import org.atoiks.games.framework2d.KeyCode;
import org.atoiks.games.framework2d.IKeyboard;

import static org.lwjgl.glfw.GLFW.*;

/* package */ final class Keyboard implements IKeyboard {

    private enum State {
        PRESSED, RELEASED, HELD;
    }

    private final EnumMap<KeyCode, State> keybuf = new EnumMap<>(KeyCode.class);
    private final StringBuilder sb = new StringBuilder();

    private boolean captureChars = false;

    private KeyCode lastKey = KeyCode.KEY_UNDEFINED;

    /* package */ final GLFWKeyCallback keyCb = new GLFWKeyCallback() {
        @Override
        public void invoke(long window, int key, int scancode, int action, int mods) {
            final KeyCode kc = getKeyCodeFromInt(key);
            if (action == GLFW_RELEASE) {
                keybuf.put(kc, State.RELEASED);
                if (lastKey == kc) {
                    lastKey = KeyCode.KEY_UNDEFINED;
                }
            } else if (action == GLFW_PRESS) {
                keybuf.put(kc, State.PRESSED);
                lastKey = kc;
            } else if (action == GLFW_REPEAT) {
                keybuf.put(kc, State.HELD);
                lastKey = kc;
            }
        }
    };

    /* package */ final GLFWCharCallback charCb = new GLFWCharCallback() {
        @Override
        public void invoke(long window, int codepoint) {
            if (captureChars) {
                sb.append(Character.toChars(codepoint));
            }
        }
    };

    @Override
    public void reset() {
        this.keybuf.clear();
        captureTypedChars(false);

        this.lastKey = KeyCode.KEY_UNDEFINED;
    }

    @Override
    public void update() {
        //
    }

    @Override
    public boolean isKeyUp(KeyCode kc) {
        final State state = this.keybuf.get(kc);
        return state == null || state == State.RELEASED;
    }

    @Override
    public boolean isKeyDown(KeyCode kc) {
        final State state = this.keybuf.get(kc);
        return state == State.PRESSED || state == State.HELD;
    }

    @Override
    public boolean isKeyPressed(KeyCode kc) {
        return this.keybuf.remove(kc, State.PRESSED);
    }

    @Override
    public KeyCode getLastDownKey() {
        return this.lastKey;
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

    private static KeyCode getKeyCodeFromInt(final int key) {
        switch (key) {
            // 1-to-1 mapping keys
            case GLFW_KEY_0: return KeyCode.KEY_0;
            case GLFW_KEY_1: return KeyCode.KEY_1;
            case GLFW_KEY_2: return KeyCode.KEY_2;
            case GLFW_KEY_3: return KeyCode.KEY_3;
            case GLFW_KEY_4: return KeyCode.KEY_4;
            case GLFW_KEY_5: return KeyCode.KEY_5;
            case GLFW_KEY_6: return KeyCode.KEY_6;
            case GLFW_KEY_7: return KeyCode.KEY_7;
            case GLFW_KEY_8: return KeyCode.KEY_8;
            case GLFW_KEY_9: return KeyCode.KEY_9;

            case GLFW_KEY_KP_0: return KeyCode.KEY_KP0;
            case GLFW_KEY_KP_1: return KeyCode.KEY_KP1;
            case GLFW_KEY_KP_2: return KeyCode.KEY_KP2;
            case GLFW_KEY_KP_3: return KeyCode.KEY_KP3;
            case GLFW_KEY_KP_4: return KeyCode.KEY_KP4;
            case GLFW_KEY_KP_5: return KeyCode.KEY_KP5;
            case GLFW_KEY_KP_6: return KeyCode.KEY_KP6;
            case GLFW_KEY_KP_7: return KeyCode.KEY_KP7;
            case GLFW_KEY_KP_8: return KeyCode.KEY_KP8;
            case GLFW_KEY_KP_9: return KeyCode.KEY_KP9;

            case GLFW_KEY_A: return KeyCode.KEY_A;
            case GLFW_KEY_B: return KeyCode.KEY_B;
            case GLFW_KEY_C: return KeyCode.KEY_C;
            case GLFW_KEY_D: return KeyCode.KEY_D;
            case GLFW_KEY_E: return KeyCode.KEY_E;
            case GLFW_KEY_F: return KeyCode.KEY_F;
            case GLFW_KEY_G: return KeyCode.KEY_G;
            case GLFW_KEY_H: return KeyCode.KEY_H;
            case GLFW_KEY_I: return KeyCode.KEY_I;
            case GLFW_KEY_J: return KeyCode.KEY_J;
            case GLFW_KEY_K: return KeyCode.KEY_K;
            case GLFW_KEY_L: return KeyCode.KEY_L;
            case GLFW_KEY_M: return KeyCode.KEY_M;
            case GLFW_KEY_N: return KeyCode.KEY_N;
            case GLFW_KEY_O: return KeyCode.KEY_O;
            case GLFW_KEY_P: return KeyCode.KEY_P;
            case GLFW_KEY_Q: return KeyCode.KEY_Q;
            case GLFW_KEY_R: return KeyCode.KEY_R;
            case GLFW_KEY_S: return KeyCode.KEY_S;
            case GLFW_KEY_T: return KeyCode.KEY_T;
            case GLFW_KEY_U: return KeyCode.KEY_U;
            case GLFW_KEY_V: return KeyCode.KEY_V;
            case GLFW_KEY_W: return KeyCode.KEY_W;
            case GLFW_KEY_X: return KeyCode.KEY_X;
            case GLFW_KEY_Y: return KeyCode.KEY_Y;
            case GLFW_KEY_Z: return KeyCode.KEY_Z;

            case GLFW_KEY_SEMICOLON: return KeyCode.KEY_SEMICOLON;
            case GLFW_KEY_EQUAL: return KeyCode.KEY_EQUAL;
            case GLFW_KEY_LEFT_BRACKET: return KeyCode.KEY_LBRACKET;
            case GLFW_KEY_RIGHT_BRACKET: return KeyCode.KEY_RBRACKET;

            case GLFW_KEY_TAB: return KeyCode.KEY_TAB;
            case GLFW_KEY_ESCAPE: return KeyCode.KEY_ESCAPE;
            case GLFW_KEY_ENTER: return KeyCode.KEY_ENTER;

            case GLFW_KEY_UP: return KeyCode.KEY_UP;
            case GLFW_KEY_DOWN: return KeyCode.KEY_DOWN;
            case GLFW_KEY_LEFT: return KeyCode.KEY_LEFT;
            case GLFW_KEY_RIGHT: return KeyCode.KEY_RIGHT;

            case GLFW_KEY_LEFT_SHIFT: return KeyCode.KEY_LSHIFT;
            case GLFW_KEY_RIGHT_SHIFT: return KeyCode.KEY_RSHIFT;

            // keys that we do not recognize
            case GLFW_KEY_UNKNOWN:
            default:
                return KeyCode.KEY_UNDEFINED;
        }
    }
}
