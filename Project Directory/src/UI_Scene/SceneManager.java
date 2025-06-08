package UI_Scene;

import Enemies.EnemyPanel;
import Map_Audio.SoundManager;
import Map_Audio.TileManager;
import Player_Item.Panel.PlayerPanel;
import main.GamePanel;
import main.GamePanel2;
import main.MapPanel;

import javax.sound.sampled.SourceDataLine;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.net.URL;
import java.util.Random;

import static Map_Audio.SoundManager.MainSceneSOUNDID;
import static Map_Audio.SoundManager.GameSceneSOUNDID;
import static Map_Audio.SoundManager.GameOverSceneSOUNDID;

//  아래에 있는 MainScene, InGameScene, BaseScene에 Object(Panel)을 넣어주세요

class MainScene extends BaseScene {
    final int originalTileSize = 16;
    final int scale = 3;
    final int tileSize = originalTileSize * scale;
    final int maxScreenCol = 10;//(가로 타일 개수)
    final int maxScreenRow = 20;//(세로 타일 개수)
    final int screenWidth = tileSize * maxScreenCol;//가로 픽셀 개수
    final int screenHeight = tileSize * maxScreenRow;//세로 픽셀 개수
    final double FPS = 60.0;

    //  UI Panel
    MainUIPanel mainUIPanel;

    public MainScene() {}

    //  TODO : Scene JLayeredPane에 대한 설정 기입
    @Override
    public void setScene() {
        setPreferredSize(new Dimension(screenWidth, screenHeight));
        setLayout(null);
    }

    // TODO :  Panel 여기에 설치
    @Override
    public void setGameObjectList() {
        MapPanel mp = new MapPanel();
        mp.setBounds(new Rectangle(0, 0, screenWidth, screenHeight));
        this.add(mp, Integer.valueOf(0));

        UIPlayerPanel playerUI = new UIPlayerPanel("player_normal.png", screenWidth / 2, screenHeight / 2, screenWidth, screenHeight);
        playerUI.setBounds(0, 0, screenWidth, screenHeight);
        this.add(playerUI, Integer.valueOf(2));
    }


    @Override
    public void setUISet() {
        mainUIPanel = new MainUIPanel(screenWidth, screenHeight);

        //   Start 버튼 클릭 시 게임 시작 로직
        mainUIPanel.getStartButton().addActionListener(e -> {
            SoundManager.play(7,1f);
            SceneManager.changeScene(SceneManager.Scene.Loading);
        });

        //   Exit 버튼 클릭 시 프로그램 종료
        mainUIPanel.getExitButton().addActionListener(e -> {
            SoundManager.play(7,1f);
            System.exit(0); // 정상 종료
        });

        this.add(mainUIPanel, Integer.valueOf(10));
    }
}


class InGameScene extends BaseScene {
    final int originalTileSize = 16;
    final int scale = 3;
    final int tileSize = originalTileSize * scale;
    final int maxScreenCol = 10;//(가로 타일 개수)
    final int maxScreenRow = 20;//(세로 타일 개수)
    final int screenWidth = tileSize * maxScreenCol;//가로 픽셀 개수
    final int screenHeight = tileSize * maxScreenRow;//세로 픽셀 개수
    final double FPS = 60.0;

    public InGameScene() {}

    @Override
    public void setScene() {
        setPreferredSize(new Dimension(screenWidth, screenHeight));
        setLayout(null);

        InGameManager.getInstance().reset();
    }

    @Override
    public void setGameObjectList() {
        MapPanel mp = new MapPanel();
        mp.setBounds(new Rectangle(0, 0, screenWidth, screenHeight));
        this.add(mp, Integer.valueOf(0));

        PlayerPanel playerPanel = new PlayerPanel(screenWidth, screenHeight);
        playerPanel.setBounds(new Rectangle(0, 0, screenWidth, screenHeight));
        this.add(playerPanel, Integer.valueOf(1));

        InGameManager.getInstance().setPlayerPanel(playerPanel);
        InGameManager.getInstance().setPlayer(playerPanel.player);

        InGameUIPanel inGameUIPanel = new InGameUIPanel(screenWidth, screenHeight, InGameManager.getInstance());
        InGamePausePanel pausePanel = new InGamePausePanel(screenWidth, screenHeight);

        InGameManager.getInstance().initialize(this, inGameUIPanel, pausePanel, playerPanel.player);

        this.add(inGameUIPanel, Integer.valueOf(5));
        pausePanel.setVisible(false);
        this.add(pausePanel, Integer.valueOf(12));

        JPanel overlay = InGameManager.getInstance().getPauseOverlayPanel();
        overlay.setBounds(0, 0, screenWidth, screenHeight);
        this.add(overlay, Integer.valueOf(10));

        InGameManager.getInstance().setPlayer(playerPanel.player);
        inGameUIPanel.setPlayer(playerPanel.player);

        EnemyPanel enemyPanel = new EnemyPanel(screenWidth, screenHeight, InGameManager.getInstance());
        enemyPanel.setBounds(new Rectangle(0, 0, screenWidth, screenHeight));
        this.add(enemyPanel, Integer.valueOf(2));

        enemyPanel.setPlayerPanel(playerPanel);
        playerPanel.setEnemyPanel(enemyPanel);
    }

    //  TODO : UI Panel 기입
    @Override
    public void setUISet() {
        InGameManager.getInstance().setPausePanelVisible(false);

        InGameManager.getInstance().setPausePanelVisible(false);
        InGameManager.getInstance().pausePanel.getMainMenuButton().addActionListener(e -> {
            SceneManager.changeScene(SceneManager.Scene.Main);
        });

        InGameManager.getInstance().pausePanel.getRetryButton().addActionListener(e -> {
            SceneManager.changeScene(SceneManager.Scene.Loading);
        });
    }
}

class LoadingScene extends BaseScene {

    private final int screenWidth = 16 * 3 * 10;
    private final int screenHeight = 16 * 3 * 20;

    private SceneManager.Scene nextScene;
    private JLabel loadingLabel = new JLabel("", SwingConstants.CENTER);
    private JLabel tipLabel = new JLabel("", SwingConstants.LEFT);
    private File textFontURL;
    private Font textFont;

    private final String[] tips = {
            "Tips : HJD는 황재동입니다.",
            "Tips : 시험 공부할 시간이 없나요? 안 하면 됩니다.",
            "Tips : Swing은 현석이가 좋아하는 재즈 장르입니다.",
            "Tips : 유니티가 그리워지는 순간이군요.",
            "Tips : 이런 강준 안강준",
            "Tips : 영준이는 제 중학교 영어 선생님 이름입니다.",
            "Tips : 항상 커밋을 잘하는 게 어떨까요?",
            "Tips : Powered by Java ? ㄴㄴ Powered By GPT",
            "Tips : 뭔 팁은 팁이야 딱 보면 몰라?"
    };

    public LoadingScene(SceneManager.Scene nextScene) {}

    public void initLoading() {
        this.nextScene = SceneManager.Scene.InGame;

        try {
            URL textFontURL = getClass().getClassLoader().getResource("Fonts/high1 Wonchuri Title B.ttf");
            if (textFontURL == null) throw new RuntimeException("폰트 파일을 찾을 수 없습니다.");
            textFont = Font.createFont(Font.TRUETYPE_FONT, new File(textFontURL.toURI())).deriveFont(20f);
        } catch (Exception e) {
            e.printStackTrace();
            textFont = new Font("SansSerif", Font.PLAIN, 20);
        }

        loadingLabel = new JLabel("", SwingConstants.CENTER);
        tipLabel = new JLabel("", SwingConstants.LEFT);
    }


    @Override
    public void setScene() {
        setPreferredSize(new Dimension(screenWidth, screenHeight));
        setLayout(null);

        setBackground(Color.BLACK);
        setOpaque(true);
    }


    @Override
    public void setGameObjectList() {}

    @Override
    public void setUISet() {
        initLoading();

        loadingLabel.setFont(textFont.deriveFont(32f));
        loadingLabel.setForeground(Color.WHITE);
        loadingLabel.setBounds(0, screenHeight / 2 - 40, screenWidth, 80);
        add(loadingLabel);

        tipLabel = new JLabel(randomTip(), SwingConstants.CENTER);
        tipLabel.setFont(textFont.deriveFont(18f));
        tipLabel.setForeground(Color.LIGHT_GRAY);
        tipLabel.setHorizontalAlignment(SwingConstants.CENTER);
        tipLabel.setVerticalAlignment(SwingConstants.CENTER);

        // === 텍스트의 실제 너비 측정 ===
        FontMetrics fm = tipLabel.getFontMetrics(tipLabel.getFont());
        int textWidth = fm.stringWidth(tipLabel.getText());

        // === 라벨의 너비와 위치 재조정 (텍스트 중앙 정렬) ===
        int tipX = (screenWidth - textWidth) / 2;
        int tipY = screenHeight - 100;
        int tipHeight = 40;
        tipLabel.setBounds(tipX, tipY, textWidth, tipHeight);

        add(tipLabel);

        Timer dotTimer = new Timer(500, null);
        dotTimer.addActionListener(e -> {
            String base = "Loading";
            int dotCount = (int) ((System.currentTimeMillis() / 500) % 4);
            loadingLabel.setText(base + ".".repeat(dotCount));
        });
        dotTimer.start();

        Timer nextSceneTimer = new Timer(5000, e -> SceneManager.changeScene(nextScene));
        nextSceneTimer.setRepeats(false);
        nextSceneTimer.start();
    }

    private String randomTip() {
        Random rand = new Random();
        return tips[rand.nextInt(tips.length)];
    }
}


class GameOverScene extends BaseScene {
    final int originalTileSize = 16;
    final int scale = 3;
    final int tileSize = originalTileSize * scale;
    final int maxScreenCol = 10;//(가로 타일 개수)
    final int maxScreenRow = 20;//(세로 타일 개수)
    final int screenWidth = tileSize * maxScreenCol;//가로 픽셀 개수
    final int screenHeight = tileSize * maxScreenRow;//세로 픽셀 개수
    final double FPS = 60.0;

    GameOverUIPanel gameOverUIPanel;

    public GameOverScene() {}

    @Override
    public void setScene() {
        setPreferredSize(new Dimension(screenWidth, screenHeight));
        setLayout(null);
    }

    //  TODO : Panel 여기에 설치
    @Override
    public void setGameObjectList() {}

    @Override
    public void setUISet() {
        gameOverUIPanel = new GameOverUIPanel(screenWidth, screenHeight);
        add(gameOverUIPanel, Integer.valueOf(10));

        gameOverUIPanel.getMainMenuButton().addActionListener(e->{
            SceneManager.changeScene(SceneManager.Scene.Main);
        });
        gameOverUIPanel.getRetryButton().addActionListener(e->{
            SceneManager.changeScene(SceneManager.Scene.Loading);
        });
    }
}


//  Non-SingleTon -> Static Class
public class SceneManager {

    // === Scene Instances ===
    public static BaseScene mainScene = null;
    public static BaseScene loadingScene = null;
    public static BaseScene gameScene = null;
    public static BaseScene gameOverScene = null;

    public static int curSceneNum;
    public static String curSceneName = null;
    public static BaseScene curScene = null;

    // === Scene Enum ===
    public enum Scene {
        Main,
        Loading,
        InGame,
        GameOver
    }

    // === Overloaded Scene Changers ===
    public static void changeScene(int sid) {
        curSceneNum = sid;
        curSceneName = Scene.values()[sid].toString();
        innerChangeScene();
    }

    public static void changeScene(Scene name) {
        curSceneNum = name.ordinal();
        curSceneName = name.toString();
        innerChangeScene();
    }

    // === 내부 씬 전환 처리 ===
    private static void innerChangeScene() {
        JFrame frame = GameManager.getInstance().getMainFrame();

        // 이전 씬 제거
        if (curScene != null) {
            frame.remove(curScene);
        }

        // ESC 리스너 등록 상태 초기화
        try {
            java.lang.reflect.Field field = InGameManager.class.getDeclaredField("escListenerRegistered");
            field.setAccessible(true);
            field.setBoolean(null, false);
        } catch (Exception e) {
            e.printStackTrace();
        }

        switch (curSceneNum) {
            case 0:
                mainScene = new MainScene();
                SoundManager.stopAll();
                SoundManager.stop(SoundManager.GameOverSceneSOUNDID);
                SoundManager.play(8, 1f);
                SoundManager.MainSceneSOUNDID = SoundManager.play(0, 0.6f);
                curScene = mainScene;
                GameManager.getInstance().resetScoreAndTimer();
                break;
            case 1:
                loadingScene = new LoadingScene(Scene.InGame);
                SoundManager.stop(SoundManager.MainSceneSOUNDID);
                SoundManager.stopAll();
                curScene = loadingScene;
                break;
            case 2:
                gameScene = new InGameScene();
                TileManager.nextTileIndex = 0;
                SoundManager.GameSceneSOUNDID = SoundManager.play(1, 0.6f);
                curScene = gameScene;
                GameManager.getInstance().resetScoreAndTimer();
                break;
            case 3:
                gameOverScene = new GameOverScene();
//                if (gameOverScene == null) gameOverScene = new GameOverScene();
                SoundManager.stop(GameSceneSOUNDID);
                SoundManager.stopAll();
                GameOverSceneSOUNDID = SoundManager.play(2,0.6f);
                SoundManager.play(9,0.6f);
                curScene = gameOverScene;
                break;
            default:
                System.err.println("Scene Error : Scene is Not Exist [" + curSceneNum + "]");
                return;
        }

        curScene.setScene();
        curScene.setGameObjectList();
        curScene.setUISet();

        frame.setContentPane(curScene);
        frame.revalidate();
        frame.repaint();
    }
}



