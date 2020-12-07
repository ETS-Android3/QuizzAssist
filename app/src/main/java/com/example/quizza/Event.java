/***
 * Event.java
 * Developers: Brandon Yip, Vatsal Parmar, Andrew Yeon
 * CMPT 276 Team 'ForTheStudents'
 * This class stores information in relation to a event (list of questions,
 * list of students in the course that are all eligible to view the question, etc.)
 * No known bugs.
 * ***/

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
    private Boolean eventStarted = false;
    private Boolean eventEnded = false;

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
        this.eventStarted = false;
        this.eventEnded = false;
    }

    public Boolean getEventEnded() {
        return eventEnded;
    }

    public void setEventEnded(Boolean eventEnded) {
        this.eventEnded = eventEnded;
    }

    public Boolean getEventStarted() {
        return eventStarted;
    }

    public void setEventStarted(Boolean eventStarted) {
        this.eventStarted = eventStarted;
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

