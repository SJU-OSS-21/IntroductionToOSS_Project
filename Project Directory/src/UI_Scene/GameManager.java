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
    public int timer;
    public int score;

    //  Game Frame
    private JFrame mainFrame;

    GameManager() {

    }

    //  Main Frame 할당
    public void setMainFrame(JFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    //  Getter
    public final JFrame getMainFrame(){
        return mainFrame;
    }

    //  점수 Sync
    public void updateScore(int score){
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    public int getTimer() {
        return timer;
    }

    //  재시작 시 제거
    public void resetScoreAndTimer(){
        score = 0;
        timer = 0;
    }

}
