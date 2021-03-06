package mathdoku.java;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

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

    private ArrayList<Cage> cages;
    private Cell[] cells;
    private ArrayList<String> allowed;
    private String puzzle;
    private int boardSize;
    private int difficulty;
    private Board board;
    private Button undo = new Button("Undo");
    private Button redo = new Button("Redo");
    private boolean mistakesMode = false;

    /**
     * Creates a new Game.
     *
     * @param stage      Stage where the Game is
     * @param boardSize  Size of the Square Board NxN as N (from 2 to 8)
     * @throws Exception Exception is thrown in case something is wrong with the stage
     */
    public Game(Stage stage, int boardSize) throws Exception {
        this.boardSize = boardSize;
        start(stage);
    }

    /**
     * Creates a new Game from a txt file.
     *
     * @param stage      Stage where the Game is
     * @param boardSize  Size of the Square Board NxN as N (from 2 to 8)
     * @param puzzle     Filepath to the puzzle
     * @throws Exception Exception is thrown in case something is wrong with the stage
     */
    public Game(Stage stage, int boardSize, String puzzle) throws Exception {
        this.boardSize = boardSize;
        this.puzzle = puzzle;
        start(stage);
    }

    /**
     * Creates a new randomly generated Game.
     *
     * @param stage      Stage where the Game is
     * @param boardSize  Size of the Square Board NxN as N (from 2 to 8)
     * @param allowed    Pre-generated Allowed numbers
     * @param cells      Pre-generated Cells
     * @param cages      Pre-generated Cages
     * @param difficulty Difficulty level (1 to 3)
     * @throws Exception Exception is thrown in case something is wrong with the stage
     */
    public Game(Stage stage, int boardSize, ArrayList<String> allowed, Cell[] cells, ArrayList<Cage> cages, int difficulty) throws Exception {
        this.boardSize = boardSize;
        this.allowed = allowed;
        this.cells = cells;
        this.cages = cages;
        this.difficulty = difficulty;
        start(stage);
    }

    @Override
    public void start(Stage stage) throws Exception {
        /* ------- Visual Elements Setup ------- */

        //Board (Canvas) setup
        if (puzzle != null) {
            this.board = new Board(boardSize, this, puzzle);
        } else if (cages != null) {
            this.board = new Board(this, boardSize, allowed,cells,cages);
        } else {
            this.board = new Board(boardSize, this);
        }


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
        MenuItem newBoard = new MenuItem("Generate new board");
        MenuItem backToMenu = new MenuItem("Back to Menu");
        MenuItem preferences = new MenuItem("Options");
        MenuItem quit = new MenuItem("Quit");
        MenuItem howTo = new MenuItem("What is MathDoku?");
        MenuItem showSolution = new MenuItem("Show solution");
        file.getItems().addAll(newBoard, backToMenu, preferences, quit);
        help.getItems().addAll(showSolution, howTo);
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(file, help);

        //Creating Buttons
        Button showMistakes = new Button("Show Mistakes");
        Button clear = new Button("Clear");
        Button hint = new Button("Hint");

        //Creating side numpad buttons
        Button buttonNum1 = new Button("1");
        Button buttonNum2 = new Button("2");
        Button buttonNum3 = new Button("3");
        Button buttonNum4 = new Button("4");
        Button buttonNum5 = new Button("5");
        Button buttonNum6 = new Button("6");
        Button buttonNum7 = new Button("7");
        Button buttonNum8 = new Button("8");
        Button buttonNumX = new Button("X");

        //Setting up button sizes and alignment
        Button[] buttons = new Button[]{undo, redo, hint, showMistakes, clear,
                buttonNum1, buttonNum2, buttonNum3, buttonNum4,
                buttonNum5, buttonNum6, buttonNum7, buttonNum8, buttonNumX};
        for (Button button : buttons) {
            button.setAlignment(Pos.CENTER);
            button.setMaxWidth(Double.MAX_VALUE);
            HBox.setHgrow(button, Priority.ALWAYS);
            button.setFocusTraversable(false);
        }

        //Setting up default focus on Board(Canvas) to read keyboard inputs
        board.setFocusTraversable(true);
        board.requestFocus();

        //HBox "toolbar" creating & aligning
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(10);
        hBox.setPadding(new Insets(10, 10, 10, 10));
        hBox.getChildren().addAll(undo, redo, hint, showMistakes, clear);

        //VBox NumPad creating & aligning
        Button[] numPad = new Button[]{buttonNum1, buttonNum2, buttonNum3, buttonNum4,
                buttonNum5, buttonNum6, buttonNum7, buttonNum8};
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(10);
        vBox.setPadding(new Insets(10, 10, 10, 10));
        vBox.getChildren().addAll(buttonNum1, buttonNum2, buttonNum3,
                buttonNum4, buttonNum5, buttonNum6, buttonNum7, buttonNum8, buttonNumX);

        //Placing elements on the borderPane
        borderPane.setCenter(pane);
        borderPane.setTop(menuBar);
        borderPane.setBottom(hBox);
        borderPane.setRight(vBox);

        /* ------- Functionality Setup (Event Handlers) ------- */
        //Disables buttons, which can't be used according to the rules of the game
        //When buttons are pressed, the number is added to the Cell value
        for (int i = 0; i < numPad.length; i++) {
            if (i > boardSize - 1) {
                numPad[i].setDisable(true);
            }
            int finalI = i;
            numPad[i].setOnAction(actionEvent -> board.setCellValue(board.getChosenCell(), finalI + 1));
        }

        //When X button is pressed, clears the chosen Cell value;
        buttonNumX.setOnAction(actionEvent -> board.clearCellValue());

        //When back to menu is pressed opens Menu in the same stage
        backToMenu.setOnAction(actionEvent -> {
            Menu menu = new Menu();
            try {
                menu.start(stage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        //When Quit is pressed quits the game
        quit.setOnAction(actionEvent -> {
            Platform.exit();
            System.exit(0);
        });

        //Opens wikipedia page for KenKen if the "What is Mathdoku" is pressed
        howTo.setOnAction(actionEvent -> {
            getHostServices().showDocument("https://en.wikipedia.org/wiki/KenKen");
        });

        //Generates a new puzzle
        newBoard.setOnAction(actionEvent -> {
            try {
                Alert newGameAlert = new Alert(Alert.AlertType.CONFIRMATION,
                        "WARNING: Generating new game will erase all previous inputs!");
                Optional<ButtonType> result = newGameAlert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    new Generator(stage, boardSize, difficulty);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        //Solves the puzzle if the Show Solution is pressed
        showSolution.setOnAction(actionEvent -> board.showSolution());

        //When Clear is pressed pops up a window asking to confirm the action
        clear.setOnAction(actionEvent -> {
            Alert clearAlert = new Alert(Alert.AlertType.CONFIRMATION,
                    "Are you sure you want to clear the board?");
            Optional<ButtonType> result = clearAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                board.clear();
            }
        });

        //When Show Mistakes toggle is pressed starts highlighting mistakes red
        showMistakes.setOnAction(e -> {
            board.mistakes();
            if (!mistakesMode) {
                highlightButton(showMistakes, true);
            } else {
                highlightButton(showMistakes, false);
            }
            mistakesMode = !mistakesMode;
        });

        //When undo is pressed, cancels the last action
        undo.setOnAction(e -> board.undo());

        //When redo is pressed, return back the last undone action
        redo.setOnAction(e -> board.redo());

        //When Mouse is clicked on the Board, finds appropriate Cell and highlights it
        board.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            int x = (int) (mouseEvent.getX() / board.getCellWidth());
            int y = (int) (mouseEvent.getY() / board.getCellHeight());
            int[] chosenXY = new int[]{x, y};

            Cell cell;
            for (Cell listOfCell : board.getArrayOfCells()) {
                if (Arrays.equals(listOfCell.getCoordinates(), chosenXY)) {
                    cell = listOfCell;
                    board.chooseBox(cell);
                    break;
                }
            }
        });

        //When a key is pressed it's saved in a variable and the board is updated
        board.setOnKeyPressed(board::validateKeyboardInput);

        hint.setOnAction(actionEvent -> {
            board.showHint();
            board.update();
        });

        //When preferences button is pressed, opens the setting window
        preferences.setOnAction(actionEvent -> {
            //Creating the Stage
            Stage preferencesStage = new Stage();
            preferencesStage.setTitle("Load From Text Input");

            //Elements setup
            Label fontSizeLabel = new Label("Font Size: ");
            ObservableList<String> fontSizeOptions =
                    FXCollections.observableArrayList("Small", "Medium", "Large");
            ComboBox<String> fontSizeBox = new ComboBox<>(fontSizeOptions);
            Button done = new Button("Done");

            //Creating Panes
            VBox preferencesVBox = new VBox();
            HBox preferencesHBox = new HBox();

            //HBox alignment and padding
            preferencesHBox.setAlignment(Pos.CENTER);
            preferencesHBox.setSpacing(10);
            preferencesHBox.setPadding(new Insets(10, 10, 10, 10));

            //VBox alignment and padding
            preferencesVBox.setAlignment(Pos.CENTER);
            preferencesVBox.setSpacing(10);
            preferencesVBox.setPadding(new Insets(10, 10, 10, 10));

            //Adding elements to the panes
            preferencesHBox.getChildren().addAll(fontSizeLabel, fontSizeBox);
            preferencesVBox.getChildren().addAll(preferencesHBox, done);

            //When done is pressed, sets the font for the board and closes the preferences window
            done.setOnAction(actionEvent1 -> {
                board.setFont(fontSizeBox.getValue());
                preferencesStage.close();
            });

            //Stage setup
            preferencesStage.setResizable(false);
            preferencesStage.setScene(new Scene(preferencesVBox));
            preferencesStage.show();

        });

        //Stage setup
        stage.setMinHeight(435);
        stage.setMinWidth(400);
        stage.setResizable(true);
        stage.setTitle("MathDoku");
        stage.setScene(new Scene(borderPane, 400, 435));
        stage.show();
    }

    /**
     * Disables/Enables undo and redo buttons,
     * depending on stacks being empty/non-empty
     */
    public void disableUndoRedo() {
        if (board.isPossibleToUndo()) {
            undo.setDisable(false);
        } else {
            undo.setDisable(true);
        }
        if (board.isPossibleToRedo()) {
            redo.setDisable(false);
        } else {
            redo.setDisable(true);
        }
    }

    /**
     * Creates an outline for the given button.
     * @param button The button to be outlined
     * @param toColor The color for the outline
     */
    private void highlightButton(Button button, boolean toColor) {
        if (toColor) {
            button.setStyle("-fx-border-width: 1;"
                    + "-fx-border-color: black;"
                    + "-fx-border-radius: 3;");
        } else {
            button.setStyle(null);
        }
    }
}
