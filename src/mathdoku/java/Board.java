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

import java.util.ArrayList;

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
    private Cell[] listOfCells;
    private ArrayList<String> allowed;

    public Board(int size) {
        this.size = size;
        this.gc = getGraphicsContext2D();

        //Listeners to make the Board resizable
        widthProperty().addListener(evt -> update());
        heightProperty().addListener(evt -> update());

        //Generates a list of Cells
        generateCells(size);

        //Generate list of allowed number inputs
        generateAllowedNumbers();
    }

    /**
     * Redraws the Board every time the window
     * is resized or a new event has happened.
     */
    private void update() {
        drawGrid(size);
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
            System.out.println(keyEvent.getText());

            if (allowed.contains(keyEvent.getText())) {
                setChosenCellValue(keyEvent);
            } else if (keyEvent.getCode() == KeyCode.BACK_SPACE) {
                clearInsideCell();
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
        for (int i = 0; i < listOfCells.length; i++) {
            if (listOfCells[i].equals(chosenCell)) {
                if (i >= size) {
                    chosenCell = listOfCells[i - size];
                } else {
                    chosenCell = listOfCells[i + (size * size - size)];
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
        for (int i = 0; i < listOfCells.length; i++) {
            if (listOfCells[i].equals(chosenCell)) {
                if ((i + size) < size * size) {
                    chosenCell = listOfCells[i + size];
                } else {
                    chosenCell = listOfCells[i - (size * size - size)];
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
        for (int i = 0; i < listOfCells.length; i++) {
            if (listOfCells[i].equals(chosenCell)) {
                if (i < listOfCells.length - 1) {
                    chosenCell = listOfCells[i + 1];
                } else {
                    chosenCell = listOfCells[0];
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
        for (int i = 0; i < listOfCells.length; i++) {
            if (listOfCells[i].equals(chosenCell)) {
                if (i > 0) {
                    chosenCell = listOfCells[i - 1];
                } else {
                    chosenCell = listOfCells[listOfCells.length - 1];
                }
                chooseBox(chosenCell);
                break;
            }
        }
    }

    /**
     * Clears the value of the chosen Cell.
     */
    private void clearInsideCell() {
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
    public void setChosenCellValue(KeyEvent keyEvent) {
        if (chosenCell != null) {
            setTextStrokeParameters(18);
            int value = Integer.parseInt(keyEvent.getText());
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
        for (Cell cell : listOfCells) {
            if (cell.getValue() > 0) {
                gc.strokeText(Integer.toString(cell.getValue()),
                        cell.getCoordinates()[0] * cellWidth + cellWidth / 2,
                        cell.getCoordinates()[1] * cellHeight + cellHeight / 2);
            }
        }
    }

    /**
     * Iteratively creates Cells and
     * fills the ArrayList with those Cells.
     *
     * @param boardSize The number of Cells in each row/column
     */
    private void generateCells(int boardSize) {
        listOfCells = new Cell[boardSize * boardSize];
        int cellID = 0;
        for (int y = 0; y < boardSize; y++) {
            for (int x = 0; x < boardSize; x++) {
                cellID++;
                listOfCells[cellID - 1] = new Cell(cellID, 0, new int[]{x, y});
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
        for (String s :
                allowed) {
            System.out.println(s);
        }
    }

    public Cell[] getListOfCells() {
        return listOfCells;
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
