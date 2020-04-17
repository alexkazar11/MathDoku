package mathdoku.java;

import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Board is a square grid of a size NxN.
 * It consists of NxN number of Cells arranged one after another and grouped in cells.
 */
public class Board extends Canvas {
    private GraphicsContext gc;
    private int size;
    private double cellWidth;
    private double cellHeight;
    private Cell chosenCell;
    private Cell[] arrayOfCells;
    private ArrayList<String> allowed;
    private ArrayList<Cage> cages = new ArrayList<>();

    public Board(int size) throws IOException {
        this.size = size;
        this.gc = getGraphicsContext2D();

        //Listeners to make the Board resizable
        widthProperty().addListener(evt -> update());
        heightProperty().addListener(evt -> update());

        //Generates a list of Cells
        generateCells(size);

        //Generate list of allowed number inputs
        generateAllowedNumbers();

        loadDefaultGame();
        drawCages();
    }

    /**
     * Redraws the Board every time the window
     * is resized or a new event has happened.
     */
    private void update() {
        drawGrid(size);
        drawCages();
        showValues();
        if (chosenCell != null) {
            chooseBox(chosenCell);
        }
    }

    /**
     * Draws a square grid filled with empty Cells.
     *
     * @param boardSize The number of Cells in each row/column
     */
    private void drawGrid(int boardSize) {
        double width = getWidth();
        double height = getHeight();

        this.cellWidth = width / size;
        this.cellHeight = height / size;

        gc.clearRect(0, 0, width, height);
        gc.setLineWidth(4);

        gc.strokeRect(0, 0, width, height);
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                double x = cellWidth * i;
                double y = cellHeight * j;
                gc.setLineWidth(1);
                gc.setStroke(Color.BLACK);
                gc.strokeRect(x, y, cellWidth, cellHeight);
            }
        }
    }

    /**
     * Clears the board.
     */
    public void clear() {
        chosenCell = null;
        for (Cell cell :
                arrayOfCells) {
            cell.setValue(0);
        }
        update();
    }

    /**
     * Clears the currently chosen Cell.
     */
    private void clearChosenCell() {
        chosenCell = null;
        update();
    }

    /**
     * Highlights a chosen Cell green.
     *
     * @param cell Cell to be highlighted
     */
    public void chooseBox(Cell cell) {
        clearChosenCell();
        chosenCell = cell;
        gc.setLineWidth(3);
        gc.setStroke(Color.GREEN);
        gc.strokeRect(cell.getCoordinates()[0] * cellWidth,
                cell.getCoordinates()[1] * cellHeight, cellWidth, cellHeight);
    }

    /**
     * Validates keyboard input and calls an appropriate method to handle it.
     *
     * @param keyEvent User keyboard input
     */
    public void validateKeyboardInput(KeyEvent keyEvent) {
        if (keyEvent != null) {
            if (allowed.contains(keyEvent.getText())) {
                setChosenCellValue(keyEvent);
            } else if (keyEvent.getCode() == KeyCode.BACK_SPACE) {
                clearCellValue();
            } else if (keyEvent.getCode() == KeyCode.RIGHT) {
                moveToNextCell();
            } else if (keyEvent.getCode() == KeyCode.LEFT) {
                moveToPreviousCell();
            } else if (keyEvent.getCode() == KeyCode.UP) {
                moveToAboveCell();
            } else if (keyEvent.getCode() == KeyCode.DOWN) {
                moveToBelowCell();
            }
        }
    }

    /**
     * Moves the selected Cell to the one above it in the column.
     */
    private void moveToAboveCell() {
        for (int i = 0; i < arrayOfCells.length; i++) {
            if (arrayOfCells[i].equals(chosenCell)) {
                if (i >= size) {
                    chosenCell = arrayOfCells[i - size];
                } else {
                    chosenCell = arrayOfCells[i + (size * size - size)];
                }
                chooseBox(chosenCell);
                break;
            }
        }
    }

    /**
     * Moves the selected Cell to the one below it in the column.
     */
    private void moveToBelowCell() {
        for (int i = 0; i < arrayOfCells.length; i++) {
            if (arrayOfCells[i].equals(chosenCell)) {
                if ((i + size) < size * size) {
                    chosenCell = arrayOfCells[i + size];
                } else {
                    chosenCell = arrayOfCells[i - (size * size - size)];
                }
                chooseBox(chosenCell);
                break;
            }
        }
    }

    /**
     * Moves the selected Cell to the next one horizontally,
     * if the border is reached, moves down to the first one of the next row.
     * If the end of the board is reached, goes to the first Cell.
     */
    private void moveToNextCell() {
        for (int i = 0; i < arrayOfCells.length; i++) {
            if (arrayOfCells[i].equals(chosenCell)) {
                if (i < arrayOfCells.length - 1) {
                    chosenCell = arrayOfCells[i + 1];
                } else {
                    chosenCell = arrayOfCells[0];
                }
                chooseBox(chosenCell);
                break;
            }
        }
    }

    /**
     * Moves the selected Cell to the previous one horizontally,
     * if the border is reached, moves up to the last one of the previous row.
     * If the beginning of the board is reached, goes to the last Cell.
     */
    private void moveToPreviousCell() {
        for (int i = 0; i < arrayOfCells.length; i++) {
            if (arrayOfCells[i].equals(chosenCell)) {
                if (i > 0) {
                    chosenCell = arrayOfCells[i - 1];
                } else {
                    chosenCell = arrayOfCells[arrayOfCells.length - 1];
                }
                chooseBox(chosenCell);
                break;
            }
        }
    }

    /**
     * Clears the value of the chosen Cell.
     */
    public void clearCellValue() {
        if (chosenCell != null) {
            chosenCell.setValue(0);
            update();
        }
    }

    /**
     * Sets the value from the keyEvent to the chosen Cell.
     *
     * @param keyEvent The keyEvent that contains the value to be used
     */
    private void setChosenCellValue(KeyEvent keyEvent) {
        if (chosenCell != null) {
            setTextStrokeParameters(18);
            int value = Integer.parseInt(keyEvent.getText());
            chosenCell.setValue(value);
            update();
        }
    }

    /**
     * Sets the value to the chosen Cell.
     *
     * @param value The value to be used
     */
    public void setChosenCellValue(int value) {
        if (chosenCell != null) {
            setTextStrokeParameters(18);
            chosenCell.setValue(value);
            update();
        }
    }

    /**
     * Sets all the required font parameters and given font size.
     *
     * @param fontSize Font size to be used
     */
    private void setTextStrokeParameters(int fontSize) {
        gc.setLineWidth(2);
        gc.setStroke(Color.BLACK);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setFont(Font.font("Verdana", FontWeight.LIGHT, fontSize));
        gc.setTextBaseline(VPos.CENTER);
    }

    /**
     * Prints the values of all Cells inside those Cells on the board.
     */
    private void showValues() {
        setTextStrokeParameters(18);
        for (Cell cell : arrayOfCells) {
            if (cell.getValue() > 0) {
                gc.strokeText(Integer.toString(cell.getValue()),
                        cell.getCoordinates()[0] * cellWidth + cellWidth / 2,
                        cell.getCoordinates()[1] * cellHeight + cellHeight / 2);
            }
        }
    }

    /**
     * Draws a border on the given side of the given Cell.
     *
     * @param side The side of the Cell for the border to be drawn on
     * @param cell The Cell for the border to be drawn on
     */
    private void drawBorder(String side, Cell cell) {
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(4);
        double cellX = cell.getCoordinates()[0] * cellWidth;
        double cellY = cell.getCoordinates()[1] * cellHeight;

        switch (side) {
            case "top":
                gc.strokeLine(cellX, cellY, cellX + cellWidth, cellY);
                break;
            case "bottom":
                gc.strokeLine(cellX, cellY + cellHeight, cellX + cellWidth, cellY + cellHeight);
                break;
            case "left":
                gc.strokeLine(cellX, cellY, cellX, cellY + cellHeight);
                break;
            case "right":
                gc.strokeLine(cellX + cellWidth, cellY, cellX + cellWidth, cellY + cellHeight);
                break;
        }
    }

    /**
     * Analyses the given Cage and draws a thicker border around it.
     *
     * @param cage The Cage for the border to be drawn around
     */
    private void drawCage(Cage cage) {
        //ArrayList of Cells that are in the given Cage
        ArrayList<Cell> cageCells = cage.getCells();
        //A Matrix of all Cells on the board (rows separated)
        ArrayList<List<Cell>> matrix = Toolbox.getMatrix(arrayOfCells, size);
        //A transpose of all Cells on the board (columns separated)
        List<List<Cell>> transpose = Toolbox.getTranspose(matrix);

        //Draws a top border for every Cell(from a Cage) that's the highest in every column
        for (List<Cell> row : transpose) {
            for (Cell cell : row) {
                if (cageCells.contains(cell)) {
                    drawBorder("top", cell);
                    break;
                }
            }
        }

        //Draws a bottom border for every Cell(from a Cage) that's the lowest in every column
        for (List<Cell> row : transpose) {
            Cell inCage = null;
            for (Cell cell : row) {
                if (cageCells.contains(cell)) {
                    inCage = cell;
                }
            }
            if (inCage != null) {
                drawBorder("bottom", inCage);
            }
        }

        //Draws a left border for every Cell(from a Cage) that's the closest in every row
        for (List<Cell> row : matrix) {
            for (Cell cell : row) {
                if (cageCells.contains(cell)) {
                    drawBorder("left", cell);
                    break;
                }
            }
        }

        //Draws a right border for every Cell(from a Cage) that's the furthest in every row
        for (List<Cell> row : matrix) {
            Cell inCage = null;
            for (Cell cell : row) {
                if (cageCells.contains(cell)) {
                    inCage = cell;
                }
            }
            if (inCage != null) {
                drawBorder("right", inCage);
            }
        }

        //Draws a target on the Cage
        drawCageLabels(cage);
    }

    /**
     * Draws a target label for the cage in the first Cell of the Cage.
     *
     * @param cage The Cage for the target to be drawn for
     */
    private void drawCageLabels(Cage cage) {
        //Sorts the cage to find the first Cell of the Cage
        List<Cell> cellsSorted = new ArrayList<Cell>(cage.getCells());
        cellsSorted.sort(cage.getComparator());
        Cell firstCell = cellsSorted.get(0);
        double cageStartY = firstCell.getCoordinates()[1] * cellHeight;
        double cageStartX = firstCell.getCoordinates()[0] * cellWidth;

        //Sets the font size for the target
        setTextStrokeParameters(10);
        gc.setLineWidth(1);

        //Changes the "padding" for the label depending on the board size
        int divisorX = 0;
        int divisorY = 0;
        switch (size) {
            case 2:
                divisorX = 10;
                divisorY = 10;
                break;
            case 3:
                divisorX = 8;
                divisorY = 8;
                break;
            case 4:
                divisorX = 7;
                divisorY = 7;
                break;
            case 5:
                divisorX = 4;
                divisorY = 4;
                break;
            case 6:
                divisorX = 4;
                divisorY = 5;
                break;
            case 7:
            case 8:
                divisorX = 3;
                divisorY = 3;
                break;
        }

        //Draws the label itself
        gc.strokeText(cage.getTarget(),
                cageStartX + cellWidth / divisorX,
                cageStartY + cellHeight / divisorY);
    }

    /**
     * Loads a default Game for the chosen Board size.
     *
     * @throws IOException Exception is thrown if there is a problem with the input file
     */
    private void loadDefaultGame() throws IOException {
        switch (size) {
            case 2: {
                readFile("src/mathdoku/resources/puzzles/2x2.txt");
                break;
            }
            case 3: {
                readFile("src/mathdoku/resources/puzzles/3x3.txt");
                break;
            }
            case 4: {
                readFile("src/mathdoku/resources/puzzles/4x4.txt");
                break;
            }
            case 5: {
                readFile("src/mathdoku/resources/puzzles/5x5.txt");
                break;
            }
            case 6: {
                readFile("src/mathdoku/resources/puzzles/6x6.txt");
                break;
            }
            case 7: {
                readFile("src/mathdoku/resources/puzzles/7x7.txt");
                break;
            }
            case 8: {
                readFile("src/mathdoku/resources/puzzles/8x8.txt");
                break;
            }
        }
    }

    /**
     * Reads a given game config file and creates Cages accordingly.
     *
     * @param filename The filepath to config
     * @throws IOException Exception is thrown if there is a problem with the input file
     */
    private void readFile(String filename) throws IOException {
        try (FileInputStream fileInputStream = new FileInputStream(filename);
             InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] var = line.split("\\s+");
                String target = var[0];
                String[] cellIDs = var[1].split(",");
                createCage(target, cellIDs);
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a cage from a given target and CellIDs.
     *
     * @param target  A target for the Cage.
     * @param cellIDs A list of CellIDs for the Cage.
     */
    private void createCage(String target, String[] cellIDs) {
        ArrayList<Cell> cells = new ArrayList<>();
        for (String id : cellIDs) {
            int cellID = Integer.parseInt(id);
            for (Cell cell : arrayOfCells) {
                if (cell.getCellID() == cellID) {
                    cells.add(cell);
                }
            }
        }
        Cage cage = new Cage(target, cells);
        cages.add(cage);
    }

    /**
     * Iteratively draws cages.
     */
    private void drawCages() {
        for (Cage cage : cages) {
            drawCage(cage);
        }
    }

    /**
     * Iteratively creates Cells and
     * fills the ArrayList with those Cells.
     *
     * @param boardSize The number of Cells in each row/column
     */
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

    /**
     * Creates a list of numbers that are allowed to be entered by the rules of the game.
     */
    private void generateAllowedNumbers() {
        allowed = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            allowed.add(Integer.toString(i + 1));
        }
    }

    public Cell[] getArrayOfCells() {
        return arrayOfCells;
    }

    @Override
    public boolean isResizable() {
        return true;
    }

    @Override
    public double prefWidth(double height) {
        return getWidth();
    }

    @Override
    public double prefHeight(double width) {
        return getHeight();
    }

    public double getCellWidth() {
        return cellWidth;
    }

    public double getCellHeight() {
        return cellHeight;
    }

}
