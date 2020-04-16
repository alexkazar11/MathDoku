package mathdoku.java;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Board is a square grid of a size NxN.
 * It consists of NxN number of Cells arranged one after another and grouped in cells.
 */
public class Board extends Canvas {
    private int size;

    public Board(int size) {
        this.size = size;
        widthProperty().addListener(evt -> update());
        heightProperty().addListener(evt -> update());
    }

    /**
     * Redraws the Board every time the window
     * is resized or a new event has happened.
     */
    private void update() {
        double width = getWidth();
        double height = getHeight();

        GraphicsContext gc = getGraphicsContext2D();
        gc.clearRect(0, 0, width, height);

        //Draws a red rectangle border for testing
        gc.setStroke(Color.RED);
        gc.setLineWidth(4);
        gc.strokeRect(0, 0, width, height);

        //Draws a red cross for testing
        gc.setStroke(Color.RED);
        gc.strokeLine(0, 0, width, height);
        gc.strokeLine(0, height, width, 0);
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
}
