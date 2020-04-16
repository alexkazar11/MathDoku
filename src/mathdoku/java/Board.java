package mathdoku.java;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

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

    public Board(int size) {
        widthProperty().addListener(evt -> update());
        heightProperty().addListener(evt -> update());

        this.size = size;
        this.gc = getGraphicsContext2D();
    }

    /**
     * Redraws the Board every time the window
     * is resized or a new event has happened.
     */
    private void update() {
        drawGrid(size);
    }

    /**
     * Draws a square grid filled with empty Cells
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
     * Clears the currently chosen Cell
     */
    private void clearChosenCell() {
        chosenCell = null;
        update();
    }

    /**
     * Highlights a chosen Cell green
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
