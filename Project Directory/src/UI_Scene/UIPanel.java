package UI_Scene;

import javax.swing.*;
import java.awt.*;

public abstract class UIPanel extends JPanel {

    protected Font mainFont;
    protected Font textFont;
    protected Font uiFont;


    public UIPanel(int width, int height) {
        setOpaque(false);
        setLayout(null);
        setBounds(0, 0, width, height);
        loadFonts();
    }

    protected void loadFonts() {
        try {
            try (var is = getClass().getClassLoader().getResourceAsStream("GameRes/Fonts/Ethnocentric Rg.otf")) {
                if (is != null) {
                    mainFont = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(36f);
                    GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(mainFont);
                } else throw new Exception("mainFont not found");
            }

            try (var is = getClass().getClassLoader().getResourceAsStream("GameRes/Fonts/Ubuntu-Regular.ttf")) {
                if (is != null) {
                    textFont = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(20f);
                    GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(textFont);
                } else throw new Exception("textFont not found");
            }

            try (var is = getClass().getClassLoader().getResourceAsStream("GameRes/Fonts/high1 Wonchuri Title B.ttf")) {
                if (is != null) {
                    uiFont = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(20f);
                    GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(uiFont);
                } else throw new Exception("uiFont not found");
            }

        } catch (Exception e) {
            //  Exception 시 기본 폰트 불려오도록 설정
            mainFont = new Font("SansSerif", Font.BOLD, 30);
            textFont = new Font("Dialog", Font.PLAIN, 18);
            uiFont = new Font("Dialog", Font.BOLD, 20);
            System.err.println("폰트 로딩 실패: 기본 폰트로 대체합니다.");
            e.printStackTrace();
        }
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawUI((Graphics2D) g);
    }

    protected abstract void drawUI(Graphics2D g2);
}
