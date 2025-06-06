package Enemies;

import Player_Item.Model.Bullet;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.List;

public class Enemy {
    public Image image;
    public float w, h;
    public float px, py;
    public float vx, vy;
    public int hp;
    public boolean active;
    public int panelWidth, panelHeight;


    public Enemy(JPanel p) {
        panelWidth = p.getWidth();
        panelHeight = p.getHeight();
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

        hp = 2;

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
                    hp -= 1;
                    URL imgUrl = getClass().getClassLoader().getResource("enemy_hit.png");
                    image = new ImageIcon(imgUrl).getImage();
                    if (hp <= 0)
                        active = false;
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
}