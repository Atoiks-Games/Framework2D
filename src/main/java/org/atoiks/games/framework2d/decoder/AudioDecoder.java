package org.atoiks.games.framework2d.decoder;

import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedInputStream;

import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public final class AudioDecoder implements IResourceDecoder<Clip> {

    public static final AudioDecoder INSTANCE = new AudioDecoder();

    private AudioDecoder() {
    }

    public Clip decode(InputStream is) throws DecodeException {
        try (final AudioInputStream in = AudioSystem.getAudioInputStream(new BufferedInputStream(is))) {
            final Clip clip = AudioSystem.getClip(null);
            clip.open(in);
            clip.stop();
            return clip;
        } catch (IOException | LineUnavailableException | UnsupportedAudioFileException ex) {
            throw new DecodeException(ex);
        }
    }
}
