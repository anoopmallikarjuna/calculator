package calculator;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/calc.fxml"));
        primaryStage.setTitle("Calculator (JavaFX APP)");
        primaryStage.setScene(new Scene(root,435,560));


        primaryStage.show();

    }

    public static void main(String[] args) {launch(args);}
}
