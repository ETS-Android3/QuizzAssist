/***
 * CourseManager.java
 * Developers: Brandon Yip, Vatsal Parmar
 * CMPT 276 Team 'ForTheStudents'
 * This class maintains a list of all of the created courses
 * No known bugs.
 */

package com.example.quizza;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CourseManager {

    private List<Course> courseList = new ArrayList<>();
    private Map<String, String> courseInviteCodes = new HashMap<>();
    private Integer courseListLength = courseList.size();

    public CourseManager() {}

    public List<Course> getCourseList() {
        return courseList;
    }

    public Map<String, String> getCourseInviteCodes() {
        return courseInviteCodes;
    }
}