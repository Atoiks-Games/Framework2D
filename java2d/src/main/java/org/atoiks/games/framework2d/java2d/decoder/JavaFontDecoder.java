package org.atoiks.games.framework2d.java2d.decoder;

import java.io.InputStream;
import java.io.IOException;

import java.awt.Font;

import org.atoiks.games.framework2d.decoder.FontDecoder;
import org.atoiks.games.framework2d.decoder.DecodeException;

import org.atoiks.games.framework2d.java2d.resource.JavaFont;

public final class JavaFontDecoder implements FontDecoder<JavaFont> {

    @Override
    public JavaFont decode(InputStream is) throws DecodeException {
        try {
            return new JavaFont(Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(16f));
        } catch (Exception ex) {
            throw new DecodeException(ex);
        }
    }
}
