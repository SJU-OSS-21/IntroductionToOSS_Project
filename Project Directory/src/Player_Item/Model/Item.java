// src/Player_Item/Model/Item.java
package Player_Item.Model; // 아이템 모델 패키지 선언

import Enemies.Enemy;               // 적 모델 참조 임포트
import Player_Item.Panel.PlayerPanel; // 플레이어 패널 참조 임포트

import javax.swing.*;  // Swing 라이브러리 임포트 (ImageIcon 등)
import java.awt.*;      // Graphics, Image, Rectangle 등 AWT 임포트
import java.net.URL;    // 리소스 로드를 위한 URL 클래스 임포트

public class Item { // 아이템 클래스 선언
    // 아이템 종류를 구분하기 위한 열거형
    public enum Type {
        HEALTH,    // 체력 회복 아이템
        UPGRADE,   // 총알 업그레이드 아이템
        BOMB       // 화면 내 모든 적 제거 아이템
    }

    private final Type type;    // 아이템 종류
    private final Image image;  // 아이템 이미지
    private int x, y;           // 아이템 위치 좌표
    private int vx, vy;         // 이동 벡터 (속도 방향)
    private final int speed = 2;// 이동 속도 상수
    private boolean active = true; // 활성 여부 플래그

    // 드랍 확률 상수 정의
    private static final double PROB_BOMB    = 0.05; // 폭탄 드랍 확률
    private static final double PROB_UPGRADE = 0.1; // 업그레이드 드랍 확률
    private static final double PROB_HEALTH  = 0.2; // 체력 드랍 확률

    // 생존 시간 관리 필드
    private final long spawnTime;          // 스폰 시각 기록
    private final long lifeTime      = 10_000; // 생존 시간(ms)
    private final long blinkDuration = 2_000;  // 깜빡임 시작 구간(ms)
    private final long blinkInterval = 200;    // 깜빡임 주기(ms)

    /**
     * 생성자: 이미지 로드, 좌표 및 방향 설정
     */
    public Item(Type type, String imgPath, int x, int y) {
        this.type = type; // 아이템 종류 저장
        URL url = getClass().getClassLoader().getResource(imgPath); // 이미지 URL 획득
        if (url == null) {
            throw new IllegalArgumentException("리소스 로드 실패: " + imgPath); // 예외 처리
        }
        this.image = new ImageIcon(url).getImage(); // 이미지 객체 생성

        this.x = x; // 초기 X 좌표 설정
        this.y = y; // 초기 Y 좌표 설정

        this.spawnTime = System.currentTimeMillis(); // 스폰 시각 기록

        int dir = (int)(Math.random() * 4); // 0~3 랜덤 방향
        switch (dir) {
            case 0: vx =  speed; vy =  speed; break; // 우하향
            case 1: vx =  speed; vy = -speed; break; // 우상향
            case 2: vx = -speed; vy =  speed; break; // 좌하향
            default: vx = -speed; vy = -speed; break; // 좌상향
        }
    }

    /**
     * 적 사망 시 확률적으로 아이템 생성
     */
    public static Item randomDrop(int x, int y) {
        double r = Math.random(); // 0.0~1.0 랜덤값
        Type type = null;         // 초기 타입 null

        if      (r < PROB_BOMB)                          type = Type.BOMB;    // 폭탄
        else if (r < PROB_BOMB + PROB_UPGRADE)           type = Type.UPGRADE; // 업그레이드
        else if (r < PROB_BOMB + PROB_UPGRADE + PROB_HEALTH) type = Type.HEALTH;  // 체력회복
        else                                             return null;         // 드랍 없음

        String path; // 이미지 경로 결정
        switch (type) {
            case HEALTH:  path = "GameRes/item_health.png";  break; // 체력회복 이미지
            case UPGRADE: path = "GameRes/item_upgrade.png"; break; // 업그레이드 이미지
            case BOMB:    path = "GameRes/item_bomb.png";    break; // 폭탄 이미지
            default:      return null;                              // 그 외는 null
        }
        return new Item(type, path, x, y); // 아이템 객체 반환
    }

    /**
     * 매 프레임 호출: 위치 이동 및 수명 체크
     */
    public void update(int maxW, int maxH) {
        if (!active) return; // 비활성화 시 무시

        x += vx; // X 위치 이동
        y += vy; // Y 위치 이동

        int iw = image.getWidth(null);  // 이미지 너비
        int ih = image.getHeight(null); // 이미지 높이
        // 경계 충돌 시 반전 + 클램핑
        if (x < 0) { x = 0; vx = -vx; }
        else if (x + iw > maxW) { x = maxW - iw; vx = -vx; }
        if (y < 0) { y = 0; vy = -vy; }
        else if (y + ih > maxH) { y = maxH - ih; vy = -vy; }

        long age = System.currentTimeMillis() - spawnTime; // 경과 시간 계산
        if (age >= lifeTime) active = false; // 수명 초과 시 비활성
    }

    /**
     * 렌더링: 수명 말기 깜빡임 로직 포함
     */
    public void draw(Graphics g) {
        if (!active) return; // 비활성화 시 무시
        long age = System.currentTimeMillis() - spawnTime; // 경과 시간
        if (age >= lifeTime - blinkDuration) {
            long t = age - (lifeTime - blinkDuration);
            if ((t / blinkInterval) % 2 == 0) { // blinkInterval 주기 번갈아 그림
                g.drawImage(image, x, y, null);
            }
        } else {
            g.drawImage(image, x, y, null); // 평상시 그리기
        }
    }

    /**
     * 충돌 판정용 경계 사각형 반환
     */
    public Rectangle getBounds() {
        return new Rectangle(x, y, image.getWidth(null), image.getHeight(null));
    }

    /**
     * 활성 여부 반환
     */
    public boolean isActive() { return active; }

    /**
     * 효과 적용: 플레이어 또는 패널, 적 리스트를 받아 처리
     */
    public void applyEffect(Player player, PlayerPanel panel, java.util.List<Enemy> enemies) {
        switch (type) {
            case HEALTH:
                player.increasePlayerHp(); // 체력 회복
                break;
            case UPGRADE:
                panel.setShotCount(panel.getShotCount() + 1); // 업그레이드
                break;
            case BOMB:
                synchronized(enemies) {
                    enemies.clear(); // 모든 적 제거
                }
                break;
        }
        active = false; // 사용 즉시 비활성
    }
}