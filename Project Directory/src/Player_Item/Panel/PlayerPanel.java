// src/Player_Item/Panel/PlayerPanel.java
package Player_Item.Panel;

import Enemies.Enemy;
import Enemies.EnemyPanel;
import Player_Item.InputController;
import Player_Item.Model.Item;
import Player_Item.Model.Player;
import Player_Item.Model.Bullet;
import UI_Scene.InGameManager;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 플레이어 이동 및 총알 발사를 처리하는 패널
 */
public class PlayerPanel extends JPanel implements Runnable {
    public final Player player;              // 플레이어 모델
    private final InputController input;      // 키 입력 컨트롤러
    public final List<Bullet> bullets = new ArrayList<>();
    public final List<Item> items = new ArrayList<>();  // 아이템 리스트

    private EnemyPanel enemyPanel; // 충돌처리 등을 위해 enemyPanel 가져오기

    private final double FPS = 60.0;
    private Thread gameThread;

    private long lastFireTime = 0;
    private final long fireInterval = 200;    // 발사 쿨다운(ms)

    private int shotCount = 3;                // 발사 개수 (1,2,3...)
    private final int bulletSpacing = 12;     // 총알 간 가로 간격(px)

    public PlayerPanel(int panelWidth, int panelHeight) {
        setOpaque(false);
        setPreferredSize(new Dimension(panelWidth, panelHeight));

        // 플레이어 초기 위치
        player = new Player("GameRes/player_normal.png", "GameRes/player_hit.png", panelWidth/2, panelHeight-100);

        // 키 입력 설정
        input = new InputController();
        setFocusable(true);
        addKeyListener(input);
        addHierarchyListener(e -> {
            if (isShowing()) requestFocusInWindow();
        });

        // 게임 루프 시작
//        startGameThread();
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
//                System.out.println("FPS: " + drawCount);
                drawCount = 0;
                timer = 0;
            }
        }
    }

    /**
     * 게임 로직 업데이트
     */
    private void updateGame() {
        if (InGameManager.getInstance().isPaused()) return;

        // 플레이어 이동
        int dx = input.isLeft() ? -1 : input.isRight() ? 1 : 0;
        int dy = input.isUp() ? -1 : input.isDown() ? 1 : 0;
        player.move(dx, dy, getWidth(), getHeight());


        // 총알 발사 처리
        if(player.getHpRatio() != 0) {
            if (input.isFire()) {
                long nowTime = System.currentTimeMillis();
                if (nowTime - lastFireTime >= fireInterval) {
                    Rectangle pr = player.getBounds();
                    int centerX = pr.x + pr.width / 2;
                    int y = pr.y;

                    // shotCount 개수만큼 발사, 가운데 정렬
                    int startIdx = -(shotCount - 1) / 2;

                    synchronized (bullets) {
                        for (int i = 0; i < shotCount; i++) {
                            int offsetX = (startIdx + i) * bulletSpacing;
                            System.out.println("player shooting ");
                            bullets.add(new Bullet("GameRes/bullet2.png", centerX + offsetX, y));
                        }
                    }
                    lastFireTime = nowTime;
                }
            }
        }
        else{
            setFocusable(false);
//            gameThread.interrupt();
        }

        // 총알 업데이트 및 비활성 총알 제거
        synchronized (bullets) {
            Iterator<Bullet> it = bullets.iterator();
            while (it.hasNext()) {
                Bullet b = it.next();
                b.update();
                if (!b.isActive()) {
                    it.remove();
                }
            }
        }

        // 아이템 검사
        List<Enemy> enemies = enemyPanel.enemies;
        synchronized (items) {
            Iterator<Item> it = items.iterator();
            while (it.hasNext()) {
                Item item = it.next();
                // 화면 테두리 안에서 튕기며 이동
                item.update(getWidth(), getHeight());

                // 플레이어와 충돌 시
                if (player.getBounds().intersects(item.getBounds())) {
                    // 효과 적용 (체력 회복, 탄알 업그레이드, 폭탄)
                    synchronized (enemies) {
                        item.applyEffect(player, this, enemies);
                    }
                    it.remove();
                    continue;
                }
                // 수명 다했거나 inactive 상태면 제거
                if (!item.isActive()) {
                    it.remove();
                }
            }

        }

        // 충돌처리 검사
        checkCollisions();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.red);

        // 플레이어 그리기
        player.draw(g);
        // debug
        // g2d.fillRect(player.getX(), player.getY(), 10, 10);

        // 총알 그리기
        synchronized (bullets) {
            for (Bullet b : bullets) {
                b.draw(g);
                // debug
                // g2d.fillRect(b.getX(), b.getY(), 2, 2);
            }
        }

        // 아이템 그리기
        synchronized (items) {
            for (Item item : items) {
                item.draw(g);
            }
        }
    }

    // 충돌 검사: 플레이어
    private void checkCollisions() {
        Rectangle pb = player.getBounds();
        // enemy list 가져오기
        List<Enemy> enemies = enemyPanel.enemies;

        synchronized (enemies) {
            Iterator<Enemy> it = enemies.iterator();
            while (it.hasNext()) {
                Enemy e = it.next();
                if (pb.intersects(e.getBound())) {
                    // 플레이어가 적과 충돌
                    player.hit();
                    // it.remove(); 디버그용, 충돌 시 적이 제거되진 않음

                    break; // 한 번만 처리하고 루프 탈출
                }
            }
        }
    }

    /**
     * 발사 개수 설정
     */
    public void setShotCount(int count) {
        this.shotCount = Math.min(5, count);
    }

    /**
     * 현재 발사 개수 반환
     */
    public int getShotCount() {
        return shotCount;
    }

    public void setEnemyPanel(EnemyPanel e) {
        enemyPanel = e;
        startGameThread();
    }
}
