package UI_Scene;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.net.URL;

public abstract class UIPanel extends JPanel {

    protected Font mainFont;
    protected Font textFont;


    public UIPanel(int width, int height) {
        setOpaque(false);
        setLayout(null);
        setBounds(0, 0, width, height);
        loadFonts();
    }

    protected void loadFonts() {
        try {
            // Main 타이틀용 폰트 (크고 스타일리시한 폰트)
            URL mainFontURL = getClass().getClassLoader().getResource("Fonts/Ethnocentric Rg.otf");
            if (mainFontURL != null) {
                mainFont = Font.createFont(Font.TRUETYPE_FONT, new File(mainFontURL.toURI())).deriveFont(36f);
                GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(mainFont);
            } else {
                throw new Exception("mainFont not found");
            }

            // 본문용 폰트 (가독성 위주)
            URL textFontURL = getClass().getClassLoader().getResource("Fonts/Ubuntu-Regular.ttf");
            if (textFontURL != null) {
                textFont = Font.createFont(Font.TRUETYPE_FONT, new File(textFontURL.toURI())).deriveFont(20f);
                GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(textFont);
            } else {
                throw new Exception("textFont not found");
            }

        } catch (Exception e) {
            // fallback fonts
            mainFont = new Font("SansSerif", Font.BOLD, 30);
            textFont = new Font("Dialog", Font.PLAIN, 18);
            System.err.println("폰트 로딩 실패: 기본 폰트로 대체합니다.");
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawUI((Graphics2D) g);
    }

    protected abstract void drawUI(Graphics2D g2);
}
