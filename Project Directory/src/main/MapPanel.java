package main;

import Map_Audio.TileManager;
import UI_Scene.InGameManager;

import javax.swing.*;
import java.awt.*;

public class MapPanel extends JPanel implements Runnable {
    final int originalTileSize = 16;
    final int scale = 3;
    public final int tileSize = originalTileSize * scale;
    public final int maxScreenCol = 10;
    public final int maxScreenRow = 20;
    final int screenWidth = tileSize * maxScreenCol;
    final int screenHeight = tileSize * maxScreenRow;
    final double FPS = 60.0;

    public TileManager tileManager = new TileManager(this);
    KeyInputSystem keyIS = new KeyInputSystem();
    Thread gameThread;

    public MapPanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setDoubleBuffered(true);
        this.addKeyListener(keyIS);
        this.setFocusable(true);
        this.startGameThread();
        setOpaque(false);
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        double drawInterval = 1_000_000_000 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();

        while (gameThread != null) {
            long now = System.nanoTime();
            delta += (now - lastTime) / drawInterval;
            lastTime = now;

            if (delta >= 1) {
                // 항상 호출: 내부에서 pause 체크하여 타일 전환 및 스크롤 정지
                tileManager.updateScroll();
                repaint();
                delta--;
            }

            // CPU 사용량 완화
            try {
                Thread.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        tileManager.draw(g2d);
        g2d.dispose();
    }
}
