package JocPAOO.Players;
import JocPAOO.Coliziuni.Coliziune;
import JocPAOO.Game;
import JocPAOO.Graphics.Sound;
import JocPAOO.Graphics.Vector2D;
import JocPAOO.Maps.Map;
import JocPAOO.Projectiles.Proiectila;

import java.util.ArrayList;
import java.util.List;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.Random;

public class Enemy implements BadGuy{
    protected BufferedImage image;
    public static List<Enemy> enemies;//=new ArrayList<Enemy>();
    public List<Proiectila> bombeInamic=new ArrayList<Proiectila>();
    public Vector2D pozitie;
    private int yposInit;
    private int xposInit;
    public int alive=0;
    private boolean miscare=true;
    private boolean orizontal;
    public boolean atins=false;
    public Sound sound=new Sound();
    public Map map;
    public Enemy(BufferedImage img, Vector2D poz,boolean pozitie,Map mapa){
        this.image=img;
        this.pozitie=poz;
        yposInit=poz.ypos;
        orizontal=pozitie;
        xposInit=poz.xpos;
        map=mapa;
    }
    @Override
    public void Draw(Graphics g,int size){
        g.drawImage(this.image, pozitie.xpos, pozitie.ypos, size,size,(ImageObserver) null);

        g.setColor(new Color(35, 35, 35));
        g.fillRect(pozitie.xpos - 2, pozitie.ypos - 17, 100, 14);
        g.setColor(new Color(255, 0, 30));
        g.fillRect(pozitie.xpos,pozitie.ypos-15,(4-alive)*24,10);


    }
    @Override
    public void Update() {
        if(new Random().nextInt(200)+1>197 &&alive<4)
        {
            bombeInamic.add(new Proiectila(pozitie.xpos+45,pozitie.ypos+64,Player.pozitie));
            if(Game.SE.equals("on"))
            {
                playSE(1);
            }
        }
        //Coliziune bombe player inamic
        for(int i=0;i<Player.bombe.size();i++){
            if (Coliziune.ColiziuneProiectilaPlayer(pozitie, new Vector2D((int) Player.bombe.get(i).xpos, (int) Player.bombe.get(i).ypos),96)) {
                Player.bombe.remove(Player.bombe.get(i));
                atins = true;
            }
        }
        if (atins) {
            alive++;
            atins = false;
        }
        if(!orizontal) {
            //miscare inamic pe vertical
            if (pozitie.ypos == yposInit + 250 && miscare)
                miscare = false;
            if (pozitie.ypos < yposInit + 250 && miscare)
                pozitie.ypos = pozitie.ypos + 2;
            if (pozitie.ypos > yposInit && !miscare)
                pozitie.ypos = pozitie.ypos - 2;
            if (pozitie.ypos == yposInit && !miscare)
                miscare = true;
        }
        else
        {
            //miscare inamic pe orizontal
            if(pozitie.xpos==xposInit-250&&miscare)
                miscare=false;
            if(pozitie.xpos>xposInit-250&&miscare)
                pozitie.xpos=pozitie.xpos-2;
            if(pozitie.xpos<xposInit&&!miscare)
                pozitie.xpos=pozitie.xpos+2;
            if(pozitie.xpos==xposInit&&!miscare)
                miscare=true;
        }
        //coliziune bombe inamic cu mapa
        for (int i = 0; i < bombeInamic.size(); i++) {
            if (bombeInamic.get(i).OnMap) {
                bombeInamic.get(i).Update();
                if (Coliziune.ColiziuneHartaProiectile(map.MatriceColiziuni, new Vector2D((int) bombeInamic.get(i).xpos, (int) bombeInamic.get(i).ypos),32)) {
                    bombeInamic.get(i).OnMap = false;
                }
                if(!bombeInamic.get(i).OnMap)
                    bombeInamic.remove(bombeInamic.get(i));
            }
        }
    }
    private void playSE(int i)
    {
        sound.setFile(i);
        sound.play();
    }
}
