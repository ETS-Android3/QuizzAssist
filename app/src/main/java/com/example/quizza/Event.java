///***
// * Course.java
// * Developers: Brandon Yip, Vatsal Parmar
// * CMPT 276 Team 'ForTheStudents'
// * This class has been newly created to manage the courses that users of application create.
// * No known bugs.
// */
//
//package com.example.quizza;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Date;
//
//
//
//public class Event {
//    Date eventStartTime;
//    Date eventEndTime;
//
//    String eventName;
//    private List<Question> questionList = new ArrayList<Question>();
//
////    boolean finishedGrading;
////    float averageGrade;
//
//    public Event() {
//        //required empty constructor
//    }
//
//    Event(String event_Name) { this.eventName = event_Name; }
//
//    //I don't know if there are built in date inputs, if not, this must be changed
//    public void scheduleCourse(Date newStartTime, Date newEndTime) {
//        this.eventStartTime = newStartTime;
//        this.eventEndTime = newEndTime;
//    }
//
//    public String getCourseName() { return this.eventName; }
//
//    public boolean isGraded() { return this.finishedGrading; }
//
//    public boolean eventInProgress(Date currentDate) {
//        return (currentDate.after(eventStartTime) && currentDate.before(eventEndTime) ? true : false);
//    }
//
//    public void GradeMultipleChoiceQuestions() {
//        for (int j = 0; j < this.questionList.size(); j++)
//            if (this.questionList.get(j).multipleChoice)
//                for (int i = 0; i < this.questionList.get(j).answerList.size() ; i++)
//                    if (this.questionList.get(j).answerList.get(i).finishedGrading == false)
//                        this.questionList.get(j).answerList.get(i).mark
//                                = (this.questionList.get(j).answerList.get(i).multipleChoiceInput
//                                == this.questionList.get(j).answerKeyMultipleChoice)
//                                ? (this.questionList.get(j).maxGrade) : 0;
//    }
//
//    //to be called during grading to get info to display the next ungraded answer
//    //this may need to return a pointer (not familiar with java)
//    public Answer getNextUngradedAnswer() {
//        for (int i = 0; i < this.questionList.size(); i++)
//            if (!this.questionList.get(i).finishedGrading)
//                for (int j = 0 ; j < this.questionList.get(i).answerList.size() ; j++)
//                    if (!this.questionList.get(i).answerList.get(j).finishedGrading)
//                        return this.questionList.get(i).answerList.get(j);
//        return null;
//    }
//
//    //to be called during event to load the next question/saved answer
//    public int getNextQuestionIndex(int currentQuestionIndex){
//        return ++currentQuestionIndex % this.questionList.size();
//    }
//
//    public int getPrevQuestionIndex(int currentQuestionIndex){
//        return --currentQuestionIndex % this.questionList.size();
//    }
//
//}
package com.example.quizza;

import java.util.ArrayList;
import java.util.List;

public class Event {

    private String eventTitle;
    private Integer startHour;
    private Integer startMns;
    private String startDate;
    private String endDate;
    private Integer endHours;
    private Integer endMns;
    private Integer numberOfQuestions;
    private List<String> questionList = new ArrayList<>();
    private String courseLink;
    private List<String> enrolledUsers = new ArrayList<>();

    public Event() {
    } //Required by Firebase

    Event (String eventTitle, String startDate, Integer startHour, Integer startMns, String endDate, Integer endHours, Integer endMns, Integer numberOfQuestions, String courseLink){
        this.eventTitle = eventTitle;
        this.startDate = startDate;
        this.startHour = startHour;
        this.startMns = startMns;
        this.endDate = endDate;
        this.endHours = endHours;
        this.endMns = endMns;
        this.numberOfQuestions = numberOfQuestions;
        this.questionList = new ArrayList<>();
        this.questionList.add("initial");
        this.courseLink = courseLink;
        this.enrolledUsers = new ArrayList<>();
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public Integer getStartHour() {
        return startHour;
    }

    public void setStartHour(Integer startHour) {
        this.startHour = startHour;
    }

    public Integer getStartMns() {
        return startMns;
    }

    public void setStartMns(Integer startMns) {
        this.startMns = startMns;
    }

    public Integer getEndHours() {
        return endHours;
    }

    public void setEndHours(Integer endHours) {
        this.endHours = endHours;
    }

    public Integer getEndMns() {
        return endMns;
    }

    public void setEndMns(Integer endMns) {
        this.endMns = endMns;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }


    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }


    public Integer getNumberOfQuestions() {
        return numberOfQuestions;
    }

    public void setNumberOfQuestions(Integer numberOfQuestions) {
        this.numberOfQuestions = numberOfQuestions;
    }

    public List<String> getQuestionList() {
        return questionList;
    }

    public void setQuestionList(List<String> questionList) {
        this.questionList = questionList;
    }

    public String getCourseLink() {
        return courseLink;
    }

    public void setCourseLink(String courseLink) {
        this.courseLink = courseLink;
    }

    public List<String> getEnrolledUsers() {
        return enrolledUsers;
    }

    public void setEnrolledUsers(List<String> enrolledUsers) {
        this.enrolledUsers = enrolledUsers;
    }
}

