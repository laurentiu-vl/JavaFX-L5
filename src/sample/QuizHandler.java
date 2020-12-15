package sample;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class QuizHandler {

    private int correctAns = 0;
    private int incorrectAns = 0;

    public int getCorrectAnswers() {
        return correctAns;
    }

    public int getIncorrectAnswers() {
        return incorrectAns;
    }

    private boolean timeOver = false;

    private Timer timer;

    private List<Question> questions = new ArrayList<>();

    public List<Question> getQuestions() {
        return questions;
    }

    public boolean isQuizStarted() {
        return quizStarted;
    }

    private boolean quizStarted = false;

    private int currentQuestion = 0;



    public void run(){ //start the quiz
        quizStarted = true;
        currentQuestion = 0;

        if(timer == null){
            timer = new Timer(this);
            timer.start();
        }

        getQuestionsFromFile(); //get questions from .txt

        Collections.shuffle(questions); //random questions

    }

    public void restart(){
        if(timer != null){

            //restart the timer
            timer.stopTimer();
            timer = new Timer(this);
            timer.start();
        }
        timeOver = false;
        currentQuestion = 0;
        correctAns = 0;
        incorrectAns = 0;
        Collections.shuffle(questions);
    }

    private void getQuestionsFromFile(){
        List<String> QuestionsText = new ArrayList();

        try {
            File myObj = new File("src/questionData");
            Scanner myReader = new Scanner(myObj);
            int i = 1;
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                QuestionsText .add(data);
                if (i%5==0)
                {
                    questions.add(new Question(QuestionsText .get(i-5), QuestionsText .get(i-4), QuestionsText .get(i-3), QuestionsText .get(i-2), QuestionsText .get(i-1)));
                }
                i++;
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public boolean timedOut() {
        return timeOver;
    }

    //Stops the quiz
    public void setOver() { //stops the quiz
        this.timeOver = true;
        timer.stopTimer();
        this.quizStarted = false;
    }

    public boolean checkQuizRunMore (Question question, String answer){
        //check if the answers are incorrect or correct
        if(question.getCorrectAns().equals(answer)){
            correctAns = correctAns + 1;
        }
        else{
            incorrectAns = incorrectAns + 1;
        }
        if(incorrectAns >= 5){
            //stops the quiz if nr. of inc answers are >=5
            setOver();
            return false;
        }
        return true;
    }

    public Question getNextQuestion(){

        if(!timeOver){
            if(currentQuestion < questions.size()){

                //get the nextQ
                Question nextQuestion = questions.get(currentQuestion);
                currentQuestion += 1;
                return nextQuestion;
            }
        }
        return null;
    }
}