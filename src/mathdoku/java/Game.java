package mathdoku.java;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

import java.util.Arrays;

/**
 * MathDoku Game rules:
 * - The only numbers allowed to be entered are from 1 to N, where N is a number of Cells in one row/column
 * - Each number must appear exactly once in each row.
 * - Each number must appear exactly once in each column.
 * - Each Cage contains a target. It must be possible to obtain the target by applying the arithmetic operator
 * to the numbers in that cage. For - and ÷, this can be done in any order.
 * - In a one-cell cage, the target is the number that must be in that cell.
 */
public class Game extends Application {

    private int boardSize;
    private int difficulty;
    private Cell[] listOfCells;

    /**
     * Creates a new Game
     *
     * @param stage      Stage where the Game is
     * @param boardSize  Size of the Square Board NxN as N (from 2 to 8)
     * @param difficulty Difficulty level (from 1 to 3, where 1 is Easy)
     * @throws Exception Exception is thrown in case something is wrong with the stage
     */
    public Game(Stage stage, int boardSize, int difficulty) throws Exception {
        this.boardSize = boardSize;
        this.difficulty = difficulty;
        start(stage);
    }

    @Override
    public void start(Stage stage) throws Exception {
        /* ------- Visual Elements Setup ------- */

        //Board (Canvas) setup
        Board board = new Board(boardSize);

        //Panes setup (wrapping Board inside Pane)
        BorderPane borderPane = new BorderPane();
        Pane pane = new Pane();
        pane.getChildren().add(board);

        //Making the Board responsive
        board.widthProperty().bind(pane.widthProperty());
        board.heightProperty().bind(pane.heightProperty());

        //Menubar (have to specify javaFX path, since the class is also called Menu
        javafx.scene.control.Menu file = new javafx.scene.control.Menu("File");
        javafx.scene.control.Menu help = new javafx.scene.control.Menu("Help");
        MenuItem backToMenu = new MenuItem("Back to Menu");
        MenuItem preferences = new MenuItem("Options");
        MenuItem quit = new MenuItem("Quit");
        file.getItems().addAll(backToMenu, preferences, quit);
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(file, help);

        //Creating Buttons
        Button undo = new Button("Undo");
        Button redo = new Button("Redo");
        Button showMistakes = new Button("Show Mistakes");
        Button clear = new Button("Clear");

        //Setting up button sizes and alignment
        Button[] buttons = new Button[]{undo, redo, showMistakes, clear};
        for (Button button : buttons) {
            button.setAlignment(Pos.CENTER);
            button.setMaxWidth(Double.MAX_VALUE);
            HBox.setHgrow(button, Priority.ALWAYS);
        }

        //HBox "toolbar" creating & aligning
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(10);
        hBox.setPadding(new Insets(10, 10, 10, 10));
        hBox.getChildren().addAll(undo, redo, showMistakes, clear);

        //Placing elements on the borderPane
        borderPane.setCenter(pane);
        borderPane.setTop(menuBar);
        borderPane.setBottom(hBox);

        /* ------- Functionality Setup ------- */
        //Generates a list of Cells
        generateCells(boardSize);

        //When back to menu is pressed opens Menu in the same stage
        backToMenu.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Menu menu = new Menu();
                try {
                    menu.start(stage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        //When Quit is pressed quits the game
        quit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Platform.exit();
                System.exit(0);
            }
        });

        //When Mouse is clicked on the Board, finds appropriate Cell and highlights it
        board.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                int x = (int) (mouseEvent.getX() / board.getCellWidth());
                int y = (int) (mouseEvent.getY() / board.getCellHeight());

                int[] chosenXY = new int[]{x, y};

                System.out.println(x + ", " + y);
                Cell cell;
                for (Cell listOfCell : listOfCells) {
                    if (Arrays.equals(listOfCell.getCoordinates(), chosenXY)) {
                        cell = listOfCell;
                        System.out.println(cell.toString());
                        board.chooseBox(cell);
                        break;
                    }
                }
            }
        });

        //Stage setup
        stage.setMinHeight(300);
        stage.setMinWidth(300);
        stage.setResizable(true);
        stage.setTitle("MathDoku");
        stage.setScene(new Scene(borderPane, 400, 435));
        stage.show();
    }

    /**
     * Iteratively creates Cells and
     * fills the ArrayList with those Cells
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
}
