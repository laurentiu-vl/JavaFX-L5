package sample;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class QuizHandler {

    private int correctAns = 0;
    private int incorrectAns = 0;

    private boolean timeOver = false;
    private Timer timer;
    private boolean quizStarted = false;
    private List<Question> questions = new ArrayList<>();
    private int currentQuestion = 0;

    //getters
    public int getCorrectAnswers() {
        return correctAns;
    }
    public int getIncorrectAnswers() {
        return incorrectAns;
    }
    public boolean isQuizStarted() {
        return quizStarted;
    }
    public boolean timedOut() {
        return timeOver;
    }
    public List<Question> getQuestions() {
        return questions;
    }

    //start the quiz
    public void run(){
        quizStarted = true;
        currentQuestion = 0;

        if(timer == null){
            timer = new Timer(this);
            timer.start();
        }
        //read from the .txt file
        getQuestionsFromFile();
        //shuffle read questions
        Collections.shuffle(questions);
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

    //read the question and answers from .txt file
    private void getQuestionsFromFile(){
        String s1 = "",s2 = "",s3 = "",s4 = "",s5 = "";
        int nr_questions = 0;
        try {
            File myObj = new File("src/data");
            Scanner myReader = new Scanner(myObj);
            int i = 1;
            while (myReader.hasNextLine()) {
                if (i%5==1){
                    s1 = myReader.nextLine();
                }
                else if (i%5==2){
                    s2 = myReader.nextLine();
                }
                else if (i%5==3){
                    s3 = myReader.nextLine();
                }
                else if (i%5==4){
                    s4 = myReader.nextLine();
                }
                else if (i%5==0){
                    s5 = myReader.nextLine();
                    if (nr_questions < 25)
                        questions.add(new Question(s1, s2, s3, s4, s5));
                        nr_questions = nr_questions + 1;
                }
                i++;
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    //stops the quiz
    public void setOver() {
        this.timeOver = true;
        timer.stopTimer();
        this.quizStarted = false;
    }

    /**
     *
     * @param question
     * @param answer
     * @return true if
     * @return false if incorectAnswer are over >= 5
     */
    public boolean checkQuizRunMore (Question question, String answer){
        //check if the answers are incorrect or correct
        if(question.getCorrectAns().equals(answer)){
            correctAns = correctAns + 1;
        }
        else{
            incorrectAns = incorrectAns + 1;
        }
        //stops the quiz if nr. of inc answers are >=5
        if(incorrectAns >= 5){
            setOver();
            return false;
        }

        return true;
    }

    //for iteration through the questions
    public Question getNextQuestion(){
        if(!timeOver){
            if(currentQuestion < getQuestions().size()){
                //get the next question
                Question nextQuestion = getQuestions().get(currentQuestion);
                currentQuestion += 1;
                return nextQuestion;
            }
        }
        return null;
    }
}