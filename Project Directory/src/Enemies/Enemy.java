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

    public Enemy(String imgPath, int _px, int _py) {
        w = 57;
        h = 42;
        px = _px;
        py = _py;

        vx = (float)(Math.floor(Math.random() * 201) - 100);
        vy = 200;

        URL imgUrl = getClass().getClassLoader().getResource(imgPath);
        this.image = new ImageIcon(imgUrl).getImage();
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