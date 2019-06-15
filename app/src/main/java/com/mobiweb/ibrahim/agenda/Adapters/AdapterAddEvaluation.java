package com.mobiweb.ibrahim.agenda.Adapters;






import android.content.res.TypedArray;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
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
import com.mobiweb.ibrahim.agenda.R;
import com.mobiweb.ibrahim.agenda.models.Student;


import java.util.ArrayList;

/**
 * Created by ibrahim on 11/8/2017.
 */


public class AdapterAddEvaluation extends RecyclerView.Adapter<AdapterAddEvaluation.VHteacher_infoStudent> {

    private ArrayList<Student> infoStudents;
    private EditTextOnKeyListener editTextOnKeyListener;



    public AdapterAddEvaluation(ArrayList<Student> infoStudents, EditTextOnKeyListener editTextOnKeyListener) {
        this.infoStudents = infoStudents;
        this.editTextOnKeyListener=editTextOnKeyListener;

    }

    @Override
    public VHteacher_infoStudent onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VHteacher_infoStudent(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_add_evaluation, parent, false));
    }

    @Override
    public void onBindViewHolder(final VHteacher_infoStudent holder, int position) {
        try { holder.etAddEvaluation.setText(infoStudents.get(position).getInfo());}catch (Exception e){}
        try {holder.ctvStudentName.setText(infoStudents.get(position).getName()); }catch (Exception e){}

       holder.etAddEvaluation.addTextChangedListener(new TextWatcher() {
           @Override
           public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
               holder.etAddEvaluation.setSelection(holder.etAddEvaluation.getText().length());
           }

           @Override
           public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
               holder.etAddEvaluation.setSelection(holder.etAddEvaluation.getText().length());
           }

           @Override
           public void afterTextChanged(Editable editable) {
             holder.etAddEvaluation.setSelection(holder.etAddEvaluation.getText().length());
           }
       });

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
        private EditText etAddEvaluation;



        public VHteacher_infoStudent(View itemView) {
            super(itemView);
            etAddEvaluation = (EditText) itemView.findViewById(R.id.etAddEvaluation);
            ctvStudentName= (CustomTextViewBold) itemView.findViewById(R.id.ctvStudentName);


            etAddEvaluation.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    etAddEvaluation.setSelection(etAddEvaluation.getText().length());
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    editTextOnKeyListener.EtOnKeyListener(getLayoutPosition(),charSequence.toString());
                    etAddEvaluation.setSelection(etAddEvaluation.getText().length());
                }

                @Override
                public void afterTextChanged(Editable editable) {

                    etAddEvaluation.setSelection(etAddEvaluation.getText().length());
                }
            });

        }


    }
}