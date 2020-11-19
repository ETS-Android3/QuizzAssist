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

public class Course {
    Integer courseID = 0;
    String courseName;
    User courseOwner;
    private List<String> userEnrolled = new ArrayList<>();

    public Course(){
        //required empty constructor
    }

    Course (String course_Name, User course_Owner, Integer course_ID) {
        this.courseID = course_ID;
        this.courseName = course_Name;
        this.courseOwner = course_Owner;
    }

    public String getCourseName() {
        return this.courseName;
    }

    public int getCourseID() {
        return this.courseID;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void setCourseOwner(User courseOwner) {
        this.courseOwner = courseOwner;
    }

    public List<String> getUserEnrolled() {
        return userEnrolled;
    }

    public void setUserEnrolled(List<String> userEnrolled) {
        this.userEnrolled = userEnrolled;
    }

    public String getCourseOwner() {
        return this.courseOwner.getName();
    }

    public Boolean isOwner(User user){
        if (user == courseOwner){
            return true;
        }
        else{
            return false;
        }
    }

}