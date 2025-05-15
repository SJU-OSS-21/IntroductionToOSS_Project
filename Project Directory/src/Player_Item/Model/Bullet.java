package Player_Item.Model;

import java.awt.Image;
import java.awt.Rectangle;
import java.net.URL;
import javax.swing.ImageIcon;

public class Bullet {
    private int x, y;
    private final int speed = 10;
    private final Image image;
    private boolean isActive = true;

    public Bullet(String resourceName, int startX, int startY) {
        // 이미지 불러오기
        URL imgUrl = getClass().getClassLoader().getResource(resourceName);
        if (imgUrl == null) {
            throw new IllegalArgumentException(String.format("총알 리소스 로드 실패: '%s'를 찾을 수 없습니다.", resourceName));
        }
        this.image = new ImageIcon(imgUrl).getImage();
        this.x = startX;
        this.y = startY;
    }

}
