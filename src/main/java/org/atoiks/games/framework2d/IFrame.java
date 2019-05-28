package org.atoiks.games.framework2d;

public interface IFrame extends AutoCloseable {

    public void init();
    public void loop();
    public void close();

    public Object getRawFrame();

    public void setFullScreen(boolean status);
    public void setSize(int width, int height);
    public int getWidth();
    public int getHeight();

    public void setVisible(boolean status);
    public boolean isVisible();

    public void setTitle(String title);
}
