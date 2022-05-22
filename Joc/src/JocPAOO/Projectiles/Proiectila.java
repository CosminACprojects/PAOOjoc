package JocPAOO.Projectiles;

import JocPAOO.Graphics.Assets;
import JocPAOO.Graphics.Vector2D;
import JocPAOO.Players.Player;
import JocPAOO.Graphics.Vector2Dmouse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.*;

public class Proiectila {
    public static List<Proiectila> bombe=new ArrayList<Proiectila>();
    public double xpos,ypos;
    protected BufferedImage image;
    private double cos;
    private double sin;
    public boolean OnMap;
    //public static int index=0;
    public Proiectila(double x,double y)
    {
        image= Assets.bomba;
        xpos=x;
        ypos=y;
        double ipo=sqrt((xpos+19-Vector2Dmouse.xpos)*(xpos+19-Vector2Dmouse.xpos)+(ypos-Vector2Dmouse.ypos)*(ypos-Vector2Dmouse.ypos));
        cos=(Vector2Dmouse.xpos-xpos-19)/ipo;
        sin=(Vector2Dmouse.ypos-ypos)/ipo;
        OnMap=true;
    }
    public Proiectila(double x, double y, Vector2D pozPlayer)
    {
        image=Assets.bomba;
        xpos=x;
        ypos=y;
        double ipo=sqrt((xpos+19-pozPlayer.xpos)*(xpos-pozPlayer.xpos)+(ypos-pozPlayer.ypos)*(ypos-pozPlayer.ypos));
        cos=(pozPlayer.xpos-xpos-19)/ipo;
        sin=(pozPlayer.ypos-ypos)/ipo;
        OnMap=true;
    }
    public Proiectila(double x, double y, Vector2D pozPlayer,BufferedImage bmb)
    {
        image=bmb;
        xpos=x;
        ypos=y;
        double ipo=sqrt((xpos+19-pozPlayer.xpos)*(xpos-pozPlayer.xpos)+(ypos-pozPlayer.ypos)*(ypos-pozPlayer.ypos));
        cos=(pozPlayer.xpos-xpos-19)/ipo;
        sin=(pozPlayer.ypos-ypos)/ipo;
        OnMap=true;
    }
    public void Update() {
        if(OnMap) {
            xpos += 7*cos;
            ypos += 7*sin;
            if(xpos>1680||xpos<0||ypos>960||ypos<0)
            {
                OnMap=false;
            }
        }
    }
    public void Draw(Graphics g,int size) {
        if (OnMap) {
            g.drawImage(this.image, (int) xpos, (int) ypos, size, size, (ImageObserver) null);
        }
    }
}
