package UI_Scene;

import javax.swing.*;
import java.awt.*;

//  PausePanel
public class InGamePausePanel extends UIPanel {

    //  Buttons
    private final JButton mainMenuButton;
    private final JButton retryButton;

    //  Res
    private static final String MAIN_MENU_IMAGE_PATH = "/GameRes/UIs/HomeBTN.png";
    private static final String RETRY_IMAGE_PATH = "/GameRes/UIs/Retry.png";

    public InGamePausePanel(int width, int height) {
        super(width, height);
        setLayout(null);
        setOpaque(false);
        setBounds(0, 0, width, height);

        mainMenuButton = new JButton(new ImageIcon(getClass().getResource(MAIN_MENU_IMAGE_PATH)));
        retryButton = new JButton(new ImageIcon(getClass().getResource(RETRY_IMAGE_PATH)));

        int buttonWidth = 200;
        int buttonHeight = 60;
        int gap = 40; // ← 간격을 40px로 늘림

        int centerX = width / 2 - buttonWidth / 2;
        int centerY = height / 2 - (buttonHeight * 2 + gap) / 2;

        mainMenuButton.setBounds(centerX, centerY, buttonWidth, buttonHeight);
        retryButton.setBounds(centerX, centerY + buttonHeight + gap, buttonWidth, buttonHeight);

        mainMenuButton.setBorderPainted(false);
        mainMenuButton.setContentAreaFilled(false);
        retryButton.setBorderPainted(false);
        retryButton.setContentAreaFilled(false);

        add(mainMenuButton);
        add(retryButton);
    }

    //  For AddListener
    public JButton getMainMenuButton() {
        return mainMenuButton;
    }

    //  For AddListener
    public JButton getRetryButton() {
        return retryButton;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(new Color(0, 0, 0, 160));
        g2.fillRect(0, 0, getWidth(), getHeight());
    }

    @Override
    protected void drawUI(Graphics2D g2) {
        //  Empty
    }
}
