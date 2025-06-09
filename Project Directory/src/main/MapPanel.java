package main;

import Map_Audio.TileManager;
import UI_Scene.InGameManager;

import javax.swing.*;
import java.awt.*;
//import UI_Scene.*;

public class MapPanel extends JPanel implements Runnable {
    final int originalTileSize = 16;
    final int scale = 3;
    public final int tileSize = originalTileSize * scale;
    public final int maxScreenCol = 10;//(가로 타일 개수)
    public final int maxScreenRow = 20;//(세로 타일 개수)
    final int screenWidth = tileSize * maxScreenCol;//가로 픽셀 개수
    final int screenHeight = tileSize * maxScreenRow;//세로 픽셀 개수
    final double FPS = 60.0;

    public TileManager tileManager = new TileManager(this);
    KeyInputSystem keyIS = new KeyInputSystem();
    Thread gameThread;

    //Player Default Pos
    int playerX = 300, playerY = 300, playerSpeed = 5;
    public int getWidth(){
        return screenWidth;
    }
    public int getHeight(){
        return screenHeight;
    }

    public MapPanel() {    //  constructor

//        //  Initialize
//        this.name = name;
//        this.sid = sid;



        //  Setting
        this.setPreferredSize(new Dimension(900, 1600));
//        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);//better rendering performance
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
        double drawInterval = 1000000000 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long now;

        long timer = 0;
        int drawCount = 0;
        while (gameThread != null) {
            if(InGameManager.getInstance() != null) {
                if (InGameManager.getInstance().isPaused()) {
                    try {
                        Thread.sleep(10); // pause 상태 유지
                        continue;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            now = System.nanoTime();

            delta += (now - lastTime) / drawInterval;
            timer += now - lastTime;
            lastTime = now;
            if (delta >= 1) {
                Update();
                repaint();
                delta--;
                drawCount++;
            }
            if (timer >= 1000000000) {
                drawCount = 0;
                timer = 0;
            }

        }
    }

    public void Update() {
        tileManager.updateScroll(); // 배경 스크롤 업데이트
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
//        g2d.setColor(Color.green);
//        g2d.fillRect(playerX, playerY, tileSize, tileSize);
//        g2d.dispose();//save memory
      tileManager.draw(g2d);
      g2d.dispose();
    }
}
