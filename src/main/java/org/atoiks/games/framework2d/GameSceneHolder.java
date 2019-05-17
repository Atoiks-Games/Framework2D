package org.atoiks.games.framework2d;

final class GameSceneHolder {

    public GameScene gs;

    private boolean initFlag;

    public GameSceneHolder(GameScene gs) {
        this.gs = gs;
    }

    public boolean tryInit() {
        if (initFlag) {
            return false;
        }

        gs.init();
        return (initFlag = true);
    }

    public boolean tryDeinit() {
        if (!initFlag) {
            return false;
        }

        gs.deinit();
        return (initFlag = false);
    }

    public void callEnter(String prevId) {
        tryInit();
        gs.enter(prevId);
    }

    public void callLeave() {
        // Only leave if scene is initialized
        // (if it is uninitialized, it wouldnt have even entered)
        if (initFlag) {
            gs.leave();
        }
    }

    public void callRender(IGraphics g) {
        tryInit();
        gs.render(g);
    }

    public boolean callUpdate(float dt) {
        tryInit();
        return gs.update(dt);
    }

    public void callResize(int w, int h) {
        tryInit();
        gs.resize(w, h);
    }
}
