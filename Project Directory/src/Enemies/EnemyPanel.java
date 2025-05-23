package Enemies;

import javax.swing.*;
import java.awt.*;
import java.util.Iterator;
import java.util.LinkedList;

public class EnemyPanel extends JPanel implements Runnable {
    LinkedList<Enemy> enemies;

    EnemyPanel() {
        enemies = new LinkedList<>();
        enemies.add(new Enemy());

        Thread t = new Thread(this);
        t.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Iterator<Enemy> it = enemies.iterator();
        while (it.hasNext()) {
            Enemy e = it.next();
            e.draw(g);
        }
    }

    public void run() {
        while (true) {
            try {
                for (var e : enemies)
                    e.update(0.033f);
                for (var e : enemies)
                    e.collisionResolution(this);

                repaint();
                Thread.sleep(33);
            } catch (InterruptedException e) {
                return;
            }
        }
    }
}
