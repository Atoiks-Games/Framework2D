package org.atoiks.games.framework2d.java2d;

import java.awt.Insets;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.RenderingHints;
import java.awt.GraphicsEnvironment;

import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentAdapter;

import java.awt.image.BufferStrategy;

import org.atoiks.games.framework2d.Input;
import org.atoiks.games.framework2d.Scene;
import org.atoiks.games.framework2d.FrameInfo;
import org.atoiks.games.framework2d.SceneManager;
import org.atoiks.games.framework2d.AbstractFrame;

import org.atoiks.games.framework2d.java2d.decoder.JavaTextureDecoder;

public class Frame extends AbstractFrame {

    // see getTextureDecoder
    private static JavaTextureDecoder textureDecoder;

    private final JavaGraphics graphics;

    private final java.awt.Frame frame;
    private final Mouse compMouse;

    private Insets insets;

    private boolean shouldCallResize = true;
    private Scene lastScene = null;

    // screen dimension before entering fullscreen mode
    private int preFullScreenW;
    private int preFullScreenH;

    public Frame(FrameInfo info) {
        super(info);
        frame = new java.awt.Frame(info.getTitle());

        frame.setResizable(info.isResizable());

        // setSize only works when layout is null
        // don't need a layout manager anyway
        frame.setLayout(null);

        // pack so getInsets and createBufferStrategy works
        frame.pack();

        // Resize taking insets into account
        insets = frame.getInsets();
        frame.setSize(info.getWidth() + insets.left + insets.right, info.getHeight() + insets.top + insets.bottom);

        // Only repaint during rendering loop
        frame.setIgnoreRepaint(true);

        // Center the frame
        frame.setLocationRelativeTo(null);

        // Create input devices and give them to input manager
        final Keyboard compKeyboard = new Keyboard();
        compMouse = new Mouse();
        compMouse.setMouseShift(-insets.left, -insets.top);

        Input.provideKeyboard(compKeyboard);
        Input.provideMouse(compMouse);

        // Frame Listeners
        frame.addKeyListener(compKeyboard);
        frame.addMouseListener(compMouse);
        frame.addMouseMotionListener(compMouse);
        frame.addMouseWheelListener(compMouse);
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent evt) {
                shouldCallResize = true;
            }
        });
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Frame.this.running = false;
            }
        });

        // Allow canvas to receive special keys (tab and shift and stuff)
        frame.setFocusTraversalKeysEnabled(false);

        // Create graphics instance
        graphics = new JavaGraphics(this);
    }

    @Override
    public int getWidth() {
        return frame.getWidth() - insets.left - insets.right;
    }

    @Override
    public int getHeight() {
        return frame.getHeight() - insets.top - insets.bottom;
    }

    @Override
    protected void renderGame() {
        final BufferStrategy strategy = frame.getBufferStrategy();
        do {
            // The following loop ensures that the contents of the drawing buffer
            // are consistent in case the underlying surface was recreated
            do {
                // Get a new graphics context every time through the loop
                // to make sure the strategy is validated
                final Graphics2D g2d = (Graphics2D) strategy.getDrawGraphics();

                // Setup graphics object
                g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
                graphics.g = g2d;

                // Calculate offset
                final Insets inset = frame.getInsets();
                g2d.translate(inset.left, inset.top);

                // Render the graphics
                SceneManager.renderCurrentScene(graphics);

                // Dispose the graphics
                g2d.dispose();

                // Repeat the rendering if the drawing buffer contents
                // were restored
            } while (strategy.contentsRestored());

            // Display the buffer
            strategy.show();

            // Repeat the rendering if the drawing buffer was lost
        } while (strategy.contentsLost());
    }

    @Override
    protected void resizeGame() {
        insets = frame.getInsets();
        compMouse.setMouseShift(-insets.left, -insets.top);

        final Scene currentScene = SceneManager.getCurrentScene();
        if (shouldCallResize || lastScene != currentScene) {
            shouldCallResize = false;
            lastScene = currentScene;
            SceneManager.resizeCurrentScene(this.getWidth(), this.getHeight());
        }
    }

    @Override
    public void setSize(int width, int height) {
        frame.setSize(width + insets.left + insets.right, height + insets.top + insets.bottom);
    }

    @Override
    public void setTitle(String title) {
        frame.setTitle(title);
    }

    @Override
    public java.awt.Frame getRawFrame() {
        return frame;
    }

    @Override
    public void setVisible(final boolean status) {
        frame.setVisible(status);
    }

    @Override
    public boolean isVisible() {
        return frame.isVisible();
    }

    @Override
    public void setFullScreen(final boolean status) {
        if (frame.isUndecorated() == status) {
            // We are already in the fullscreen state
            // ignore and do nothing
            return;
        }

        // temporarily dispose for setUndecorated to work!
        frame.dispose();
        frame.setUndecorated(status);
        frame.setVisible(true);

        final GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        if (status) {
            // Save the screen size
            preFullScreenW = getWidth();
            preFullScreenH = getHeight();

            if (gd.isFullScreenSupported()) {
                // setFullScreenWindow seems to use its own buffer strategy?
                gd.setFullScreenWindow(frame);
            } else {
                // Max out the window as if it fills the whole screen
                frame.createBufferStrategy(2);
                frame.setExtendedState(java.awt.Frame.MAXIMIZED_BOTH);
            }

            // Get the frame focused
            frame.toFront();
            frame.requestFocus();
        } else {
            frame.createBufferStrategy(2);
            if (gd.isFullScreenSupported()) {
                exitExclusiveFullScreen();
            } else {
                // reset the extended state
                frame.setExtendedState(java.awt.Frame.NORMAL);
                // restore size
                setSize(preFullScreenW, preFullScreenH);
                // center the frame
                frame.setLocationRelativeTo(null);
            }
        }
    }

    private void exitExclusiveFullScreen() {
        final GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        if (gd.getFullScreenWindow() == frame) {
            gd.setFullScreenWindow(null);
        }
    }

    @Override
    public void init() {
        super.init();
        frame.setVisible(true);

        // Use double buffering
        frame.createBufferStrategy(2);
    }

    @Override
    public void close() {
        // Exit exclusive fullscreen mode if we are in it
        exitExclusiveFullScreen();

        super.close();
        frame.dispose();
    }

    @Override
    public JavaTextureDecoder getTextureDecoder() {
        JavaTextureDecoder local = textureDecoder;
        if (local == null) {
            synchronized (textureDecoder) {
                if (textureDecoder == null) {
                    textureDecoder = local = new JavaTextureDecoder();
                }
            }
        }
        return local;
    }
}
