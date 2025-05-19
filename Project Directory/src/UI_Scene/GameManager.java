package UI_Scene;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class GameManager {

    //  region SingleTon
    private static GameManager instance;

    //  수정을 방지하기 위한 final 키워드 사용
    public static final GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();

            return instance;
        }

        return instance;
    }
    //  endregion

    //  Handlers
    public boolean isPlaying;
    public boolean isInGame;


    //  Counters
    public double timer;
    public int score;

    //  Game Frame
    private JFrame mainFrame;

    GameManager() {

    }

    public void setMainFrame(JFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    public final JFrame getMainFrame(){
        return mainFrame;
    }


}
