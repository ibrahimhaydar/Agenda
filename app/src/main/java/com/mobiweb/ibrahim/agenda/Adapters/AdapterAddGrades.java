package com.mobiweb.ibrahim.agenda.Adapters;

import android.content.res.TypedArray;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.mobiweb.ibrahim.agenda.Adapters.interfaces.EditTextOnKeyListener;
import com.mobiweb.ibrahim.agenda.Adapters.interfaces.RVOnItemClickListener;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextView;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextViewBold;
import com.mobiweb.ibrahim.agenda.Custom.MinMaxFilter;
import com.mobiweb.ibrahim.agenda.R;
import com.mobiweb.ibrahim.agenda.models.Student;


import java.util.ArrayList;

/**
 * Created by ibrahim on 11/8/2017.
 */


public class AdapterAddGrades extends RecyclerView.Adapter<AdapterAddGrades.VHteacher_infoStudent> {

    private ArrayList<Student> infoStudents;
    private EditTextOnKeyListener editTextOnKeyListener;
    private int maxGrade;



    public AdapterAddGrades(ArrayList<Student> infoStudents, EditTextOnKeyListener editTextOnKeyListener,int maxGrade) {
        this.infoStudents = infoStudents;
        this.editTextOnKeyListener=editTextOnKeyListener;
        this.maxGrade=maxGrade;

    }

    @Override
    public VHteacher_infoStudent onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VHteacher_infoStudent(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_add_grade, parent, false));
    }

    @Override
    public void onBindViewHolder(VHteacher_infoStudent holder, int position) {
        try { holder.etAddGrade.setText(infoStudents.get(position).getGrade());}catch (Exception e){}
        try {holder.ctvStudentName.setText(infoStudents.get(position).getName()); }catch (Exception e){}
        try{
            holder.etAddGrade.setFilters(new InputFilter[]{ new MinMaxFilter("0", maxGrade+"")});
        }catch (Exception e){}
    }

    @Override
    public int getItemCount() {
        return infoStudents.size();
    }

    public ArrayList<Student> getinfoStudents() {
        return infoStudents;
    }

    protected class VHteacher_infoStudent extends RecyclerView.ViewHolder{


        private CustomTextViewBold ctvStudentName;
        private EditText etAddGrade;



        public VHteacher_infoStudent(View itemView) {
            super(itemView);
            etAddGrade = (EditText) itemView.findViewById(R.id.etAddGrade);
            ctvStudentName= (CustomTextViewBold) itemView.findViewById(R.id.ctvStudentName);




            etAddGrade.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    editTextOnKeyListener.EtOnKeyListener(getLayoutPosition(),charSequence.toString());
                }

                @Override
                public void afterTextChanged(Editable editable) {

                    etAddGrade.setSelection(etAddGrade.getText().length());
                }
            });

        }


    }
}