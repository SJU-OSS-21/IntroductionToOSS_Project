package UI_Scene;

import Player_Item.Model.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;

public class InGameUIPanel extends UIPanel {

    //  Attributes
    private int score = 0;

    private float displayedHealth = 1f;       //    보간된 체력 값 (진한 빨간색)
    private float bufferedHealth = 1f;        //    버퍼 체력 값 (연한 빨간색)
    private float targetHealth = 1f;
    private long lastUpdateTime;

    private int elapsedTimeInSeconds = 0;
    private final BufferedImage timerImage;

    private Player player; //   플레이어 참조

    //      Manager Ref
    InGameManager inGameManager;

    public InGameUIPanel(int width, int height, InGameManager inGameManager) {
        super(width, height);
        this.inGameManager = inGameManager;

        lastUpdateTime = System.currentTimeMillis();

        //  Timer
        BufferedImage tempImg = null;
        try {
            URL resource = getClass().getResource("/GameRes/UIs/Timer.png");
            if (resource == null) {
                throw new IllegalArgumentException("이미지 리소스를 찾을 수 없습니다: /UIs/Timer.png");
            }
            tempImg = ImageIO.read(resource);
        } catch (IOException | IllegalArgumentException e) {
            e.printStackTrace();
        }
        timerImage = tempImg;

        startTimerUpdate();
    }

    //  Score 갱신
    public void setScore(int score) {
        this.score = score;
        repaint();
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    //  내부 Timer
    private void startTimerUpdate() {
        new Timer(1000, e -> {
            //  InGameManager가 존재하고, 일시정지 상태가 아니어야 증가
            if (InGameManager.getInstance() != null && !InGameManager.getInstance().isPaused()) {
                elapsedTimeInSeconds++;
                repaint();
            }
        }).start();
    }

    public void UIUpdate() {
        interpolateHealth(); // 내부 상태 갱신
        repaint();           // 화면에 실제로 반영
    }

    private boolean deathHandled = false; // 사망 처리 중복 방지용

    //  체력바 보간
    private void interpolateHealth() {
        if (player != null) {
            targetHealth = Math.max(0f, Math.min(1f, player.getHpRatio()));
        }

        long currentTime = System.currentTimeMillis();
        float delta = (currentTime - lastUpdateTime) / 1000f;
        lastUpdateTime = currentTime;

        //  실제 체력 : 빠르게 접근
        float speed = 1.5f;
        if (displayedHealth < targetHealth) {
            displayedHealth = Math.min(displayedHealth + speed * delta, targetHealth);
        } else if (displayedHealth > targetHealth) {
            displayedHealth = Math.max(displayedHealth - speed * delta, targetHealth);
        }

        //  버퍼 체력 : 느리게 접근
        float bufferSpeed = 0.5f;
        if (bufferedHealth > displayedHealth) {
            bufferedHealth = Math.max(bufferedHealth - bufferSpeed * delta, displayedHealth);
        } else if (bufferedHealth < displayedHealth) {
            bufferedHealth = displayedHealth;
        }

        //  체력 게이지가 완전히 0이 된 후 처리 (체력을 Player가 아니라 실질적으로 UI로 처리)
        if (displayedHealth == 0f && !deathHandled) {
            deathHandled = true;

            //  게임 매니저에 점수와 생존 시간 등록
            GameManager.getInstance().score = this.score;
            GameManager.getInstance().timer = this.elapsedTimeInSeconds;

            // 씬 전환
            SwingUtilities.invokeLater(() -> {
                SceneManager.changeScene(SceneManager.Scene.GameOver);
            });
        }
    }




    @Override
    protected void drawUI(Graphics2D g2) {
        interpolateHealth();

        // === 타이머 (좌측 상단) ===
        if (timerImage != null) {
            int originalW = timerImage.getWidth();
            int originalH = timerImage.getHeight();

            int targetWidth = 120; // 가로 크기만 조절
            float ratio = (float) originalH / originalW;
            int timerW = targetWidth;
            int timerH = (int) (targetWidth * ratio);

            int timerX = 10;  // 좌측 상단으로 고정
            int timerY = 10;

            g2.drawImage(timerImage, timerX, timerY, timerW, timerH, null);

            String timeStr = String.format("%02d:%02d", elapsedTimeInSeconds / 60, elapsedTimeInSeconds % 60);
            g2.setFont(uiFont.deriveFont(16f));
            g2.setColor(Color.WHITE);

            // 타이머 이미지 내부 숫자 위치 수동 조정
            int textX = timerX + 57;  // 필요 시 미세 조정
            int textY = timerY + 28;  // 필요 시 미세 조정

            g2.drawString(timeStr, textX, textY);
        }

        // === Score (우측 상단) ===
        g2.setFont(uiFont.deriveFont(18f));
        g2.setColor(Color.YELLOW);

        String scoreStr = "Score : " + score;
        FontMetrics fm = g2.getFontMetrics();
        int scoreX = getWidth() - fm.stringWidth(scoreStr) - 20;  // 우측 끝에서 20px 안쪽
        int scoreY = 30;

        g2.drawString(scoreStr, scoreX, scoreY);

        // === 체력바 (하단) ===
        int barWidth = getWidth() - 40;
        int barHeight = 8;
        int barX = 20;
        int barY = getHeight() - barHeight - 5;

        g2.setColor(Color.DARK_GRAY);
        g2.fillRect(barX, barY, barWidth, barHeight);

        g2.setColor(new Color(255, 120, 120));
        g2.fillRect(barX, barY, (int) (barWidth * bufferedHealth), barHeight);

        g2.setColor(Color.RED);
        g2.fillRect(barX, barY, (int) (barWidth * displayedHealth), barHeight);

        g2.setColor(Color.BLACK);
        g2.drawRect(barX, barY, barWidth, barHeight);
    }

}
