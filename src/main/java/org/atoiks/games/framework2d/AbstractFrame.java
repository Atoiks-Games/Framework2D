package org.atoiks.games.framework2d;

public abstract class AbstractFrame<T, G> implements IFrame<T> {

    private static final boolean ON_MAC = System.getProperty("os.name").toLowerCase().indexOf("mac") >= 0;

    protected boolean running = true;

    protected SceneManager<G> sceneMgr;
    private final float secPerUpdate;
    private final float msPerUpdate;

    protected AbstractFrame(final float fps, SceneManager<G> mgr) {
        this.sceneMgr = mgr;
        this.secPerUpdate = 1.0f / fps;
        this.msPerUpdate = 1000.0f / fps;
    }

    public void init() {
        if (ON_MAC) {
            try {
                Runtime.getRuntime().exec("defaults write NSGlobalDomain ApplePressAndHoldEnabled -bool false");
            } catch (Exception ex) {
                //
            }
        }
    }

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
                if (!sceneMgr.updateCurrentScene(secPerUpdate)) {
                    return;
                }

                // Invoke frame update on input devices
                Input.invokeFrameUpdate();

                if (sceneMgr.shouldSkipCycle()) {
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

    public void close() {
        // Ensures leave for Scene gets called
        sceneMgr.switchToScene(-1);
        // Deinitalize all game scenes
        sceneMgr.callDeinit();

        // Restore the mac stuff
        if (ON_MAC) {
            try {
                Runtime.getRuntime().exec("defaults write NSGlobalDomain ApplePressAndHoldEnabled -bool true");
            } catch (Exception ex) {
                //
            }
        }
    }

    @Override
    public SceneManager getSceneManager() {
        return sceneMgr;
    }

    protected abstract int getWidth();

    protected abstract int getHeight();

    protected abstract void resizeGame();

    protected abstract void renderGame();
}
