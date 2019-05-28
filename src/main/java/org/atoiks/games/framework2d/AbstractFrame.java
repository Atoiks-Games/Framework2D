package org.atoiks.games.framework2d;

public abstract class AbstractFrame implements IFrame {

    private static final boolean ON_MAC = System.getProperty("os.name").toLowerCase().indexOf("mac") >= 0;

    protected boolean running = true;

    private final float secPerUpdate;
    private final float msPerUpdate;

    protected AbstractFrame(final FrameInfo info) {
        final float fps = info.getFps();
        this.secPerUpdate = 1.0f / fps;
        this.msPerUpdate = 1000.0f / fps;

        SceneManager.setFrameContext(this, info);
    }

    @Override
    public void init() {
        if (ON_MAC) {
            try {
                Runtime.getRuntime().exec("defaults write NSGlobalDomain ApplePressAndHoldEnabled -bool false");
            } catch (Exception ex) {
                //
            }
        }
    }

    @Override
    public void loop() {
        double previous = System.currentTimeMillis();
        double steps = 0.0f;

        outer:
        while (running) {
            final double now = System.currentTimeMillis();
            final double elapsed = now - previous;
            previous = now;
            steps += elapsed;

            resizeGame();
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

            // Force redraw here
            renderGame();
        }
    }

    @Override
    public void close() {
        // Ensures leave for Scene gets called
        SceneManager.switchToScene(null);
        // Deinitalize all scenes
        SceneManager.callSceneDeinit();

        // Restore the mac stuff
        if (ON_MAC) {
            try {
                Runtime.getRuntime().exec("defaults write NSGlobalDomain ApplePressAndHoldEnabled -bool true");
            } catch (Exception ex) {
                //
            }
        }
    }

    protected abstract void resizeGame();

    protected abstract void renderGame();
}
