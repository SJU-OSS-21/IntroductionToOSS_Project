package UI_Scene;

import javax.swing.*;
import java.awt.*;

public class InGamePausePanel extends JPanel {

    private final JButton mainMenuButton;
    private final JButton retryButton;

    // 이미지 경로 (이미지 파일 경로는 사용자가 프로젝트에 맞게 수정 가능)
    private static final String MAIN_MENU_IMAGE_PATH = "/UIs/HomeBTN.png";
    private static final String RETRY_IMAGE_PATH = "/UIs/Retry.png";

    public InGamePausePanel(int width, int height) {
        setLayout(null);
        setOpaque(false);
        setBounds(0, 0, width, height);

        // === 버튼 생성 및 아이콘 설정 ===
        mainMenuButton = new JButton(new ImageIcon(getClass().getResource(MAIN_MENU_IMAGE_PATH)));
        retryButton = new JButton(new ImageIcon(getClass().getResource(RETRY_IMAGE_PATH)));

        int buttonWidth = 200;
        int buttonHeight = 60;
        int gap = 20;

        int centerX = width / 2 - buttonWidth / 2;
        int centerY = height / 2 - (buttonHeight * 2 + gap) / 2;

        mainMenuButton.setBounds(centerX, centerY, buttonWidth, buttonHeight);
        retryButton.setBounds(centerX, centerY + buttonHeight + gap, buttonWidth, buttonHeight);

        // 배경 제거 (이미지 아이콘만 보이게)
        mainMenuButton.setBorderPainted(false);
        mainMenuButton.setContentAreaFilled(false);
        retryButton.setBorderPainted(false);
        retryButton.setContentAreaFilled(false);

        add(mainMenuButton);
        add(retryButton);
    }

    public JButton getMainMenuButton() {
        return mainMenuButton;
    }

    public JButton getRetryButton() {
        return retryButton;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(new Color(0, 0, 0, 160)); // 반투명 배경
        g2.fillRect(0, 0, getWidth(), getHeight());
    }
}
