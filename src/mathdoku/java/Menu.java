package mathdoku.java;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;


/**
 * Menu allows user to start a new game of MathDoku with certain settings, such as difficulty level and a grid size.
 * Menu also allows user to load an old game, if a text file exists.
 */
public class Menu extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        //Fonts
        Font headingFont = new Font(52);
        Font labelFont = new Font(26);
        Font elementFont = new Font(18);

        //Title for the page
        Label title = new Label("MATHDOKU");
        title.setFont(headingFont);

        //Label for Difficulty Level
        Label difficultyLabel = new Label("Difficulty: ");
        difficultyLabel.setFont(labelFont);

        //Label for Board Size
        Label boardSizeLabel = new Label("Board size: ");
        boardSizeLabel.setFont(labelFont);

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
        file.getItems().addAll(loadFromFile, loadFromInput);
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(file, help);

        //Horizontal separator
        Separator separatorTop = new Separator();
        Separator separatorBottom = new Separator();

        //Inside VBox setup (contains the title and the gridPane)
        VBox insideVBox = new VBox(menuBar, separatorTop, title, gridPane, separatorBottom);
        insideVBox.setAlignment(Pos.CENTER);
        insideVBox.setSpacing(10);

        Label madeByLabel = new Label("Made by Alex Kazaryan in 2020");

        //Outside VBox setup (contains the menubar and the insideVBox)
        VBox outsideVBox = new VBox(menuBar, insideVBox, madeByLabel);
        outsideVBox.setAlignment(Pos.CENTER);
        outsideVBox.setSpacing(70);
        outsideVBox.setPadding(new Insets(0, 0, 20, 0));

        //Stage setup
        stage.setResizable(false);
        stage.setTitle("MathDoku");
        stage.setScene(new Scene(outsideVBox, 400, 435));
        stage.show();
    }
}