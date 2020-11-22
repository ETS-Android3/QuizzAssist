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