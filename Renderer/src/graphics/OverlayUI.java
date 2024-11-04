package src.graphics;

import javax.swing.*;
import java.awt.*;

public class OverlayUI extends JPanel {
    private final JLabel playerCoordinatesLabel;
    private final JLabel cameraCoordinatesLabel;

    public OverlayUI() {
        setOpaque(false);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        
        playerCoordinatesLabel = new JLabel();
        cameraCoordinatesLabel = new JLabel();
        
        playerCoordinatesLabel.setForeground(Color.WHITE);
        cameraCoordinatesLabel.setForeground(Color.WHITE);

        add(playerCoordinatesLabel);
        add(cameraCoordinatesLabel);
    }

    public void updateCoordinates(String playerCoords, String cameraCoords) {
        playerCoordinatesLabel.setText(playerCoords);
        cameraCoordinatesLabel.setText(cameraCoords);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
        g2d.setColor(new Color(0, 0, 0, 128));
        g2d.fillRect(0, 0, getWidth(), getHeight());
        g2d.dispose();
    }
}
