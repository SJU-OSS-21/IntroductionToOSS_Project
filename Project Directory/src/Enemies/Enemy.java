package Enemies;

import Map_Audio.SoundManager;
import Player_Item.Model.Bullet;
import UI_Scene.GameManager;
import UI_Scene.InGameManager;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

import java.util.List;

public class Enemy implements Runnable {
    public Image image;
    public float w, h;
    public float px, py;
    public float vx, vy;
    public int hp;
    public boolean active;
    public int panelWidth, panelHeight;


    //  For Score Count
    InGameManager inGameManager;


    public Enemy(JPanel p, InGameManager inGameManager) {
        panelWidth = p.getWidth();
        panelHeight = p.getHeight();

        this.inGameManager = inGameManager;

        init();

    }
    public void init() {
        URL imgUrl = getClass().getClassLoader().getResource("enemy.png");
        image = new ImageIcon(imgUrl).getImage();
        w = 57;
        h = 42;
        px = (float)Math.floor(Math.random() * (panelWidth - w));
        py = -100;
        vx = (float)Math.floor(Math.random() * 201) - 100;
        vy = 100;

        hp = 1;

        active = true;
    }
    public void draw(Graphics g) {
        g.setColor(Color.BLACK);
        //g.fillRect((int)px, (int)py, (int) w, (int) h);
        g.drawImage(image, (int)px, (int)py, null);
    }
    public void update(float dt) {
        px += vx * dt;
        py += vy * dt;
    }
    public void collisionResolution(List<Bullet> bullets) {
        if (panelWidth < 1)
            return;

        if (px + w > panelWidth)    { px = panelWidth - w;   vx *= -1; }
        if (px < 0)                 { px = 0;           vx *= -1; }

        if (py > panelHeight)    { active = false; }

        synchronized (bullets) {
            for (var b : bullets) {
                if (px <= b.getX() && b.getX() <= px + w && py <= b.getY() && b.getY() <= py + h) {
                    reduceHP();
                    SoundManager.play(5,0.5f);
                    if (hp <= 0) {
                        SoundManager.play(6, 1f);

                        //  Score
                        inGameManager.updateScore(1);
                        active = false;
                    }
                    b.active = false;
                    // System.out.println("hit");
                    break;
                }
            }
        }
    }
    public boolean isActive() {
        return active;
    }
    public Rectangle getBound() {
        return new Rectangle((int)px, (int)py, (int)w, (int)h);
    }
    public void reduceHP() {
        hp -= 1;
        Thread t = new Thread(this);
        t.start();
    }

    public void run() {
        try {
            URL imgUrl = getClass().getClassLoader().getResource("enemy_hit.png");
            image = new ImageIcon(imgUrl).getImage();
            Thread.sleep(150);
            imgUrl = getClass().getClassLoader().getResource("enemy.png");
            image = new ImageIcon(imgUrl).getImage();
        } catch (Exception e) {
            return;
        }
    }
}