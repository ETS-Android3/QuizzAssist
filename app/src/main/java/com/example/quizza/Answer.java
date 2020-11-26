/***
    write your class description here for whoever wrote it
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