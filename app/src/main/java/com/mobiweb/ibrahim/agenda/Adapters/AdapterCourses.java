package com.mobiweb.ibrahim.agenda.Adapters;


/**
 * Created by ibrahim on 11/8/2017.
 */

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mobiweb.ibrahim.agenda.Adapters.interfaces.RVOnItemCourseClickListener;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextView;
import com.mobiweb.ibrahim.agenda.R;
import com.mobiweb.ibrahim.agenda.models.entities.Courses;

import java.util.ArrayList;


public class AdapterCourses extends RecyclerView.Adapter<AdapterCourses.VHCourses> {

    private ArrayList<Courses> courses;
    private RVOnItemCourseClickListener itemCourseClickListener;
    private int rowPosition;


    public AdapterCourses(ArrayList<Courses> courses,RVOnItemCourseClickListener itemCourseClickListener,int position) {
        this.courses = courses;
        this.itemCourseClickListener=itemCourseClickListener;
        this.rowPosition=position;
    }

    @Override
    public VHCourses onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VHCourses(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_courses, parent, false));
    }

    @Override
    public void onBindViewHolder(VHCourses holder, int position) {
        holder.ctvCourseName.setText(courses.get(position).getCourse_name());
        //holder.ctvCourseName.setText("asdsad");



    }

    @Override
    public int getItemCount() {
        return courses.size();
    }

    public ArrayList<Courses> getcourses() {
        return courses;
    }

    protected class VHCourses extends RecyclerView.ViewHolder implements View.OnClickListener {

        private CustomTextView ctvCourseName;


        public VHCourses(View itemView) {
            super(itemView);
            ctvCourseName = (CustomTextView) itemView.findViewById(R.id.ctvCourseName);


            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemCourseClickListener.onItemClicked(v, getLayoutPosition(),rowPosition);
        }
    }
}