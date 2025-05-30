package UI_Scene;

import Player_Item.Panel.PlayerPanel;
import main.GamePanel;
import main.GamePanel2;

import java.awt.*;

//  아래에 있는 MainScene, InGameScene, BaseScene에 Object(Panel)을 넣어주세요

class MainScene extends BaseScene {

    public MainScene() {
        super();
    }

    //  TODO : Scene JLayeredPane에 대한 설정 기입
    @Override
    public void setScene() {
        setPreferredSize(new Dimension(900,1600));
        setLayout(null);

    }

    // TODO :  Panel 여기에 설치
    @Override
    public void setGameObjectList() {
        PlayerPanel playerPanel = new PlayerPanel(600, 1000);
        playerPanel.setBounds(new Rectangle(0,0,600,1000));
        this.add(playerPanel, Integer.valueOf(0));

        setPreferredSize(new Dimension(600,1000)); // 실제 창 크기
        setLayout(null);
    }


    @Override
    public void setUISet() {

    }
}

class InGameScene extends BaseScene {

    public InGameScene() {
        setPreferredSize(new Dimension(900,1600));
        setLayout(null);
    }

    @Override
    public void setScene() {

    }

    @Override
    public void setGameObjectList() {
        GamePanel game1 = new GamePanel();
        GamePanel2 game2 = new GamePanel2();

        game1.setBounds(new Rectangle(0,0,900,1600));
        game2.setBounds(new Rectangle(0,0,900,1600));
        this.add(game1,Integer.valueOf(0));
        this.add(game2,Integer.valueOf(1));
    }

    //  TODO : UI Panel 기입
    @Override
    public void setUISet() {

    }
}

class GameOverScene extends BaseScene {

    public GameOverScene() {
        super();
    }

    //  TODO : Scene JLayeredPane에 대한 설정 기입
    @Override
    public void setScene() {
        setPreferredSize(new Dimension(900,1600));
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
    //  region SingleTon
//    private static SceneManager instance;
//
//    //  수정을 방지하기 위한 final 키워드 사용
//    public static final SceneManager getInstance() {
//        if (instance == null) {
//            instance = new SceneManager();
//
//            return instance;
//        }
//
//        return instance;
//    }
    //  endregion

    //  Scenes
    public static BaseScene mainScene = null;
    public static BaseScene gameScene = null;
    public static BaseScene gameOverScene = null;

    public static int curSceneNum;
    public static String curSceneName = null;
    public static BaseScene curScene = null;

    //  Scene Names
    public enum Scene{
        Main,
        InGame,
        GameOver
    }

    //  Scene을 불러오는 함수
    //  Overloading Functions
    //  idx나 enum을 통해 접근
    public static void changeScene(int sid){
        curSceneNum = sid;
        curSceneName = Scene.values()[sid].toString();
        innerChangeScene();
    }
    public static void changeScene(Scene name){
        curSceneNum = name.ordinal();
        curSceneName = name.toString();
        innerChangeScene();
    }

    //  To prevent Duplicated snippet
    private static void innerChangeScene(){
        BaseScene newGameScene = null;

        switch (curSceneNum){
            case 0 :
                newGameScene = new MainScene();
                break;
            case 1 :
                newGameScene = new InGameScene();
                break;
            case 2 :
                newGameScene = new GameOverScene();
                break;
        }

        if(newGameScene == null){
            System.out.println("Scene Error : Scene is Not Exist [" + curSceneNum + "]");
            return;
        }

        curScene = newGameScene;

        //  Add Scene
        GameManager.getInstance().getMainFrame().setContentPane(newGameScene);

        //  Remove Game Scene
        if(gameScene != null){
            GameManager.getInstance().getMainFrame().remove(gameScene);
        }

        //  TODO : Game Over Scene
    }

}

