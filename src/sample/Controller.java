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

    public void setHandler(QuizHandler handler) {
        this.handler = handler;
    }

    //overriding the base initalize method of Initializable
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        nextQuestion.setDisable(true); //start quiz with the questions unloaded --poate merge si fara
    }

    public void startQuiz(){

        if(handler == null){
            Alert alert = new Alert(Alert.AlertType.ERROR, "The program Handler could not be found!");
            alert.setTitle("HandlerNotFoundException");
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
        loadNextQuestion(); //load the next question and show c/i answers
        correctAnswers.setText(String.valueOf(handler.getCorrectAnswers()));
        incorrectAnswers.setText(String.valueOf(handler.getIncorrectAnswers()));

    }

    //Starts the timer animation
    private void startTimerAnimation(){
        if(timeline != null){
            timeline.stop();
        }
        //Calculate the the time when the quiz will end
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

    public void buttonNextQuestion(){
        //Check if the quiz is running
        if(!handler.isQuizStarted()){
            return;
        }
        //Get the selection
        RadioButton user_option = (RadioButton) questionOptions.getSelectedToggle(); //select opt
        //Check if the answer is correct and log it into the handler
        if(user_option==null){
            Alert noOption = new Alert(Alert.AlertType.ERROR, "No Option selected!!\nPlease select 1 option!!");
            noOption.setTitle("NO Option!!");
            noOption.showAndWait();
        }
        else{
            boolean checkQuizOver = handler.checkQuizRunMore(currentQuestion, user_option.getText()); //check the answer and then nr. of c/i answer
            //Update the answer labels
            correctAnswers.setText(String.valueOf(handler.getCorrectAnswers()));
            incorrectAnswers.setText(String.valueOf(handler.getIncorrectAnswers()));
            //End the quiz if there >= incorect answers
            if (!checkQuizOver) {
                endQuiz();
            }

            //Remove the selection from the radio buttons
            option1.setSelected(false);
            option2.setSelected(false);
            option3.setSelected(false);

            loadNextQuestion();
        }
    }

    //load next q into the GUI
    private void loadNextQuestion(){
        currentQuestion = handler.getNextQuestion(); //get next q from Handler
        //if there are no more questions end the quiz
        if(currentQuestion == null){
            endQuiz();
            return;
        }

        questionText.setText(currentQuestion.getQuestion()); //load q in textbox

        List<String> options = currentQuestion.getShuffledAnswers();//get random opt

        //load options into the buttons
        option1.setText(options.get(0));
        option2.setText(options.get(1));
        option3.setText(options.get(2));
    }

    private void endQuiz(){
        nextQuestion.setDisable(true);
        Alert quizOver;

        //Quiz ending conditions
        if(handler.getIncorrectAnswers() >= 5){
            quizOver = new Alert(Alert.AlertType.ERROR, "Quiz failed! Too many incorrect answers");
        }
        else if(handler.getCorrectAnswers() >=22){
            quizOver = new Alert(Alert.AlertType.INFORMATION, "Quiz passed! Congratulations!");
        }
        else if (handler.timedOut()) {
            if(handler.getCorrectAnswers() >=22)
                quizOver = new Alert(Alert.AlertType.INFORMATION, "The time is up! Quiz passed! Congratulations!");
            else
                quizOver = new Alert(Alert.AlertType.ERROR, "The time is up! Quiz failed!");
        }
        else{
            quizOver = new Alert(Alert.AlertType.INFORMATION, "Quiz passed! Congratulations!");
        }
        quizOver.setTitle("Quiz over!");
        quizOver.showAndWait();
    }
}
