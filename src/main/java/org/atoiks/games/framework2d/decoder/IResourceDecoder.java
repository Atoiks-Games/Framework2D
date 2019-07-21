package org.atoiks.games.framework2d.decoder;

import java.io.InputStream;

import java.util.concurrent.Callable;

public interface IResourceDecoder<T> {

    public T decode(InputStream is) throws DecodeException;

    public default Callable<T> delayDecode(final InputStream is) throws DecodeException {
        // IO operation must be completed before it enters the callable
        final T obj = this.decode(is);
        return () -> obj;
    }
}
