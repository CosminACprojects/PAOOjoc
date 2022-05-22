package JocPAOO.Graphics;



import java.awt.*;
import java.awt.image.BufferedImage;

public class Assets {

    public static BufferedImage map1;
    public static BufferedImage map2;
    public static BufferedImage map3;

    public static BufferedImage player1;
    public static BufferedImage player1left;

    public static BufferedImage beast;
    public static BufferedImage beastright;

    public static BufferedImage bomba;
    public static BufferedImage fireball;

    public static BufferedImage navy;

    public static BufferedImage loadingscreen;
    public static BufferedImage inbetweenscreen;
    public static BufferedImage inbetweenscreenHardcore;
    public static BufferedImage lostscreen;
    public static BufferedImage lostscreenHardcore;
    public static BufferedImage gameCompleted;


    public static BufferedImage blackbeard;
    public static BufferedImage blackbeardright;

    public static BufferedImage finalBoss1;
    public static BufferedImage finalBoss2;
    public static BufferedImage finalBoss3;
    public static BufferedImage finalBoss4;


    public Assets() {
    }

    public static void Init() {
        SpriteSheet sheetPlayer1= new SpriteSheet(ImageLoader.LoadImage("/JocPAOO/textures/player1.png"),96);
        SpriteSheet sheetBeast= new SpriteSheet(ImageLoader.LoadImage("/JocPAOO/textures/beastship.png"),96);
        SpriteSheet sheetBlack=new SpriteSheet(ImageLoader.LoadImage("/JocPAOO/textures/blackbeard.png"),96);
        SpriteSheet sheetFinal= new SpriteSheet(ImageLoader.LoadImage("/JocPAOO/textures/finalboss.png"),350);

        player1=sheetPlayer1.crop(0,0);
        player1left=sheetPlayer1.crop(1,0);
        beast=sheetBeast.crop(1,0);
        beastright=sheetBeast.crop(0,0);
        blackbeard=sheetBlack.crop(0,0);
        blackbeardright=sheetBlack.crop(1,0);
        navy=ImageLoader.LoadImage("/JocPAOO/textures/navy.png");

        map1=ImageLoader.LoadImage("/JocPAOO/textures/map1.jpg");
        map2=ImageLoader.LoadImage("/JocPAOO/textures/map2.png");
        map3=ImageLoader.LoadImage("/JocPAOO/textures/map3.png");

        bomba=ImageLoader.LoadImage("/JocPAOO/textures/projectile.png");
        fireball=ImageLoader.LoadImage("/JocPAOO/textures/fireball.png");

        loadingscreen=ImageLoader.LoadImage("/JocPAOO/textures/LoadingScreen.png");
        inbetweenscreen= ImageLoader.LoadImage("/JocPAOO/textures/InbetweenScreen.jpg");
        inbetweenscreenHardcore=ImageLoader.LoadImage("/JocPAOO/textures/InbetweenScreenHardcore.png");
        lostscreen=ImageLoader.LoadImage("/JocPAOO/textures/lostlevel.png");
        lostscreenHardcore=ImageLoader.LoadImage("/JocPAOO/textures/lostlevelHardcore.png");
        gameCompleted=ImageLoader.LoadImage("/JocPAOO/textures/gameCompleted.png");

        finalBoss1=sheetFinal.crop(0,0);
        finalBoss2=sheetFinal.crop(2,0);
        finalBoss3=sheetFinal.crop(3,0);
        finalBoss4=sheetFinal.crop(1,0);


    }
}

