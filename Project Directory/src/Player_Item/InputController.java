package Player_Item;// src/com/example/strikers1945/controller/InputController.java


import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class InputController extends KeyAdapter {
    private boolean up, down, left, right;
    private boolean fire;

    @Override
    public void keyPressed(KeyEvent e) {
        switch(e.getKeyCode()) {
            case KeyEvent.VK_LEFT  -> left  = true;
            case KeyEvent.VK_RIGHT -> right = true;
            case KeyEvent.VK_UP    -> up    = true;
            case KeyEvent.VK_DOWN  -> down  = true;
            case KeyEvent.VK_SPACE  -> fire  = true;
        }
    }
    @Override
    public void keyReleased(KeyEvent e) {
        switch(e.getKeyCode()) {
            case KeyEvent.VK_LEFT  -> left  = false;
            case KeyEvent.VK_RIGHT -> right = false;
            case KeyEvent.VK_UP    -> up    = false;
            case KeyEvent.VK_DOWN  -> down  = false;
            case KeyEvent.VK_SPACE  -> fire  = false;
        }
    }
    public boolean isUp()    { return up; }
    public boolean isDown()  { return down; }
    public boolean isLeft()  { return left; }
    public boolean isRight() { return right; }
    public boolean isFire() { return fire; }
}