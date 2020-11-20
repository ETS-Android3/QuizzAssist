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
    private Integer courseID = 0;
    private String courseName;
    String courseOwner;
    String joinCode;
    String Uid;
    private List<String> userEnrolled = new ArrayList<>();

    public Course(){
        //required empty constructor
    }

    public Course (String course_Name, String course_Owner, Integer course_ID, String joinCode, String Uid) {
        this.courseID = course_ID;
        this.courseName = course_Name;
        this.courseOwner = course_Owner;
        this.joinCode = joinCode;
        this.Uid = Uid;
        this.userEnrolled = new ArrayList<>();
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getJoinCode() {
        return joinCode;
    }

    public void setJoinCode(String joinCode) {
        this.joinCode = joinCode;
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

    public void setCourseOwner(String courseOwner) {
        this.courseOwner = courseOwner;
    }

    public List<String> getUserEnrolled() {
        return userEnrolled;
    }

    public void setUserEnrolled(List<String> userEnrolled) {
        this.userEnrolled = userEnrolled;
    }

    public String getCourseOwner() {
        return this.courseOwner;
    }

//    public Boolean isOwner(String user){
//        if (user == courseOwner){
//            return true;
//        }
//        else{
//            return false;
//        }
//    }

}