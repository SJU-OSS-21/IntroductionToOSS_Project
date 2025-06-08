package Player_Item.Model;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class Item {
    // 아이템 종류
    public enum Type {
        /** 체력 회복 */
        HEALTH,
        /** 총알 업그레이드(탄막 강화) */
        UPGRADE,
        /** 폭탄: 화면 내 모든 적 제거 */
        BOMB
    }
    private final Type type;
    private final Image image;
    private int x, y;
    private int vx, vy;
    private final int speed = 2;
    private boolean active = true;

    // 각 아이템 드랍 확률
    private static final double PROB_BOMB    = 0.1;
    private static final double PROB_UPGRADE = 0.2;
    private static final double PROB_HEALTH  = 0.3;

    // 아이템 -> 필드에서 생존 시간 (ms 기준)
    private final long spawnTime;
    private final long lifeTime       = 10_000; // 10초
    private final long blinkDuration  = 2_000;  // 마지막 2초간 깜빡임
    private final long blinkInterval  = 200;    // 200ms 간격 깜빡임


    /**
     * 생성자
     * @param type 아이템 종류
     * @param imgPath 클래스패스 상의 이미지 경로
     * @param x 초기 X 좌표
     * @param y 초기 Y 좌표
     */
    public Item(Type type, String imgPath, int x, int y) {
        this.type = type;
        URL url = getClass().getClassLoader().getResource(imgPath);
        if (url == null) {
            throw new IllegalArgumentException("리소스 로드 실패: " + imgPath);
        }
        this.image = new ImageIcon(url).getImage();

        // 좌표 지정
        this.x = x;
        this.y = y;

        // 아이템 스폰 시간(현재 시간으로 기록)
        this.spawnTime = System.currentTimeMillis();

        // 랜덤 방향 설정 (speed 픽셀/프레임)
        int dx = (int)(Math.random() * 2 * speed) - speed;
        int dy = (int)(Math.random() * 2 * speed) - speed;
        this.vx = dx == 0 ? speed : dx;
        this.vy = dy == 0 ? speed : dy;
    }

    /**
     * 적 사망시 호출할 함수 -> 랜덤 아이템 드랍
     * @param x 화면 좌표
     * @param y 화면 좌표
     * @return 드롭된 아이템 혹은 null
     */
    public static Item randomDrop(int x, int y) {
        double r = Math.random();
        Type type = null;

        if (r < PROB_BOMB) {
            type = Type.BOMB;
        } else if (r < PROB_BOMB + PROB_UPGRADE) {
            type = Type.UPGRADE;
        } else if (r < PROB_BOMB + PROB_UPGRADE + PROB_HEALTH) {
            type = Type.HEALTH;
        }

        // 경로는 패널에서 실제 클래스로딩경로에 맞게 설정
        String path;
        switch (type) {
            case HEALTH:  path = "res/item_health.png"; break;
            case UPGRADE: path = "res/item_upgrade.png"; break;
            default:      path = "res/item_bomb.png";    break;
        }

        // 아이템 생성자로 반환
        return new Item(type, path, x, y);
    }

    /**
     * 프레임마다 호출: 이동 및 생존시간 체크
     * @param maxW 화면 너비
     * @param maxH 화면 높이
     */
    public void update(int maxW, int maxH) {
        if (!active) return; // 비활성화 시 무시

        // 1. 위치 업데이트
        x += vy;
        y += vy;

        // 2. 벽 충돌 시 법선 방향으로 방향 전환
        if (x <= 0 || x + image.getWidth(null) >= maxW) vx = -vx;
        if (y <= 0 || y + image.getHeight(null) >= maxH) vy = -vy;

        // 3. 생존 시간 경과 시 비활성
        long age = System.currentTimeMillis() - spawnTime;
        if (age >= lifeTime) {
            active = false;
        }
    }

    /**
     * 렌더링 처리: 깜빡임 포함
     * @param g 그래픽
     */
    public void draw(Graphics g) {
        if (!active) return;
        long age = System.currentTimeMillis() - spawnTime;
        if (age >= lifeTime - blinkDuration) {
            long t = age - (lifeTime - blinkDuration);
            // 깜빡임 로직
            if ((t / blinkInterval) % 2 == 0) {
                g.drawImage(image, x, y, null);
            }
        } else {
            g.drawImage(image, x, y, null);
        }
    }
    /** 충돌 범위 반환 */
    public Rectangle getBounds() {
        return new Rectangle(x, y, image.getWidth(null), image.getHeight(null));
    }
    /** 활성 여부 */
    public boolean isActive() {
        return active;
    }

}
