package Player_Item.Panel;

import Player_Item.InputController;
import Player_Item.Model.Bullet;
import Player_Item.Model.Player;
import main.KeyInputSystem;
import org.w3c.dom.css.Rect;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PlayerPanel extends JPanel implements ActionListener, Runnable {
    private final Player player;            // 플레이어
    private final InputController input;     // 키 입력 시스템
    private final List<Bullet> bullets = new ArrayList<>();

    final double FPS = 60.0;
    Thread gameThread;

    // 총알 관련
    private long lastFireTime = 0;
    private final long fireInterval = 300; // 발사 쿨타임(ms)

    public PlayerPanel(int width, int height) {
        setOpaque(false);
        setPreferredSize(new Dimension(width, height));

        // 플레이어 생성
        player = new Player("player2.png", width/2 - 30, height-100);

        // 키보드 입력 설정
        input = new InputController();
        setFocusable(true);
        addKeyListener(input);

        this.startGameThread();

        // 게임 루프 (60FPS)
        // timer = new Timer(16, this);
        // timer.start();
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
                System.out.println("FPS : " + drawCount);
                drawCount = 0;
                timer = 0;
            }
//            System.out.println("Game Thread Running");
//            Update();
//            repaint();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.red);

        // 1. 플레이어 그리기
        player.draw(g);

        // 2. 총알 그리기
        for (Bullet b : bullets) {
            b.draw(g);
            g2d.fillRect(b.getX(), b.getY(), 10, 10);
        }

        g2d.fillRect(player.getX(), player.getY(), 10, 10);
    }

    public void Update() {
        // 1. 플레이어 업데이트
        int dx = input.isLeft()  ? -1 : input.isRight() ? 1 : 0;
        int dy = input.isUp()    ? -1 : input.isDown()  ? 1 : 0;
        player.move(dx, dy, getWidth(), getHeight());

        // 2. 총알 업데이트
        if (input.isFire()) {
            long now = System.currentTimeMillis(); // ms로 쿨타임 판단
            if (now - lastFireTime > fireInterval) {
                Rectangle pr = player.getBounds(); // 플레이어 크기 가져오기

                // 플레이어 중앙에서 총알 생성
                int bx = pr.x + pr.width / 2;
                int by = pr.y;
                Bullet bullet = new Bullet("bullet.png", bx, by-1);
                bullets.add(bullet); // 총알 배열에 추가

                lastFireTime = now; // 쏜 시간 업데이트
            }
        }

        // 3. 총알 업데이트 및 비활성 총알 제거
        Iterator<Bullet> it = bullets.iterator();
        while (it.hasNext()) {
            Bullet b = it.next();
            b.update();
            if (!b.isActive()) {
                it.remove();
            }
        }

        // 디버그용

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // 1) 플레이어 이동
//        int dx = input.isLeft()  ? -1 : input.isRight() ? 1 : 0;
//        int dy = input.isUp()    ? -1 : input.isDown()  ? 1 : 0;
//        player.move(dx, dy, getWidth(), getHeight());

        // 총알 발사 처리

        // 총알 업데이트 및 비활성 총알 제거

        // 아이템 접촉 시 아이템 제거 및 효과 적용

        repaint();
    }
}
