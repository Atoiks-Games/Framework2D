package org.atoiks.games.framework2d.decoder;

public class DecodeException extends RuntimeException {

    public DecodeException() {
    }

    public DecodeException(String msg) {
        super(msg);
    }

    public DecodeException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public DecodeException(Throwable cause) {
        super(cause);
    }
}
