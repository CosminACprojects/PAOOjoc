package JocPAOO.Players;

import JocPAOO.Graphics.Vector2D;
import JocPAOO.Maps.Map;

import java.awt.image.BufferedImage;

public class CreatorEnemy extends Creator {

    @Override
    public BadGuy CreateEnemy(BufferedImage img, Vector2D poz, boolean pozitie, Map mapa) {
        return new Enemy(img, poz, pozitie, mapa);
    }
}
