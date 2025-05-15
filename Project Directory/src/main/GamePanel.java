package main;

import javax.swing.JPanel;
import java.awt.*;
//import UI_Scene.*;

public class GamePanel extends UI_Scene.BaseScene implements Runnable {
    final int originalTileSize = 16;
    final int scale = 3;
    final int tileSize = originalTileSize * scale;
    final int maxScreenCol = 7;//(가로 타일 개수)
    final int maxScreenRow = 12;//(세로 타일 개수)
    final int screenWidth = tileSize * maxScreenCol;//가로 픽셀 개수
    final int screenHeight = tileSize * maxScreenRow;//세로 픽셀 개수
    final double FPS = 60.0;
    KeyInputSystem keyIS = new KeyInputSystem();
    Thread gameThread;

    //Player Default Pos
    int playerX = 100, playerY = 100, playerSpeed = 5;
    public GamePanel() {//constructor
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);//better rendering performance
        this.addKeyListener(keyIS);
        this.setFocusable(true);
    }
    public void startGameThread(){
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        double drawInterval = 1000000000/FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long now;

        long timer = 0;
        int drawCount = 0;
        while(gameThread != null){
            now = System.nanoTime();

            delta += (now - lastTime)/drawInterval;
            timer += now - lastTime;
            lastTime = now;
            if(delta >= 1){
                Update();
                repaint();
                delta--;
                drawCount++;
            }
            if(timer>=1000000000){
                System.out.println("FPS : "+drawCount);
                drawCount = 0;
                timer = 0;
            }
//            System.out.println("Game Thread Running");
//            Update();
//            repaint();
        }
    }
    public void Update(){
        if(keyIS.isUp){playerY-=playerSpeed;}
        if(keyIS.isDown){playerY+=playerSpeed;}
        if(keyIS.isLeft){playerX-=playerSpeed;}
        if(keyIS.isRight){playerX+=playerSpeed;}
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.white);
        g2d.fillRect(playerX, playerY, tileSize, tileSize);
        g2d.dispose();//save memory
    }

    @Override
    public void setScene() {

    }

    @Override
    public void setGameObjectList() {

    }

    @Override
    public void setUISet() {

    }
}
