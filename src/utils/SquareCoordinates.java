package utils;

import squares.Square;

public abstract class SquareCoordinates {
    public static String coords(Square square) {
        return "r" + square.row + "-c" + square.column;
    }
}
