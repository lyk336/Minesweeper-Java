package utils;

import field.Game;
import squares.Square;

public abstract class ForEachSquare {
    public static void loop(int rows, int cols, LoopCb cb) {
        for(int row = 0; row < rows; row++) {
            for(int col = 0; col < cols; col++) {
                cb.run(row, col);
            }
        }
    }

    public static void loop(Square[][] field, LoopCb cb) {
        int rows = field.length;
        int cols = field[0].length;

        ForEachSquare.loop(rows, cols, cb);
    }

    public static void loop(Game game, LoopCb cb) {
        ForEachSquare.loop(game.rows, game.columns, cb);
    }
}
