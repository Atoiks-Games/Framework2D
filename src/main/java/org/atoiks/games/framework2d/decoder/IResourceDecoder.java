package org.atoiks.games.framework2d.decoder;

import java.io.InputStream;

public interface IResourceDecoder<T> {

    public T decode(InputStream is) throws DecodeException;
}
