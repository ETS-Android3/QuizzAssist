/***
 * User.java
 * Developers: Brandon Yip, Vatsal Parmar
 * CMPT 276 Team 'ForTheStudents'
 * This class is used to store user information when an account is created from the sign-up page,
 * which will then be stored on the Firebase Database. Each user will have a list of courses that
 * they have created and a list of courses that they have enrolled in (courses that have been
 * created by other users). This class provides functionality for the genereic expected
 * methods for a User class, such as setting/retrieving name and email, and retrieving lists of
 * courses that they are a part of. Passwords are not saved here for security reasons.
 * No known bugs.
 */


package com.example.quizza;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String userName;
    private String userFirstName;
    private String userMiddleName;
    private String userLastName;
    private String userEmail;
    private String userStudentNumber;
    private List<String> createdCourses = new ArrayList<>();
    private List<String> enrolledCourses = new ArrayList<>();


    public User() {}

    public User(String userName, String userFirstName, String userLastName, String userEmail) {
        this.userName = userName;
        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
        this.userEmail = userEmail;
        this.createdCourses = new ArrayList<String>();
        this.enrolledCourses = new ArrayList<String>();
    }

    public List<String> getCreatedCourses() {
        return createdCourses;
    }

    public void setCreatedCourses(List<String> createdCourses) {
        this.createdCourses = createdCourses;
    }

    public List<String> getEnrolledCourses() {
        return enrolledCourses;
    }

    public void setEnrolledCourses(List<String> enrolledCourses) {
        this.enrolledCourses = enrolledCourses;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail(){
        return this.userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    public String getUserMiddleName() {
        return userMiddleName;
    }

    public void setUserMiddleName(String userMiddleName) {
        this.userMiddleName = userMiddleName;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }

    public String getUserStudentNumber() {
        return userStudentNumber;
    }

    public void setUserStudentNumber(String userStudentNumber) {
        this.userStudentNumber = userStudentNumber;
    }
}
