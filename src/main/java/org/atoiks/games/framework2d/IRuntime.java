package org.atoiks.games.framework2d;

import org.atoiks.games.framework2d.decoder.TextureDecoder;

public interface IRuntime {

    public IFrame createFrame(FrameInfo info);

    public TextureDecoder getTextureDecoder();
}
