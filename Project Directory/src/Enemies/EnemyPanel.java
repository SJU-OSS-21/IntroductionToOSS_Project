package Enemies;

import Player_Item.Panel.PlayerPanel;
import UI_Scene.InGameManager;

import javax.swing.*;
import java.awt.*;
import java.util.Iterator;
import java.util.LinkedList;

public class EnemyPanel extends JPanel implements Runnable {
    public LinkedList<Enemy> enemies;
    public PlayerPanel playerPanel;

    public InGameManager inGameManager;

    public EnemyPanel(int panelWidth, int panelHeight, InGameManager inGameManager) {
        setOpaque(false);
        setPreferredSize(new Dimension(panelWidth, panelHeight));

        this.inGameManager = inGameManager;

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
        int baseHP = 4;
        float baseSpeed = 200;
        while (true) {
            if (InGameManager.global.isPaused()) {
                try {
                    Thread.sleep(10); // pause 상태 유지
                    continue;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
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
                        Enemy e = new Enemy(this, inGameManager);
                        e.hp = baseHP / 2;
                        e.vy = baseSpeed;
                        enemies.add(e);
                        dt = 0.0f;
                        //System.out.println("enemies : " + enemies.size());
                    }
                    if (time > 5000) {
                        spawnCoolTime *= 0.9f;
                        baseHP += 1;
                        baseSpeed *= 1.05;
                        time = 0.0f;
                    }
                    if (spawnCoolTime < 100)
                        spawnCoolTime = 100.0f;
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
