/***
    write your class description here for whoever wrote it
 */

package com.example.quizza;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Answer {

    String answerURLS;
    String questionID;
    String userSubmitted;
    String eventLink;


    public Answer() {
        //required empty constructor
    }

    public Answer(String answerURLS, String questionID, String userSubmitted, String eventLink) {
        this.answerURLS = answerURLS;
        this.questionID = questionID;
        this.userSubmitted = userSubmitted;
        this.eventLink = eventLink;
    }

    public String getAnswerURLS() {
        return answerURLS;
    }

    public void setAnswerURLS(String answerURLS) {
        this.answerURLS = answerURLS;
    }

    public String getQuestionID() {
        return questionID;
    }

    public void setQuestionID(String questionID) {
        this.questionID = questionID;
    }

    public String getUserSubmitted() {
        return userSubmitted;
    }

    public void setUserSubmitted(String userSubmitted) {
        this.userSubmitted = userSubmitted;
    }

    public String getEventLink() {
        return eventLink;
    }

    public void setEventLink(String eventLink) {
        this.eventLink = eventLink;
    }
}