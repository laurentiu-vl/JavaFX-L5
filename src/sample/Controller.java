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

        nextQuestion.setDisable(true); //start with the questions unloaded
    }

    public void startQuiz(){

        if(handler == null){
            Alert alert = new Alert(Alert.AlertType.ERROR, "The program manager could not be found!");
            alert.setTitle("ManagerNotFoundException");
            alert.showAndWait();
            return;
        }
        //Check if the quiz has already been started; if it has, restart it
        if(!handler.isQuizStarted()){
            //Quiz isn't started
            //Start the quiz
            handler.run();
            //Start the timer animation
            startTimerAnimation();

        }
        else{
            //Quiz is started
            //Restart the quiz
            handler.restart();
            //restart timer animation
            startTimerAnimation();

        }
        nextQuestion.setDisable(false);
        loadNextQuestion();
        //Load the labels with the data
        correctAnswers.setText(String.valueOf(handler.getCorrectAnswers()));
        incorrectAnswers.setText(String.valueOf(handler.getIncorrectAnswers()));

    }


    //Starts the timer animation
    private void startTimerAnimation(){
        if(timeline != null){
            timeline.stop();
        }
        //Find the end time (the time when the quiz will end
        endTime = System.currentTimeMillis() + timeDurationMillis;

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
        //Check if the quiz is running
        if(!handler.isQuizStarted()){
            return;
        }


        //Get the selection
        RadioButton selected = (RadioButton) questionOptions.getSelectedToggle();
        //Check if the answer is correct and log it into the manager
        boolean checkQuizOver = handler.checkQuizRunMore(currentQuestion, selected.getText());
        //Update the answer labels
        correctAnswers.setText(String.valueOf(handler.getCorrectAnswers()));
        incorrectAnswers.setText(String.valueOf(handler.getIncorrectAnswers()));
        //End the quiz if there are too many wrong answers
        if( checkQuizOver == false){

            endQuiz();
        }

        //Remove the selection from the radio buttons
        option1.setSelected(false);
        option2.setSelected(false);
        option3.setSelected(false);

        loadNextQuestion();

    }

    //Loads the next question into the GUI
    private void loadNextQuestion(){
        currentQuestion = handler.getNextQuestion();

        if(currentQuestion == null){
            //The quiz is over
            endQuiz();
            return;
        }

        //Load the question's data into the text boxes
        questionText.setText(currentQuestion.getText());
        //Get the shuffled answers
        List<String> options = currentQuestion.getShuffledAnswers();
        //Load them into the buttons
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
