package UI_Scene;

import Enemies.EnemyPanel;
import Player_Item.Panel.PlayerPanel;
import main.GamePanel;
import main.GamePanel2;
import main.MapPanel;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

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
            SceneManager.changeScene(SceneManager.Scene.Loading);
        });

        //   Exit 버튼 클릭 시 프로그램 종료
        mainUIPanel.getExitButton().addActionListener(e -> {
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

    public InGameScene() {
        super();
        setUISet();
    }

    @Override
    public void setScene() {
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

        EnemyPanel enemyPanel = new EnemyPanel(screenWidth, screenHeight);
        enemyPanel.setBounds(new Rectangle(0, 0, screenWidth, screenHeight));
        this.add(enemyPanel, Integer.valueOf(2));

        enemyPanel.setPlayerPanel(playerPanel);
        playerPanel.setEnemyPanel(enemyPanel);
    }

    //  TODO : UI Panel 기입
    @Override
    public void setUISet() {

    }
}

class LoadingScene extends BaseScene {

    private final int screenWidth = 16 * 3 * 10;
    private final int screenHeight = 16 * 3 * 20;

    private final SceneManager.Scene nextScene;
    private JLabel loadingLabel = new JLabel("", SwingConstants.CENTER);
    private JLabel tipLabel = new JLabel("", SwingConstants.LEFT);

    private final String[] tips = {
            "💡 팁: Shift 키를 누르면 빠르게 달릴 수 있습니다!",
            "💡 팁: 적을 만났을 때는 조심하세요!",
            "💡 팁: 모든 오브젝트는 상호작용할 수 있습니다.",
            "💡 팁: 미니맵을 확인하세요!",
            "💡 팁: 시간을 절약하려면 경로를 외우세요!"
    };

    public LoadingScene(SceneManager.Scene nextScene) {
        this.nextScene = nextScene;


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
        loadingLabel.setFont(new Font("SansSerif", Font.BOLD, 36));
        loadingLabel.setForeground(Color.WHITE);
        loadingLabel.setBounds(0, screenHeight / 2 - 40, screenWidth, 80);
        add(loadingLabel);

        // === Tip 라벨 설정 ===
        tipLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        tipLabel.setForeground(Color.LIGHT_GRAY);
        tipLabel.setBounds(30, screenHeight - 50, screenWidth - 60, 30);
        tipLabel.setText(randomTip());
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

    public GameOverScene() {
        super();

        setUISet();
    }

    //  TODO : Scene JLayeredPane에 대한 설정 기입
    @Override
    public void setScene() {
        setPreferredSize(new Dimension(screenWidth, screenHeight));
        setLayout(null);
    }

    //  TODO : Panel 여기에 설치
    @Override
    public void setGameObjectList() {

    }

    //  TODO : UI Panel 기입
    @Override
    public void setUISet() {

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
                if (mainScene == null) mainScene = new MainScene();
                curScene = mainScene;
                break;
            case 1:
                // 항상 새로 만들어야 다음Scene 다르게 지정 가능 (옵션)
                loadingScene = new LoadingScene(Scene.InGame);
                curScene = loadingScene;
                break;
            case 2:
                if (gameScene == null) gameScene = new InGameScene();
                curScene = gameScene;
                break;
            case 3:
                if (gameOverScene == null) gameOverScene = new GameOverScene();
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



