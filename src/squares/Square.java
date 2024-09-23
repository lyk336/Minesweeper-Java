package squares;

import field.Game;
import field.GameResult;
import utils.ForAdjacentSquares;
import utils.SquareCoordinates;

import java.util.ArrayList;
import java.util.HashSet;

public class Square implements IInteractive{
    final public int row;
    final public int column;
    public boolean isMine = false;
    public boolean isRevealed = false;
    public boolean isFlagged = false;
    public int adjacentMines = 0;

    public Square(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public void open(Game game) {
        if(this.isRevealed) return;

        // if this is the first square opened - start the game
        if(!game.isGameInProgress) {
            ArrayList<String> safeAreaList = new ArrayList<>();
            safeAreaList.add(SquareCoordinates.coords(this));
            ForAdjacentSquares.loop(game, this, (int row, int col) -> {
                safeAreaList.add(SquareCoordinates.coords(game.field[row][col]));
            });
            HashSet<String> safeArea = new HashSet<>(safeAreaList);

            game.startGame(safeArea);
        }

        this.isRevealed = true;

        // decrement the number of unrevealed squares and remove flag to correct count of unmarked mines
        game.squaresRemain--;
        if (this.isFlagged) this.toggleFlag(game);

        // if no squares remain to reveal = player wins
        if(game.squaresRemain == 0) {
            game.endGame(GameResult.win);
        }

        if(this.adjacentMines != 0) return;

        // open all adjacent squares which does not have any adjacent mine
        ForAdjacentSquares.loop(game, this, (int row, int col) -> game.field[row][col].open(game));
    }

    public void toggleFlag(Game game) {
        this.isFlagged = !this.isFlagged;
        // update the count of flags
        if (this.isFlagged) {
            // flag added
            game.minesLeft--;
            return;
        }
        // flag removed
        game.minesLeft++;
    }
}
