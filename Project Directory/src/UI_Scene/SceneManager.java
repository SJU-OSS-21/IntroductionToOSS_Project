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

    public MainScene() {
        super();
        setUISet();
    }

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

    InGameManager inGameManager;
    InGameUIPanel inGameUIPanel;
    InGamePausePanel pausePanel;

    public InGameScene() {
        super();


        setUISet();
    }

    @Override
    public void setScene() {
        inGameUIPanel = new InGameUIPanel(screenWidth, screenHeight, inGameManager);
        this.add(inGameUIPanel, Integer.valueOf(5));

        pausePanel = new InGamePausePanel(screenWidth, screenHeight);
        pausePanel.setVisible(false);
        this.add(pausePanel, Integer.valueOf(12));

        inGameManager = new InGameManager(this, inGameUIPanel, pausePanel); // 수정됨

        inGameManager.setInGameUIPanel(inGameUIPanel);


        JPanel overlay = inGameManager.getPauseOverlayPanel();
        overlay.setBounds(0, 0, screenWidth, screenHeight);
        this.add(overlay, Integer.valueOf(10)); // 꼭 높은 레이어에

        setPreferredSize(new Dimension(screenWidth, screenHeight)); // 실제 창 크기
        setLayout(null);
    }

    @Override
    public void setGameObjectList() {
        MapPanel mp = new MapPanel();
        mp.setBounds(new Rectangle(0, 0, screenWidth, screenHeight));
        this.add(mp, Integer.valueOf(0));

        PlayerPanel playerPanel = new PlayerPanel(screenWidth, screenHeight);
        playerPanel.setBounds(new Rectangle(0, 0, screenWidth, screenHeight));
        this.add(playerPanel, Integer.valueOf(1));

        inGameManager.setPlayerPanel(playerPanel);
        inGameManager.setPlayer(playerPanel.player);
        inGameUIPanel.setPlayer(playerPanel.player);

        EnemyPanel enemyPanel = new EnemyPanel(screenWidth, screenHeight, inGameManager);
        enemyPanel.setBounds(new Rectangle(0, 0, screenWidth, screenHeight));
        this.add(enemyPanel, Integer.valueOf(2));

        enemyPanel.setPlayerPanel(playerPanel);
        playerPanel.setEnemyPanel(enemyPanel);
    }

    //  TODO : UI Panel 기입
    @Override
    public void setUISet() {
        pausePanel.getMainMenuButton().addActionListener(e -> {
            SceneManager.changeScene(SceneManager.Scene.Main);
        });

        pausePanel.getRetryButton().addActionListener(e -> {
            SceneManager.changeScene(SceneManager.Scene.Loading);
        });
    }
}

class LoadingScene extends BaseScene {

    private final int screenWidth = 16 * 3 * 10;
    private final int screenHeight = 16 * 3 * 20;

    private final SceneManager.Scene nextScene;
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

    public LoadingScene(SceneManager.Scene nextScene) {
        this.nextScene = nextScene;

        try {
            URL textFontURL = getClass().getClassLoader().getResource("Fonts/high1 Wonchuri Title B.ttf");
            if (textFontURL == null) throw new RuntimeException("폰트 파일을 찾을 수 없습니다.");
            textFont = Font.createFont(Font.TRUETYPE_FONT, new File(textFontURL.toURI())).deriveFont(20f);
        } catch (Exception e) {
            e.printStackTrace();
            textFont = new Font("SansSerif", Font.PLAIN, 20); // 폰트 로딩 실패 시 기본 폰트로 대체
        }

        loadingLabel = new JLabel("", SwingConstants.CENTER);
        tipLabel = new JLabel("", SwingConstants.LEFT);

        setUISet();
    }


    @Override
    public void setScene() {
        setPreferredSize(new Dimension(screenWidth, screenHeight));
        setLayout(null);

        setBackground(Color.BLACK);     // 배경색 설정
        setOpaque(true);                // 배경색이 보이게 설정
    }


    @Override
    public void setGameObjectList() {
        // 사용 안 함
    }

    @Override
    public void setUISet() {
        // === Loading 텍스트 설정 ===
        loadingLabel.setFont(textFont.deriveFont(32f));
        loadingLabel.setForeground(Color.WHITE);
        loadingLabel.setBounds(0, screenHeight / 2 - 40, screenWidth, 80);
        add(loadingLabel);

        // Tip 라벨 설정
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


        // === 점 애니메이션 처리 ===
        Timer dotTimer = new Timer(500, null);
        dotTimer.addActionListener(e -> {
            String base = "Loading";
            int dotCount = (int) ((System.currentTimeMillis() / 500) % 4); // 0~3
            loadingLabel.setText(base + ".".repeat(dotCount));
        });
        dotTimer.start();

        // === 씬 전환 타이머 === -> ms 단위
        Timer nextSceneTimer = new Timer(5000, e -> SceneManager.changeScene(nextScene)); // 5초로 증가
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

    public GameOverScene() {
        super();

        setUISet();
    }

    //  TODO : Scene JLayeredPane에 대한 설정 기입
    @Override
    public void setScene() {
        setPreferredSize(new Dimension(screenWidth, screenHeight));
        setLayout(null);
        setFocusable(true);
    }

    //  TODO : Panel 여기에 설치
    @Override
    public void setGameObjectList() {

    }

    //  TODO : UI Panel 기입
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

        // 새 씬 선택 또는 생성
        switch (curSceneNum) {
            case 0:
                mainScene = new MainScene();
//                if (mainScene == null) mainScene = new MainScene();
                SoundManager.stopAll();
                SoundManager.stop(GameOverSceneSOUNDID);
                SoundManager.play(8,1f);
                MainSceneSOUNDID =  SoundManager.play(0,0.6f);
                curScene = mainScene;

                //  RESET
                GameManager.getInstance().resetScoreAndTimer();
                break;
            case 1:
                // 항상 새로 만들어야 다음 Scene 다르게 지정 가능 (옵션)
                loadingScene = new LoadingScene(Scene.InGame);
                SoundManager.stop(MainSceneSOUNDID);
                SoundManager.stopAll();
//                MainSceneSOUNDID = -1;

                curScene = loadingScene;
                break;
            case 2:
                //  Please Reallocate
                gameScene = new InGameScene();

//                if (gameScene == null) gameScene = new InGameScene();
                TileManager.nextTileIndex = 0;
                GameSceneSOUNDID = SoundManager.play(1,0.6f);
                curScene = gameScene;

                //  RESET
                GameManager.getInstance().resetScoreAndTimer();
                break;
            case 3:
                gameOverScene = new GameOverScene();
//                if (gameOverScene == null) gameOverScene = new GameOverScene();
                SoundManager.stop(GameSceneSOUNDID);
//                SoundManager.stopAll();
                GameOverSceneSOUNDID = SoundManager.play(2,0.6f);
                SoundManager.play(9,0.6f);
                curScene = gameOverScene;
                break;
            default:
                System.err.println("Scene Error : Scene is Not Exist [" + curSceneNum + "]");
                return;
        }

        frame.setContentPane(curScene);
        frame.revalidate();
        frame.repaint();
    }
}



