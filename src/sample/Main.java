package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        FXMLLoader loader = new FXMLLoader(getClass().getResource("view.fxml"));

        QuizHandler qHandler = new QuizHandler();

        Parent root = loader.load();

        Controller fxController = loader.getController();

        fxController.setHandler(qHandler);

        primaryStage.setTitle("Quiz Auto");

        primaryStage.setScene(new Scene(root, 800, 670));

        primaryStage.show();
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}