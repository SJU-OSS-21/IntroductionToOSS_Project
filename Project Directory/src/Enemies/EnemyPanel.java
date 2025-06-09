package Enemies;

import Player_Item.Panel.PlayerPanel;
import UI_Scene.InGameManager;

import javax.swing.*;
import java.awt.*;
import java.util.Iterator;
import java.util.LinkedList;

public class EnemyPanel extends JPanel implements Runnable {
    public LinkedList<Enemy> enemies;   // 적 객체 배열
    public PlayerPanel playerPanel;     // 총알 배열 전달 위한 플레이어 패널 저장

    public InGameManager inGameManager;

    public EnemyPanel(int panelWidth, int panelHeight, InGameManager inGameManager) {

        // 패널 크기 설정
        setOpaque(false);
        setPreferredSize(new Dimension(panelWidth, panelHeight));

        this.inGameManager = inGameManager;

        // 적 객체 배열 초기화
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
        float time = 0.0f;      // 5초마다 처리
        float dt = 0.0f;        // 적 생성 카운트
        float spawnCoolTime = 1000.0f;  // 적 생성 주기
        int baseHP = 4;         // 적 기본 체력 = 4 / 2 = 2
        float baseSpeed = 200;  // 적 기본 속도
        while (true) {

            // 게임 정지 처리
            if (InGameManager.getInstance().isPaused()) {
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

                    // 객체 리스트 업데이트
                    for (var e : enemies)
                        e.update(0.033f);

                    // 객체 리스트 충돌 처리
                    for (var e : enemies)
                        e.collisionResolution(playerPanel.bullets);

                    // 삭제 필요 객체 삭제
                    while (it.hasNext()) {
                        Enemy e = it.next();
                        if (!e.isActive())
                            it.remove();
                    }

                    // 적 생성, 초기화
                    if (dt > spawnCoolTime) {
                        Enemy e = new Enemy(this, inGameManager);   // 객체 생성
                        e.hp = baseHP / 2;  // 체력 초기화
                        e.vy = baseSpeed;   // 속도 초기화
                        enemies.add(e);     // 리스트에 추가
                        dt = 0.0f;          // 생성 타이머 초기화
                    }
                    
                    // 5초마다 적 난이도 상승
                    if (time > 5000) {
                        
                        // 적 생성 주기 90%로 감소
                        spawnCoolTime *= 0.9f;
                        
                        // 적 체력 0.5 증가
                        baseHP += 1;
                        
                        // 적 속도 105%로 증가
                        baseSpeed *= 1.05;
                        
                        // 난이도 상승 타이머 초기화
                        time = 0.0f;
                    }

                    // 적 생성 주기 100ms 미만으로 내려가지 않도록 처리
                    if (spawnCoolTime < 100)
                        spawnCoolTime = 100.0f;
                }

                // 33ms 마다 다시 그리기
                repaint();
                Thread.sleep(33);
                time += 33;
                dt += 33;
            } catch (InterruptedException e) {
                return;
            }
        }
    }

    // 플레이어 패널 등록
    public void setPlayerPanel(PlayerPanel p) {
        playerPanel = p;
    }
}
