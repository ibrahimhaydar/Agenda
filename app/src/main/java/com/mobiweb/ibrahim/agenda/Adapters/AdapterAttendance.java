package com.mobiweb.ibrahim.agenda.Adapters;





import android.content.res.TypedArray;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.mobiweb.ibrahim.agenda.Adapters.interfaces.RGOnChangeListener;
import com.mobiweb.ibrahim.agenda.Adapters.interfaces.RVOnItemClickListener;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextView;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextViewBold;
import com.mobiweb.ibrahim.agenda.R;
import com.mobiweb.ibrahim.agenda.models.Student;



import java.util.ArrayList;

/**
 * Created by ibrahim on 11/8/2017.
 */


public class AdapterAttendance extends RecyclerView.Adapter<AdapterAttendance.VHteacher_infoStudent> {

    private ArrayList<Student> infoStudents;
    private RGOnChangeListener onChangeListener;



    public AdapterAttendance(ArrayList<Student> infoStudents, RGOnChangeListener itemChangeListener) {
        this.infoStudents = infoStudents;
        this.onChangeListener = itemChangeListener;

    }

    @Override
    public VHteacher_infoStudent onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VHteacher_infoStudent(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_attendance, parent, false));
    }

    @Override
    public void onBindViewHolder(VHteacher_infoStudent holder, int position) {
        try {
            holder.ctvStudentName.setText(infoStudents.get(position).getName());
        }catch (Exception e){}


        if(infoStudents.get(position).getIsPresent().matches("1"))
            holder.rdPresent.setChecked(true);
        else
            holder.rdAbsent.setChecked(true);



    }

    @Override
    public int getItemCount() {
        return infoStudents.size();
    }

    public ArrayList<Student> getinfoStudents() {
        return infoStudents;
    }

    protected class VHteacher_infoStudent extends RecyclerView.ViewHolder implements RadioGroup.OnCheckedChangeListener {

        private CustomTextView ctvStudentName;
        private RadioButton rdPresent,rdAbsent;
        private RadioGroup rgAttendance;
        public VHteacher_infoStudent(final View itemView) {
            super(itemView);

            ctvStudentName= (CustomTextView) itemView.findViewById(R.id.ctvStudentName);
            rgAttendance=(RadioGroup)itemView.findViewById(R.id.rgAttendance);
            rdPresent=(RadioButton)rgAttendance.findViewById(R.id.rdPresent);
            rdAbsent=(RadioButton)rgAttendance.findViewById(R.id.rdAbsent);
          rgAttendance.setOnCheckedChangeListener(this);

       }

        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int i) {
            onChangeListener.onRadioChanged(rgAttendance.getRootView(),getLayoutPosition(),radioGroup,i);
        }
    }
}






