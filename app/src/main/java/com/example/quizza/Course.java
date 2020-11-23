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
    private String courseOwner;
    private String inviteCode;
    private final Integer inviteCodeLength = 10;
    private List<String> enrolledUsers;

    public Course() {}

    Course (String course_Name, String courseOwner, Integer course_ID) {
        this.courseID = course_ID;
        this.courseName = course_Name;
        this.courseOwner = courseOwner;
        this.enrolledUsers = new ArrayList<>();
        this.enrolledUsers.add(this.courseOwner);
        this.inviteCode = generateInviteCode(this.inviteCodeLength);
    }

    public String getCourseName() { return this.courseName; }

    public int getCourseID() { return this.courseID; }

    public void setCourseID(int courseID) { this.courseID = courseID; }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void setCourseOwner(String courseOwnerName) {
        this.courseOwner = courseOwnerName;
    }

    public List<String> getEnrolledUsers() {
        return enrolledUsers;
    }

    public void setEnrolledUsers(List<String> enrolledUsers) {
        this.enrolledUsers = enrolledUsers;
    }

    public String getCourseOwner() {
        return this.courseOwner;
    }

    public Boolean isOwner(String courseOwner) {
        return (courseOwner == this.courseOwner) ? true : false;
    }

    public String getInviteCode() {
        return this.inviteCode;
    }

    public Integer getInviteCodeLength() {
        return inviteCodeLength;
    }

    public void generateNewInviteCode() {
        this.inviteCode = generateInviteCode(this.inviteCodeLength);
    }

    public String generateInviteCode(int inviteCodeLength) {
        return UUID.randomUUID().toString().replace("-", "").substring(0, inviteCodeLength);
    }
}
