package JocPAOO;

public class Main {
    public static Game game;
    public Main() {
    }

    public static void main(String[] args) {
      game =Game.GetInstance("The Journey of the Pirate King", 1680, 960);
      game.StartGame();
    }
}
