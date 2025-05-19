package main;

import UI_Scene.GameManager;
import UI_Scene.SceneManager;

import javax.swing.*;

public class MyFrame extends JFrame {
    public MyFrame() {
        JFrame mainFrame = new JFrame("My Window");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setResizable(false);
        mainFrame.setTitle("Shooting Game");

        // region Register in GM
        GameManager.getInstance().setMainFrame(mainFrame);
        // endregion

        SceneManager.changeScene(SceneManager.Scene.Main);

        mainFrame.pack();   // set size of window by GamePanel
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);

        // Start

    }
}
