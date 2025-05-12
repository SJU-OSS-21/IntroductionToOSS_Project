package Player_Item/Model;

import java.awt.*;

public class Player {
    private int x, y; // 플레이어 위치
    private int speed; // 플레이어 속도
    private Image image; // 플레이어 이미지(에셋)

    // 생성자
    public Player(String imgPath, int startX, int startY) {
        // ClassLoader 방식: 리소스 루트에 복사된 파일명만 사용
        URL imgUrl = getClass().getClassLoader().getResource(imgPath);
        this.image = new ImageIcon(imgUrl).getImage();
        this.x = startX;
        this.y = startY;
    }

    // 이동 함수
    public void move(int dx, int dy, int maxW, int maxH) {

    }
    // draw()
    public void draw(Graphics g) {

    }
    // 경계값
    public Rectangle getBounds() {

    }
}