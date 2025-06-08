package Player_Item.Model;

import Map_Audio.SoundManager;
import UI_Scene.InGameUIPanel;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class Player {
    private int x, y; // 플레이어 위치
    private int speed = 5; // 플레이어 속도
    private Image normalImage; // 플레이어 정상 이미지
    private Image hitImage;    // 플레이어 피격 시 이미지
    private int maxHp = 3;
    private int curHp = 3; // 플레이어 체력

    // 무적 관련 필드
    private boolean invincible = false; // 무적 여부
    private long invincibleStartTime = 0; // 무적 시작
    private final long invincibleDuration = 2000; // 무적 지속 시간 (ms)
    private final long blinkInterval = 200;      // 깜빡임 간격 (ms)

    //  InGameUI
    private InGameUIPanel uiPanel;

    public void setUiPanel(InGameUIPanel panel) {
        this.uiPanel = panel;
    }


    // 생성자
    public Player(String normalImgPath, String hitImgPath, int startX, int startY) {
        // ClassLoader 방식: 리소스 루트에 복사된 파일명만 사용
        URL normalUrl = getClass().getClassLoader().getResource(normalImgPath);
        URL hitUrl    = getClass().getClassLoader().getResource(hitImgPath);
        if (normalUrl == null || hitUrl == null) {
            throw new IllegalArgumentException("리소스 로드 실패: " + normalImgPath + " 또는 " + hitImgPath);
        }
        this.normalImage = new ImageIcon(normalUrl).getImage();
        this.hitImage    = new ImageIcon(hitUrl).getImage();
        // 초기 좌표
        this.x = startX - normalImage.getWidth(null) / 2;
        this.y = startY - normalImage.getHeight(null) / 2;
    }

    // 이동 함수
    public void move(int dx, int dy, int maxW, int maxH) {
        // System.out.println(x+", "+y); // debug
        x = Math.max(0, Math.min(x + dx * speed, maxW - normalImage.getWidth(null)));
        y = Math.max(0, Math.min(y + dy * speed, maxH - normalImage.getHeight(null)));
    }
    // 피격 처리
    public void hit() {
        if (invincible) return; // 무적 상태인 경우 return
        curHp = Math.max(0, curHp-1);

        //  HP UI Update
        if (uiPanel != null) {
            uiPanel.UIUpdate(); // 체력 감소 시 UI 알림
        }

        // 무적 처리
        invincible = true;
        invincibleStartTime = System.currentTimeMillis();
    }


    // 매 프레임마다 호출하여 무적 상태를 갱신
    private void updateInvincible() {
        if (!invincible) return;
        long elapsed = System.currentTimeMillis() - invincibleStartTime;
        if (elapsed >= invincibleDuration) {
            // 무적 시간 종료
            invincible = false;
        }
    }

    // draw()
    public void draw(Graphics g) {
        updateInvincible();

        if (invincible) {
            long elapsed = System.currentTimeMillis() - invincibleStartTime;
            // blinkInterval 간격마다 이미지 토글
            if ((elapsed / blinkInterval) % 2 == 0) {
                g.drawImage(hitImage, x, y, null);
            } else {
                g.drawImage(normalImage, x, y, null);
            }
        } else {
            g.drawImage(normalImage, x, y, null);
        }
    }
    // 경계값
    public Rectangle getBounds() {
        return new Rectangle(x, y,
                normalImage.getWidth(null),
                normalImage.getHeight(null));
    }

    // getter
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }

    // 무적 상태 여부 반환
    public boolean isInvincible() {
        return invincible;
    }

    public float getHpRatio(){
        return curHp / (float) maxHp;
    }

    // setter
    public void decreasePlayerHp() {
        curHp -= 1;
    }
    public void increasePlayerHp() {
        curHp += 1;
    }
}