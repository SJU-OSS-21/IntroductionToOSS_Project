package UI_Scene;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;

public abstract class BaseScene extends JLayeredPane {
//    //  Properties
//    private int width;
//    private int height;
//
//    //  Identifier - NOTE : Be careful to use public var.
//    public int sid;
//    public String name;

    //  Objects
    public Vector<Object> gameObjectList;

    //  TODO : Map Elements

    //  Scene UI Set
    public JPanel sceneUI;


//    //  Non-Abstract Functions
//    public int getAndSetWidth() {
//        return width;
//    }
//
//    public int getAndSetHeight() {
//        return height;
//    }

    //  Constructor
    protected BaseScene(){
        //  호출 타이밍 문제로 Do Nothing -> SceneManager에서 초기화

//        setScene();
//        setGameObjectList();
        //  Init 타이밍 문제로 명시적 선언 요함
//        setUISet();
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

//SceneManager.curScene.startGameThread();
}
