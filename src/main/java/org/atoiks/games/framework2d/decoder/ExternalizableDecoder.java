package org.atoiks.games.framework2d.decoder;

import java.io.InputStream;
import java.io.IOException;
import java.io.Externalizable;
import java.io.ObjectInputStream;
import java.io.BufferedInputStream;

import java.util.function.Supplier;

public final class ExternalizableDecoder<T extends Externalizable> implements IResourceDecoder<T> {

    private final Supplier<? extends T> sup;

    // Marking it private just in case someone did left out the generic
    // parameter when constructing, in which case, type checking will
    // be completely disabled (which is very bad!)
    private ExternalizableDecoder(Supplier<? extends T> sup) {
        this.sup = sup;
    }

    @Override
    public T decode(InputStream is) throws DecodeException {
        try {
            final T data = sup.get();
            final ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(is));
            data.readExternal(ois);
            return data;
        } catch (Exception ex) {
            throw new DecodeException(ex);
        }
    }

    public static <T extends Externalizable> ExternalizableDecoder<T> forInstance(Supplier<? extends T> instanceSupplier) {
        return new ExternalizableDecoder<>(instanceSupplier);
    }
}
