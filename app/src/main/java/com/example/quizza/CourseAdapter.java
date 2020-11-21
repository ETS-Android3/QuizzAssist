package com.example.quizza;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CourseAdapter extends ArrayAdapter {

    ArrayList<String> courseList = new ArrayList<>();

    public CourseAdapter(@NonNull Context context, int resource, ArrayList<String> courseList) {
        super(context, resource);
        this.courseList = courseList;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.list_view_courses, null);
        TextView courseName = (TextView) v.findViewById(R.id.className);
        TextView courseJoinCode = (TextView) v.findViewById(R.id.classJoinCode);
        courseName.setText(courseList.get(position));
        courseJoinCode.setText(courseList.get(position));
        return v;
    }
}
