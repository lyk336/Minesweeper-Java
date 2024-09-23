package field;

import squares.Mine;
import squares.Square;
import utils.ForAdjacentSquares;
import utils.ForEachSquare;

import java.util.ArrayList;
import java.util.HashSet;

public class Game {
    public FieldSize fieldSize;
    public int rows;
    public int columns;
    public boolean isGameInProgress;
    public Square[][] field;
    private int mineNumber;
    // store mines to reveal them on lose
    final private ArrayList<Mine> mines = new ArrayList<>();
    // count non mines to end game when the size of set will be == 0
    public int squaresRemain;
    // indicate how many mines left to flag or if there are extra flags
    public int minesLeft;

    public Game(FieldSize fieldSize) {
        this.createField(fieldSize);
    }

    public void createField(FieldSize fieldSize) {
        // define properties of the field
        this.fieldSize = fieldSize;
        switch (fieldSize) {
            case FieldSize.small:
                this.rows = 10;
                this.columns = 8;
                this.mineNumber = 10;
                break;
            case FieldSize.medium:
                this.rows = 18;
                this.columns = 14;
                this.mineNumber = 40;
                break;
            case FieldSize.large:
                this.rows = 24;
                this.columns = 20;
                this.mineNumber = 99;
                break;
        }

        this.isGameInProgress = false;
        this.field = new Square[this.rows][this.columns];
        this.minesLeft = this.mineNumber;

        // fill the field
        ForEachSquare.loop(this.field, (int row, int col) -> this.field[row][col] = new Square(row, col));
    }

    public void startGame(HashSet<String> safeArea) {
        if(this.isGameInProgress) {
            throw new IllegalArgumentException("The game has already started.");
        }

        // clear the list of mines before creating new field
        this.mines.clear();

        this.initialiseField(safeArea);
        this.isGameInProgress = true;

        this.squaresRemain = this.rows * this.columns - this.mineNumber;
    }

    public void endGame(GameResult result) {
        this.isGameInProgress = false;

        // if player wins => just show the end game modal
        if(result == GameResult.win) return;

        // reveal all mines
        for(Mine mine : this.mines) {
           mine.isRevealed = true;
        }
    }

    private void initialiseField(HashSet<String> safeArea) {
        // create mines
        for(int i = 0; i < this.mineNumber; i++) {
            Integer[] coordinates = Mine.randomMinePosition(this, safeArea);
            int row = coordinates[0];
            int column = coordinates[1];

            // create a mine and add it to the field and list of mines
            Mine mine = new Mine(row, column);
            this.field[row][column] = mine;
            this.mines.add(mine);

            // increment the adjacentMines prop for all adjacent squares
            ForAdjacentSquares
                .loop(this, row, column, (int squareRow, int squareCol) -> this.field[squareRow][squareCol].adjacentMines++);
        }
    }
}
