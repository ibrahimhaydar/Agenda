package com.mobiweb.ibrahim.agenda.Adapters;

import android.content.res.TypedArray;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mobiweb.ibrahim.agenda.Adapters.interfaces.RVOnItemClickListener;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextViewBold;
import com.mobiweb.ibrahim.agenda.R;
import com.mobiweb.ibrahim.agenda.models.entities.teacher_course;

import java.util.ArrayList;

/**
 * Created by ibrahim on 11/8/2017.
 */


public class AdapterTeacherCourses extends RecyclerView.Adapter<AdapterTeacherCourses.VHteacher_course> {

    private ArrayList<teacher_course> courses;
    private RVOnItemClickListener itemClickListener;


    public AdapterTeacherCourses(ArrayList<teacher_course> courses, RVOnItemClickListener itemClickListener) {
        this.courses = courses;
        this.itemClickListener = itemClickListener;
    }

    @Override
    public VHteacher_course onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VHteacher_course(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_teacher_courses, parent, false));
    }

    @Override
    public void onBindViewHolder(VHteacher_course holder, int position) {
        holder.courseName.setText(courses.get(position).getNameCourse());

        TypedArray ta = holder.itemView.getResources().obtainTypedArray(R.array.colors);
        int[] colors = new int[ta.length()];
        for (int i = 0; i < ta.length(); i++) {
            colors[i] = ta.getColor(i, 0);
        }
        ta.recycle();

        // GradientDrawable bgShape = (GradientDrawable)holder.ClassName.getBackground();
        holder.courseName.setBackgroundColor(colors[position%4]);

    }

    @Override
    public int getItemCount() {
        return courses.size();
    }

    public ArrayList<teacher_course> getcourses() {
        return courses;
    }

    protected class VHteacher_course extends RecyclerView.ViewHolder implements View.OnClickListener {

        private CustomTextViewBold courseName;


        public VHteacher_course(View itemView) {
            super(itemView);
            courseName = (CustomTextViewBold) itemView.findViewById(R.id.courseName);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onItemClicked(v, getLayoutPosition());
        }
    }
}