package src.graphics;

import src.entities.World;
import src.entities.Player;
import src.entities.Camera;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class RendererManager extends JPanel {
    private final World world;
    private JFrame frame;
    private static int width = 1920;
    private static int height = 1080;
    
    private final OverlayUI overlayUI;

    public RendererManager() {
        world = new World();
        adjustAspectRatio();
        setPreferredSize(new Dimension(width, height));
        setBackground(Color.BLACK);
        setFocusable(true);
        
        overlayUI = new OverlayUI();
        overlayUI.setBounds(0, 0, width, height); 
        add(overlayUI);
        
        updateCoordinates();
    }

    public void initCanvas(KeyListener listener) {
        addKeyListener(listener);
    }

    private void adjustAspectRatio() {
        if (width > height) {
            width = (width - height) % width;
        } else {
            height = (height - width) % height;
        }
    }

    public void createWindow() {
        frame = new JFrame("Game");
        frame.setLayout(new BorderLayout());
        
        frame.add(this, BorderLayout.CENTER);
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void processInput(KeyEvent e) {
        switch (e.getKeyChar()) {
            case 'w': world.getPlayer().moveForward(); break;
            case 's': world.getPlayer().moveBackward(); break;
            case 'a': world.getPlayer().moveLeft(); break;
            case 'd': world.getPlayer().moveRight(); break;
        }

        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP: world.getCamera().rotateAroundPlayer(0, 0.1); break;
            case KeyEvent.VK_DOWN: world.getCamera().rotateAroundPlayer(0, -0.1); break;
            case KeyEvent.VK_LEFT: world.getCamera().rotateAroundPlayer(-0.1, 0); break;
            case KeyEvent.VK_RIGHT: world.getCamera().rotateAroundPlayer(0.1, 0); break;
        }

        updateCoordinates();
        repaint();
    }

    private void updateCoordinates() {
        Player player = world.getPlayer();
        Camera camera = world.getCamera();
        double[] cameraPosition = camera.getPosition(player);

        String playerCoords = String.format("Player: X=%.2f Y=%.2f Z=%.2f", player.getX(), player.getY(), player.getZ());
        String cameraCoords = String.format("Camera: X=%.2f Y=%.2f Z=%.2f", cameraPosition[0], cameraPosition[1], cameraPosition[2]);
        
        overlayUI.updateCoordinates(playerCoords, cameraCoords);
    }

    public void update() {
        updateCoordinates();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        world.draw(g2d, getWidth(), getHeight());
    }
}
