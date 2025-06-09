package Enemies;

import Map_Audio.SoundManager;
import Player_Item.Model.Bullet;
import Player_Item.Model.Item;
import UI_Scene.InGameManager;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.List;

public class Enemy implements Runnable {
    public Image image;     // 적 이미지
    public float w, h;      // 크기
    public float px, py;    // 위치
    public float vx, vy;    // 속도
    public int hp;          // 체력
    public boolean active;  // 삭제 필요 여부
    public int panelWidth, panelHeight; // 패널 크기

    //  For Score Count
    InGameManager inGameManager;

    public Enemy(JPanel p, InGameManager inGameManager) {

        // 패널 크기 설정
        panelWidth = p.getWidth();
        panelHeight = p.getHeight();

        this.inGameManager = inGameManager;

        // 초기 설정
        init();
    }
    public void init() {

        // 이미지 설정
        URL imgUrl = getClass().getClassLoader().getResource("GameRes/enemy.png");
        image = new ImageIcon(imgUrl).getImage();

        // 크기 설정
        w = 57;
        h = 42;

        // 위치 설정
        px = (float)Math.floor(Math.random() * (panelWidth - w));
        py = -100;

        // 속도 설정
        vx = (float)Math.floor(Math.random() * 201) - 100;
        vy = 100;

        // 체력 설정
        hp = 1;

        // active == false 일 때 삭제 처리
        active = true;
    }

    // 패널에 객체 그리기
    public void draw(Graphics g) {
        g.drawImage(image, (int)px, (int)py, null);
    }

    // 속도와 시간으로 위치 업데이트
    public void update(float dt) {
        px += vx * dt;
        py += vy * dt;
    }

    // 객체와 벽, 객체와 총알 충돌 판정, 처리
    public void collisionResolution(List<Bullet> bullets) {

        // 패널 생성 전 호출 거부
        if (panelWidth < 1)
            return;

        // 패널 좌우 경계 충돌 처리
        if (px + w > panelWidth)    { px = panelWidth - w;   vx *= -1; }
        if (px < 0)                 { px = 0;           vx *= -1; }

        // 패널 아래 영역으로 이동 시 삭제
        if (py > panelHeight)    { active = false; }

        // 객체와 총알 충돌 처리
        synchronized (bullets) {
            for (var b : bullets) {
                if (px <= b.getX() && b.getX() <= px + w && py <= b.getY() && b.getY() <= py + h) {

                    // 체력 감소, 피격 이펙트 재생
                    reduceHP();
                    SoundManager.play(5,0.5f);
                    if (hp <= 0) {
                        SoundManager.play(6, 1f);

                        //  Score
                        inGameManager.updateScore(1);
                        active = false;

                        // 아이템 랜덤 드랍
                        Item drop = Item.randomDrop((int) px, (int) py);
                        if (drop != null) {
                            inGameManager.playerPanel.items.add(drop);
                        }
                    }

                    // 충돌한 총알 삭제
                    b.active = false;
                    break;
                }
            }
        }
    }

    // 삭제 처리 필요 여부 반환
    public boolean isActive() {
        return active;
    }

    // 객체 크기 반환
    public Rectangle getBound() {
        return new Rectangle((int)px, (int)py, (int)w, (int)h);
    }

    // 체력 감소 처리, 피격 이펙트 재생
    public void reduceHP() {
        hp -= 1;
        Thread t = new Thread(this);
        t.start();
    }

    // 피격 이펙트 재생
    public void run() {
        try {

            // 0.15초로 이미지 전환
            URL imgUrl = getClass().getClassLoader().getResource("GameRes/enemy_hit.png");
            image = new ImageIcon(imgUrl).getImage();
            Thread.sleep(150);
            imgUrl = getClass().getClassLoader().getResource("GameRes/enemy.png");
            image = new ImageIcon(imgUrl).getImage();
        } catch (Exception e) {
            return;
        }
    }
}