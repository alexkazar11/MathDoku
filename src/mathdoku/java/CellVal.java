package mathdoku.java;

/**
 * CellVal stores a Cell (object) with a Value (int).
 * This is pushed onto action stack to trace user input for undo/redo functionality.
 */
public class CellVal {
    private Cell cell;
    private int value;

    public CellVal(Cell cell, int value) {
        this.cell = cell;
        this.value = value;
    }

    public Cell getCell() {
        return cell;
    }

    public int getValue() {
        return value;
    }
}
