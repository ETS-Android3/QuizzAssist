/***
 * Course.java
 * Developers: Brandon Yip, Vatsal Parmar
 * CMPT 276 Team 'ForTheStudents'
 * This class has been newly created to manage the courses that users of application create.
 *
 * Features to add: Implement the public and private settings for the courses
 * Private courses will (most likely) have a password in addition to the invite code.
 * Implementation will be something along the lines of users being able to create classes and join
 * other classes via invite code. UI will be given to users that displays a list of every class
 * that currently exists and users can join freely into any class that may seem interesting, however
 * rooms set to private will require a password as well. Therefore, invite codes are only to join a
 * class directly, but the class may be found from the list of all classes that exist as well.
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

    public Course() {}

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

    public boolean isOwner(User user){
        return  (user == this.courseOwner) ? true : false;
    }
}