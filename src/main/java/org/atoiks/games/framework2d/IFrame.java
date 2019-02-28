package org.atoiks.games.framework2d;

public interface IFrame extends AutoCloseable {

    public void init();
    public void loop();
    public void close();

    public Object getRawFrame();

    public void setSize(int width, int height);
    public void setTitle(String title);
    public void setFullScreen(boolean status);

    public int getWidth();
    public int getHeight();

    public SceneManager getSceneManager();
}
