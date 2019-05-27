package org.atoiks.games.framework2d.resolver;

import java.io.InputStream;
import java.io.IOException;

public interface IPathResolver {

    public InputStream openStream(String path) throws IOException;
}
