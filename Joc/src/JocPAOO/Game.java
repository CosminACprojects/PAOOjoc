package JocPAOO;

import JocPAOO.Coliziuni.Coliziune;
import JocPAOO.GameWindow.GameWindow;
import JocPAOO.Graphics.Assets;
import JocPAOO.Graphics.Sound;
import JocPAOO.Graphics.Vector2Dmouse;
import JocPAOO.Players.*;
import JocPAOO.Maps.Map;
import JocPAOO.Graphics.Vector2D;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import JocPAOO.MouseEvents.Mouse;
import org.w3c.dom.css.Counter;

import java.sql.*;
import java.util.ArrayList;


public class Game implements Runnable {
    private static Game instance = null;
    public  GameWindow wnd;
    private boolean runState;
    private Thread gameThread;
    private BufferStrategy bs;
    private static Graphics g;
    private Player StrawHat;
    public static String screen="Loading";
    public static String lastMap;
    public static String enemyType="enemy";
    public Creator creator;
    private String music;
    public boolean Exit=false;
    public static boolean HardCore=false;
    public static int stage=1;
    public static String SE;
    public Sound sound=new Sound();


    public Game(String title, int width, int height) {
        this.wnd = new GameWindow(title, width, height);
        this.runState = false;
    }
    public static Game GetInstance(String name,int width,int height)
    {
        if(instance == null){
            instance=new Game(name,width,height);
        }
        return instance;
    }
    public void Reset()
    {
        instance=null;
    }
    private void InitGame() {
        this.wnd.BuildGameWindow();
        //GameWindow.Counter.setVisible(false);
        Assets.Init();
        Connection c=null;
        Statement st=null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:Database.db");
            c.setAutoCommit(false);
            st = c.createStatement();
            ResultSet rs = st.executeQuery("SELECT Music,SoundEffects FROM Joc;");
            music=rs.getString("Music");
            if(music.equals("on")){
                playMusic();
            }
            SE=rs.getString("SoundEffects");
            rs.close();
            st.close();
            c.close();
        }catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }


    }
    public void run() {
        this.InitGame();
        long oldTime = System.nanoTime();
        Boolean framesPerSecond = true;
        double var6 = 1.6666666E7D;
        while(this.runState) {
            long curentTime = System.nanoTime();
            if ((double)(curentTime - oldTime) > var6) {
                this.Update();
                this.Draw();
                oldTime = curentTime;
            }
        }

    }

    public synchronized void StartGame() {
        if (!this.runState) {
            this.runState = true;
            this.gameThread = new Thread(this);
            this.gameThread.start();
        }
    }

    public synchronized void StopGame() {
        if (this.runState) {
            this.runState = false;
            this.wnd.wndFrame.setVisible(false);
            System.exit(0);
            try {
                this.gameThread.join();

            } catch (InterruptedException var2) {
                var2.printStackTrace();
            }
        }
    }

    private void Update() {
        switch (screen){
            case "Map1":
                Map1Update();
                break;
            case "Map2":
                Map2Update();
                break;
            case "Map3":
                Map3Update();
                break;
        }
    }

    private void Draw()throws IllegalArgumentException {
        this.bs = this.wnd.GetCanvas().getBufferStrategy();
        if (this.bs == null) {
            try {
                this.wnd.GetCanvas().createBufferStrategy(3);
                return;
            } catch (Exception var2) {
                var2.printStackTrace();
            }
        }

        this.g = this.bs.getDrawGraphics();
        this.g.clearRect(0, 0, this.wnd.GetWndWidth(), this.wnd.GetWndHeight());

        switch (screen)
        {
            //desen loading
            case "Loading":
                Map.LoadingScreen.Draw(this.g);
                LoadingScreen();
                break;
            //Desen map1
            case "Map1":
                Map1Draw();
                break;
            //Desen map2
            case "Map2":
                Map2Draw();

                break;
            //Desen map3
            case "Map3":
                Map3Draw();
                break;
            //desen inbetween levels
            case "inbetween":
                Map.InbetweenScreen.Draw(this.g);
                InbetweenScreen();
                break;
            case "inbetweenHardcore":
                Map.InbetweenScreenHardcore.Draw(this.g);
                InbetweenScreenHardcore();
                break;
            case "lost":
                Map.LostScreen.Draw(this.g);
                LostLevelScreen();
                break;
            case "lostHardcore":
                Map.LostScreenHardcore.Draw(this.g);
                LostLevelScreenHardcore();
                break;
            case "finish":
                Map.gameCompleted.Draw(this.g);
                GameCompletedScreen();
                break;
            default:
                throw new IllegalArgumentException("No screen with the name :"+screen);

        }
        this.bs.show();
        this.g.dispose();
    }


    //Loading screen
    private void LoadingScreen() throws IllegalArgumentException{
        if (Mouse.rightclick) {
            if (Vector2Dmouse.xpos >= 78 && Vector2Dmouse.ypos >= 75 && Vector2Dmouse.ypos <= 152 && Vector2Dmouse.xpos <= 440) {
                enemyType="enemy";
                InitMap1();
                screen = "Map1";
            }
            if (Vector2Dmouse.xpos >= 78 && Vector2Dmouse.ypos >= 224 && Vector2Dmouse.ypos <= 310 && Vector2Dmouse.xpos <= 464) {
                Connection c=null;
                Statement st=null;
                try{
                    Class.forName("org.sqlite.JDBC");
                    c = DriverManager.getConnection("jdbc:sqlite:Database.db");
                    c.setAutoCommit(false);
                    st=c.createStatement();
                    ResultSet rs = st.executeQuery("SELECT Map,EnemyType FROM Joc;");
                    lastMap= rs.getString("Map");
                    enemyType=rs.getString("EnemyType");
                    if(lastMap.equals("Map1")&&enemyType.equals("enemy"))
                    {
                        stage=1;
                        screen="Map2";
                        InitMap2();
                    }
                    else
                        if(lastMap.equals("Map2")&&enemyType.equals("final"))
                        {
                            stage=1;
                            screen="Map3";
                            InitMap3();
                        }
                        else {
                            if(lastMap.equals("Map0")&&enemyType.equals("enemy")) {
                                screen="Map1";
                                stage=1;
                                InitMap1();
                            }
                            else{
                            throw new IllegalArgumentException("Parametrii de initializare nivel din baza de data sunt gresiti!");
                            }
                        }
                    rs.close();
                    st.close();
                    c.close();
                }
                catch ( Exception e ) {
                    System.err.println( e.getClass().getName() + ": " + e.getMessage() );
                    System.exit(0);
                }

            }
            if (Vector2Dmouse.xpos >= 104 && Vector2Dmouse.ypos >= 791 && Vector2Dmouse.ypos <= 878 && Vector2Dmouse.xpos <= 581) {
                Connection c=null;
                Statement st=null;
                try{
                    Class.forName("org.sqlite.JDBC");
                    c = DriverManager.getConnection("jdbc:sqlite:Database.db");
                    c.setAutoCommit(false);
                    st = c.createStatement();
                    String sql=" ";
                    if(SE.equals("on")) {
                        sql= "UPDATE Joc SET SoundEffects = 'off' ;";
                        SE="off";
                    }else{
                        sql="UPDATE Joc SET SoundEffects = 'on' ;";
                        SE="on";
                    }
                    st.executeUpdate(sql);
                    c.commit();
                    st.close();
                    c.close();
                }
                catch ( Exception e ) {
                    System.err.println( e.getClass().getName() + ": " + e.getMessage() );
                    System.exit(0);
                }

            }
            if (Vector2Dmouse.xpos >= 95 && Vector2Dmouse.ypos >= 644 && Vector2Dmouse.ypos <= 725 && Vector2Dmouse.xpos <= 300)
            {
                Connection c=null;
                Statement st=null;
                try {
                    Class.forName("org.sqlite.JDBC");
                    c = DriverManager.getConnection("jdbc:sqlite:Database.db");
                    c.setAutoCommit(false);
                    st = c.createStatement();
                    String sql=" ";
                    if(music.equals("on")) {
                        sql= "UPDATE Joc SET Music = 'off' ;";
                        stopMusic();
                        music="off";
                    }else{
                        sql="UPDATE Joc SET Music = 'on' ;";
                        playMusic();
                        music="on";
                    }
                    st.executeUpdate(sql);
                    c.commit();
                    st.close();
                    c.close();
                }
                catch ( Exception e ) {
                    System.err.println( e.getClass().getName() + ": " + e.getMessage() );
                    System.exit(0);
                }
            }
            if (Vector2Dmouse.xpos >= 78 && Vector2Dmouse.ypos >= 380 && Vector2Dmouse.ypos <= 464 && Vector2Dmouse.xpos <= 404) {
                HardCore=true;
                enemyType="enemy";
                InitMap1();
                screen = "Map1";
            }
            if(Vector2Dmouse.xpos>=1500&&Vector2Dmouse.ypos>=69&&Vector2Dmouse.ypos<=160&&Vector2Dmouse.xpos<=1650) {
                Main.game.Reset();
                Main.game.StopGame();
            }
            Mouse.rightclick = false;
        }
    }
    //InbetweenLevels Screen
    private void InbetweenScreen(){
        if(Mouse.rightclick)
        {
            if(Vector2Dmouse.xpos>=80&&Vector2Dmouse.ypos>=250&&Vector2Dmouse.ypos<=335&&Vector2Dmouse.xpos<=390)
            {
                if(lastMap.equals("Map1")) {
                    enemyType="enemy";
                    InitMap2();
                    screen = "Map2";
                }
                if(lastMap.equals("Map2")) {
                    enemyType="final";
                    InitMap3();
                    screen = "Map3";
                }

            }
            if(Vector2Dmouse.xpos>=84&&Vector2Dmouse.ypos>=415&&Vector2Dmouse.ypos<=495&&Vector2Dmouse.xpos<=536)
            {
                Connection c=null;
                Statement st=null;
                try {
                    Class.forName("org.sqlite.JDBC");
                    c = DriverManager.getConnection("jdbc:sqlite:Database.db");
                    c.setAutoCommit(false);
                    st = c.createStatement();
                    String sql=" ";
                    if(lastMap=="Map1") {
                         sql= "UPDATE Joc SET Map = 'Map1' ;";
                    }
                    if(lastMap=="Map2")
                    {
                        sql= "UPDATE Joc SET Map = 'Map2' ;";
                    }
                    st.executeUpdate(sql);
                    if(enemyType=="enemy")
                    {
                    sql="UPDATE Joc SET EnemyType = 'enemy' ;";
                    }
                    if(enemyType=="final")
                    {
                        sql="UPDATE Joc SET EnemyType = 'final' ;";
                    }
                    st.executeUpdate(sql);
                    c.commit();
                    st.close();
                    c.close();
                }
                catch ( Exception e ) {
                    System.err.println( e.getClass().getName() + ": " + e.getMessage() );
                    System.exit(0);
                }
            }
            if (Vector2Dmouse.xpos >= 92 && Vector2Dmouse.ypos >= 558 && Vector2Dmouse.ypos <= 622 && Vector2Dmouse.xpos <= 280)
            {
                Connection c=null;
                Statement st=null;
                try {
                    Class.forName("org.sqlite.JDBC");
                    c = DriverManager.getConnection("jdbc:sqlite:Database.db");
                    c.setAutoCommit(false);
                    st = c.createStatement();
                    String sql=" ";
                    if(music.equals("on")) {
                        sql= "UPDATE Joc SET Music = 'off' ;";
                        stopMusic();
                        music="off";
                    }else{
                        sql="UPDATE Joc SET Music = 'on' ;";
                        playMusic();
                        music="on";
                    }
                    st.executeUpdate(sql);
                    c.commit();
                    st.close();
                    c.close();
                }
                catch ( Exception e ) {
                    System.err.println( e.getClass().getName() + ": " + e.getMessage() );
                    System.exit(0);
                }
            }
            if(Vector2Dmouse.xpos>=80&&Vector2Dmouse.ypos>=715&&Vector2Dmouse.ypos<=780&&Vector2Dmouse.xpos<=450) {
              screen="Loading";
            }
            if(Vector2Dmouse.xpos>=1530&&Vector2Dmouse.ypos>=85&&Vector2Dmouse.ypos<=155&&Vector2Dmouse.xpos<=1666) {
                Main.game.Reset();
                Main.game.StopGame();
            }
            Mouse.rightclick=false;
        }
    }
    //InbetweenLevels Hardcore Screen
    private void InbetweenScreenHardcore(){
        if(Mouse.rightclick)
        {
            if(Vector2Dmouse.xpos>=80&&Vector2Dmouse.ypos>=250&&Vector2Dmouse.ypos<=335&&Vector2Dmouse.xpos<=450)
            {
                if(lastMap.equals("Map1")) {
                    enemyType="enemy";
                    InitMap2();
                    screen = "Map2";
                }
                if(lastMap.equals("Map2")) {
                    enemyType="final";
                    InitMap3();
                    screen = "Map3";
                }
            }
            if (Vector2Dmouse.xpos >= 92 && Vector2Dmouse.ypos >= 558 && Vector2Dmouse.ypos <= 622 && Vector2Dmouse.xpos <= 280)
            {
                Connection c=null;
                Statement st=null;
                try {
                    Class.forName("org.sqlite.JDBC");
                    c = DriverManager.getConnection("jdbc:sqlite:Database.db");
                    c.setAutoCommit(false);
                    st = c.createStatement();
                    String sql=" ";
                    if(music.equals("on")) {
                        sql= "UPDATE Joc SET Music = 'off' ;";
                        stopMusic();
                        music="off";
                    }else{
                        sql="UPDATE Joc SET Music = 'on' ;";
                        playMusic();
                        music="on";
                    }
                    st.executeUpdate(sql);
                    c.commit();
                    st.close();
                    c.close();
                }
                catch ( Exception e ) {
                    System.err.println( e.getClass().getName() + ": " + e.getMessage() );
                    System.exit(0);
                }
            }
            if(Vector2Dmouse.xpos>=80&&Vector2Dmouse.ypos>=715&&Vector2Dmouse.ypos<=780&&Vector2Dmouse.xpos<=450) {
                screen="Loading";
                HardCore=false;
            }
            if(Vector2Dmouse.xpos>=1530&&Vector2Dmouse.ypos>=85&&Vector2Dmouse.ypos<=155&&Vector2Dmouse.xpos<=1666) {
                Main.game.Reset();
                Main.game.StopGame();
            }
            Mouse.rightclick=false;
        }
    }
    //LostLevel Screen
    private void LostLevelScreen() {
        if (Mouse.rightclick){
            if (Vector2Dmouse.xpos >= 1010 && Vector2Dmouse.ypos >= 115 && Vector2Dmouse.ypos <= 216 && Vector2Dmouse.xpos <= 1432) {
                if(lastMap.equals("Map0"))
                {
                    stage=1;
                    InitMap1();
                    screen="Map1";

                }
                if(lastMap.equals("Map1")) {
                    stage=1;
                    InitMap2();
                    screen = "Map2";

                }
                if(lastMap.equals("Map2")) {
                    stage=1;
                    InitMap3();
                    screen = "Map3";

                }
            }
            if (Vector2Dmouse.xpos >= 783 && Vector2Dmouse.ypos >= 862 && Vector2Dmouse.ypos <= 934 && Vector2Dmouse.xpos <= 986)
            {
                Connection c=null;
                Statement st=null;
                try {
                    Class.forName("org.sqlite.JDBC");
                    c = DriverManager.getConnection("jdbc:sqlite:Database.db");
                    c.setAutoCommit(false);
                    st = c.createStatement();
                    String sql=" ";
                    if(music.equals("on")) {
                        sql= "UPDATE Joc SET Music = 'off' ;";
                        stopMusic();
                        music="off";
                    }else{
                        sql="UPDATE Joc SET Music = 'on' ;";
                        playMusic();
                        music="on";
                    }
                    st.executeUpdate(sql);
                    c.commit();
                    st.close();
                    c.close();
                }
                catch ( Exception e ) {
                    System.err.println( e.getClass().getName() + ": " + e.getMessage() );
                    System.exit(0);
                }
            }
            if (Vector2Dmouse.xpos >= 930 && Vector2Dmouse.ypos >= 300 && Vector2Dmouse.ypos <= 413 && Vector2Dmouse.xpos <= 1396)
            {
                Connection c=null;
                Statement st=null;
                try {
                    Class.forName("org.sqlite.JDBC");
                    c = DriverManager.getConnection("jdbc:sqlite:Database.db");
                    c.setAutoCommit(false);
                    st = c.createStatement();
                    String sql=" ";
                    if(lastMap=="Map0")
                    {
                        sql="UPDATE Joc SET Map = 'Map0';";
                    }
                    if(lastMap=="Map1") {
                        sql= "UPDATE Joc SET Map = 'Map1' ;";
                    }
                    if(lastMap=="Map2")
                    {
                        sql= "UPDATE Joc SET Map = 'Map2' ;";
                    }
                    st.executeUpdate(sql);
                    if(enemyType=="enemy")
                    {
                        sql="UPDATE Joc SET EnemyType = 'enemy' ;";
                    }
                    if(enemyType=="final")
                    {
                        sql="UPDATE Joc SET EnemyType = 'final' ;";
                    }
                    st.executeUpdate(sql);
                    c.commit();
                    st.close();
                    c.close();
                }
                catch ( Exception e ) {
                    System.err.println( e.getClass().getName() + ": " + e.getMessage() );
                    System.exit(0);
                }
            }
            if (Vector2Dmouse.xpos >= 851 && Vector2Dmouse.ypos >= 479 && Vector2Dmouse.ypos <= 575 && Vector2Dmouse.xpos <= 1240)
            {
                enemyType="enemy";
                screen="Loading";
            }
            if (Vector2Dmouse.xpos >= 1090 && Vector2Dmouse.ypos >= 865 && Vector2Dmouse.ypos <= 930 && Vector2Dmouse.xpos <= 1235)
            {
                Main.game.Reset();
                Main.game.StopGame();
            }
            Mouse.rightclick=false;
        }
    }
    //LostLevelHardcore Screen
    private void LostLevelScreenHardcore(){
        if(Mouse.rightclick) {
            if (Vector2Dmouse.xpos >= 851 && Vector2Dmouse.ypos >= 479 && Vector2Dmouse.ypos <= 575 && Vector2Dmouse.xpos <= 1240) {
                screen = "Loading";
                enemyType="enemy";
                HardCore=false;
            }
            if (Vector2Dmouse.xpos >= 1090 && Vector2Dmouse.ypos >= 865 && Vector2Dmouse.ypos <= 930 && Vector2Dmouse.xpos <= 1235)
            {
                Main.game.Reset();
                Main.game.StopGame();
            }
            if (Vector2Dmouse.xpos >= 783 && Vector2Dmouse.ypos >= 862 && Vector2Dmouse.ypos <= 934 && Vector2Dmouse.xpos <= 986)
            {
                Connection c=null;
                Statement st=null;
                try {
                    Class.forName("org.sqlite.JDBC");
                    c = DriverManager.getConnection("jdbc:sqlite:Database.db");
                    c.setAutoCommit(false);
                    st = c.createStatement();
                    String sql=" ";
                    if(music.equals("on")) {
                        sql= "UPDATE Joc SET Music = 'off' ;";
                        stopMusic();
                        music="off";
                    }else{
                        sql="UPDATE Joc SET Music = 'on' ;";
                        playMusic();
                        music="on";
                    }
                    st.executeUpdate(sql);
                    c.commit();
                    st.close();
                    c.close();
                }
                catch ( Exception e ) {
                    System.err.println( e.getClass().getName() + ": " + e.getMessage() );
                    System.exit(0);
                }
            }
            Mouse.rightclick=false;
        }
    }
    //GameCompleted Screen
    private void GameCompletedScreen(){
        if(Mouse.rightclick) {
            if (Vector2Dmouse.xpos >= 650 && Vector2Dmouse.ypos >= 228 && Vector2Dmouse.ypos <= 300 && Vector2Dmouse.xpos <= 1032) {
                enemyType="enemy";
                screen="Loading";
                if(HardCore)
                    HardCore=false;
            }
            if (Vector2Dmouse.xpos >= 775 && Vector2Dmouse.ypos >= 756 && Vector2Dmouse.ypos <= 832 && Vector2Dmouse.xpos <= 913)
            {
                Main.game.Reset();
                Main.game.StopGame();
            }

            Mouse.rightclick=false;
        }
    }
    //map1 updates
    private void Map1Update() {
        Vector2D poz=new Vector2D(StrawHat.pozitie.xpos,StrawHat.pozitie.ypos);
        StrawHat.Update();
        if(StrawHat.vieti<=0)
        {
            if(Enemy.enemies!=null){
            for (int i = 0; i < Enemy.enemies.size(); i++) {
                for (int j = 0; j < Enemy.enemies.get(i).bombeInamic.size(); j++) {
                    Enemy.enemies.get(i).bombeInamic.remove(Enemy.enemies.get(i).bombeInamic.get(j));
                }
                Enemy.enemies.remove(Enemy.enemies.get(i));
            }
           for(int i=0;i<Enemy.enemies.size();i++)
           {
               Enemy.enemies.remove(Enemy.enemies.get(i));
           }}
            Enemy.enemies=null;
            StrawHat.Reset();
            if(HardCore){
                screen="lostHardcore";}
            else
            {
                screen="lost";
            }
            enemyType="enemy";

        }
        else {
            if (Coliziune.ColiziuneHarta(Map.Map1.MatriceColiziuni, StrawHat.pozitie)) {
                StrawHat.pozitie = poz;
            }
            if(Enemy.enemies!=null) {
                for (int j = 0; j < Enemy.enemies.size(); j++) {
                    Enemy.enemies.get(j).Update();
                    if (Enemy.enemies.get(j).alive >= 4 && Enemy.enemies.get(j).bombeInamic.isEmpty()) {
                        Enemy.enemies.remove(Enemy.enemies.get(j));
                    }
                }
                if (Enemy.enemies.isEmpty()) {
                    Enemy.enemies = null;
                }
            }
        }

    }
    //map2 updates
    private void Map2Update() {
        Vector2D poz=new Vector2D(StrawHat.pozitie.xpos,StrawHat.pozitie.ypos);
        StrawHat.Update();
        if(StrawHat.vieti<=0)
        {
            if(Enemy.enemies!=null){
            for (int i = 0; i < Enemy.enemies.size(); ++i) {
                for (int j = 0; j < Enemy.enemies.get(i).bombeInamic.size(); j++) {
                    Enemy.enemies.get(i).bombeInamic.remove(Enemy.enemies.get(i).bombeInamic.get(j));
                }
                Enemy.enemies.remove(Enemy.enemies.get(i));
            }
            for(int i=0;i<Enemy.enemies.size();i++)
            {
                Enemy.enemies.remove(Enemy.enemies.get(i));
            }}
            Enemy.enemies=null;
            StrawHat.Reset();
            if(HardCore){
                screen="lostHardcore";}
            else
            {
                screen="lost";
            }
            enemyType="enemy";
        }
        else {
            if (Coliziune.ColiziuneHarta(Map.Map2.MatriceColiziuni, StrawHat.pozitie)) {
                StrawHat.pozitie = poz;
            }
            if(Enemy.enemies!=null){
            for (int j = 0; j < Enemy.enemies.size(); j++) {
                Enemy.enemies.get(j).Update();
                if (Enemy.enemies.get(j).alive >= 4 && Enemy.enemies.get(j).bombeInamic.isEmpty()) {
                    Enemy.enemies.remove(Enemy.enemies.get(j));
                }
            }
            if(Enemy.enemies.isEmpty()){Enemy.enemies=null;}}
        }
    }
    //map3 updates
    private void Map3Update() {
        Vector2D poz=new Vector2D(StrawHat.pozitie.xpos,StrawHat.pozitie.ypos);
        StrawHat.Update();
        if(StrawHat.vieti<=0)
        {
            for(int i=0;i<FinalBoss.finalBoss.flacariBoss.size();i++)
            {
                FinalBoss.finalBoss.flacariBoss.remove(FinalBoss.finalBoss.flacariBoss.get(i));
            }
            if(Enemy.enemies!=null){
            for (int i = 0; i < Enemy.enemies.size(); i++) {
                for (int j = 0; j < Enemy.enemies.get(i).bombeInamic.size(); j++) {
                    Enemy.enemies.get(i).bombeInamic.remove(Enemy.enemies.get(i).bombeInamic.get(j));
                }
                Enemy.enemies.remove(Enemy.enemies.get(i));
            }
            for(int i=0;i<Enemy.enemies.size();i++)
            {
                Enemy.enemies.remove(Enemy.enemies.get(i));
            }}
            Enemy.enemies=null;
            enemyType="final";
            FinalBoss.finalBoss=null;
            StrawHat.Reset();
            if(HardCore){
                screen="lostHardcore";}
            else
            {
                screen="lost";
            }

        }
        else {
            if (Coliziune.ColiziuneHarta(Map.Map3.MatriceColiziuni, StrawHat.pozitie)) {
                StrawHat.pozitie = poz;
            }
            if (FinalBoss.finalBoss != null) {
                FinalBoss.finalBoss.Update();
                if(Enemy.enemies!=null){
                for (int j = 0; j < Enemy.enemies.size(); j++) {
                    Enemy.enemies.get(j).Update();
                    if (Enemy.enemies.get(j).alive >= 4 && Enemy.enemies.get(j).bombeInamic.isEmpty()) {
                        Enemy.enemies.remove(Enemy.enemies.get(j));
                    }
                }
                    if(Enemy.enemies.isEmpty()){Enemy.enemies=null;}
                }
                for (int j = 0; j < FinalBoss.finalBoss.flacariBoss.size(); j++) {
                    if (Coliziune.ColiziuneProiectilaPlayer(StrawHat.pozitie, new Vector2D((int) FinalBoss.finalBoss.flacariBoss.get(j).xpos, (int) FinalBoss.finalBoss.flacariBoss.get(j).ypos), 96)) {
                        FinalBoss.finalBoss.flacariBoss.remove(FinalBoss.finalBoss.flacariBoss.get(j));
                        StrawHat.vieti--;
                    }
                }
            }
        }
    }

    //initializare barci map1
    private void InitMap1() throws IllegalArgumentException {
        if (enemyType.equals("enemy")) {
            creator=new CreatorEnemy();
            Enemy.enemies=new ArrayList<Enemy>();
            Enemy.enemies.add((Enemy) creator.CreateEnemy(Assets.navy, new Vector2D(1300, 200), false, Map.Map1));
            Enemy.enemies.add((Enemy) creator.CreateEnemy(Assets.navy, new Vector2D(1100, 450), false, Map.Map1));
            Enemy.enemies.add((Enemy) creator.CreateEnemy(Assets.navy, new Vector2D(816, 770), true, Map.Map1));
            lastMap="Map0";
            if(HardCore==true) {
                StrawHat = Player.GetInstance(Assets.player1, new Vector2D(30, 130), Map.Map1,1,25);
            }
            else
            {
                StrawHat = Player.GetInstance(Assets.player1, new Vector2D(30, 130), Map.Map1,4,25);
            }

        }
        else
        {
            throw new IllegalArgumentException("tipul inamicilor de la nivelul 1 trebuie sa fie \"enemy\"");
        }
    }
    //initializare barci map2
    private void InitMap2() throws IllegalArgumentException {
        if (enemyType.equals("enemy")) {
            creator = new CreatorEnemy();
            Enemy.enemies=new ArrayList<Enemy>();
            Enemy.enemies.add((Enemy) creator.CreateEnemy(Assets.beast, new Vector2D(1500, 130), false, Map.Map2));
            Enemy.enemies.add((Enemy) creator.CreateEnemy(Assets.beast, new Vector2D(1100, 340), false, Map.Map2));
            Enemy.enemies.add((Enemy) creator.CreateEnemy(Assets.beast, new Vector2D(816, 770), true, Map.Map2));
            if(HardCore==true) {
                StrawHat = Player.GetInstance(Assets.player1, new Vector2D(30, 130), Map.Map2,1,75);
            }
            else
            {
                StrawHat = Player.GetInstance(Assets.player1, new Vector2D(30, 130), Map.Map2,4,75);
            }
        }
        else
        {
            throw new IllegalArgumentException("tipul inamicilor de la nivelul 2 trebuie sa fie \"enemy\"");
        }
    }
    //wave 2 map2
    private void InitMap2Wave2() throws IllegalArgumentException{
        if (enemyType.equals("enemy")) {
            creator = new CreatorEnemy();
            Enemy.enemies=new ArrayList<Enemy>();
            Enemy.enemies.add((Enemy) creator.CreateEnemy(Assets.beastright, new Vector2D(600, 110), true, Map.Map2));
            Enemy.enemies.add((Enemy) creator.CreateEnemy(Assets.beastright, new Vector2D(110, 500), false, Map.Map2));
            Enemy.enemies.add((Enemy) creator.CreateEnemy(Assets.beast, new Vector2D(1535, 490), false, Map.Map2));
            Enemy.enemies.add((Enemy) creator.CreateEnemy(Assets.beast, new Vector2D(1050, 310), false, Map.Map2));
            stage=2;
            enemyType="final";
        }
        else
        {
            throw new IllegalArgumentException("tipul inamicilor de la nivelul 2 trebuie sa fie \"enemy\"");
        }
    }
    //initializare barci map3
    private void InitMap3()throws IllegalArgumentException{
        if(enemyType.equals("final")){
            creator=new CreatorBoss();
            FinalBoss.finalBoss=(FinalBoss) creator.CreateEnemy(Assets.finalBoss1,new Vector2D(1330,305),false,Map.Map3);
            if(HardCore==true) {
                StrawHat = Player.GetInstance(Assets.player1, new Vector2D(30, 130), Map.Map3,1,250);
            }
            else
            {
                StrawHat = Player.GetInstance(Assets.player1, new Vector2D(30, 130), Map.Map3,4,250);
            }
            enemyType="enemy";
            stage=1;
        }
        else
        {
            throw new IllegalArgumentException("Tipul inamicilor de la prima initializarea a nivelului trei trebuie sa fie \"final\"");
        }
    }
    //wave 1 map3
    private void InitMap3Wave1() throws IllegalArgumentException{
        if (enemyType.equals("enemy")) {
            creator = new CreatorEnemy();
            Enemy.enemies=new ArrayList<Enemy>();
            FinalBoss.finalBoss.image=Assets.finalBoss2;
            Enemy.enemies.add((Enemy) creator.CreateEnemy(Assets.blackbeard, new Vector2D(1130, 200), false, Map.Map3));
//            Enemy.enemies.add((Enemy) creator.CreateEnemy(Assets.blackbeard, new Vector2D(1100, 340), false, Map.Map3));
            Enemy.enemies.add((Enemy) creator.CreateEnemy(Assets.blackbeard, new Vector2D(1070, 870), true, Map.Map3));
            stage=2;
        }
        else
        {
            throw new IllegalArgumentException("tipul inamicilor de la nivelul 3 wave 1 trebuie sa fie \"enemy\"");
        }
    }
    //wave 2 map3
    private void InitMap3Wave2() throws IllegalArgumentException{
        if (enemyType.equals("enemy")) {
            creator = new CreatorEnemy();
            Enemy.enemies=new ArrayList<Enemy>();
            FinalBoss.finalBoss.image=Assets.finalBoss3;
            //Enemy.enemies.add((Enemy) creator.CreateEnemy(Assets.blackbeardright, new Vector2D(600, 110), true, Map.Map3));
            Enemy.enemies.add((Enemy) creator.CreateEnemy(Assets.blackbeardright, new Vector2D(30, 600), false, Map.Map3));
            Enemy.enemies.add((Enemy) creator.CreateEnemy(Assets.blackbeard, new Vector2D(1230, 600), false, Map.Map3));
            Enemy.enemies.add((Enemy) creator.CreateEnemy(Assets.blackbeardright, new Vector2D(500, 40), true, Map.Map3));
            stage=3;
        }
        else
        {
            throw new IllegalArgumentException("tipul inamicilor de la nivelul 3 wave 2 trebuie sa fie \"enemy\"");
        }
    }
    //wave 3 map3
    private void InitMap3Wave3() throws IllegalArgumentException{
        if (enemyType.equals("enemy")) {
            creator = new CreatorEnemy();
            Enemy.enemies=new ArrayList<Enemy>();
            FinalBoss.finalBoss.image=Assets.finalBoss4;
            Enemy.enemies.add((Enemy) creator.CreateEnemy(Assets.blackbeard, new Vector2D(700, 700), true, Map.Map3));
           // Enemy.enemies.add((Enemy) creator.CreateEnemy(Assets.blackbeardright, new Vector2D(110, 500), false, Map.Map3));
            Enemy.enemies.add((Enemy) creator.CreateEnemy(Assets.blackbeard, new Vector2D(930, 385), false, Map.Map3));
            Enemy.enemies.add((Enemy) creator.CreateEnemy(Assets.blackbeard, new Vector2D(1220, 610), false, Map.Map3));
            stage=4;
        }
        else
        {
            throw new IllegalArgumentException("tipul inamicilor de la nivelul 3 wave 3 trebuie sa fie \"enemy\"");
        }
    }

    //Desen map1
    private void Map1Draw(){
        Map.Map1.Draw(this.g);
        StrawHat.Draw(this.g);
        for (int i = 0; i < Player.bombe.size(); i++) {
            Player.bombe.get(i).Draw(this.g,32);
        }
        if(Enemy.enemies!=null){
        for (int i = 0; i < Enemy.enemies.size(); i++) {
            if (Enemy.enemies.get(i).alive < 4) {
                Enemy.enemies.get(i).Draw(this.g,96);
            }
            for (int j = 0; j < Enemy.enemies.get(i).bombeInamic.size(); j++) {
                Enemy.enemies.get(i).bombeInamic.get(j).Draw(this.g,32);
            }
        }}
        if(Enemy.enemies==null&&StrawHat.bombe.isEmpty()&&StrawHat.vieti>=1)
        {
            StrawHat.Reset();
            lastMap = "Map1";
            if(HardCore)
            {
                screen="inbetweenHardcore";
            }
            else{
            screen="inbetween";
            }
        }
    }
    //Desen map2
    private void Map2Draw(){
        Map.Map2.Draw(this.g);
        StrawHat.Draw(this.g);
        for (int i = 0; i < Player.bombe.size(); i++) {
            Player.bombe.get(i).Draw(this.g,32);
        }
        if(Enemy.enemies!=null){
        for (int i = 0; i < Enemy.enemies.size(); i++) {
            if (Enemy.enemies.get(i).alive < 4) {
                Enemy.enemies.get(i).Draw(this.g,96);
            }
            for (int j = 0; j < Enemy.enemies.get(i).bombeInamic.size(); j++) {
                Enemy.enemies.get(i).bombeInamic.get(j).Draw(this.g,32);
            }
        }}
        if (Enemy.enemies==null && StrawHat.bombe.isEmpty() && StrawHat.vieti >= 1&&stage==1) {
            InitMap2Wave2();
        }
        if (Enemy.enemies==null && StrawHat.bombe.isEmpty() && StrawHat.vieti >= 1&&stage==2){
            StrawHat.Reset();
            stage=1;
            lastMap="Map2";
            if(HardCore)
            {
                screen="inbetweenHardcore";
            }
            else{
                screen="inbetween";
            }
        }
    }
    //Desen map3
    private void Map3Draw(){
        Map.Map3.Draw(this.g);
        StrawHat.Draw(this.g);
        for (int i = 0; i < Player.bombe.size(); i++) {
            Player.bombe.get(i).Draw(this.g,32);
        }
        if(Enemy.enemies!=null){
        for (int i = 0; i < Enemy.enemies.size(); i++) {
            if (Enemy.enemies.get(i).alive < 4) {
                Enemy.enemies.get(i).Draw(this.g,96);
            }
            for (int j = 0; j < Enemy.enemies.get(i).bombeInamic.size(); j++) {
                Enemy.enemies.get(i).bombeInamic.get(j).Draw(this.g,32);
            }
        }}

        if (FinalBoss.finalBoss.alive < 80) {
                FinalBoss.finalBoss.Draw(this.g,350);
            }
        for (int j = 0; j < FinalBoss.finalBoss.flacariBoss.size(); j++) {
                FinalBoss.finalBoss.flacariBoss.get(j).Draw(this.g,60);
            }
        if(FinalBoss.finalBoss.alive==19&&stage==1)
        {
            FinalBoss.finalBoss.invincibility=true;
            InitMap3Wave1();
        }
        if(stage==2&&Enemy.enemies==null)
        {
            FinalBoss.finalBoss.invincibility=false;
        }
        if(FinalBoss.finalBoss.alive==39&&stage==2)
        {
            FinalBoss.finalBoss.invincibility=true;
            InitMap3Wave2();
        }
        if(stage==3&&Enemy.enemies==null)
        {
            FinalBoss.finalBoss.invincibility=false;
        }
        if(FinalBoss.finalBoss.alive==59&&stage==3)
        {
            FinalBoss.finalBoss.invincibility=true;
            InitMap3Wave3();
        }
        if(stage==4&&Enemy.enemies==null)
        {
            FinalBoss.finalBoss.invincibility=false;
        }
        if (FinalBoss.finalBoss.flacariBoss.isEmpty() && StrawHat.bombe.isEmpty() && StrawHat.vieti >= 1&&stage==4&&FinalBoss.finalBoss.alive>=79){
            StrawHat.Reset();
            stage=1;
            screen="finish";
            FinalBoss.finalBoss=null;
        }
    }

    private void playMusic()
    {
        sound.setFile(2);
        sound.play();
        sound.loop();
    }
    private void stopMusic(){
        sound.stop();
    }
}

