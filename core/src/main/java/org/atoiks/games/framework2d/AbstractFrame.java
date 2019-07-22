package org.atoiks.games.framework2d;

import java.util.concurrent.FutureTask;

public abstract class AbstractFrame implements IFrame {

    private final IRuntime rt;

    private final float secPerUpdate;
    private final float msPerUpdate;

    protected AbstractFrame(final FrameInfo info, final IRuntime associatedRuntime) {
        this.rt = associatedRuntime;

        final float fps = info.getFps();
        this.secPerUpdate = 1.0f / fps;
        this.msPerUpdate = 1000.0f / fps;

        SceneManager.setFrameContext(this);
    }

    @Override
    public void loop() {
        double previous = System.currentTimeMillis();
        double steps = 0.0f;

        outer:
        while (this.shouldContinueRunning()) {
            final double now = System.currentTimeMillis();
            final double elapsed = now - previous;
            previous = now;
            steps += elapsed;

            this.resizeGame();
            this.pollDelayedTasks();

            while (steps >= msPerUpdate) {
                if (!SceneManager.updateCurrentScene(secPerUpdate)) {
                    return;
                }

                // Invoke frame update on input devices
                Input.invokeFrameUpdate();

                if (SceneManager.shouldSkipCycle()) {
                    // Reset time info
                    previous = System.currentTimeMillis();
                    steps = 0.0f;
                    continue outer; // Restart entire process
                }
                steps -= msPerUpdate;
            }

            this.renderGame();
            this.postRender();
        }
    }

    @Override
    public void init() {
        this.setVisible(true);
    }

    @Override
    public void close() {
        SceneManager.clearAllScenes();
    }

    private void pollDelayedTasks() {
        final FutureTask<?> task = ResourceManager.pollNextTask();
        if (task != null) {
            task.run();
        }
    }

    protected abstract boolean shouldContinueRunning();

    protected abstract void resizeGame();

    protected abstract void renderGame();

    protected void postRender() {
        // Default do nothing
    }

    @Override
    public final IRuntime getRuntime() {
        return this.rt;
    }
}
