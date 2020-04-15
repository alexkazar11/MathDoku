package mathdoku.java;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * MathDoku Cells are grouped in Cages (which can be of any size ranging from 1 to NxN,
 * where N is a number of Cells in one row/column).
 * <p>
 * Cages are highlighted on the board by thicker boundaries.
 * Each Cage has a label showing a target number followed by an arithmetic operator (+, -, x, ÷).
 * It must be possible to obtain the target by applying the arithmetic operator to the numbers in that cage.
 * For - and ÷, this can be done in any order.
 * <p>
 * Note: If a cage consists of a single cell, then no arithmetic operator is shown.
 * The label simply shows the number that must be in that cell.
 */
public class Cage {
    private int size;
    private String target;
    private ArrayList<Cell> cells;

    public Cage(String target, ArrayList<Cell> cells) {
        this.target = target;
        this.cells = cells;
        this.size = cells.size();
    }

    @Override
    public String toString() {
        return "Cage{" +
                "size=" + size +
                ", target='" + target + '\'' +
                ", cells=" + cells +
                '}';
    }

    public Comparator<Cell> getComparator() {
        return Comparator.comparingInt(Cell::getCellID);
    }

    public int getSize() {
        return size;
    }

    public String getTarget() {
        return target;
    }

    public ArrayList<Cell> getCells() {
        return cells;
    }
}
