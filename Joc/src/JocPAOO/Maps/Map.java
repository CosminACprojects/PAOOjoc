package JocPAOO.Maps;
import JocPAOO.Graphics.Assets;
import JocPAOO.Graphics.Vector2D;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Map {
    public static Map Map1=new Map(Assets.map1, "Resources/map1.txt");
    public static Map Map2=new Map(Assets.map2, "Resources/map2.txt");
    public static Map Map3=new Map(Assets.map3, "Resources/map3.txt");
    public static Map LoadingScreen= new Map(Assets.loadingscreen);
    public static Map InbetweenScreen=new Map(Assets.inbetweenscreen);
    public static Map InbetweenScreenHardcore=new Map(Assets.inbetweenscreenHardcore);
    public static Map LostScreen=new Map(Assets.lostscreen);
    public static Map LostScreenHardcore=new Map(Assets.lostscreenHardcore);
    public static Map gameCompleted=new Map(Assets.gameCompleted);
    protected BufferedImage img;
    public int[][] MatriceColiziuni;

    public Map(BufferedImage image)
    {
        this.img=image;

    }
    public Map(BufferedImage image,String path) {
        this.img = image;
        MatriceColiziuni=new int[20][35];
        try {
            File map = new File(path);
            Scanner myReader = new Scanner(map);
            int j=0;
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] temp=data.split(",",data.length());
                for (int i = 0; i < 35; i++) {
                    MatriceColiziuni[j][i] = Integer.parseInt(temp[i]);
                }
                j++;

            }
            myReader.close();
        } catch (FileNotFoundException e){
            System.out.println("An error occured.");
            e.printStackTrace();
        }
    }
    public void Draw(Graphics g)
    {
        g.drawImage(this.img,0,0,1680,960,null);
    }

}
