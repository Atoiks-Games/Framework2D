package org.atoiks.games.framework2d.decoder;

import java.io.InputStream;
import java.io.IOException;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public final class ImageDecoder implements IResourceDecoder<BufferedImage> {

    public static final ImageDecoder INSTANCE = new ImageDecoder();

    private ImageDecoder() {
    }

    @Override
    public BufferedImage decode(InputStream is) throws DecodeException {
        try {
            return ImageIO.read(is);
        } catch (Exception ex) {
            throw new DecodeException(ex);
        }
    }
}
