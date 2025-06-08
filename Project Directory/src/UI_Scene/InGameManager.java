package UI_Scene;

import Player_Item.Model.Player;
import Player_Item.Panel.PlayerPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class InGameManager {

    // === 싱글톤 인스턴스 ===
    private static final InGameManager instance = new InGameManager();
    public static InGameManager getInstance() {
        return instance;
    }

    // === 참조 및 상태 변수 ===
    public InGameScene inGameScene;
    public InGameUIPanel inGameUIPanel;
    public InGamePausePanel pausePanel;
    public PauseOverlayPanel pauseOverlayPanel;

    public Player player;
    public JPanel pauseOverlay;
    public final List<Timer> managedTimers = new ArrayList<>();

    public PlayerPanel playerPanel;

    private int score = 0;
    private boolean isPaused = false;

    private static boolean escListenerRegistered = false;

    // === 생성자 비공개 ===
    private InGameManager() {
        // ESC 리스너는 최초 1회만 등록
        setupKeyListener();
    }

    /**
     * 초기화 함수 (씬 전환 시 호출 필수)
     */
    public void initialize(InGameScene scene, InGameUIPanel uiPanel, InGamePausePanel pausePanel, Player player) {
        this.inGameScene = scene;
        this.inGameUIPanel = uiPanel;
        this.pausePanel = pausePanel;
        this.player = player;

        this.pauseOverlayPanel = new PauseOverlayPanel();
        this.pauseOverlayPanel.setBounds(0, 0, scene.getWidth(), scene.getHeight());
        this.pauseOverlayPanel.setVisible(false);
        scene.add(pauseOverlayPanel);

        this.pauseOverlay = pauseOverlayPanel;

        this.managedTimers.clear();
        this.isPaused = false;
        this.score = 0;
    }

    public void resetPause(){
        this.isPaused = false;
    }

    public void reset() {
        inGameScene = null;
        inGameUIPanel = null;
        pausePanel = null;
        pauseOverlayPanel = null;
        pauseOverlay = null;
        player = null;
        managedTimers.clear();
        isPaused = false;
        score = 0;
        // escListenerRegistered는 유지 (한 번만 등록됨)
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
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

    public void setupKeyListener() {
        if (escListenerRegistered) return;

        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(e -> {
            // 현재 씬이 InGameScene일 때만 ESC 처리
            if (SceneManager.curScene instanceof InGameScene &&
                    e.getID() == KeyEvent.KEY_PRESSED &&
                    e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                togglePause();
                return true;
            }
            return false;
        });

        escListenerRegistered = true;
    }

    public void togglePause() {
        isPaused = !isPaused;
        setPausePanelVisible(isPaused);

        for (Timer timer : managedTimers) {
            if (isPaused) timer.stop();
            else timer.start();
        }

        if (inGameScene != null) {
            inGameScene.repaint();
        }
    }

    public void setPausePanelVisible(boolean visible) {
        if (pauseOverlayPanel != null) pauseOverlayPanel.setVisible(visible);
        if (pausePanel != null) pausePanel.setVisible(visible);
    }

    public void setPausePanel(InGamePausePanel pausePanel) {
        this.pausePanel = pausePanel;
    }

    public void setInGameUIPanel(InGameUIPanel inGameUIPanel) {
        this.inGameUIPanel = inGameUIPanel;
    }
    public void setPlayerPanel(PlayerPanel playerPanel) {
        this.playerPanel = playerPanel;
    }


    public void updateScore(int offset) {
        this.score += offset;
        GameManager.getInstance().updateScore(score);

        if (inGameUIPanel != null) {
            inGameUIPanel.setScore(score);
        }
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
                g2d.setColor(new Color(0, 0, 0, 150));
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.dispose();
            }
            super.paintComponent(g);
        }
    }
}
