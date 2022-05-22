package JocPAOO.Players;
import JocPAOO.Coliziuni.Coliziune;
import JocPAOO.Game;
import JocPAOO.GameWindow.GameWindow;
import JocPAOO.Graphics.Assets;
import JocPAOO.Graphics.Sound;
import JocPAOO.Graphics.Vector2D;

import java.awt.*;
import java.awt.image.ImageObserver;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import JocPAOO.KeyboardController.Keyboard;
import JocPAOO.Maps.Map;
import JocPAOO.MouseEvents.Mouse;
import JocPAOO.Projectiles.Proiectila;

public class Player {
    private static Player instance=null;
    public static List<Proiectila> bombe;
    protected BufferedImage image;
    public static Vector2D pozitie;
    public boolean img=true;
    public int nrbombe=0;
    public int vieti;
    public Map map;
    public int nrMax;
    public Sound sound=new Sound();
    public Player(BufferedImage img,Vector2D poz,Map mapa,int hp,int nr)
    {
        this.image=img;
        pozitie=poz;
        map=mapa;
        vieti=hp;
        nrMax=nr;
        bombe=new ArrayList<Proiectila>();
    }
    public static Player GetInstance(BufferedImage img,Vector2D poz,Map mapa,int hp,int nr)
    {
        if(instance==null)
        {
            instance=new Player(img,poz,mapa,hp,nr);
        }
        return instance;
    }
    public void Reset(){
        instance=null;
    }
    public void Draw(Graphics g){
        g.drawImage(this.image, pozitie.xpos, pozitie.ypos, 96,96,(ImageObserver) null);
        g.setColor(new Color(35, 35, 35));
        g.fillRect(pozitie.xpos - 2, pozitie.ypos - 17, 100, 14);
        g.setColor(new Color(255, 0, 30));
        g.fillRect(pozitie.xpos,pozitie.ypos-15,(4-4+vieti)*24,10);
        Font font = new Font("Times New Roman",Font.BOLD,40);
        g.setColor(Color.RED);
        g.setFont(font);
        if(nrbombe<nrMax)
        {
            g.drawString(String.valueOf(nrMax-nrbombe),60,97);
        }
        else{
            if(bombe.isEmpty()) {
                vieti = 0;
            }
        }

    }
    public void Update()
    {
        //creare bombe
        if(Mouse.rightclick) {
            if (nrbombe < nrMax) {

                if (Game.SE.equals("on")) {
                    playSE(1);
                }
                Player.bombe.add(new Proiectila(pozitie.xpos + 45, pozitie.ypos + 64));
                nrbombe++;
                Mouse.rightclick = false;
            }
        }
        if(Keyboard.wpressed)
        {
            if(pozitie.ypos-4>=0)
                pozitie.ypos-=4;
        }
        if(Keyboard.spressed)
        {

            if(pozitie.ypos+100<=960)
                pozitie.ypos+=4;
        }
        if(Keyboard.dpressed)
        {
            if(!img)
            {
                this.image=Assets.player1;
                img=true;
            }
            if(pozitie.xpos+100<=1680)
                pozitie.xpos+=4;
        }
        if(Keyboard.apressed)
        {
            this.image=Assets.player1left;
            img=false;
            if(pozitie.xpos-4>=0)
                pozitie.xpos-=4;
        }
        //Coliziune bombe inamice---player
        if(Enemy.enemies!=null){
        for(int i=0;i<Enemy.enemies.size();i++) {
            for (int j = 0; j < Enemy.enemies.get(i).bombeInamic.size(); j++) {
                if (Coliziune.ColiziuneProiectilaPlayer(pozitie, new Vector2D((int) Enemy.enemies.get(i).bombeInamic.get(j).xpos, (int) Enemy.enemies.get(i).bombeInamic.get(j).ypos),96)) {
                    Enemy.enemies.get(i).bombeInamic.remove(Enemy.enemies.get(i).bombeInamic.get(j));
                    vieti--;
                }
            }
        }}
        //Coliziuni bombe mapa
        for (int i = 0; i < bombe.size(); i++) {
            if (bombe.get(i).OnMap) {
                bombe.get(i).Update();
                if (Coliziune.ColiziuneHartaProiectile(map.MatriceColiziuni, new Vector2D((int) bombe.get(i).xpos, (int) bombe.get(i).ypos),32)) {
                    bombe.get(i).OnMap = false;
                }
                if(!bombe.get(i).OnMap)
                    bombe.remove(bombe.get(i));
            }
        }
    }
    private void playSE(int i)
    {
        sound.setFile(i);
        sound.play();
    }
}
