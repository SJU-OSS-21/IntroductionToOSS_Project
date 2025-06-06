package Enemies;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class Enemy {
    float w, h;
    float px, py;
    float vx, vy;
    float type;
    Image image;
    boolean active;
    int panelWidth, panelHeight;

    public Enemy(JPanel p) {
        panelWidth = p.getWidth();
        panelHeight = p.getHeight();
        init();
    }
    public void init() {
        w = 57;
        h = 42;

        // 0 ~ panelWidth - w & += 10
        px = (float)Math.floor(Math.random() * (panelWidth - w));
        py = -100;

        vx = (float)Math.floor(Math.random() * 201) - 100;
        //vx = 0;
        vy = 100;

        URL imgUrl = getClass().getClassLoader().getResource("enemy.png");
        this.image = new ImageIcon(imgUrl).getImage();

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
    public void collisionResolution() {
        if (panelWidth < 1)
            return;

        if (px + w > panelWidth)    { px = panelWidth - w;   vx *= -1; }
        if (px < 0)                 { px = 0;           vx *= -1; }

        //if (py + h > height)    { py = height - h;  vy *= -1; }
        //if (py < 0)             { py = 0;           vy *= -1; }

        if (py > panelHeight)    { active = false; }
    }
    public boolean isActive() {
        return active;
    }
}