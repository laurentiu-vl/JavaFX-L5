package sample;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Question {

    //The text of the question
    private String question;
    //The question's answers
    private List<String> allAnswers;
    //The correct answer
    private String correctAns;

    //setter
    public Question(String question, String ans1, String ans2, String ans3, String correctAns){

        this.question = question;
        this.correctAns = correctAns;
        allAnswers = new ArrayList<>(Arrays.asList(ans1, ans2, ans3));
    }

    //getters
    public String getQuestion() {
        return question;
    }

    public String getCorrectAns() {
        return correctAns;
    }

    //getter for answers, but shuffled
    public List<String> getShuffledAnswers(){
        List<String> shuffledAnswers = new ArrayList<>(allAnswers);
        Collections.shuffle(shuffledAnswers);

        return shuffledAnswers;
    }
}