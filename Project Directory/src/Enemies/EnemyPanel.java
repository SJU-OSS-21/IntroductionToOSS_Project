package Enemies;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Iterator;
import java.util.LinkedList;

public class EnemyPanel extends JPanel implements Runnable, KeyListener {
    LinkedList<Enemy> enemies;

    EnemyPanel() {
        enemies = new LinkedList<>();
        enemies.add(new Enemy());

        Thread t = new Thread(this);
        t.start();

        addKeyListener(this);
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

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE)
            enemies.add(new Enemy());
    }

    public void keyTyped(KeyEvent e) {}
    public void keyReleased(KeyEvent e) {}
}
