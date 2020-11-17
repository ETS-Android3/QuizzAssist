/***
 * Course.java
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
    private String name;
    private String email;
    private List<String> createdCourses = new ArrayList<>();
    private List<String> enrolledCourses = new ArrayList<>();


    public User(){
        this.name = "Default";
        this.createdCourses = new ArrayList<>();
    }

    public User(String name){
        this.name = name;
        this.createdCourses = new ArrayList<>();
    }

    public User(String userName, String userEmail) {
        this.name = userName;
        this.email = userEmail;
    }

    public String getName() {
        return this.name;
    }

    public String getEmail(){
        return this.email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCreatedCourses(List<String> createdCourses) {
        this.createdCourses = createdCourses;
    }

    public void setEnrolledCourses(List<String> enrolledCourses) {
        this.enrolledCourses = enrolledCourses;
    }

}