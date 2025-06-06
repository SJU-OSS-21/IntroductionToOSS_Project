package UI_Scene;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.ArrayList;

public class InGameManager {

    // 전역 접근용
    public static InGameManager global;

    private final InGameScene inGameScene;
    private final JPanel pausePanel;
    private final PauseOverlayPanel pauseOverlayPanel;

    private boolean isPaused = false;
    private final List<Timer> managedTimers = new ArrayList<>();

    public InGameManager(InGameScene scene, JPanel pausePanel) {
        this.inGameScene = scene;
        this.pausePanel = pausePanel;
        this.pauseOverlayPanel = new PauseOverlayPanel();

        global = this; // 전역 인스턴스 등록
        setupKeyListener();
    }

    public boolean isPaused() {
        return isPaused;
    }

    public PauseOverlayPanel getPauseOverlayPanel() {
        return pauseOverlayPanel;
    }

    public void registerTimer(Timer timer) {
        managedTimers.add(timer);
    }

    private void setupKeyListener() {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(e -> {
            if (e.getID() == KeyEvent.KEY_PRESSED && e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                togglePause();
                return true; // 이벤트 소비
            }
            return false;
        });
    }

    public void togglePause() {
        isPaused = !isPaused;
        setPausePanelVisible(isPaused);

        for (Timer timer : managedTimers) {
            if (isPaused) timer.stop();
            else timer.start();
        }

        inGameScene.repaint();
    }

    public void setPausePanelVisible(boolean visible) {
        pausePanel.setVisible(visible);
        pauseOverlayPanel.setVisible(visible);
    }

    /**
     * 내부 클래스: 일시정지 상태를 표시할 반투명 회색 패널
     */
    public class PauseOverlayPanel extends JPanel {
        public PauseOverlayPanel() {
            setOpaque(false);
            setVisible(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            if (isVisible()) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setColor(new Color(0, 0, 0, 150)); // 반투명 회색
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.dispose();
            }
            super.paintComponent(g);
        }
    }
}
