// src/Player_Item/Panel/PlayerPanel.java
package Player_Item.Panel; // 이 클래스가 속한 패키지 선언

import Enemies.Enemy;                   // 적 모델 클래스 임포트 (충돌 검사에 사용)
import Enemies.EnemyPanel;              // 적 패널 클래스 임포트 (적 리스트 참조용)
import Player_Item.InputController;     // 키 입력 처리 클래스 임포트
import Player_Item.Model.Item;          // 아이템 모델 클래스 임포트
import Player_Item.Model.Player;        // 플레이어 모델 클래스 임포트
import Player_Item.Model.Bullet;        // 총알 모델 클래스 임포트
import UI_Scene.InGameManager;          // 게임 상태 관리 (일시정지 여부 확인용)

import javax.swing.*;                    // Swing 컴포넌트 및 JPanel 사용
import java.awt.*;                       // 그래픽 처리용 AWT 클래스 임포트
import java.util.ArrayList;              // ArrayList 자료구조 임포트
import java.util.Iterator;               // Iterator 사용 임포트
import java.util.List;                   // List 인터페이스 임포트

/**
 * 플레이어 이동, 총알 발사, 아이템 관리, 충돌 처리 등을 담당하는 패널 클래스
 */
public class PlayerPanel extends JPanel implements Runnable {
    public final Player player;               // 플레이어 상태와 로직을 가진 모델 객체
    private final InputController input;      // 키 입력을 처리하는 컨트롤러
    public final List<Bullet> bullets = new ArrayList<>();     // 발사된 총알 목록을 저장하는 리스트
    public final List<Item> items = new ArrayList<>();         // 화면에 존재하는 아이템 목록을 저장하는 리스트
    private EnemyPanel enemyPanel;            // 충돌 처리를 위해 적 패널 참조를 저장할 필드

    private final double FPS = 60.0;          // 목표 프레임 레이트 (초당 프레임 수)
    private Thread gameThread;                // 게임 루프를 실행할 스레드 객체

    private long lastFireTime = 0;            // 마지막으로 총알을 발사한 시각 기록(ms)
    private final long fireInterval = 200;    // 총알 발사 쿨다운 시간(ms)

    private int shotCount = 3;                // 한 번에 발사할 총알의 개수
    private final int bulletSpacing = 12;     // 연속 발사 시 총알 간 가로 간격(px)

    /**
     * 생성자: 패널 초기화 및 플레이어, 입력 리스너 설정
     * @param panelWidth 패널의 가로 크기(px)
     * @param panelHeight 패널의 세로 크기(px)
     */
    public PlayerPanel(int panelWidth, int panelHeight) {
        setOpaque(false);                                      // 패널 투명 설정
        setPreferredSize(new Dimension(panelWidth, panelHeight)); // 패널 크기 지정

        player = new Player(
                "GameRes/player_normal.png",                      // 정상 이미지 파일 경로
                "GameRes/player_hit.png",                         // 피격 시 이미지 파일 경로
                panelWidth / 2,                                     // 초기 X 좌표: 화면 중앙
                panelHeight - 100                                   // 초기 Y 좌표: 화면 하단에서 100px 위
        );

        input = new InputController();                          // 키 입력 컨트롤러 인스턴스 생성
        setFocusable(true);                                     // 포커스 가능 설정 (키 입력 받기 위해)
        addKeyListener(input);                                  // 키 리스너 등록
        addHierarchyListener(e -> {
            if (isShowing()) requestFocusInWindow();           // 패널이 화면에 보일 때 포커스 요청
        });

        // 적 패널 연결 및 게임 루프 시작은 setEnemyPanel() 메서드에서 수행
    }

    /**
     * 적 패널을 주입하고 게임 루프를 시작하는 메서드
     * @param e EnemyPanel 인스턴스
     */
    public void setEnemyPanel(EnemyPanel e) {
        this.enemyPanel = e;        // 전달받은 적 패널 참조 저장
        startGameThread();          // 게임 루프 스레드 시작
    }

    /**
     * 게임 루프를 담당할 스레드를 생성하고 시작하는 메서드
     */
    private void startGameThread() {
        gameThread = new Thread(this); // 현재 객체(Runnable)를 인수로 전달
        gameThread.start();            // 스레드 시작
    }

    /**
     * Runnable 구현 메서드: 목표 FPS로 update/repaint 반복
     */
    @Override
    public void run() {
        double drawInterval = 1_000_000_000.0 / FPS; // 한 프레임당 소요 시간(ns)
        double delta = 0;                            // 누적 프레임 카운터
        long lastTime = System.nanoTime();           // 이전 루프 시각 기록
        long now;
        long timer = 0;                              // 1초 측정용 타이머
        int drawCount = 0;                           // 실제 출력된 프레임 수
        while (gameThread != null) {
            now = System.nanoTime();                // 현재 시각
            delta += (now - lastTime) / drawInterval; // 프레임 도달 여부 계산
            timer += now - lastTime;                // 타이머 누적
            lastTime = now;                         // 루프 종료 시각 업데이트

            if (delta >= 1) {                       // 1 프레임 분량 시간이 지났으면
                updateGame();                       // 게임 로직 업데이트
                repaint();                          // 화면 갱신 호출
                delta--;                           // 프레임 카운터 초기화
                drawCount++;                       // 프레임 카운터 증가
            }

            if (timer >= 1_000_000_000) {           // 1초 경과 시
                drawCount = 0;                     // 프레임 카운터 초기화
                timer = 0;                         // 타이머 초기화
            }
        }
    }

    /**
     * 게임 로직 업데이트 메서드: 이동, 발사, 아이템, 충돌 처리 수행
     */
    private void updateGame() {
        if (InGameManager.getInstance().isPaused()) return; // 일시정지 중이면 처리 중단

        // 플레이어 이동 계산
        int dx = input.isLeft()  ? -1               // 왼쪽 키 입력 시 -1
                : input.isRight() ?  1               // 오른쪽 키 입력 시 +1
                : 0;                                 // 입력 없으면 0
        int dy = input.isUp()    ? -1               // 위쪽 키 입력 시 -1
                : input.isDown()  ?  1               // 아래쪽 키 입력 시 +1
                : 0;                                 // 입력 없으면 0
        player.move(dx, dy, getWidth(), getHeight()); // 경계 내 이동 수행

        // 총알 발사 처리: 체력이 남아있고 발사 키를 눌렀으면
        if (player.getHpRatio() != 0 && input.isFire()) {
            long nowTime = System.currentTimeMillis();       // 현재 시간(ms)
            if (nowTime - lastFireTime >= fireInterval) {    // 쿨다운 체크
                Rectangle pr = player.getBounds();           // 플레이어 경계
                int centerX = pr.x + pr.width / 2;          // 중앙 X 좌표 계산
                int y = pr.y;                               // 발사 시작 Y 좌표
                int startIdx = -(shotCount - 1) / 2;        // 연발 중앙 정렬 인덱스
                synchronized (bullets) {                    // 동기화 블록 시작
                    for (int i = 0; i < shotCount; i++) {   // shotCount만큼 반복
                        int offsetX = (startIdx + i) * bulletSpacing; // 개별 총알 가로 오프셋
                        bullets.add(new Bullet(
                                "GameRes/bullet2.png",       // 총알 이미지 경로
                                centerX + offsetX,               // 발사 X 좌표
                                y                                // 발사 Y 좌표
                        ));
                    }
                }                                            // 동기화 블록 종료
                lastFireTime = nowTime;                      // 마지막 발사 시각 갱신
            }
        } else if (player.getHpRatio() == 0) {
            setFocusable(false); // 체력 없음 → 키 입력 무시
        }

        // 총알 목록 업데이트 및 비활성화 총알 제거
        synchronized (bullets) {                            // 동기화 블록 시작
            Iterator<Bullet> it = bullets.iterator();       // Iterator 생성
            while (it.hasNext()) {
                Bullet b = it.next();                       // 다음 bullet 참조
                b.update();                                 // bullet 위치 업데이트
                if (!b.isActive()) it.remove();             // 비활성화된 총알 제거
            }
        }                                                  // 동기화 블록 종료

        // 아이템 업데이트 및 플레이어 충돌 처리
        List<Enemy> enemies = enemyPanel.enemies;          // 적 리스트 참조
        synchronized (items) {                             // 동기화 블록 시작
            Iterator<Item> it = items.iterator();          // Iterator 생성
            while (it.hasNext()) {
                Item item = it.next();                     // 다음 item 참조
                item.update(getWidth(), getHeight());     // item 위치 및 상태 업데이트
                if (player.getBounds().intersects(item.getBounds())) { // 충돌 검사
                    synchronized (enemies) {              // 적 리스트 동기화
                        item.applyEffect(player, this, enemies); // 효과 적용
                    }
                    it.remove();                         // 픽업 후 제거
                    continue;                            // 다음 반복으로 건너뛰기
                }
                if (!item.isActive()) it.remove();       // 수명 종료 시 제거
            }
        }                                                  // 동기화 블록 종료

        // 플레이어와 적 충돌 처리 호출
        checkCollisions();
    }

    /**
     * 화면 그리기 메서드: 플레이어, 총알, 아이템 렌더링 순서대로 출력
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);                         // 부모 클래스 paintComponent 호출
        Graphics2D g2d = (Graphics2D) g;                // Graphics2D 형 변환
        player.draw(g);                                   // 플레이어 렌더링
        synchronized (bullets) {                          // 총알 렌더링 동기화
            for (Bullet b : bullets) b.draw(g);          // 각 총알 그리기
        }
        synchronized (items) {                            // 아이템 렌더링 동기화
            for (Item item : items) item.draw(g);        // 각 아이템 그리기
        }
    }

    /**
     * 플레이어↔적 충돌 검사 및 처리
     */
    private void checkCollisions() {
        Rectangle pb = player.getBounds();               // 플레이어 경계 사각형 획득
        List<Enemy> enemies = enemyPanel.enemies;        // 적 리스트 참조
        synchronized (enemies) {                         // 동기화 블록 시작
            Iterator<Enemy> it = enemies.iterator();     // Iterator 생성
            while (it.hasNext()) {
                Enemy e = it.next();                    // 다음 적 참조
                if (pb.intersects(e.getBound())) {      // 충돌 검사
                    player.hit();                       // 피격 처리
                    break;                               // 첫 충돌만 처리 후 탈출
                }
            }
        }                                                  // 동기화 블록 종료
    }

    /**
     * 한 번에 발사할 총알 개수 설정 (최대 5)
     */
    public void setShotCount(int count) {
        this.shotCount = Math.min(5, count);             // 최대 5개 제한
    }

    /**
     * 현재 설정된 발사 총알 개수 반환
     */
    public int getShotCount() { return shotCount; }      // 발사 개수 반환
}
