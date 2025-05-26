package Enemies;

import javax.swing.*;
import java.awt.*;

public class Enemy {
    float w, h;
    float px, py;
    float vx, vy;
    float type;
    Image image;

    Enemy() {
        w = 100;
        h = 50;
        px = 100;
        py = 100;

        vx = (float)(Math.floor(Math.random() * 501) - 250);
        vy = 400;
    }

    public void draw(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect((int)px, (int)py, (int) w, (int) h);
    }
    public void update(float dt) {
        px += vx * dt;
        py += vy * dt;
    }
    public void collisionResolution(JPanel p) {
        int width = p.getWidth();
        int height = p.getHeight();

        if (width < 1 || height < 1)
            return;

        if (px + w > width) { px = width - w;   vx *= -1; }
        if (px < 0)         { px = 0;           vx *= -1; }

        if (py + h > height)    { py = height - h;  vy *= -1; }
        if (py < 0)             { py = 0;           vy *= -1; }
    }
    public boolean isDead() {
        return false;
    }
}