package com.mobiweb.ibrahim.agenda.Adapters;




import android.content.res.TypedArray;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mobiweb.ibrahim.agenda.Adapters.interfaces.RVOnItemClickListener;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextView;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextViewBold;
import com.mobiweb.ibrahim.agenda.R;
import com.mobiweb.ibrahim.agenda.models.entities.InfoStudent;


import java.util.ArrayList;

/**
 * Created by ibrahim on 11/8/2017.
 */


public class AdapterStudentInfo extends RecyclerView.Adapter<AdapterStudentInfo.VHteacher_infoStudent> {

    private ArrayList<InfoStudent> infoStudents;



    public AdapterStudentInfo(ArrayList<InfoStudent> infoStudents) {
        this.infoStudents = infoStudents;

    }

    @Override
    public VHteacher_infoStudent onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VHteacher_infoStudent(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_info_student, parent, false));
    }

    @Override
    public void onBindViewHolder(VHteacher_infoStudent holder, int position) {
    try { holder.ctvEvaluation.setText(infoStudents.get(position).getEvaluation());}catch (Exception e){}
    try {holder.ctvStudentName.setText(infoStudents.get(position).getName_student()); }catch (Exception e){}
    try {holder.ctvCourseName.setText(infoStudents.get(position).getCourse_name()); }catch (Exception e){}

    }

    @Override
    public int getItemCount() {
        return infoStudents.size();
    }

    public ArrayList<InfoStudent> getinfoStudents() {
        return infoStudents;
    }

    protected class VHteacher_infoStudent extends RecyclerView.ViewHolder{

        private CustomTextView ctvEvaluation,ctvStudentName;
        private CustomTextView ctvCourseName;



        public VHteacher_infoStudent(View itemView) {
            super(itemView);
            ctvEvaluation = (CustomTextView) itemView.findViewById(R.id.ctvEvaluation);
            ctvStudentName= (CustomTextView) itemView.findViewById(R.id.ctvStudentName);
            ctvCourseName=(CustomTextView)itemView.findViewById(R.id.ctvCourseName);

        }


    }
}