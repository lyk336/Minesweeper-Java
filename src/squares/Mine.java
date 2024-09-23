package squares;

import field.Game;
import field.GameResult;
import utils.SquareCoordinates;

import java.util.HashSet;

public class Mine extends Square {
    public Mine(int row, int column) {
        super(row, column);
        this.isMine = true;
    }

    @Override
    public void open(Game game) {
        this.isRevealed = true;
        game.endGame(GameResult.lose);
    }

    public static Integer[] randomMinePosition(Game game, HashSet<String> safeArea) {
        int row = (int) Math.floor(Math.random() * game.rows);
        int col = (int) Math.floor(Math.random() * game.columns);

        Square square = game.field[row][col];

        // check if field was initialised
        if(square == null) {
            throw new IllegalArgumentException("The field was not initialized.");
        }

        boolean isInSafeArea = safeArea.contains(SquareCoordinates.coords(square));
        if(square.isMine || isInSafeArea) {
            return Mine.randomMinePosition(game, safeArea);
        }

        return new Integer[] { row, col };
    }
}
