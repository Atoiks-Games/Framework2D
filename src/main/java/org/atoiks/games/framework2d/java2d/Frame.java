package org.atoiks.games.framework2d.java2d;

import java.awt.Graphics2D;
import java.awt.RenderingHints;

import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentAdapter;

import java.awt.Insets;
import java.awt.image.BufferStrategy;

import org.atoiks.games.framework2d.Input;
import org.atoiks.games.framework2d.FrameInfo;
import org.atoiks.games.framework2d.SceneManager;
import org.atoiks.games.framework2d.AbstractFrame;

public class Frame extends AbstractFrame<java.awt.Frame, Graphics2D> {

    private final JavaGraphics graphics;

    private final java.awt.Frame frame;
    private final Insets insets;
    private final BufferStrategy strategy;

    private boolean shouldCallResize = true;
    private int lastSceneId = SceneManager.UNKNOWN_SCENE_ID;

    public Frame(FrameInfo info) {
        super(info.getFps(), new SceneManager<>(info));
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

        // Use double buffering
        frame.createBufferStrategy(2);
        strategy = frame.getBufferStrategy();

        // Only repaint during rendering loop
        frame.setIgnoreRepaint(true);

        // Center the frame
        frame.setLocationRelativeTo(null);

        // Create input devices and give them to input manager
        final Keyboard compKeyboard = new Keyboard();
        final Mouse compMouse = new Mouse(-insets.left, -insets.top);

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
                sceneMgr.renderCurrentScene(graphics);

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
        final int currentSceneId = sceneMgr.getCurrentSceneId();
        if (shouldCallResize || lastSceneId != currentSceneId) {
            shouldCallResize = false;
            lastSceneId = currentSceneId;
            sceneMgr.resizeCurrentScene(this.getWidth(), this.getHeight());
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
    public void init() {
        super.init();
        frame.setVisible(true);
    }

    @Override
    public void close() {
        super.close();
        frame.dispose();
    }
}
