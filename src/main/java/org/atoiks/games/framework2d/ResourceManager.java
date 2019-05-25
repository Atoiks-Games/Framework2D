package org.atoiks.games.framework2d;

import java.io.InputStream;

import java.util.Map;
import java.util.HashMap;
import java.util.function.Function;

import org.atoiks.games.framework2d.decoder.DecodeException;
import org.atoiks.games.framework2d.decoder.IResourceDecoder;

public final class ResourceManager {

    private static final Map<String, Object> CACHE = new HashMap<>();

    private ResourceManager() {
    }

    public static <T> T reload(final String path, final IResourceDecoder<? extends T> decoder) throws DecodeException {
        final InputStream is = ResourceManager.class.getResourceAsStream(path);
        if (is == null) {
            return null;
        }

        final T obj = decoder.decode(is);
        CACHE.put(path, obj);
        return obj;
    }

    public static <T> T load(final String path, final IResourceDecoder<? extends T> decoder) throws DecodeException {
        final T existing = get(path);
        if (existing != null) {
            return existing;
        }

        return reload(path, decoder);
    }

    @SuppressWarnings("unchecked")
    public static <T> T get(final String path) {
        return (T) CACHE.get(path);
    }

    public static void unload(final String path) {
        if (CACHE.containsKey(path)) {
            CACHE.remove(path);
        }
    }

    public static void unloadAll() {
        CACHE.clear();
    }
}
