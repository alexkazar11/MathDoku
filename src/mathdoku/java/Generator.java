package mathdoku.java;

import javafx.stage.Stage;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Generator {
    private Cell[] arrayOfCells;
    private int size;
    private ArrayList<String> allowed;
    private ArrayList<Cage> cages = new ArrayList<>();
    private int difficulty = 0;

    public Generator(Stage stage, int size, int difficulty) throws Exception {
        this.size = size;
        this.difficulty = difficulty;
        generateAllowedNumbers();
        generateCells(size);
        genRowsCols();
        genCages();
        clearCells();
        new Game(stage, size, allowed, arrayOfCells, cages, difficulty);
    }

    /**
     * Returns a random neighbour Cell for the given Cell.
     *
     * @param cell Cell for the neighbour to be found for
     * @return Random neighbour Cell
     */
    private Cell getRandomNeighbour(Cell cell) {

        int cellX = cell.getCoordinates()[0];
        int cellY = cell.getCoordinates()[1];

        ArrayList<Cell> neighbours = new ArrayList<>();

        for (Cell cell1 : arrayOfCells) {
            int nextCellX = cell1.getCoordinates()[0];
            int nextCellY = cell1.getCoordinates()[1];

            if (cellX == nextCellX && cellY == nextCellY + 1) {
                neighbours.add(cell1);
            }
            if (cellX == nextCellX && cellY == nextCellY - 1) {
                neighbours.add(cell1);
            }
            if (cellY == nextCellY && cellX == nextCellX + 1) {
                neighbours.add(cell1);
            }
            if (cellY == nextCellY && cellX == nextCellX - 1) {
                neighbours.add(cell1);
            }
        }
        int randomNum = ThreadLocalRandom.current().nextInt(0, neighbours.size());

        return neighbours.get(randomNum);
    }

    /**
     * Checks whether the cell has any neighbours at all.
     *
     * @param cell Cell for the neighbour to be found for
     * @return true - has neighbours, false - otherwise
     */
    private boolean hasNeighbours(Cell cell) {
        return (getRandomNeighbour(cell) != null);
    }

    /**
     * Generates random Cages with assigned targets
     */
    private void genCages() {
        int cellsInCages = 0;

        while (cellsInCages < arrayOfCells.length) {
            cellsInCages = 0;

            for (Cell cell : arrayOfCells) {
                if (cell.isInCage()) {
                    cellsInCages++;
                }
            }

            Cage cage = generateRandomCage();
            if (cage != null) {
                cage.setTarget(generateRandomTarget(cage));
                cages.add(cage);
            }

        }
    }

    /**
     * Generates a random Cage (without a target).
     *
     * @return A random Cage
     */
    private Cage generateRandomCage() {
        ArrayList<Cell> cells = new ArrayList<>();
        for (Cell cell : arrayOfCells) {
            if (!cell.isInCage()) {
                cells.add(cell);
                if (hasNeighbours(cell)) {
                    int randomNum;

                    if (difficulty == 1) {
                        randomNum = ThreadLocalRandom.current().nextInt(1, 2);
                    } else if (difficulty == 2) {
                        randomNum = ThreadLocalRandom.current().nextInt(2, 3);
                    } else {
                        randomNum = ThreadLocalRandom.current().nextInt(4, 8);
                    }

                    for (int i = 0; i < randomNum; i++) {
                        Cell neighbourCell = getRandomNeighbour(cell);
                        if (neighbourCell != null && !cells.contains(neighbourCell) && !neighbourCell.isInCage()) {
                            cells.add(neighbourCell);
                        }
                    }
                }
                if (!cells.isEmpty()) {
                    return new Cage(cells);
                }
                break;

            }
        }
        return null;
    }

    /**
     * Fills the array of cells with random digits, with no duplicates in any row nor column.
     *
     * @return true - success; false - otherwise
     */
    private boolean genRowsCols() {
        for (Cell arrayOfCell : arrayOfCells) {
            if (arrayOfCell.getValue() == 0) {
                Collections.shuffle(allowed);
                for (String s : allowed) {
                    arrayOfCell.setValue(Integer.parseInt(s));
                    if (checkCols() && checkRows() && genRowsCols()) {
                        return true;
                    } else {
                        arrayOfCell.setValue(0);
                    }
                }
                return false;
            }
        }
        return true;
    }

    /**
     * Generates a random target for the given Cage.
     *
     * @param cage The Cage for the target to be generated for
     * @return The target for the Cage
     */
    private String generateRandomTarget(Cage cage) {
        ArrayList<Cell> cells = cage.getCells();
        if (cells.size() == 1) {
            return Integer.toString(cells.get(0).getValue());
        }

        String multiplier = "";

        String target;

        cells.sort(Cell::compareTo);

        int total = 0;
        total = cells.get(0).getValue();


        boolean canBeDivided = true;
        boolean canBeSubtracted = true;

        for (int i = 1; i < cells.size(); i++) {
            if (total % cells.get(i).getValue() == 0) {
                total = total / cells.get(i).getValue();
            } else {
                canBeDivided = false;
            }
        }

        total = cells.get(0).getValue();

        for (int i = 1; i < cells.size(); i++) {
            if (total - cells.get(i).getValue() > 0) {
                total = total - cells.get(i).getValue();
            } else {
                canBeSubtracted = false;
            }
        }

        total = 0;
        if (canBeDivided) {
            multiplier = "\u00f7";
            total = cells.get(0).getValue();
            for (int i = 1; i < cells.size(); i++) {
                total = total / cells.get(i).getValue();
            }
        } else if (canBeSubtracted) {
            multiplier = "-";
            total = cells.get(0).getValue();
            for (int i = 1; i < cells.size(); i++) {
                total = total - cells.get(i).getValue();
            }
        } else {
            int randomNum = ThreadLocalRandom.current().nextInt(0, 2);
            switch (randomNum) {
                case 0:
                    multiplier = "+";
                    for (Cell cell : cells) {
                        total += cell.getValue();
                    }
                    break;
                case 1:
                    multiplier = "x";
                    total = cells.get(0).getValue();
                    for (int i = 1; i < cells.size(); i++) {
                        total = total * cells.get(i).getValue();
                    }
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + randomNum);
            }
        }

        target = total + multiplier;
        return target;
    }

    /**
     * Clears the values in all Cells
     */
    private void clearCells() {
        for (Cell cell : arrayOfCells) {
            cell.setValue(0);
        }
    }

    /**
     * Checks rows to be correctly filled.
     *
     * @return true - If rows are correctly filled, false - otherwise
     */
    private boolean checkRows() {
        boolean correct = true;
        for (int i = 0; i < arrayOfCells.length; i = i + size) {
            Cell[] row = Arrays.copyOfRange(arrayOfCells, i, i + size);
            if (findDuplicates(row)) {
                correct = false;
            }
        }
        return correct;
    }

    /**
     * Checks columns to be correctly filled.
     *
     * @return true - If columns are correctly filled, false - otherwise
     */
    private boolean checkCols() {
        boolean correct = true;
        for (int i = 0; i < size; i++) {
            Cell[] col = new Cell[size];
            int j = 0; //position in a col
            for (int k = 0; k < arrayOfCells.length; k = k + size) {
                col[j] = arrayOfCells[i + k];
                j++;
            }
            if (findDuplicates(col)) {
                correct = false;
            }
        }
        return correct;

    }

    /**
     * Travers an array of Cells and finds duplicates.
     *
     * @param cells An array of Cells to be traversed.
     * @return true - if duplicates are found, false otherwise
     */
    private boolean findDuplicates(Cell[] cells) {
        Set<Integer> lump = new HashSet<>();
        for (Cell cell : cells) {
            if (lump.contains(cell.getValue()) && cell.getValue() != 0) {
                return true;
            }
            lump.add(cell.getValue());
        }
        return false;
    }

    /**
     * Creates a list of numbers that are allowed to be entered by the rules of the game.
     */
    private void generateAllowedNumbers() {
        allowed = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            allowed.add(Integer.toString(i + 1));
        }
    }

    private void generateCells(int boardSize) {
        arrayOfCells = new Cell[boardSize * boardSize];
        int cellID = 0;
        for (int y = 0; y < boardSize; y++) {
            for (int x = 0; x < boardSize; x++) {
                cellID++;
                arrayOfCells[cellID - 1] = new Cell(cellID, 0, new int[]{x, y});
            }
        }
    }

}
