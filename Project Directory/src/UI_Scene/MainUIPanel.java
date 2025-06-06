package UI_Scene;

import java.awt.*;

public class MainUIPanel extends UIPanel {

    public MainUIPanel(int width, int height) {
        super(width, height);
    }


    @Override
    protected void drawUI(Graphics2D g2) {
        g2.setFont(customFont);
        g2.setColor(Color.WHITE);
        g2.drawString("PRESS ENTER TO START", 100, 300);
    }
}


