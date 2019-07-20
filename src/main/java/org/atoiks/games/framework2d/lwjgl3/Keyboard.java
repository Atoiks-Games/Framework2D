package org.atoiks.games.framework2d.lwjgl3;

import java.util.Arrays;

import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWCharCallback;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_REPEAT;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UNKNOWN;

import org.atoiks.games.framework2d.IKeyboard;

/* package */ final class Keyboard implements IKeyboard {

    private enum State {
        PRESSED, RELEASED, HELD;
    }

    private final State[] keybuf = new State[256];
    private final StringBuilder sb = new StringBuilder();

    private boolean captureChars = false;

    private int lastKey = GLFW_KEY_UNKNOWN;

    /* package */ final GLFWKeyCallback keyCb = new GLFWKeyCallback() {
        @Override
        public void invoke(long window, int key, int scancode, int action, int mods) {
            if (key < keybuf.length) {
                if (action == GLFW_RELEASE) {
                    keybuf[key] = State.RELEASED;
                } else if (action == GLFW_PRESS) {
                    keybuf[key] = State.PRESSED;
                    lastKey = key;
                } else if (action == GLFW_REPEAT) {
                    keybuf[key] = State.HELD;
                    lastKey = key;
                }
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
        Arrays.fill(keybuf, State.RELEASED);
        captureTypedChars(false);

        lastKey = GLFW_KEY_UNKNOWN;
    }

    @Override
    public void update() {
        //
    }

    @Override
    public boolean isKeyUp(int kc) {
        if (kc < keybuf.length) {
            return keybuf[kc] == State.RELEASED;
        }
        return true;
    }

    @Override
    public boolean isKeyDown(int kc) {
        if (kc < keybuf.length) {
            return keybuf[kc] != State.RELEASED;
        }
        return false;
    }

    @Override
    public boolean isKeyPressed(int kc) {
        if (kc < keybuf.length) {
            return keybuf[kc] == State.PRESSED;
        }
        return false;
    }

    @Override
    public int getLastDownKey() {
        return lastKey;
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
