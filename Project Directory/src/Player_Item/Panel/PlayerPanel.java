// src/Player_Item/Panel/PlayerPanel.java
package Player_Item.Panel;

import Player_Item.InputController;
import Player_Item.Model.Player;
import Player_Item.Model.Bullet;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 플레이어 이동 및 총알 발사를 처리하는 패널
 */
public class PlayerPanel extends JPanel implements Runnable {
    private final Player player;              // 플레이어 모델
    private final InputController input;      // 키 입력 컨트롤러
    private final List<Bullet> bullets = new ArrayList<>();

    private final double FPS = 60.0;
    private Thread gameThread;

    private long lastFireTime = 0;
    private final long fireInterval = 300;    // 발사 쿨다운(ms)

    private int shotCount = 3;                // 발사 개수 (1,2,3...)
    private final int bulletSpacing = 12;     // 총알 간 가로 간격(px)

    public PlayerPanel(int width, int height) {
        setOpaque(false);
        setPreferredSize(new Dimension(width, height));

        // 플레이어 초기 위치
        player = new Player("player2.png", width/2 - 30, height - 100);

        // 키 입력 설정
        input = new InputController();
        setFocusable(true);
        addKeyListener(input);
        addHierarchyListener(e -> {
            if (isShowing()) requestFocusInWindow();
        });

        // 게임 루프 시작
        startGameThread();
    }

    private void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        double drawInterval = 1_000_000_000.0 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long now;
        long timer = 0;
        int drawCount = 0;

        while (gameThread != null) {
            now = System.nanoTime();
            delta += (now - lastTime) / drawInterval;
            timer += now - lastTime;
            lastTime = now;

            if (delta >= 1) {
                updateGame();
                repaint();
                delta--;
                drawCount++;
            }

            if (timer >= 1_000_000_000) {
                System.out.println("FPS: " + drawCount);
                drawCount = 0;
                timer = 0;
            }
        }
    }

    /**
     * 게임 로직 업데이트
     */
    private void updateGame() {
        // 플레이어 이동
        int dx = input.isLeft() ? -1 : input.isRight() ? 1 : 0;
        int dy = input.isUp()   ? -1 : input.isDown()  ? 1 : 0;
        player.move(dx, dy, getWidth(), getHeight());

        // 총알 발사 처리
        if (input.isFire()) {
            long nowTime = System.currentTimeMillis();
            if (nowTime - lastFireTime >= fireInterval) {
                Rectangle pr = player.getBounds();
                int centerX = pr.x + pr.width / 2;
                int y = pr.y;

                // shotCount 개수만큼 발사, 가운데 정렬
                int startIdx = -(shotCount - 1) / 2;
                for (int i = 0; i < shotCount; i++) {
                    int offsetX = (startIdx + i) * bulletSpacing;
                    bullets.add(new Bullet("bullet2.png", centerX + offsetX, y));
                }
                lastFireTime = nowTime;
            }
        }

        // 총알 업데이트 및 비활성 총알 제거
        Iterator<Bullet> it = bullets.iterator();
        while (it.hasNext()) {
            Bullet b = it.next();
            b.update();
            if (!b.isActive()) {
                it.remove();
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.red);

        // 총알 그리기
        for (Bullet b : bullets) {
            b.draw(g);
            g2d.fillRect(b.getX(), b.getY(), 2, 2);
        }
        // 플레이어 그리기
        player.draw(g);
        g2d.fillRect(player.getX(), player.getY(), 10, 10);
    }

    /**
     * 발사 개수 설정
     */
    public void setShotCount(int count) {
        this.shotCount = Math.max(1, count);
    }

    /**
     * 현재 발사 개수 반환
     */
    public int getShotCount() {
        return shotCount;
    }
}
