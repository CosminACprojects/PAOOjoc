package JocPAOO.Coliziuni;

import JocPAOO.Graphics.Rect;
import JocPAOO.Graphics.Vector2D;
import JocPAOO.Maps.Map;
import JocPAOO.Players.Player;

import java.util.Objects;

public class Coliziune {

    public static boolean testColiziune(Rect A, Rect B)
    {
        if(
                A.xpos+A.width>=B.xpos+15&&
                B.xpos+ B.width>=A.xpos+15 &&
                A.ypos + A.heigth>=B.ypos+15&&
                B.ypos + B.heigth>=A.ypos+15
        ) {

            return true;

        }
        return false;
    }
    public static boolean ColiziuneHarta(int[][] map,Vector2D poz) {
        for (int i = 0; i < 20; i++)
            for (int j = 0; j < 35; j++) {
                if (map[i][j] == 1) {
                    if (testColiziune(new Rect(poz.xpos, poz.ypos, 96, 96), new Rect(j * 48, i * 48, 48, 48))) {
                        return true;
                    }
                }
            }

    return false;
}
    public static boolean ColiziuneHartaProiectile(int[][] map,Vector2D poz,int size) {
        for (int i = 0; i < 20; i++)
            for (int j = 0; j < 35; j++) {
                if (map[i][j] == 1) {
                    if (testColiziune(new Rect(poz.xpos, poz.ypos, size, size), new Rect(j * 48, i * 48, 48, 48))) {
                        return true;
                    }
                }
            }
        return false;
    }
    public static boolean ColiziuneProiectilaPlayer(Vector2D pozPlayer,Vector2D pozProiectila,int size)
    {
        if (testColiziune(new Rect(pozPlayer.xpos, pozPlayer.ypos, size, size), new Rect(pozProiectila.xpos, pozProiectila.ypos, 32, 32))) {
            return true;
        }
        return false;
    }


}
