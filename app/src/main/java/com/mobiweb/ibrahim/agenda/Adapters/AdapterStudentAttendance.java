package com.mobiweb.ibrahim.agenda.Adapters;



/**
 * Created by ibrahim on 1/28/2018.
 */

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mobiweb.ibrahim.agenda.Adapters.interfaces.RVOnItemClickListener;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextView;
import com.mobiweb.ibrahim.agenda.R;
import com.mobiweb.ibrahim.agenda.models.entities.Attendance;

import java.util.ArrayList;

/**
 * Created by ibrahim on 11/8/2017.
 */


public class AdapterStudentAttendance extends RecyclerView.Adapter<AdapterStudentAttendance.VHAttendance> {

    private ArrayList<Attendance> attendances;
    private RVOnItemClickListener itemClickListener;



    public AdapterStudentAttendance(ArrayList<Attendance> attendances, RVOnItemClickListener itemClickListener) {
        this.attendances = attendances;
        this.itemClickListener = itemClickListener;
    }


  

    @Override
    public VHAttendance onCreateViewHolder(ViewGroup parent, int viewType) {
       
            return new VHAttendance(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student_attendance, parent, false));
       
    }


    @Override
    public void onBindViewHolder(VHAttendance holder, int position) {

        try {
            if (attendances.get(position).getPresent().matches("1"))
                holder.ivAttendance.setImageResource(R.drawable.attendance_in);
            else
                holder.ivAttendance.setImageResource(R.drawable.attendance_out);
        }catch (Exception e){
            holder.ivAttendance.setImageResource(R.drawable.attendance_out);
        }

           try {
               holder.ctvDate.setText(attendances.get(position).getDate());
           }catch (Exception e){}

    
    }

    @Override
    public int getItemCount() {
        return attendances.size();
    }

    public ArrayList<Attendance> getattendances() {
        return attendances;
    }

    protected class VHAttendance extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView ivAttendance;
        private CustomTextView ctvDate;


        public VHAttendance(View itemView) {
            super(itemView);

            ivAttendance=(ImageView)itemView.findViewById(R.id.ivAttendance);
            ctvDate=(CustomTextView)itemView.findViewById(R.id.ctvDate);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            itemClickListener.onItemClicked(v, getLayoutPosition());
        }
    }




}