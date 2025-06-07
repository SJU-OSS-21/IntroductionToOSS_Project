package UI_Scene;

import java.awt.*;

public class InGameUIPanel extends UIPanel {

    private int score = 0;

    public InGameUIPanel(int width, int height) {
        super(width, height);
    }

    public void setScore(int score) {
        this.score = score;
        repaint();
    }

    @Override
    protected void drawUI(Graphics2D g2) {
        g2.setFont(mainFont.deriveFont(18f));
        g2.setColor(Color.YELLOW);
        g2.drawString("Score: " + score, 20, 30);
    }
}
