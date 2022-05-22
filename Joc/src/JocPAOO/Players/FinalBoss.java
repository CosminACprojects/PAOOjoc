package JocPAOO.Players;

import JocPAOO.Coliziuni.Coliziune;
import JocPAOO.Game;
import JocPAOO.Graphics.Assets;
import JocPAOO.Graphics.Sound;
import JocPAOO.Graphics.Vector2D;
import JocPAOO.Maps.Map;
import JocPAOO.Projectiles.Proiectila;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FinalBoss implements BadGuy{
    public BufferedImage image;
    public static FinalBoss finalBoss;
    public List<Proiectila> flacariBoss=new ArrayList<Proiectila>();
    public Vector2D pozitie;
    private int yposInit;
    public int alive=0;
    private boolean miscare=true;
    public boolean atins=false;
    public Map map;
    public Sound sound=new Sound();
    public boolean invincibility=false;
    public FinalBoss(BufferedImage img, Vector2D poz,Map mapa)
    {
        this.image=img;
        this.pozitie=poz;
        yposInit=poz.ypos;
        map=mapa;
    }

    @Override
    public void Update() {
        if(new Random().nextInt(200)+1>197&&alive<80)
        {
            if(Game.SE.equals("on"))
            {
                playSE(0);
            }
            flacariBoss.add(new Proiectila(pozitie.xpos+15,pozitie.ypos+150,Player.pozitie, Assets.fireball));
        }
        for(int i=0;i<Player.bombe.size();i++){
            if (Coliziune.ColiziuneProiectilaPlayer(pozitie, new Vector2D((int) Player.bombe.get(i).xpos, (int) Player.bombe.get(i).ypos),350)) {
                Player.bombe.remove(Player.bombe.get(i));
                atins = true;
            }
        }
        if(atins&&!invincibility)
        {
            alive++;
            atins=false;
        }
        if(pozitie.ypos==yposInit+250&&miscare)
        {
            miscare=false;
        }
        if(pozitie.ypos<yposInit+250&&miscare)
        {
            pozitie.ypos=pozitie.ypos+2;
        }
        if(pozitie.ypos>yposInit-250&&!miscare)
        {
            pozitie.ypos=pozitie.ypos-2;
        }
        if(pozitie.ypos==yposInit-250&&!miscare)
        {
            miscare=true;
        }
        for(int i=0;i< flacariBoss.size();i++){
            if(flacariBoss.get(i).OnMap)
            {
                flacariBoss.get(i).Update();
                if (Coliziune.ColiziuneHartaProiectile(Map.Map3.MatriceColiziuni,new Vector2D((int) flacariBoss.get(i).xpos, (int) flacariBoss.get(i).ypos),60))
                {
                    flacariBoss.get(i).OnMap=false;
                }
                if(!flacariBoss.get(i).OnMap)
                    flacariBoss.remove(flacariBoss.get(i));
            }
        }
    }

    @Override
    public void Draw(Graphics g,int size) {
        g.drawImage(this.image,pozitie.xpos,pozitie.ypos,size,size,(ImageObserver) null);

        g.setColor(new Color(35, 35, 35));
        g.fillRect(38, 2, 1608, 14);
        g.setColor(new Color(255, 0, 30));
        g.fillRect(40,4,(80-alive)*20,10);
    }
    private void playSE(int i)
    {
        sound.setFile(i);
        sound.play();
    }
}
