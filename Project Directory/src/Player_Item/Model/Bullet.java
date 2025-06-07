package Player_Item.Model;

import Map_Audio.SoundManager;

import java.awt.*;
import java.net.URL;
import javax.swing.ImageIcon;

public class Bullet {
    private int x, y;
    private final int speed = 8;
    private final Image image;
    public boolean active = true;

    public Bullet(String resourceName, int startX, int startY) {
        // 이미지 불러오기
        URL imgUrl = getClass().getClassLoader().getResource(resourceName);
        if (imgUrl == null) {
            throw new IllegalArgumentException(String.format("총알 리소스 로드 실패: '%s'를 찾을 수 없습니다.", resourceName));
        }
        this.image = new ImageIcon(imgUrl).getImage();
        this.x = startX - image.getWidth(null)/2;
        this.y = startY;
        SoundManager.play(4,1f);
//        SoundManager.play(5,1f);
    }
    public void draw(Graphics g) {
        // 활성화 중일 시 그리기
        if (active) g.drawImage(image, x, y, null);
    }
    public Rectangle getBounds() {
        return new Rectangle(x, y,
                image.getWidth(null),
                image.getHeight(null));
    }
    public boolean isActive() {return active;}

    // 매 프레임마다 호출 → 위쪽으로 이동
    public void update() {
        y -= speed;
        if (y + image.getHeight(null) < 0) {
            active = false;
        }
    }

    // getter
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
}
