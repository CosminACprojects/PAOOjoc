package JocPAOO.KeyboardController;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
public class Keyboard implements KeyListener {
    public static boolean wpressed;
    public static boolean dpressed;
    public static boolean apressed;
    public static boolean spressed;

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_A) {
            apressed = true;
        }

        if (key == KeyEvent.VK_D) {
            dpressed = true;
        }
            if (key == KeyEvent.VK_W) {
                wpressed = true;
            }

            if (key == KeyEvent.VK_S) {
                spressed = true;
            }
            //hero.isMoving=true;
        }
    @Override
    public void keyReleased (KeyEvent e){
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_A) {
            apressed=false;
        }

        if (key == KeyEvent.VK_S) {
            spressed=false;
        }

        if (key == KeyEvent.VK_W) {
            wpressed=false;
        }

        if (key == KeyEvent.VK_D) {
           dpressed=false;
        }

    }
}


