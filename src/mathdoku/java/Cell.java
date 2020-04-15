package mathdoku.java;

import java.util.Arrays;

/**
 * MathDoku Board consists of a number of Cells (for example 6x6 board has 36 Cells).
 * <p>
 * Each Cell has a CellID that is used to identify the Cell
 * (starting from 1 to NxN, where N is a number of Cells in one row/column).
 * <p>
 * Each Cell has a value property that stores the value entered into the Cell by a user.
 * <p>
 * Each Cell has coordinates in the format [x,y],
 * where x and y are both integers that indicate row and column on the board.
 */
public class Cell implements Comparable<Cell> {
    private int cellID;
    private int value;
    private int[] coordinates;

    public Cell(int cellID, int value, int[] coordinates) {
        this.cellID = cellID;
        this.value = value;
        this.coordinates = coordinates;
    }

    @Override
    public int compareTo(Cell cell) {
        int compareVal = cell.getValue();
        return compareVal - this.getValue();
    }

    @Override
    public String toString() {
        return "Cell{" +
                "cellID=" + cellID +
                ", value=" + value +
                ", coordinates=" + Arrays.toString(coordinates) +
                '}';
    }

    public int getCellID() {
        return cellID;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int[] getCoordinates() {
        return coordinates;
    }
}
