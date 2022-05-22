package JocPAOO.Players;

import JocPAOO.Graphics.Vector2D;
import JocPAOO.Maps.Map;

import java.awt.image.BufferedImage;

public class CreatorBoss extends Creator{
    @Override
    public BadGuy CreateEnemy(BufferedImage img, Vector2D poz, boolean pozitie, Map mapa) {
       return new FinalBoss(img,poz,mapa);
    }
}
