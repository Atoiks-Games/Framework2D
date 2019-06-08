package org.atoiks.games.framework2d.decoder;

import java.io.InputStream;
import java.io.IOException;
import java.io.Externalizable;
import java.io.ObjectInputStream;
import java.io.BufferedInputStream;

import java.util.function.Supplier;

public final class ExternalizableDecoder<T extends Externalizable> implements IResourceDecoder<T> {

    private final Supplier<? extends T> sup;

    private DecodeFailureBehaviour policy = DecodeFailureBehaviour.DECODE_EXCEPTION_ON_FAIL;

    // Marking it private just in case someone did left out the generic
    // parameter when constructing, in which case, type checking will
    // be completely disabled (which is very bad!)
    private ExternalizableDecoder(Supplier<? extends T> sup) {
        this.sup = sup;
    }

    public void setDecodeFailureBehaviour(DecodeFailureBehaviour policy) {
        this.policy = policy != null ? policy : DecodeFailureBehaviour.DECODE_EXCEPTION_ON_FAIL;
    }

    public DecodeFailureBehaviour getDecodeFailureBehaviour() {
        return policy;
    }

    @Override
    public T decode(InputStream is) throws DecodeException {
        final T data;
        try {
            data = sup.get();
        } catch (Exception ex) {
            throw new DecodeException(ex);
        }

        try {
            final ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(is));
            data.readExternal(ois);
        } catch (Exception ex) {
            switch (policy) {
                case RETURN_INSTANCE_ON_FAIL:
                    // Just print stacktrace, but return the default instance
                    ex.printStackTrace();
                    break;
                case SWALLOW_EXCEPTION_ON_FAIL:
                    // Just returns the default instance, that's all
                    // (why would you ever want to do this... oh well)
                    break;
                default:
                case DECODE_EXCEPTION_ON_FAIL:
                    // This is the default behaviour
                    throw new DecodeException(ex);
            }
        }

        return data;
    }

    public static <T extends Externalizable> ExternalizableDecoder<T> forInstance(Supplier<? extends T> instanceSupplier) {
        return new ExternalizableDecoder<>(instanceSupplier);
    }
}
