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
    private final int speed = 2;
    private boolean active = true;

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
        this.x = x;
        this.y = y;
    }
    public static Item of(Type type, int x, int y) {
        String path;
        switch (type) {
            case HEALTH: path = "res/item_health.png"; break;
            case UPGRADE: path = "res/item_upgrade.png"; break;
            case BOMB:    path = "res/item_bomb.png";    break;
            default:      path = "res/item_health.png"; break;
        }
        return new Item(type, path, x, y);
    }


    /** 렌더링 처리 */
    public void draw(Graphics g) {
        if (!active) return;
        g.drawImage(image, x, y, null);
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
