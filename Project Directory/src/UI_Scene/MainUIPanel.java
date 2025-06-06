package UI_Scene;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class MainUIPanel extends UIPanel {

    private JLabel titleLabel;
    private JButton startButton;
    private JButton exitButton;
    private JLabel versionLabel;

    private static final String TITLE_TEXT = "<html><div style='text-align: center;'>Pull<br>The<br>Request</div></html>";
    private static final int TITLE_LINE_COUNT = 3;
    private static final int TITLE_LINE_SPACING = 6;

    public MainUIPanel(int width, int height) {
        this(width, height, 60f, 16f); // 기본 폰트 크기 설정
    }

    // 타이틀/버전 폰트 크기를 외부에서 주입 가능하도록 오버로드
    public MainUIPanel(int width, int height, float titleFontSize, float versionFontSize) {
        super(width, height);
        initUIComponents(width, height, titleFontSize, versionFontSize);
    }

    private void initUIComponents(int width, int height, float titleFontSize, float versionFontSize) {
        int centerX = width / 2;
        int buttonWidth = 150;

        // === Title Label (줄바꿈 + 폰트 사이즈 적용 + 위치 보정)
        titleLabel = new JLabel(TITLE_TEXT, SwingConstants.CENTER);
        titleLabel.setFont(mainFont.deriveFont(titleFontSize));
        titleLabel.setForeground(Color.BLACK);

        int estimatedLineHeight = Math.round(titleFontSize + 12); // 기존보다 더 여유
        int titleHeight = TITLE_LINE_COUNT * estimatedLineHeight;

        int titleY = height / 7 - 30; // 위로 조금 올리고
        titleLabel.setBounds(0, titleY, width, titleHeight + 20); // 아래로 공간 더 줌

        add(titleLabel);

        // === Start 버튼 ===
        startButton = new JButton("Start"); // ← 텍스트 추가
        int startY = height / 2;
        applyImageToButton(startButton, "UIs/DefaultRectBTN.png", centerX, startY, buttonWidth, mainFont.deriveFont(35f));
        add(startButton);

        // === Exit 버튼 ===
        exitButton = new JButton("Exit"); // ← 텍스트 추가
        int exitY = startButton.getY() + startButton.getHeight() + 35;
        applyImageToButton(exitButton, "UIs/DefaultRectBTN.png", centerX, exitY, buttonWidth, mainFont.deriveFont(35f));
        add(exitButton);


        // === Version Label ===
        versionLabel = new JLabel("OSSW Final Project v.20250607 by HJD Team", SwingConstants.LEFT);
        versionLabel.setFont(textFont.deriveFont(versionFontSize - 5f)); // 약간 더 작게
        versionLabel.setForeground(Color.DARK_GRAY);

        // 좌측 하단에 고정
        versionLabel.setBounds(8, height - 25, width, 20);
        add(versionLabel);
    }

    // 이미지 비율 유지 + 지정된 width 기준으로 JButton 설정
    private void applyImageToButton(JButton button, String imagePath, int centerX, int topY, int targetWidth, Font font) {
        URL iconURL = getClass().getClassLoader().getResource(imagePath);
        if (iconURL != null) {
            ImageIcon rawIcon = new ImageIcon(iconURL);
            int originalWidth = rawIcon.getIconWidth();
            int originalHeight = rawIcon.getIconHeight();

            float aspectRatio = (float) originalHeight / originalWidth;
            int scaledHeight = Math.round(targetWidth * aspectRatio);

            Image scaledImg = rawIcon.getImage().getScaledInstance(targetWidth, scaledHeight, Image.SCALE_SMOOTH);
            ImageIcon scaledIcon = new ImageIcon(scaledImg);

            button.setIcon(scaledIcon);
            button.setBounds(centerX - targetWidth / 2, topY, targetWidth, scaledHeight);

            // === 텍스트 스타일 설정 ===
            button.setFont(font);
            button.setForeground(Color.WHITE);
            button.setHorizontalTextPosition(SwingConstants.CENTER);
            button.setVerticalTextPosition(SwingConstants.CENTER);
            button.setIconTextGap(-15); //  텍스트 위로 밀기

        }

        // === 스타일 제거로 이미지 버튼처럼 만들기 ===
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setOpaque(false);
    }


    @Override
    protected void drawUI(Graphics2D g2) {
        // 필요 시 배경 이미지나 효과 그리기
    }

    // 버튼 접근용 getter (이벤트 연결할 때 사용)
    public JButton getStartButton() {
        return startButton;
    }

    public JButton getExitButton() {
        return exitButton;
    }
}
