package org.atoiks.games.framework2d.decoder;

import java.io.InputStream;
import java.io.IOException;
import java.io.Serializable;
import java.io.ObjectInputStream;
import java.io.BufferedInputStream;

public final class SerializableDecoder<T extends Serializable> implements IResourceDecoder<T> {

    private static final SerializableDecoder INSTANCE = new SerializableDecoder();

    private SerializableDecoder() {
    }

    @Override
    @SuppressWarnings("unchecked")
    public T decode(InputStream is) throws DecodeException {
        try {
            final ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(is));
            return (T) ois.readObject();
        } catch (Exception ex) {
            throw new DecodeException(ex);
        }
    }

    // Without the dummy Class<T> parameter, incorrect code like
    //    final Object kelvin = ResourceManager.load("dummy", ObjectDecoder.getInstance());
    // will compile successfully when it should fail since Object does not subtype Serializable
    //
    // additional note regarding above code: if ObjectDecoder.<Object>getInstance() was used
    // instead, compilation will fail (which we want that). but why would we do that when we can
    // get guaranteed type checking everytime?
    @SuppressWarnings("unchecked")
    public static <T extends Serializable> SerializableDecoder<T> forType(Class<T> type) {
        return SerializableDecoder.INSTANCE;
    }
}
