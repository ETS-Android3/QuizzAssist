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
