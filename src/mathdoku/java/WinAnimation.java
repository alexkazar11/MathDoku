package mathdoku.java;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * A window, which is called once the user completed the puzzle correctly.
 */
public class WinAnimation extends Application {
    @Override
    public void start(Stage stage) {
        Text text = new Text("CONGRATULATIONS!");
        text.setFont(Font.font(36));
        Separator separator = new Separator();
        Label description = new Label("You won the game, neat! Try another one!");
        Button ok = new Button("OK");

        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.setSpacing(10);
        root.setPadding(new Insets(10, 10, 10, 10));
        root.setPrefSize(500, 100);
        root.getChildren().addAll(text, separator, description, ok);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("You won!");
        stage.show();

        //Animation settings
        // Get the Width of the Scene and the Text
        double sceneWidth = scene.getWidth();
        double textWidth = text.getLayoutBounds().getWidth();

        // Define the Durations
        Duration startDuration = Duration.ZERO;
        Duration endDuration = Duration.seconds(1);

        // Create the start and end Key Frames
        KeyValue startKeyValue = new KeyValue(text.translateXProperty(), sceneWidth - textWidth - 100);
        KeyFrame startKeyFrame = new KeyFrame(startDuration, startKeyValue);
        KeyValue endKeyValue = new KeyValue(text.translateXProperty(), -50.0);
        KeyFrame endKeyFrame = new KeyFrame(endDuration, endKeyValue);

        //Setup the timeline
        Timeline timeline = new Timeline(startKeyFrame, endKeyFrame);
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.setAutoReverse(true);
        timeline.play();

        ok.setOnAction(actionEvent -> stage.close());
    }
}