package sample;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import java.net.URL;
import java.net.URI;
import java.io.File;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        /*
        String fxmlpath = "D:\\Faculta\\Anul2\\Sem1\\MAP\\L5test\\src\\sample\\sample.fxml";

        Parent root = FXMLLoader.load(Paths.get(fxmlpath).toUri().toURL());

        QuizHandler qHandler = new QuizHandler();

        Controller fxController = FXMLLoader.load(Paths.get(fxmlpath).toUri().toURL()).getController();

        ======================
        getClass().getResource("view.fxml")
         */
        FXMLLoader loader = new FXMLLoader();

        File f1 = new File("D:/Faculta/Anul2/Sem1/MAP/L5test/src/sample/sample.fxml");

        System.out.println(f1.toURI().toURL());
        System.out.println(getClass().getResource("sample.fxml"));

        loader.setLocation(getClass().getResource("sample.fxml"));

        //loader.setLocation(new URL("file:///D:/Faculta/Anul2/Sem1/MAP/L5test/src/sample/sample.fxml"));
        //loader.load(f1.toURI().toURL());

        QuizHandler qHandler = new QuizHandler();

        Parent root = loader.load();

        Controller fxController = loader.getController(); //Cache the controller

        fxController.setHandler(qHandler);

        primaryStage.setTitle("Quiz Auto");

        primaryStage.setScene(new Scene(root, 900, 700));

        primaryStage.show();
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}