package UI_Scene;

import javax.swing.*;
import java.awt.*;

public class GameOverUIPanel extends UIPanel {

    private final String gameOverText = "GAME OVER";
    private final JButton retryButton;
    private final JButton mainMenuButton;

    public GameOverUIPanel(int width, int height) {
        super(width, height);

        int buttonWidth = 200;
        int buttonHeight = 60;
        int gap = 20;

        retryButton = new JButton("Retry");
        mainMenuButton = new JButton("Main Menu");

        retryButton.setFont(uiFont);
        mainMenuButton.setFont(uiFont);

        int baseY = height / 2 + 100;
        retryButton.setBounds(width / 2 - buttonWidth / 2, baseY, buttonWidth, buttonHeight);
        mainMenuButton.setBounds(width / 2 - buttonWidth / 2, baseY + buttonHeight + gap, buttonWidth, buttonHeight);

        add(retryButton);
        add(mainMenuButton);
    }

    public JButton getRetryButton() {
        return retryButton;
    }

    public JButton getMainMenuButton() {
        return mainMenuButton;
    }

    @Override
    protected void drawUI(Graphics2D g2) {
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, getWidth(), getHeight());

        // GAME OVER 텍스트
        g2.setFont(mainFont.deriveFont(40f));
        g2.setColor(Color.WHITE);
        FontMetrics fm = g2.getFontMetrics();
        int textWidth = fm.stringWidth(gameOverText);
        int x = (getWidth() - textWidth) / 2;
        int y = getHeight() / 2 - 100;
        g2.drawString(gameOverText, x, y);

        // 점수 출력
        int score = GameManager.getInstance().getScore();
        String scoreText = "Your Score: " + score;
        g2.setFont(uiFont.deriveFont(28f));
        FontMetrics scoreFM = g2.getFontMetrics();
        int scoreX = (getWidth() - scoreFM.stringWidth(scoreText)) / 2;
        int scoreY = y + 60;
        g2.drawString(scoreText, scoreX, scoreY);

        // 생존 시간 출력
        int totalSeconds = GameManager.getInstance().getTimer();
        String timeStr = String.format("Survived Time: %02d:%02d", totalSeconds / 60, totalSeconds % 60);
        FontMetrics timeFM = g2.getFontMetrics();
        int timeX = (getWidth() - timeFM.stringWidth(timeStr)) / 2;
        int timeY = scoreY + 40;
        g2.drawString(timeStr, timeX, timeY);
    }
}
