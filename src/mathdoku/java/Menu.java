package mathdoku.java;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;


/**
 * Menu allows user to start a new game of MathDoku with certain settings, such as difficulty level and a grid size.
 * Menu also allows user to load an old game, if a text file exists.
 */
public class Menu extends Application {
    private int boardSize = 0;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        //Fonts
        Font headingFont = new Font(52);
        Font labelFont = new Font(26);
        Font elementFont = new Font(18);

        /* ------- Visual Elements Setup ------- */

        //Title for the page
        Label title = new Label("MATHDOKU");
        title.setFont(headingFont);

        //Label for Difficulty Level
        Label difficultyLabel = new Label("Difficulty: ");
        difficultyLabel.setFont(labelFont);

        //Label for Board Size
        Label boardSizeLabel = new Label("Board size: ");
        boardSizeLabel.setFont(labelFont);

        //Bottom label
        Label madeByLabel = new Label("Made by Alex Kazaryan in 2020");

        //Buttons
        Button start = new Button("Start");
        start.setFont(elementFont);
        start.setMaxWidth(100);
        Button showRules = new Button("Show rules");
        showRules.setFont(elementFont);
        showRules.setMaxWidth(120);

        //ComboBox for Difficulty Level
        ObservableList<String> difficultyOptions =
                FXCollections.observableArrayList("Easy", "Medium", "Hard");
        ComboBox<String> difficultyBox = new ComboBox<>(difficultyOptions);
        difficultyBox.setMaxWidth(100);
        difficultyBox.getEditor().setFont(elementFont);

        //ComboBox for Board Size
        ObservableList<String> boardSizeOptions =
                FXCollections.observableArrayList("2x2", "3x3", "4x4", "5x5", "6x6", "7x7", "8x8");
        ComboBox<String> boardSizeBox = new ComboBox<>(boardSizeOptions);
        boardSizeBox.setMaxWidth(100);
        boardSizeBox.getEditor().setFont(elementFont);

        //GridPane setup
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setVgap(5);
        gridPane.setHgap(5);
        gridPane.setAlignment(Pos.CENTER);

        //Arranging all the nodes in the grid
        gridPane.add(title, 0, 0);
        gridPane.add(boardSizeLabel, 0, 1);
        gridPane.add(boardSizeBox, 1, 1);
        gridPane.add(difficultyLabel, 0, 2);
        gridPane.add(difficultyBox, 1, 2);
        gridPane.add(showRules, 0, 3);
        gridPane.add(start, 1, 3);

        //Menubar (have to specify javaFX path, since the class is also called Menu
        javafx.scene.control.Menu file = new javafx.scene.control.Menu("File");
        javafx.scene.control.Menu help = new javafx.scene.control.Menu("Help");
        MenuItem loadFromFile = new MenuItem("Load from file");
        MenuItem loadFromInput = new MenuItem("Load from text input");
        MenuItem quit = new MenuItem("Quit");
        MenuItem howTo = new MenuItem("What is MathDoku?");
        file.getItems().addAll(loadFromFile, loadFromInput, quit);
        help.getItems().addAll(howTo);
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(file, help);

        //Horizontal separator
        Separator separatorTop = new Separator();
        Separator separatorBottom = new Separator();

        //Inside VBox setup (contains the title and the gridPane)
        VBox insideVBox = new VBox(menuBar, separatorTop, title, gridPane, separatorBottom);
        insideVBox.setAlignment(Pos.CENTER);
        insideVBox.setSpacing(10);

        //Outside VBox setup (contains the menubar and the insideVBox)
        VBox outsideVBox = new VBox(menuBar, insideVBox, madeByLabel);
        outsideVBox.setAlignment(Pos.CENTER);
        outsideVBox.setSpacing(70);
        outsideVBox.setPadding(new Insets(0, 0, 20, 0));

        /* ------- Functionality Setup (Event Handlers)  ------- */

        //When the Start button is pressed, opens Game in the same stage
        start.setOnAction(actionEvent -> {
            int difficulty = 0;

            //Gets the values chosen by the user from the ComboBoxes
            boardSize = getBoardSize(boardSizeBox);
            difficulty = getDifficulty(difficultyBox);

            //Checks whether both ComboBoxes have something chosen
            if (boardSize != 0 && difficulty != 0) {
                try {
                    //Starts the Game with the chosen difficulty and boarder size
                    new Generator(stage, boardSize, difficulty);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                //If the ComboBoxes are empty - the error message is shown
                errorMsg("You have to choose one of the options for the board size and difficulty level!");
            }

        });

        showRules.setOnAction(actionEvent -> {
            Stage rulesStage = new Stage();
            rulesStage.setTitle("Rules of the game");

            VBox vBox = new VBox();
            vBox.setAlignment(Pos.CENTER);
            vBox.setSpacing(10);
            vBox.setPadding(new Insets(20, 20, 20, 20));

            Separator separator1 = new Separator();
            Separator separator2 = new Separator();

            Label rules = new Label("RULES");
            rules.setFont(Font.font(30));
            Label rule1 = new Label("1. Digits may appear only once in each row and column");
            rule1.setFont(Font.font(16));
            Label rule2 = new Label("2.The grids are divided in cages with target numbers and operators");
            rule2.setFont(Font.font(16));
            Label rule3 = new Label("3. Find a mathematical solution using the operator and target");
            rule3.setFont(Font.font(16));
            Label rule4 = new Label("4. Know the number combinations");
            rule4.setFont(Font.font(16));
            Label rule5 = new Label("5. Each puzzle has only 1 solution");
            rule5.setFont(Font.font(16));

            Button gotIt = new Button("Got it!");

            gotIt.setOnAction(actionEvent1 -> rulesStage.close());

            vBox.getChildren().addAll(rules, separator1, rule1, rule2, rule3, rule4, rule5, separator2, gotIt);

            Scene scene = new Scene(vBox);
            rulesStage.setScene(scene);
            rulesStage.show();

        });

        //Opens wikipedia page for KenKen if the "What is Mathdoku" is pressed
        howTo.setOnAction(actionEvent -> {
            getHostServices().showDocument("https://en.wikipedia.org/wiki/KenKen");
        });

        //When Quit is pressed quits the game
        quit.setOnAction(actionEvent -> {
            Platform.exit();
            System.exit(0);
        });

        //When load from the file is pressed, opens up a file chooser window
        loadFromFile.setOnAction(actionEvent -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialDirectory(new File("src/mathdoku/resources/puzzles"));
            File puzzleFile = fileChooser.showOpenDialog(stage);
            if (puzzleFile != null) {
                try {
                    if (checkFile(puzzleFile.getPath())) {
                        new Game(stage, boardSize, puzzleFile.getPath());
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        //When load from the text input is pressed, opens up a new window with a TextArea
        loadFromInput.setOnAction(actionEvent -> {
            Stage loadFromFileStage = new Stage();
            loadFromFileStage.setTitle("Load From Text Input");
            TextArea textArea = new TextArea();
            Button submit = new Button("Submit");

            submit.setOnAction(e -> {
                try {
                    String pathToPuzzle = "src/mathdoku/resources/puzzles/puzzleFromInput.txt";
                    FileWriter w = new FileWriter(pathToPuzzle);
                    w.write(textArea.getText());
                    w.close();
                    if (checkFile(pathToPuzzle)) {
                        new Game(stage, boardSize, pathToPuzzle);
                        loadFromFileStage.close();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });

            BorderPane borderPane = new BorderPane();
            borderPane.setCenter(textArea);

            HBox hBox = new HBox(submit);
            hBox.setAlignment(Pos.CENTER);
            hBox.setPadding(new Insets(5, 50, 5, 50));
            borderPane.setBottom(hBox);
            Scene scene = new Scene(borderPane, 300, 250);
            loadFromFileStage.setScene(scene);
            loadFromFileStage.show();
        });

        //Stage setup
        stage.setResizable(false);
        stage.setTitle("MathDoku");
        stage.setScene(new Scene(outsideVBox, 400, 435));
        stage.show();
    }

    /**
     * Traverses a puzzle file line by line to check the formatting.
     *
     * @param filename Path to the puzzle file
     * @return true - file is correctly formatted, false - otherwise
     * @throws IOException Exception is thrown if there is a problem with the file
     */
    private boolean checkFile(String filename) throws IOException {
        try (FileInputStream fileInputStream = new FileInputStream(filename);
             InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {

            //An ArrayList to store all CellIDs in the file
            ArrayList<Integer> allCellIDs = new ArrayList<>();

            //Stores the total number of lines in the txt file
            int numberOfLines = 0;

            //Stores the largest CellID in the ArrayList
            int largestID = 0;

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                //Increments a number of lines
                numberOfLines++;

                //Trimming the line from leading and trailing whitespaces
                String trimmedLine = line.trim();

                //Target is a substring until the first whitespace
                String target = trimmedLine.substring(0, trimmedLine.indexOf(' '));

                //CellIDs are everything after the first whitespace
                String cellIDs = trimmedLine.substring(trimmedLine.indexOf(' ') + 1);

                //Creating an array of CellIDs, also trimming any extra whitespaces
                String[] cellIDsArray = cellIDs.trim().split("\\s*,\\s*");

                //Checks that Cages without an arithmetic operator only have one Cell ID
                if (target.length() == 1 && cellIDsArray.length > 1) {
                    errorMsg("Cages without an arithmetic operator must only have one Cell ID");
                    return false;
                }

                //Adding all CellIDs from current line to an ArrayList of all CellIDs
                for (String cellID : cellIDsArray) {
                    allCellIDs.add(Integer.parseInt(cellID));
                    System.out.println(cellID);
                }
            }

            //Checking an array of Cell Ids for duplicates
            if (Toolbox.findDuplicates(allCellIDs)) {
                errorMsg("You can't have the same Cell being assigned to more than one Cage!");
                return false;
            }

            //Finds the largest CellID in the file
            for (int cellID : allCellIDs) {
                if (cellID > largestID) {
                    largestID = cellID;
                }
            }

            //The number of cells must be equal to the largest CellID
            if (allCellIDs.size() != largestID) {
                errorMsg("Some Cell IDs might be missing, please check the formatting guide!");
                return false;
            }

            //Calculates the Board size for the Game
            this.boardSize = (int) Math.sqrt(largestID);
            if (Math.pow(boardSize, 2) != largestID) {
                errorMsg("The largest Cell ID is not a perfect square!");
                return false;
            }

            //Checks the number of lines in the file
            if (numberOfLines == 0) {
                errorMsg("You can't have an empty input!");
                return false;
            } else if (numberOfLines > 64) {
                errorMsg("Too many lines, please check the formatting guide!");
            }

        } catch (Exception e) {
            errorMsg("There is a problem with your input, please check the formatting guide!");
            return false;
        }
        return true;
    }

    /**
     * Pops up an error message with a given description.
     *
     * @param msg Description of the error
     */
    private void errorMsg(String msg) {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText("Input not valid");
        errorAlert.setContentText(msg);
        errorAlert.showAndWait();
    }

    /**
     * Transforms the difficulty chosen by the user from String to int.
     * null -> 0
     * "Easy" -> 1
     * "Medium" -> 2
     * "Hard" -> 3
     *
     * @param difficultyBox The ComboBox where the difficulty is chosen
     * @return The difficulty level as an integer
     */
    private int getDifficulty(ComboBox<String> difficultyBox) {
        String difficultyValue = difficultyBox.getValue();
        int difficulty = 0;

        if (difficultyValue == null) {
            return difficulty;
        }

        switch (difficultyValue) {
            case "Easy":
                difficulty = 1;
                break;
            case "Medium":
                difficulty = 2;
                break;
            case "Hard":
                difficulty = 3;
                break;
        }
        return difficulty;
    }

    /**
     * Transforms the difficulty chosen by the user from String to int.
     * null -> 0
     * "2x2" -> 2
     * "3x3" -> 3
     * "4x4" -> 4
     * "5x5" -> 5
     * "6x6" -> 6
     * "7x7" -> 7
     * "8x8" -> 8
     *
     * @param boardSizeBox The ComboBox where the board size is chosen
     * @return The board size as an integer
     */
    private int getBoardSize(ComboBox<String> boardSizeBox) {
        String boardSizeValue = boardSizeBox.getValue();
        int boardSize = 0;

        if (boardSizeValue == null) {
            return boardSize;
        }

        switch (boardSizeValue) {
            case "2x2":
                boardSize = 2;
                break;
            case "3x3":
                boardSize = 3;
                break;
            case "4x4":
                boardSize = 4;
                break;
            case "5x5":
                boardSize = 5;
                break;
            case "6x6":
                boardSize = 6;
                break;
            case "7x7":
                boardSize = 7;
                break;
            case "8x8":
                boardSize = 8;
                break;
        }
        return boardSize;
    }

}
