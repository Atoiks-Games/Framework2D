package org.atoiks.games.framework2d.swing;

import java.awt.Toolkit;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import java.awt.event.KeyAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.WindowAdapter;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.atoiks.games.framework2d.Input;
import org.atoiks.games.framework2d.FrameInfo;
import org.atoiks.games.framework2d.SceneManager;
import org.atoiks.games.framework2d.AbstractFrame;

public class Frame extends AbstractFrame<JFrame, Graphics2D> {

    private final JPanel canvas = new JPanel() {

        private static final long serialVersionUID = 91727385L;

        @Override
        protected void paintComponent(final Graphics g) {
            super.paintComponent(g);

            final Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
            graphics.g = g2d;
            graphics.width = this.getWidth();
            graphics.height = this.getHeight();
            sceneMgr.renderCurrentScene(graphics);
        }
    };

    private final SwingGraphics graphics = new SwingGraphics();

    private final JFrame frame;

    public Frame(FrameInfo info) {
        super(info.getFps(), new SceneManager<>(info));
        frame = new JFrame(info.getTitle());

        frame.setContentPane(canvas);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setResizable(info.isResizable());

        canvas.setPreferredSize(new Dimension(info.getWidth(), info.getHeight()));
        canvas.setIgnoreRepaint(true);

        frame.pack();
        frame.setLocationRelativeTo(null);

        // Create input devices and give them to input manager
        final Keyboard compKeyboard = new Keyboard();
        final Mouse compMouse = new Mouse();

        Input.provideKeyboard(compKeyboard);
        Input.provideMouse(compMouse);

        // Frame Listeners
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Frame.this.running = false;
            }
        });
        frame.addKeyListener(compKeyboard);

        // Allow canvas to receive special keys (tab and shift and stuff)
        frame.setFocusTraversalKeysEnabled(false);

        // Canvas Listeners
        canvas.addMouseListener(compMouse);
        canvas.addMouseMotionListener(compMouse);
        canvas.addMouseWheelListener(compMouse);
    }

    @Override
    protected int getWidth() {
        return canvas.getPreferredSize().width;
    }

    @Override
    protected int getHeight() {
        return canvas.getPreferredSize().height;
    }

    @Override
    protected void renderGame() {
        try {
            canvas.paintImmediately(canvas.getBounds());
        } catch (ClassCastException ex) {
            // Swallow this exception:
            // sun.java2d.NullSurfaceData cannot be cast to sun.java2d.opengl.OGLSurfaceData
            //
            // Similar to the bug link (except for it throws at a different path)
            // https://bugs.java.com/view_bug.do?bug_id=JDK-8158495
        }
    }

    @Override
    public void setSize(int width, int height) {
        canvas.setPreferredSize(new Dimension(width, height));
        frame.pack();
    }

    @Override
    public void setTitle(String title) {
        frame.setTitle(title);
    }

    @Override
    public JFrame getRawFrame() {
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
