package Enemies;

import Player_Item.Panel.PlayerPanel;

import javax.swing.*;
import java.awt.*;
import java.util.Iterator;
import java.util.LinkedList;

public class EnemyPanel extends JPanel implements Runnable {
    public LinkedList<Enemy> enemies;
    public PlayerPanel playerPanel;

    public EnemyPanel(int panelWidth, int panelHeight) {
        setOpaque(false);
        setPreferredSize(new Dimension(panelWidth, panelHeight));

        enemies = new LinkedList<>();

        Thread t = new Thread(this);
        t.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        synchronized (enemies) {
            Iterator<Enemy> it = enemies.iterator();
            while (it.hasNext()) {
                Enemy e = it.next();
                e.draw(g);
            }
        }
    }

    public void run() {
        float time = 0.0f;
        float dt = 0.0f;
        float spawnCoolTime = 1000.0f;
        while (true) {
            try {
                synchronized (enemies) {
                    Iterator<Enemy> it = enemies.iterator();

                    for (var e : enemies)
                        e.update(0.033f);

                    for (var e : enemies)
                        e.collisionResolution(playerPanel.bullets);

                    while (it.hasNext()) {
                        Enemy e = it.next();
                        if (!e.isActive())
                            it.remove();
                    }

                    if (dt > spawnCoolTime) {
                        enemies.add(new Enemy(this));
                        dt = 0.0f;
                        //System.out.println("enemies : " + enemies.size());
                    }
                    if (time > 1000) {
                        spawnCoolTime *= 0.95f;
                        time = 0.0f;
                    }
                    if (spawnCoolTime < 300)
                        spawnCoolTime = 300.0f;
                }

                repaint();
                Thread.sleep(33);
                time += 33;
                dt += 33;
            } catch (InterruptedException e) {
                return;
            }
        }
    }

    public void setPlayerPanel(PlayerPanel p) {
        playerPanel = p;
    }
}
