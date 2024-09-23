import gui.FieldDisplay;
import field.FieldSize;
import field.Game;

public class Main {
    public static void main(String[] args) {
        Game game = new Game(FieldSize.small);
        new FieldDisplay(game);
    }
}