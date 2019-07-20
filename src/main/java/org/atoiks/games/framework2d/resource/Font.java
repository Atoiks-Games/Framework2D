package org.atoiks.games.framework2d.resource;

import org.atoiks.games.framework2d.IGraphics;

public interface Font extends Resource {

    public Font deriveSize(float size);

    public float getSize();

    public void renderText(IGraphics g, String text, float x, float y);
}
