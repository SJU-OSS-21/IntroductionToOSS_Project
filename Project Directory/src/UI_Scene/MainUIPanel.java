package UI_Scene;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.net.URL;

public class MainUIPanel extends UIPanel {

    private JButton startButton;
    private JButton exitButton;
    private JLabel versionLabel;

    //  정렬을 위해 html 사용
    private static final String TITLE_TEXT = "<html><div style='text-align: center;'>Pull<br>The<br>Request</div></html>";
    private static final int TITLE_LINE_COUNT = 3;
    private static final int TITLE_LINE_SPACING = 6;

    public MainUIPanel(int width, int height) {
        this(width, height, 65f, 16f); // 기본 폰트 크기 설정
    }

    //   타이틀/버전 폰트 크기를 외부에서 주입 가능하도록 오버로드
    public MainUIPanel(int width, int height, float titleFontSize, float versionFontSize) {
        super(width, height);

        initUIComponents(width, height, titleFontSize, versionFontSize);
    }

    private void initUIComponents(int width, int height, float titleFontSize, float versionFontSize) {
        int centerX = width / 2;
        int buttonWidth = 200;

        //  Title
        GradientTitlePanel titlePanel = new GradientTitlePanel(mainFont.deriveFont(titleFontSize));
        titlePanel.setBounds(0, height / 8, width, 3 * (int)(titleFontSize + TITLE_LINE_SPACING) + 50);
        add(titlePanel);

        //  Start
        startButton = new JButton("Start");
        startButton.setForeground(Color.BLACK);
        int startY = height / 2 + 60; // ⬅ 기존보다 60px 아래로
        applyImageToButton(startButton, "GameRes/UIs/DefaultRectBTN.png", centerX, startY, buttonWidth, mainFont.deriveFont(30f));
        add(startButton);

        //  Exit
        exitButton = new JButton("Exit");
        exitButton.setForeground(Color.BLACK);
        int exitY = startButton.getY() + startButton.getHeight() + 35;
        applyImageToButton(exitButton, "GameRes/UIs/DefaultRectBTN.png", centerX, exitY, buttonWidth, mainFont.deriveFont(33f));
        add(exitButton);

        //  Version Label
        versionLabel = new JLabel("OSSW Final Project v.20250609 by HJD Team", SwingConstants.LEFT);
        versionLabel.setFont(textFont.deriveFont(versionFontSize - 5f)); // 약간 더 작게
        versionLabel.setForeground(Color.DARK_GRAY);

        versionLabel.setBounds(8, height - 25, width, 20);
        add(versionLabel);
    }

    //  버튼에 크기 조절 가능한 Image 첨부
    private void applyImageToButton(JButton button, String imagePath, int centerX, int topY, int targetWidth, Font font) {
        URL iconURL = getClass().getClassLoader().getResource(imagePath);
        if (iconURL != null) {
            ImageIcon rawIcon = new ImageIcon(iconURL);

            int fixedHeight = 70; // 버튼 높이 고정
            Image scaledImg = rawIcon.getImage().getScaledInstance(targetWidth, fixedHeight, Image.SCALE_SMOOTH);
            ImageIcon scaledIcon = new ImageIcon(scaledImg);

            button.setIcon(scaledIcon);
            button.setBounds(centerX - targetWidth / 2, topY, targetWidth, fixedHeight);

            // 텍스트 스타일
            button.setFont(font);
            button.setForeground(Color.WHITE);
            button.setHorizontalTextPosition(SwingConstants.CENTER);
            button.setVerticalTextPosition(SwingConstants.CENTER);
            button.setIconTextGap(-15);
        }

        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setOpaque(false);
    }



    @Override
    protected void drawUI(Graphics2D g2) {
        // 필요 시 배경 이미지나 효과 그리기
    }

    //  For AddListener
    public JButton getStartButton() {
        return startButton;
    }

    public JButton getExitButton() {
        return exitButton;
    }

    //  Title Panel에 Gradient를 적용
    private class GradientTitlePanel extends JPanel {
        private final String[] lines = {"Pull", "The", "Request"};
        private final Font font;

        public GradientTitlePanel(Font font) {
            this.font = font;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setFont(font);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            FontMetrics fm = g2.getFontMetrics();
            int x = getWidth() / 2;
            int ascent = fm.getAscent();
            int descent = fm.getDescent();
            int lineHeight = ascent + descent;

            int y = ascent + 30; // 기준선 확보 + 위쪽 padding


            for (String line : lines) {
                int textWidth = fm.stringWidth(line);
                int textX = x - textWidth / 2;

                //  그림자 효과
                g2.setColor(new Color(0, 0, 0, 120)); // 반투명 블랙
                g2.drawString(line, textX + 2, y + 2); // 살짝 아래쪽으로 offset

                //  양방향 그라데이션 텍스트
                //  텍스트 Shape 생성
                Shape textShape = font.createGlyphVector(g2.getFontRenderContext(), line).getOutline();
                AffineTransform transform = AffineTransform.getTranslateInstance(textX, y);
                Shape translatedShape = transform.createTransformedShape(textShape);

                // 중앙 기준 위, 아래 대칭 그라데이션
                int gradientHeight = lineHeight;
                int centerY = y - fm.getAscent() / 2;

                // 중앙 기준 위쪽 (연한 회색 -> 밝은 은색)
                GradientPaint gradient = new GradientPaint(
                        0, centerY - gradientHeight / 2,
                        new Color(100, 100, 100),
                        0, centerY,
                        new Color(220, 220, 220),
                        true
                );

                // 중앙 기준 아래쪽 (밝은 은색 -> 짙은 회색)
                GradientPaint gradient2 = new GradientPaint(
                        0, centerY,
                        new Color(220, 220, 220),
                        0, centerY + gradientHeight / 2,
                        new Color(80, 80, 80), // 어두운 회색
                        true
                );


                // 상반 + 하반을 두 번 나눠서 fill (대칭 그라데이션 구현)
                g2.setClip(translatedShape);
                g2.setPaint(gradient);
                g2.fillRect(textX, centerY - gradientHeight / 2, textWidth, gradientHeight / 2);

                g2.setPaint(gradient2);
                g2.fillRect(textX, centerY, textWidth, gradientHeight / 2);

                g2.setClip(null);

                y += lineHeight + TITLE_LINE_SPACING;
            }
        }
    }


}
