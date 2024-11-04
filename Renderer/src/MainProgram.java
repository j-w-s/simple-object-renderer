package src;

import src.graphics.RendererManager;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.Timer;

public class MainProgram implements KeyListener {
    private final RendererManager rendererManager;
    private static final int fps = 240;
    private Timer gameLoop;

    public MainProgram() {
        rendererManager = new RendererManager();
        rendererManager.initCanvas(this); 
        initGameLoop();
    }

    @SuppressWarnings("unused")
    private void initGameLoop() {
        int delay = 1000/fps;
        
        gameLoop = new Timer(delay, e -> {
            update();
            rendererManager.repaint();
        });
        gameLoop.setCoalesce(true);
    }

    private void update() {
        rendererManager.update();
    }

    public static void main(String[] args) {
        MainProgram program = new MainProgram();
        program.start();
    }

    public void start() {
        rendererManager.createWindow();
        gameLoop.start();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        rendererManager.processInput(e);
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}
}
