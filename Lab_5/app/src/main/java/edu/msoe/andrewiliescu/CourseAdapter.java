/*
 * CourseAdapter
 * Author: Andrew Iliescu
 * Date: 3/29/21
 * This file extends the ArrayAdapter class to create a custom ArrayAdapter
 * that can be used to populate the ListView the user sees which displays
 * their course information
 */
package edu.msoe.andrewiliescu;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class CourseAdapter extends ArrayAdapter<Course> {
    private final ArrayList<Course> courseList;

    /**
     * This is the default constructor
     *
     * @param context App context
     * @param list    The list of Courses
     */
    public CourseAdapter(Context context, ArrayList<Course> list) {
        super(context, 0, list);
        courseList = list;
    }

    /**
     * This is the method which generates the View from the xml document and also adds the
     * appropriate data to the text boxes which appear in the ListView
     *
     * @param position    location in the array
     * @param convertView separate view
     * @param parent      the ViewGroup the view belongs to
     * @return the updated view
     */
    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(getContext()).inflate(R.layout.row_layout, parent, false);
        }

        //Initialize the text boxes that display user data
        TextView classText = listItem.findViewById(R.id.classInfo);
        TextView creditsText = listItem.findViewById(R.id.creditsInfo);
        TextView gradeText = listItem.findViewById(R.id.gradeInfo);

        //Set text boxes with appropriate data
        classText.setText(courseList.get(position).getName());
        creditsText.setText(Integer.toString(courseList.get(position).getCredits()));
        gradeText.setText(courseList.get(position).getGrade());

        return listItem;
    }
}
