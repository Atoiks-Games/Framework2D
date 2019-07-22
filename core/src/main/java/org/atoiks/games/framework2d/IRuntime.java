package org.atoiks.games.framework2d;

import org.atoiks.games.framework2d.decoder.FontDecoder;
import org.atoiks.games.framework2d.decoder.TextureDecoder;

public interface IRuntime {

    public IFrame createFrame(FrameInfo info);

    public FontDecoder<?> getFontDecoder();
    public TextureDecoder<?> getTextureDecoder();
}
