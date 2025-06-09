package Player_Item.Model; // Player 모델 클래스가 속한 패키지

import Map_Audio.SoundManager; // 사운드 재생을 위한 매니저 클래스 임포트
import UI_Scene.InGameUIPanel; // 게임 내 UI 패널 임포트 (체력 바 갱신용)

import javax.swing.*;  // Swing 컴포넌트 및 ImageIcon 사용을 위한 임포트
import java.awt.*;     // Graphics, Image, Rectangle 등 AWT 클래스 임포트
import java.net.URL;   // 리소스 로드를 위한 URL 클래스 임포트

public class Player { // Player 클래스 선언
    private int x, y; // 플레이어의 화면 내 위치(X, Y)
    private int speed = 5; // 이동 속도 기본값 설정
    private Image normalImage; // 평상시 표현할 이미지
    private Image hitImage;    // 피격 시 잠시 표시할 이미지
    private int maxHp = 3;     // 최대 체력
    private int curHp = 3;     // 현재 체력 (초기값은 최대 체력)

    // 무적 처리 관련 필드
    private boolean invincible = false;       // 무적 상태 여부 플래그
    private long invincibleStartTime = 0;     // 무적 시작 시각(ms)
    private final long invincibleDuration = 2000; // 무적 지속 시간(ms)
    private final long blinkInterval = 200;       // 깜빡임 주기(ms)

    // InGameUI와 연결하기 위한 참조
    private InGameUIPanel uiPanel;

    public void setUiPanel(InGameUIPanel panel) { // UI 패널 설정 메서드
        this.uiPanel = panel;
    }

    // 생성자: 두 가지 이미지를 클래스패스에서 로드하고 초기 위치 계산
    public Player(String normalImgPath, String hitImgPath, int startX, int startY) {
        URL normalUrl = getClass().getClassLoader().getResource(normalImgPath); // 평상시 이미지 URL
        URL hitUrl    = getClass().getClassLoader().getResource(hitImgPath);    // 피격 이미지 URL
        if (normalUrl == null || hitUrl == null) { // 리소스 로드 실패 체크
            throw new IllegalArgumentException("리소스 로드 실패: " + normalImgPath + " 또는 " + hitImgPath);
        }
        this.normalImage = new ImageIcon(normalUrl).getImage(); // 평상시 이미지 객체 생성
        this.hitImage    = new ImageIcon(hitUrl).getImage();    // 피격 이미지 객체 생성
        // 초기 좌표 계산: 중앙 정렬
        this.x = startX - normalImage.getWidth(null) / 2;
        this.y = startY - normalImage.getHeight(null) / 2;
    }

    // 이동 메서드: dx, dy 방향으로 speed만큼 이동하며 화면 경계 내 클램핑
    public void move(int dx, int dy, int maxW, int maxH) {
        x = Math.max(0, Math.min(x + dx * speed, maxW - normalImage.getWidth(null)));
        y = Math.max(0, Math.min(y + dy * speed, maxH - normalImage.getHeight(null)));
    }

    // 피격 처리: 무적 중이 아니면 HP 감소, UI 갱신, 무적 상태 진입
    public void hit() {
        if (invincible) return;               // 이미 무적 상태면 무시
        curHp = Math.max(0, curHp - 1);       // 체력 1 감소 (최소 0)

        // UI 패널이 연결되어 있으면 체력 바 갱신 호출
        if (uiPanel != null) {
            uiPanel.UIUpdate();
        }

        // 무적 상태 시작
        invincible = true;
        invincibleStartTime = System.currentTimeMillis();
    }

    // 매 프레임 호출: 무적 지속 시간 경과 시 해제
    private void updateInvincible() {
        if (!invincible) return; // 무적 중이 아닐 때는 아무 것도 안 함
        long elapsed = System.currentTimeMillis() - invincibleStartTime; // 경과 시간 계산
        if (elapsed >= invincibleDuration) {
            invincible = false; // 무적 해제
        }
    }

    // 그리기 메서드: 무적 중에는 깜빡임 효과, 아니면 평상시 이미지
    public void draw(Graphics g) {
        updateInvincible(); // 무적 상태 업데이트

        if (invincible) {
            long elapsed = System.currentTimeMillis() - invincibleStartTime; // 무적 경과 시간
            // blinkInterval 간격으로 hitImage와 normalImage 번갈아 가며 표시
            if ((elapsed / blinkInterval) % 2 == 0) {
                g.drawImage(hitImage, x, y, null);
            } else {
                g.drawImage(normalImage, x, y, null);
            }
        } else {
            g.drawImage(normalImage, x, y, null); // 평상시 이미지
        }
    }

    // 충돌 판정을 위한 경계(Rectangle) 반환
    public Rectangle getBounds() {
        return new Rectangle(x, y,
                normalImage.getWidth(null),
                normalImage.getHeight(null));
    }

    // 현재 X 좌표 반환
    public int getX() {
        return x;
    }

    // 현재 Y 좌표 반환
    public int getY() {
        return y;
    }

    // 무적 상태 여부 반환
    public boolean isInvincible() {
        return invincible;
    }

    // HP 비율(0.0~1.0) 반환: UI 바 그릴 때 유용
    public float getHpRatio() {
        return curHp / (float) maxHp;
    }

    // HP 감소 (추가 호출용)
    public void decreasePlayerHp() {
        curHp -= 1;
    }

    // HP 증가 (아이템 사용 등)
    public void increasePlayerHp() {
        curHp += 1;
        if (curHp > maxHp) curHp = maxHp; // 최대 HP 초과 방지
    }
}