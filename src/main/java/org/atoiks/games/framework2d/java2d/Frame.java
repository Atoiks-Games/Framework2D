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

    private final JavaGraphics graphics = new JavaGraphics();

    private final java.awt.Frame frame;
    private final Insets insets;

    private BufferStrategy strategy;

    private boolean shouldCallResize = true;
    private int lastSceneId = SceneManager.UNKNOWN_SCENE_ID;

    public Frame(FrameInfo info) {
        super(info.getFps(), new SceneManager<>(info));
        frame = new java.awt.Frame(info.getTitle());

        frame.setResizable(info.isResizable());

        // setSize only works when layout is null
        // don't need a layout manager anyway
        frame.setLayout(null);

        // temporarily setVisible since Insets only
        // work when frame is visible

        frame.setVisible(true);
        insets = frame.getInsets();
        setSize(info.getWidth(), info.getHeight());
        frame.setVisible(false);

        // Only repaint during rendering loop
        frame.setIgnoreRepaint(true);

        // Center the frame
        frame.setLocationRelativeTo(null);

        // Create input devices and give them to input manager
        final Keyboard compKeyboard = new Keyboard();
        final Mouse compMouse = new Mouse() {
            @Override
            public final int getLocalX() {
                return super.getLocalX() - Frame.this.insets.left;
            }

            @Override
            public final int getLocalY() {
                return super.getLocalY() - Frame.this.insets.top;
            }
        };

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
    }

    @Override
    protected int getWidth() {
        return frame.getWidth() - insets.left - insets.right;
    }

    @Override
    protected int getHeight() {
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
                graphics.width = this.getWidth();
                graphics.height = this.getHeight();

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

        // Use double buffering
        frame.createBufferStrategy(2);
        strategy = frame.getBufferStrategy();
    }

    @Override
    public void close() {
        super.close();
        frame.dispose();
    }
}
