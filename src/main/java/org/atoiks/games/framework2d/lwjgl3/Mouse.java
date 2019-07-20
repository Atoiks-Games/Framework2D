package org.atoiks.games.framework2d.lwjgl3;
import java.util.Arrays;

import org.lwjgl.glfw.GLFWScrollCallback;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWCursorEnterCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

import org.atoiks.games.framework2d.IMouse;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LAST;

/* package */ final class Mouse implements IMouse {

    private enum MouseState {
        PRESSED, RELEASED, HELD;
    }

    private final boolean[] state;
    private final MouseState[] poll;

    private int localX, localY;
    private int wheelRot;
    private boolean inFrame;
    private boolean moved;

    /* package */ final GLFWMouseButtonCallback mouseButtonCb = new GLFWMouseButtonCallback() {
        @Override
        public void invoke(long window, int button, int action, int mods) {
            if (button < state.length) {
                state[button] = action == GLFW_PRESS;
            }
        }
    };

    /* package */ final GLFWCursorPosCallback cursorPosCb = new GLFWCursorPosCallback() {
        @Override
        public void invoke(long window, double x, double y) {
            localX = (int) x;
            localY = (int) y;
            moved = true;
        }
    };

    /* package */ final GLFWCursorEnterCallback cursorEnterCb = new GLFWCursorEnterCallback() {
        @Override
        public void invoke(long window, boolean entered) {
            inFrame = entered;
        }
    };

    /* package */ final GLFWScrollCallback scrollCb = new GLFWScrollCallback() {
        @Override
        public void invoke(long window, double xoffset, double yoffset) {
            wheelRot = (int) yoffset;
        }
    };

    public Mouse() {
        state = new boolean[GLFW_MOUSE_BUTTON_LAST];
        poll = new MouseState[GLFW_MOUSE_BUTTON_LAST];
        Arrays.fill(poll, MouseState.RELEASED);
    }

    @Override
    public void reset() {
        Arrays.fill(poll, MouseState.RELEASED);
        wheelRot = 0;
        moved = false;

        localX = localY = 0;
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
    public int getX() {
        return localX;
    }

    @Override
    public int getY() {
        return localY;
    }

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

    public boolean isButtonClicked(int btn) {
        if (btn < poll.length) {
            return poll[btn] == MouseState.PRESSED;
        }
        return false;
    }

    public boolean isInFrame() {
        return inFrame;
    }

    public boolean mouseMoved() {
        return moved;
    }
}
