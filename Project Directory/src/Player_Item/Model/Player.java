package Player_Item.Model;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class Player {
    private int x, y; // 플레이어 위치
    private int speed = 5; // 플레이어 속도
    private Image image; // 플레이어 이미지(에셋)
    private int hp = 3;

    // 생성자
    public Player(String imgPath, int startX, int startY) {
        // ClassLoader 방식: 리소스 루트에 복사된 파일명만 사용
        URL imgUrl = getClass().getClassLoader().getResource(imgPath);
        if (imgUrl == null) {
            throw new IllegalArgumentException("리소스 로드 실패: " + imgPath);
        }
        this.image = new ImageIcon(imgUrl).getImage();
        this.x = startX;
        this.y = startY;
    }

    // 이동 함수
    public void move(int dx, int dy, int maxW, int maxH) {
        // System.out.println(x+", "+y); // debug
        x = Math.max(0, Math.min(x + dx * speed, maxW - image.getWidth(null)));
        y = Math.max(0, Math.min(y + dy * speed, maxH - image.getHeight(null)));
    }
    // draw()
    public void draw(Graphics g) {
        g.drawImage(image, x, y, null);
    }
    // 경계값
    public Rectangle getBounds() {
        return new Rectangle(x, y,
                image.getWidth(null),
                image.getHeight(null));
    }

    // getter
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }

    // setter
    public void decreasePlayerHp() {
        hp -= 1;
    }
    public void increasePlayerHp() {
        hp += 1;
    }
}