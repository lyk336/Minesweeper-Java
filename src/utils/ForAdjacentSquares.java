package utils;

import field.Game;
import squares.Square;

public abstract class ForAdjacentSquares {
    public static void loop(Game game, int currentRow, int currentCol, AdjSqrCb cb) {
        for(int offsetRow = -1; offsetRow <= 1; offsetRow++) {
            for(int offsetCol = -1; offsetCol <= 1; offsetCol++) {
                if(offsetRow == 0 && offsetCol == 0){
                    continue;
                }

                int squareRow = currentRow + offsetRow;
                int squareCol = currentCol + offsetCol;

                if(
                    squareRow < 0 ||
                    squareCol < 0 ||
                    squareRow >= game.rows ||
                    squareCol >= game.columns
                ) {
                    continue;
                }

                cb.execute(squareRow, squareCol);
            }
        }
    }

    public static void loop(Game game, Square currentSquare, AdjSqrCb cb){
        int currentRow = currentSquare.row;
        int currentCol = currentSquare.column;
        ForAdjacentSquares.loop(game, currentRow, currentCol, cb);
    }
}
