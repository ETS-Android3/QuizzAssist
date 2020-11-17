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



public class Event {
    Date eventStartTime;
    Date eventEndTime;

    String eventName;
    private List<Question> questionList = new ArrayList<Question>();

    boolean finishedGrading;
    float averageGrade;

    public Event() {
        //required empty constructor
    }

    Event(String event_Name) {
        this.eventName = event_Name;
    }

    //I don't know if there are built in date inputs, if not, this must be changed
    public void scheduleCourse(Date newStartTime, Date newEndTime) {
        this.eventStartTime = newStartTime;
        this.eventEndTime = newEndTime;
    }

    public String getCourseName() {
        return this.eventName;
    }


    public boolean isGraded() {
        return this.finishedGrading;
    }

    public boolean eventInProgress(Date currentDate) {
        if (currentDate.after(eventStartTime) && currentDate.before(eventEndTime))
            return true;
        return false;
    }

    public void GradeMultipleChoiceQuestions() {
        for (int j = 0; j < this.questionList.size(); j++)
            if (this.questionList.get(j).multipleChoice)
                for (int i = 0; i < this.questionList.get(j).answerList.size() ; i++)
                    if (this.questionList.get(j).answerList.get(i).finishedGrading == false)
                    {

                        if (this.questionList.get(j).answerList.get(i).multipleChoiceInput == this.questionList.get(j).answerKeyMultipleChoice)
                            this.questionList.get(j).answerList.get(i).mark = this.questionList.get(j).maxGrade;
                        else
                            this.questionList.get(j).answerList.get(i).mark = 0;
                 }
    }

}