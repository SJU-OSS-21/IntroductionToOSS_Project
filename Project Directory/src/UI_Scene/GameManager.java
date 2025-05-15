package UI_Scene;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class GameManager {

    //  region SingleTon
    private static GameManager instance;

    //  수정을 방지하기 위한 final 키워드 사용
    public static final GameManager getInstance(){
        if(instance == null){
            instance = new GameManager();

            return instance;
        }

        return instance;
    }
    //  endregion

    //  Scenes
    private Vector<BaseScene> sceneList;
    public int curSceneNum;
    public String curSceneName;

    //  Handlers
    public boolean isPlaying;

    //  TODO : Player instance

    //  Counters
    public double timer;
    public int score;

    GameManager(){
        sceneList = new Vector<>();

    }


}
