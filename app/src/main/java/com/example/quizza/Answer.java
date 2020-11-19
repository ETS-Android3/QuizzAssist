/***
 * Course.java
 * Developers: Brandon Yip, Vatsal Parmar
 * CMPT 276 Team 'ForTheStudents'
 * This class has been newly created to manage the courses that users of application create.
 * No known bugs.
 */

package com.example.quizza;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Answer {

    //saved answer inputs
    //image link
    String shortAnswerInput;
    char multipleChoiceInput;

    //grading data
    boolean finishedGrading;
    int mark;

    public Answer() {
        //required empty constructor
    }

    public Answer(char input) { this.multipleChoiceInput = input; }

    public Answer(String input) { this.shortAnswerInput = input; }

    //public Answer(picture link input) { this.multipleChoiceInput = input; }

}