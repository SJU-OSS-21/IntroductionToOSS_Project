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

    //  Abstract Functions

    //  TODO : 사실상 생성자를 강제로 생성하게 끔 설정 - called in Constructor
    public abstract void setScene();

    //  TODO : Scene 내에 존재해야 하는 gameObject setting
    public abstract void setGameObjectList();

    //  TODO : Scene 내에 존재해야 하는 UI Set에 대한 설정
    public abstract void setUISet();

//SceneManager.curScene.startGameThread();
}
