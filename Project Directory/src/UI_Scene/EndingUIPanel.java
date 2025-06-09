package UI_Scene;

import java.awt.*;

//  Non-Used
public class EndingUIPanel extends UIPanel {

    private String resultText = "GAME OVER";

    public EndingUIPanel(int width, int height) {
        super(width, height);
    }


    public void setResultText(String text) {
        this.resultText = text;
        repaint();
    }

    @Override
    protected void drawUI(Graphics2D g2) {
        g2.setFont(mainFont);
        g2.setColor(Color.RED);
        g2.drawString(resultText, 100, 200);
    }
}
