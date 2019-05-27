package org.atoiks.games.framework2d;

import java.io.InputStream;
import java.io.IOException;

import java.util.Map;
import java.util.HashMap;

import org.atoiks.games.framework2d.decoder.DecodeException;
import org.atoiks.games.framework2d.decoder.IResourceDecoder;

import org.atoiks.games.framework2d.resolver.IPathResolver;
import org.atoiks.games.framework2d.resolver.InternalResourceResolver;

public final class ResourceManager {

    private static final Map<String, Object> CACHE = new HashMap<>();

    private ResourceManager() {
    }

    public static <T> T reload(final String path, final IPathResolver resolver, final IResourceDecoder<? extends T> decoder) throws DecodeException {
        try (final InputStream is = resolver.openStream(path)) {
            final T obj = decoder.decode(is);
            CACHE.put(path, obj);
            return obj;
        } catch (IOException ex) {
            throw new DecodeException(ex);
        }
    }

    public static <T> T reload(final String path, final IResourceDecoder<? extends T> decoder) throws DecodeException {
        return reload(path, InternalResourceResolver.INSTANCE, decoder);
    }

    public static <T> T load(final String path, final IPathResolver resolver, final IResourceDecoder<? extends T> decoder) throws DecodeException {
        final T existing = get(path);
        if (existing != null) {
            return existing;
        }

        return reload(path, resolver, decoder);
    }

    public static <T> T load(final String path, final IResourceDecoder<? extends T> decoder) throws DecodeException {
        return load(path, InternalResourceResolver.INSTANCE, decoder);
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
