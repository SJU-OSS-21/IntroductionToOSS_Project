package main;

import UI_Scene.GameManager;
import UI_Scene.SceneManager;

import javax.swing.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        JFrame mainFrame = new JFrame("My Window");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setResizable(false);
        mainFrame.setTitle("Shooting Game");

        //  region Register in GM

        GameManager.getInstance().setMainFrame(mainFrame);

        GameScene gamePanel = new GameScene("Main", 0);
        SceneManager.getInstance().sceneList.add(gamePanel);
        mainFrame.add(gamePanel);

        gamePanel = new GameScene("InGame", 1);
        SceneManager.getInstance().sceneList.add(gamePanel);
        mainFrame.add(gamePanel);

        gamePanel = new GameScene("GameOver", 2);
        SceneManager.getInstance().sceneList.add(gamePanel);
        mainFrame.add(gamePanel);

        //  endregion

        SceneManager.getInstance().changeScene(0);

        mainFrame.pack();//set size of window by GamePanel
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);

        gamePanel.startGameThread();

    }
}
