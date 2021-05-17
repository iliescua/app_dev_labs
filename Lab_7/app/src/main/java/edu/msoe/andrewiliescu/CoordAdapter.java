/*
 * CoordAdapter
 * Author: Andrew Iliescu
 * Date: 4/27/21
 * This file extends the ArrayAdapter class to create a custom ArrayAdapter
 * that can be used to populate the ListView the user sees which displays
 * their coordinate information
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

public class CoordAdapter extends ArrayAdapter<CoordClass> {
    private final ArrayList<CoordClass> coordsList;

    /**
     * This is the default constructor
     *
     * @param context App context
     * @param list    The list of coordinates
     */
    public CoordAdapter(Context context, ArrayList<CoordClass> list) {
        super(context, 0, list);
        coordsList = list;
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
        TextView latText = listItem.findViewById(R.id.latTB);
        TextView longText = listItem.findViewById(R.id.longTB);

        //Set text boxes with appropriate data
        latText.setText(coordsList.get(position).getLat());
        longText.setText(coordsList.get(position).getLong());

        return listItem;
    }
}