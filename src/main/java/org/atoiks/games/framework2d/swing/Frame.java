package org.atoiks.games.framework2d.swing;

import java.awt.Toolkit;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.WindowAdapter;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.atoiks.games.framework2d.FrameInfo;
import org.atoiks.games.framework2d.SceneManager;
import org.atoiks.games.framework2d.AbstractFrame;

public class Frame extends AbstractFrame<JFrame, KeyAdapter, MouseAdapter, Graphics2D> {

    private final JPanel canvas = new JPanel() {

        private static final long serialVersionUID = 91727385L;

        @Override
        protected void paintComponent(final Graphics g) {
            super.paintComponent(g);

            graphics.g = (Graphics2D) g;
            graphics.width = this.getWidth();
            graphics.height = this.getHeight();
            sceneMgr.renderCurrentScene(graphics);
        }
    };

    private final SwingGraphics graphics = new SwingGraphics();

    private final JFrame frame;

    public Frame(FrameInfo info) {
        super(info.getFps(), new SceneManager(new Keyboard(), new Mouse(), info.getScenes()));
        frame = new JFrame(info.getTitle());

        frame.setContentPane(canvas);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setResizable(info.isResizable());
        canvas.setPreferredSize(new Dimension(info.getWidth(), info.getHeight()));
        frame.pack();
        frame.setLocationRelativeTo(null);

        // Frame Listeners
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Frame.this.running = false;
            }
        });
        frame.addKeyListener(sceneMgr.keyboard().getRawInputDevice());

        // Canvas Listeners
        canvas.addMouseListener(sceneMgr.mouse().getRawInputDevice());
        canvas.addMouseMotionListener(sceneMgr.mouse().getRawInputDevice());
        canvas.addMouseWheelListener(sceneMgr.mouse().getRawInputDevice());
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
        canvas.repaint();
        Toolkit.getDefaultToolkit().sync();
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