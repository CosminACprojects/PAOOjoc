package JocPAOO.Players;

import JocPAOO.Graphics.Vector2D;
import JocPAOO.Maps.Map;

import java.awt.image.BufferedImage;

public abstract class Creator {
    public abstract BadGuy CreateEnemy(BufferedImage img, Vector2D poz, boolean pozitie, Map mapa);
}
