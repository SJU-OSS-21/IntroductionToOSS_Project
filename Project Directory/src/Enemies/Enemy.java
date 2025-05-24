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
        w = 20;
        h = 20;
        px = 100;
        py = 100;

        vx = 200;
        vy = 200;
    }

    public void draw(Graphics g) {
        g.setColor(Color.GREEN);
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
        if (px - w < 0)     { px = w;           vx *= -1; }

        if (py + h > height)    { py = height - h;  vy *= -1; }
        if (py - h < 0)         { py = h;           vy *= -1; }
    }
    public boolean isDead() {
        return false;
    }
}
