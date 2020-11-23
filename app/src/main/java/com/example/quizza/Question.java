/***
 * Course.java
 * Developers: Brandon Yip, Vatsal Parmar
 * CMPT 276 Team 'ForTheStudents'
 * This class has been newly created to manage the courses that users of application create.
 * No known bugs.
 */

package com.example.quizza;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class Question {
    //question data to display
    String questionText;
    //link to image?

    //type of question
    boolean multipleChoice;
    boolean shortAnswer;
    //else assume answer is drawn

    //answer key
    String answerKeyText;
    //link to answer key image
    char answerKeyMultipleChoice;

    //store all submitted answers to question
    List<Answer> answerList = new ArrayList<Answer>();

    //grading data
    boolean finishedGrading;
    int maxGrade;


    public Question(){
        //required empty constructor
    }

    public void setQuestionText(String questionInputText) { this.questionText = questionInputText; }

    public String getQuestionText() { return this.questionText; }

    public boolean isGraded() { return this.finishedGrading; }

    //this save function assumes an answer data type is already created
    //in future, may be easier create and save answer to do it in a single function
    //for now I'm not sure how we're saving data from the canvas
    public void saveAnswer(Answer answer , int studentIndex) { this.answerList.set(studentIndex , answer); }
}

