package main;

import Map_Audio.TileMapGenerator;

import javax.swing.*;
import java.awt.*;

public class MapPanel extends JPanel implements Runnable {
    final int resPixelSize = 16;//사용 타일 애셋 16*16
    final int scale = 3;//3배로 키워서 사용
    public final int tileSize = resPixelSize * scale;//실제 게임 타일 사이즈

    public final int maxScreenCol = 10;//가로 타일개수
    public final int maxScreenRow = 20;//세로 타일 개수
    final double FPS = 60.0;//항상 60프레임 유지

    public TileMapGenerator tileMapGenerator = new TileMapGenerator(this);//tilemapGenerator 갖고오기
    Thread gameThread;

    public MapPanel(int screenWidth, int screenHeight) {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setDoubleBuffered(true);
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

        while (gameThread != null) {//delta time 기법으로 update 호출
            long now = System.nanoTime();
            delta += (now - lastTime) / drawInterval;
            lastTime = now;

            if (delta >= 1) {//델타타임만큼 보정
                // 항상 호출: 내부에서 pause 체크하여 타일 전환 및 스크롤 정지
                tileMapGenerator.updateScroll();
                delta--;
            }
            repaint();//보정 후 렌더링

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
        tileMapGenerator.draw(g2d);
        g2d.dispose();
    }
}