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
import java.util.UUID;

public class Course {
    private Integer courseID;
    private String courseName;
    private String inviteCode;
    private final Integer inviteCodeLength = 10;
    User courseOwner;
    private List<String> enrolledUsers = new ArrayList<>();

    public Course() {}

    Course (String course_Name, User course_Owner, Integer course_ID) {
        this.courseID = course_ID;
        this.courseName = course_Name;
        this.courseOwner = course_Owner;
        this.inviteCode = generateInviteCode(this.inviteCodeLength);
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
        return enrolledUsers;
    }

    public void setUserEnrolled(List<String> enrolledUsers) {
        this.enrolledUsers = enrolledUsers;
    }

    public String getCourseOwner() {
        return this.courseOwner.getName();
    }

    public Boolean isOwner(User user) {
        return (user == this.courseOwner ? true : false);
    }

    public String getInviteCode() {
        return this.inviteCode;
    }

    public void generateNewInviteCode() {
        this.inviteCode = generateInviteCode(this.inviteCodeLength);
    }

    public String generateInviteCode(int inviteCodeLength) {
        return UUID.randomUUID().toString().replace("-", "").substring(0, inviteCodeLength);
    }
}