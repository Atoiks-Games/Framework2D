package org.atoiks.games.framework2d;

import java.io.Serializable;

public abstract class Scene implements IScene, Serializable {

    private static final long serialVersionUID = -8839650470492997448L;

    protected transient SceneManager scene;

    protected Scene() {
    }

    final void attachSceneManager(SceneManager mgr) {
        this.scene = mgr;
    }

    @Override
    public void render(IGraphics g) {
    }

    @Override
    public boolean update(float dt) {
        return true;
    }

    @Override
    public void resize(int x, int y) {
    }

    @Override
    public void init() {
    }

    @Override
    public void deinit() {
    }

    @Override
    public void enter(String from) {
    }

    @Override
    public void leave() {
    }
}
