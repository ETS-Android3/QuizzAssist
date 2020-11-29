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
    String questionID;
    List<String> answerList = new ArrayList<>();
    List<String> enrolledUsers = new ArrayList<>();
    String userCreated;
    String courseLink;
    //link to image?

    //type of question
//    boolean multipleChoice;
//    boolean shortAnswer;
    //else assume answer is drawn

    //answer key
//    String answerKeyText;
    //link to answer key image
//    char answerKeyMultipleChoice;

    //store all submitted answers to question
//    List<Answer> answerList = new ArrayList<Answer>();

    //grading data
//    boolean finishedGrading;
//    int maxGrade;


    public Question(){
        //required empty constructor
    }

    public Question(String questionText, String courseLink, String userCreated) {
        this.questionText = questionText;
        this.courseLink = courseLink;
        this.userCreated = userCreated;
        this.enrolledUsers = new ArrayList<>();
        enrolledUsers.add(userCreated);
    }


    public String getCourseLink() {
        return courseLink;
    }

    public void setCourseLink(String courseLink) {
        this.courseLink = courseLink;
    }

    public void setQuestionText(String questionInputText) { this.questionText = questionInputText; }

    public String getQuestionText() { return this.questionText; }

    //this save function assumes an answer data type is already created
    //in future, may be easier create and save answer to do it in a single function
    //for now I'm not sure how we're saving data from the canvas
//    public void saveAnswer(Answer answer , int studentIndex) { this.answerList.set(studentIndex , answer); }

    public String getQuestionID() {
        return questionID;
    }

    public void setQuestionID(String questionID) {
        this.questionID = questionID;
    }

    public List<String> getAnswerList() {
        return answerList;
    }

    public void setAnswerList(List<String> answerList) {
        this.answerList = answerList;
    }

    public List<String> getEnrolledUsers() {
        return enrolledUsers;
    }

    public void setEnrolledUsers(List<String> enrolledUsers) {
        this.enrolledUsers = enrolledUsers;
    }

    public String getUserCreated() {
        return userCreated;
    }

    public void setUserCreated(String userCreated) {
        this.userCreated = userCreated;
    }
}

