package org.atoiks.games.framework2d;

import java.awt.Toolkit;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Frame extends AbstractFrame<JFrame> {

    private final JPanel canvas = new JPanel() {

        private static final long serialVersionUID = 91727385L;

        @Override
        protected void paintComponent(final Graphics g) {
            super.paintComponent(g);
            sceneMgr.renderCurrentScene(g);
        }
    };

    private final Keyboard kb = new Keyboard();
    private final Mouse m = new Mouse();

    private final JFrame frame;

    public Frame(FrameInfo info) {
        super(info.fps, new SceneManager(info.scenes));
        frame = new JFrame(info.titleName);

        frame.setContentPane(canvas);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setResizable(info.resizable);
        canvas.setPreferredSize(new Dimension(info.width, info.height));
        frame.pack();
        frame.setLocationRelativeTo(null);

        // Frame Listeners
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Frame.this.running = false;
            }
        });
        frame.addKeyListener(kb.getRawInputDevice());

        // Canvas Listeners
        canvas.addMouseListener(m.getRawInputDevice());
        canvas.addMouseMotionListener(m.getRawInputDevice());
        canvas.addMouseWheelListener(m.getRawInputDevice());

        // Transfer input devices to scene manager
        sceneMgr.kbHandle = kb;
        sceneMgr.mouseHandle = m;
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