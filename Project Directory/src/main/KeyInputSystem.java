package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyInputSystem implements KeyListener {
    public boolean isUp, isDown, isLeft, isRight;

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_W) {//up
            isUp = true;
        }
        if (code == KeyEvent.VK_S) {//down
            isDown = true;
        }
        if (code == KeyEvent.VK_A) {//left
            isLeft = true;
        }
        if (code == KeyEvent.VK_D) {//right
            isRight = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_W) {//up
            isUp = false;
        }
        if (code == KeyEvent.VK_S) {//down
            isDown = false;
        }
        if (code == KeyEvent.VK_A) {//left
            isLeft = false;
        }
        if (code == KeyEvent.VK_D) {//right
            isRight = false;
        }
    }
}
