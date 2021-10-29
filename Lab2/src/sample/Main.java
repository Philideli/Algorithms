package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.IndexFileStructure;

import java.io.IOException;
import java.util.Random;

import sample.Controller.*;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Lab2");
        primaryStage.setScene(new Scene(root, 700, 420));
        primaryStage.show();
    }

    public static void main(String[] args) throws IOException {
        launch(args);

    }

}
