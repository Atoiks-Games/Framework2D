package org.atoiks.games.framework2d.java2d;

import org.atoiks.games.framework2d.IRuntime;
import org.atoiks.games.framework2d.FrameInfo;

import org.atoiks.games.framework2d.decoder.FontDecoder;
import org.atoiks.games.framework2d.decoder.TextureDecoder;

import org.atoiks.games.framework2d.java2d.decoder.JavaFontDecoder;
import org.atoiks.games.framework2d.java2d.decoder.JavaTextureDecoder;

public final class JavaRuntime implements IRuntime {

    private static class SingletonHelper {

        private static final JavaFontDecoder fontDecoder = new JavaFontDecoder();
        private static final JavaTextureDecoder textureDecoder = new JavaTextureDecoder();
    }

    @Override
    public Frame createFrame(FrameInfo info) {
        return new Frame(info, this);
    }

    @Override
    public FontDecoder<?> getFontDecoder() {
        return SingletonHelper.fontDecoder;
    }

    @Override
    public TextureDecoder<?> getTextureDecoder() {
        return SingletonHelper.textureDecoder;
    }
}
