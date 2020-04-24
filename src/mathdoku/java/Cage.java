package mathdoku.java;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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

        for (Cell cell : cells) {
            cell.setInCage(true);
        }
    }

    public Cage(ArrayList<Cell> cells) {
        this.cells = cells;
        this.size = cells.size();

        for (Cell cell : cells) {
            cell.setInCage(true);
        }
    }

    /**
     * Checks the cage, if it follows the target rule correctly.
     *
     * @return true - if cage is correct or empty, false - if there is a mistake
     */
    public boolean checkCage() {
        if (isEmpty()) {
            return true;
        }
        for (Cell cell : cells) {
            if (cell.getValue() == 0) {
                return true;
            }
        }

        if (target.length() == 1) {
            return Integer.parseInt(target) == cells.get(0).getValue();
        }

        int multiplierInt = Integer.parseInt(target.substring(0, target.length() - 1));
        String multiplierSign = target.substring(target.length() - 1);
        switch (multiplierSign) {
            case "+": {
                int total = 0;
                for (Cell cell : cells) {
                    total = total + cell.getValue();
                }
                return total == multiplierInt;

            }
            case "x": {
                int total = 1;
                for (Cell cell : cells) {
                    total = total * cell.getValue();
                }
                return total == multiplierInt;

            }
            case "-": {
                int total = 0;
                List<Cell> cellsSorted = new ArrayList<Cell>(cells);
                cellsSorted.sort(Cell::compareTo);
                int largest = cellsSorted.get(0).getValue();
                total = total + largest;

                for (int i = 1; i < cellsSorted.size(); i++) {
                    total = total - cellsSorted.get(i).getValue();
                }

                return total == multiplierInt;
            }

            case "\u00f7":
            case "/": {
                int total = 0;

                List<Cell> cellsSorted = new ArrayList<Cell>(cells);
                cellsSorted.sort(Cell::compareTo);
                int largest = cellsSorted.get(0).getValue();
                total = total + largest;

                for (int i = 1; i < cellsSorted.size(); i++) {
                    if (cellsSorted.get(i).getValue() == 0) {
                        return false;
                    }
                    total = total / cellsSorted.get(i).getValue();
                }

                return total == multiplierInt;
            }
        }
        return false;
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

    public void setTarget(String target) {
        this.target = target;
    }

    public ArrayList<Cell> getCells() {
        return cells;
    }

    private boolean isEmpty() {
        for (Cell cell : cells) {
            if (cell.getValue() != 0) {
                return false;
            }
        }
        return true;
    }
}
