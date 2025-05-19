package UI_Scene;

import main.GameLayeredPane;

class MainScene extends GameLayeredPane {

    public MainScene() {

    }
}

class InGameScene extends GameLayeredPane {

    public InGameScene() {

    }
}

class GameOverScene extends GameLayeredPane {

    public GameOverScene() {

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
    public static GameLayeredPane mainScene = null;
    public static GameLayeredPane gameScene = null;
    public static GameLayeredPane gameOverScene = null;

    public static int curSceneNum;
    public static String curSceneName = null;
    public static GameLayeredPane curScene = null;

    //  Scene Names
    public enum Scene{
        Main,
        InGame,
        GameOver
    }

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
        GameLayeredPane newGameScene = null;

        switch (curSceneNum){
            case 0 :
                newGameScene = new MainScene();
                break;
            case 1 :
                newGameScene = new GameLayeredPane();
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
        GameManager.getInstance().getMainFrame().add(newGameScene);

        //  Remove Game Scene
        if(gameScene != null){
            GameManager.getInstance().getMainFrame().remove(gameScene);
        }

        //  TODO : Game Over Scene
    }

}

