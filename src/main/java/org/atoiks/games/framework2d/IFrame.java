package org.atoiks.games.framework2d;

public interface IFrame<T> extends AutoCloseable {

    public void init();
    public void loop();
    public void close();

    public T getRawFrame();

    public void setSize(int width, int height);
    public void setTitle(String title);

    public SceneManager getSceneManager();
}
