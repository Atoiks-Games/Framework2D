package org.atoiks.games.framework2d.java2d.decoder;

import java.io.InputStream;
import java.io.IOException;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import org.atoiks.games.framework2d.decoder.TextureDecoder;
import org.atoiks.games.framework2d.decoder.DecodeException;

import org.atoiks.games.framework2d.java2d.resource.JavaTexture;

public final class JavaTextureDecoder implements TextureDecoder<JavaTexture> {

    @Override
    public JavaTexture decode(InputStream is) throws DecodeException {
        try {
            return new JavaTexture(ImageIO.read(is));
        } catch (Exception ex) {
            throw new DecodeException(ex);
        }
    }
}
