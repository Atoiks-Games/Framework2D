package org.atoiks.games.framework2d;

public abstract class AbstractFrame implements IFrame {

    private static final boolean ON_MAC = System.getProperty("os.name").toLowerCase().indexOf("mac") >= 0;

    protected boolean running = true;

    protected SceneManager sceneMgr;
    private final float secPerUpdate;
    private final float msPerUpdate;

    protected AbstractFrame(final float fps, SceneManager mgr) {
        this.sceneMgr = mgr;
        this.secPerUpdate = 1.0f / fps;
        this.msPerUpdate = 1000.0f / fps;

        this.sceneMgr.frame = this;
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

        this.sceneMgr.callLoaderInit();
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

    @Override
    public void close() {
        // Ensures leave for Scene gets called
        sceneMgr.switchToScene(null);
        // Deinitalize all game scenes
        sceneMgr.callGameSceneDeinit();
        // Deinitialize loader
        sceneMgr.callLoaderDeinit();

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

    protected abstract void resizeGame();

    protected abstract void renderGame();
}
