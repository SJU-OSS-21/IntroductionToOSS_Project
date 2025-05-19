package UI_Scene;

import java.util.Vector;

public class SceneManager {
    //  region SingleTon
    private static SceneManager instance;

    //  수정을 방지하기 위한 final 키워드 사용
    public static final SceneManager getInstance() {
        if (instance == null) {
            instance = new SceneManager();

            return instance;
        }

        return instance;
    }
    //  endregion

    //  Scenes
    public Vector<BaseScene> sceneList;
    public int curSceneNum;
    public String curSceneName;

    //  Scene Names
    public enum Scene{
        Main,
        InGame,
        GameOver
    }

    //  Overloading Functions
    public void changeScene(int sid){
        curSceneNum = sid;
        curSceneName = sceneList.get(sid).name;
    }
    public void changeScene(Scene name){
        curSceneNum = name.ordinal();
        curSceneName = sceneList.get(curSceneNum).name;
    }

    private SceneManager() {
        sceneList = new Vector<>();
    }
}
