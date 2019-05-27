package org.atoiks.games.framework2d.resolver;

import java.io.IOException;
import java.io.FileInputStream;

public final class ExternalResourceResolver implements IPathResolver {

    public static final ExternalResourceResolver INSTANCE = new ExternalResourceResolver();

    private ExternalResourceResolver() {
    }

    public FileInputStream openStream(String path) throws IOException {
        return new FileInputStream(path);
    }
}
