package org.atoiks.games.framework2d.resolver;

import java.io.InputStream;
import java.io.IOException;

public final class InternalResourceResolver implements IPathResolver {

    public static final InternalResourceResolver INSTANCE = new InternalResourceResolver();

    private InternalResourceResolver() {
    }

    public InputStream openStream(String path) throws IOException {
        // ClassLoader treats all paths as absolutes.
        // A path of "/hello/world.txt" would break if the leading '/'
        // is not removed!
        if (path.charAt(0) == '/') {
            path = path.substring(1);
        }

        final InputStream is = ClassLoader.getSystemResourceAsStream(path);
        if (is == null) {
            throw new IOException("Cannot open internal resource: " + path);
        }
        return is;
    }
}
