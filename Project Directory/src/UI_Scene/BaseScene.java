package UI_Scene;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;

public abstract class BaseScene extends JLayeredPane {

    //  Objects
    public Vector<Object> gameObjectList;

    //  Scene UI Set
    public JPanel sceneUI;


    //  Constructor
    protected BaseScene(){
        //  호출 타이밍 문제로 Do Nothing -> SceneManager에서 초기화
    }

    //  Abstract Functions

    //  TODO : Scene과 관련한 설정 사항
    public abstract void setScene();

    //  TODO : Scene 내에 존재해야 하는 gameObject setting
    public abstract void setGameObjectList();

    //  TODO : Scene 내에 존재해야 하는 UI Set에 대한 설정
    public abstract void setUISet();

    //  TODO : to Init Scene
    public void Init(){
        setScene();
        setGameObjectList();
        setUISet();
    }
}
