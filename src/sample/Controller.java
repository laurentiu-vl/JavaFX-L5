package sample;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Duration;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    public Label questionText;
    @FXML
    public RadioButton option1;
    @FXML
    public RadioButton option2;
    @FXML
    public RadioButton option3;
    @FXML
    public Button startButton;
    @FXML
    public Button nextQuestion;
    @FXML
    public Label correctAnswers;
    @FXML
    public Label incorrectAnswers;
    @FXML
    public ToggleGroup questionOptions;
    @FXML
    public Label timeLeft;

    private DateFormat timeFormat = new SimpleDateFormat("mm:ss");
    private Timeline timeline;
    private long timeDurationMillis = 30*60*1000;
    private long endTime;

    private QuizHandler handler;

    private Question currentQuestion = null; //the current question being displayed

    public void setManager(QuizHandler manager) {
        this.handler = manager;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        nextQuestion.setDisable(true); //start quiz with the questions unloaded
    }

    public void startQuiz(){

        if(handler == null){
            Alert alert = new Alert(Alert.AlertType.ERROR, "The program manager could not be found!");
            alert.setTitle("ManagerNotFoundException");
            alert.showAndWait();
            return;
        }

        if(!handler.isQuizStarted()){ //check if quiz is started; if no, start it
            handler.run();
            startTimerAnimation();

        }
        else{           //else restart quiz
            handler.restart();
            startTimerAnimation();

        }
        nextQuestion.setDisable(false);
        loadNextQuestion(); //load the next q and show c/i ans
        correctAnswers.setText(String.valueOf(handler.getCorrectAnswers()));
        incorrectAnswers.setText(String.valueOf(handler.getIncorrectAnswers()));

    }



    private void startTimerAnimation(){ //timer function
        if(timeline != null){
            timeline.stop();
        }

        endTime = System.currentTimeMillis() + timeDurationMillis; //time when quiz will finish

        timeline = new Timeline(new KeyFrame( Duration.millis(500), event -> {
            long diff = endTime - System.currentTimeMillis();
            if(diff < 0){
                timeLeft.setText(timeFormat.format(0));
            }
            else{
                timeLeft.setText(timeFormat.format(diff));
            }
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    //Accepts the user input and calls loadNextQuestion()
    public void buttonNextQuestion(){

        if(!handler.isQuizStarted()){ //chech is quiz is out
            return;
        }

        RadioButton selected = (RadioButton) questionOptions.getSelectedToggle(); //select opt

        boolean checkQuizOver = handler.checkQuizRunMore(currentQuestion, selected.getText()); //check the answer and then nr. of c/i answer
        correctAnswers.setText(String.valueOf(handler.getCorrectAnswers()));
        incorrectAnswers.setText(String.valueOf(handler.getIncorrectAnswers()));

        if( checkQuizOver == false){

            endQuiz();
        }

        option1.setSelected(false);  //remove buttons, and load next q
        option2.setSelected(false);
        option3.setSelected(false);

        loadNextQuestion();

    }

    //Loads the next question into the GUI
    private void loadNextQuestion(){
        currentQuestion = handler.getNextQuestion(); //get next q from Handler

        if(currentQuestion == null){

            endQuiz();
            return;
        }

        questionText.setText(currentQuestion.getText()); //load q in textb

        List<String> options = currentQuestion.getShuffledAnswers(); //get random opt

        option1.setText(options.get(0));
        option2.setText(options.get(1));
        option3.setText(options.get(2));
    }

    private void endQuiz(){
        nextQuestion.setDisable(true);
        Alert quizOver;

        if(handler.getIncorrectAnswers() >= 5){
            quizOver = new Alert(Alert.AlertType.ERROR, "Quiz failed! Too many incorrect answers");
        }
        else if (handler.timedOut()) {
            quizOver = new Alert(Alert.AlertType.ERROR, "Quiz failed! The time is up!");
        }
        else{
            quizOver = new Alert(Alert.AlertType.INFORMATION, "Quiz passed! Congratulations!");
        }

        quizOver.setTitle("Quiz over!");
        quizOver.showAndWait();
    }
}
