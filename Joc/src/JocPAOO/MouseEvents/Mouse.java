package JocPAOO.MouseEvents;

import JocPAOO.Graphics.Vector2Dmouse;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
public class Mouse implements MouseListener {
    public Vector2Dmouse vec;
    public static boolean rightclick;
    @Override
    public void mouseClicked(MouseEvent d) {

    }

    @Override
    public void mousePressed(MouseEvent d) {
    }

    @Override
    public void mouseReleased(MouseEvent d) {
        int k=d.getButton();
        if(k==1) {
            rightclick = true;
            int x = d.getX();
            int y = d.getY();
            vec=new Vector2Dmouse(x,y);
        }

    }

    @Override
    public void mouseEntered(MouseEvent d) {
    }

    @Override
    public void mouseExited(MouseEvent d) {
    }
}
