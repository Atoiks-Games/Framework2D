package org.atoiks.games.framework2d.decoder;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.BufferedInputStream;
import java.io.IOException;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public final class ObjectDecoder<T> implements IResourceDecoder<T> {

    public static final ObjectDecoder INSTANCE = new ObjectDecoder();

    private ObjectDecoder() {
    }

    @SuppressWarnings("unchecked")
    public T decode(InputStream is) throws DecodeException {
        try {
            final ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(is));
            return (T) ois.readObject();
        } catch (Exception ex) {
            throw new DecodeException(ex);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> ObjectDecoder<T> getInstance() {
        return INSTANCE;
    }
}
