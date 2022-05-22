package JocPAOO.Graphics;


import java.awt.image.BufferedImage;

public class SpriteSheet {
    private BufferedImage spriteSheet;
    private  int tileSize ;

    public SpriteSheet(BufferedImage buffImg,int size) {
        this.spriteSheet = buffImg;
        tileSize=size;
    }

    public BufferedImage crop(int x, int y) {
        return this.spriteSheet.getSubimage(x *tileSize , y * tileSize, tileSize, tileSize);
    }
}
