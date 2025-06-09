// src/Player_Item/Model/Bullet.java
package Player_Item.Model; // 총알 모델 패키지

import Map_Audio.SoundManager; // 사운드 효과 매니저
import java.awt.*;             // Graphics, Image, Rectangle 임포트
import java.net.URL;           // URL 임포트
import javax.swing.ImageIcon; // 이미지 아이콘 임포트

public class Bullet { // Bullet 클래스 선언
    private int x, y;            // 총알 위치
    private final int speed = 8; // 총알 속도
    private final Image image;   // 총알 이미지
    public boolean active = true; // 활성 여부

    // 생성자: 리소스 로드, 위치 초기화, 발사음 재생
    public Bullet(String resourceName, int startX, int startY) {
        URL imgUrl = getClass().getClassLoader().getResource(resourceName); // 리소스 URL
        if (imgUrl == null) {
            throw new IllegalArgumentException(
                    String.format("총알 리소스 로드 실패: '%s'를 찾을 수 없습니다.", resourceName));
        }
        this.image = new ImageIcon(imgUrl).getImage(); // 이미지 객체 생성
        this.x = startX - image.getWidth(null)/2; // 이미지 중앙 정렬 X
        this.y = startY;                          // 시작 Y 좌표
        SoundManager.play(4, 0.2f);               // 발사 사운드 재생
    }

    // 그리기: 활성화 시에만 이미지 렌더링
    public void draw(Graphics g) {
        if (active) g.drawImage(image, x, y, null);
    }

    // 충돌 판정용 경계 반환
    public Rectangle getBounds() {
        return new Rectangle(x, y, image.getWidth(null), image.getHeight(null));
    }

    // 활성 여부 반환
    public boolean isActive() { return active; }

    // 이동 업데이트: 매 프레임 Y축 위로 이동 후 화면 상단 벗어나면 비활성
    public void update() {
        y -= speed;                                // 위쪽으로 이동
        if (y + image.getHeight(null) < 0) {
            active = false; // 화면 밖으로 완전히 벗어나면 비활성
        }
    }

    // X 좌표 getter
    public int getX() { return x; }
    // Y 좌표 getter
    public int getY() { return y; }
}
