package org.atoiks.games.framework2d;

final class SceneHolder {

    public Scene scene;

    private boolean initFlag;

    public SceneHolder(Scene scene) {
        this.scene = scene;
    }

    public boolean tryInit() {
        if (initFlag) {
            return false;
        }

        scene.init();
        return (initFlag = true);
    }

    public boolean tryDeinit() {
        if (!initFlag) {
            return false;
        }

        scene.deinit();
        return (initFlag = false);
    }

    public void callEnter(String prevId) {
        tryInit();
        scene.enter(prevId);
    }

    public void callLeave() {
        // Only leave if scene is initialized
        // (if it is uninitialized, it wouldnt have even entered)
        if (initFlag) {
            scene.leave();
        }
    }

    public void callRender(IGraphics g) {
        tryInit();
        scene.render(g);
    }

    public boolean callUpdate(float dt) {
        tryInit();
        return scene.update(dt);
    }

    public void callResize(int w, int h) {
        tryInit();
        scene.resize(w, h);
    }
}
