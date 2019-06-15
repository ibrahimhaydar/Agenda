package com.mobiweb.ibrahim.agenda.Adapters;

/**
 * Created by ibrahim on 4/19/2018.
 */

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.mobiweb.ibrahim.agenda.Adapters.interfaces.EditTextFocusListener;
import com.mobiweb.ibrahim.agenda.Adapters.interfaces.EditTextOnKeyListener;
import com.mobiweb.ibrahim.agenda.Adapters.interfaces.RVOnItemClickListener;
import com.mobiweb.ibrahim.agenda.Adapters.interfaces.RVOnLongItemClickListener;
import com.mobiweb.ibrahim.agenda.Adapters.interfaces.SpinnerOnSelection;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextView;
import com.mobiweb.ibrahim.agenda.R;
import com.mobiweb.ibrahim.agenda.models.entities.ScheduleExamPost;
import com.mobiweb.ibrahim.agenda.utils.AppConstants;

import java.util.ArrayList;

/**
 * Created by ibrahim on 11/8/2017.
 */



public class AdapterAddExamDetails extends RecyclerView.Adapter<AdapterAddExamDetails.VHScheduleExamPost> {

    private ArrayList<ScheduleExamPost> details;
    private RVOnItemClickListener itemClickListener;
    private RVOnLongItemClickListener longItemClickListener;
    private SpinnerOnSelection spinnerOnSelection;
    private EditTextOnKeyListener editTextOnKeyListener;
    private EditTextFocusListener editTextFocusListener;

    int check = 0;
    private boolean userIsInteracting=false;



    public AdapterAddExamDetails(ArrayList<ScheduleExamPost> details, RVOnItemClickListener itemClickListener,SpinnerOnSelection spinnerOnSelection,RVOnLongItemClickListener onLongItemClickListener,EditTextOnKeyListener editTextOnKeyListener,EditTextFocusListener editTextFocusListener) {
        this.details = details;
        this.itemClickListener = itemClickListener;
        this.spinnerOnSelection=spinnerOnSelection;
        this.longItemClickListener=onLongItemClickListener;
        this.editTextOnKeyListener=editTextOnKeyListener;
        this.editTextFocusListener=editTextFocusListener;
    }

    @Override
    public VHScheduleExamPost onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_exams_add_details, parent, false);
        VHScheduleExamPost vh = new VHScheduleExamPost(v);

        return vh;


    }

    @Override
    public void onBindViewHolder(final VHScheduleExamPost holder, final int position) {
        holder.ctvDate.setText(details.get(position).getDate());

        if(details.get(position).getFrom().length()==8)
          holder.ctvFrom.setText(details.get(position).getFrom().substring(0,details.get(position).getFrom().length()-3));
        else
          holder.ctvFrom.setText(details.get(position).getFrom());

        if(details.get(position).getTo().length()==8)
            holder.ctvTo.setText(details.get(position).getTo().substring(0,details.get(position).getTo().length()-3));
        else
            holder.ctvTo.setText(details.get(position).getTo());


    //   holder.ctvTo.setText(details.get(position).getTo());

        holder.ctvTitle.setText(details.get(position).getTitle());
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(holder.itemView.getContext(),R.array.spinnerItems, R.layout.item_spinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.spinner.setAdapter(adapter);






        switch (details.get(position).getType()){
            case AppConstants.TYPE_EXAM:
                holder.disableFrom.setVisibility(View.GONE);
                holder.disableTo.setVisibility(View.GONE);
                holder.spinner.setSelection(0);
                break;
            case AppConstants.TYPE_BREAK:
                holder.disableFrom.setVisibility(View.GONE);
                holder.disableTo.setVisibility(View.GONE);
                holder.spinner.setSelection(1);
                break;
            case AppConstants.TYPE_OFF:
                holder.disableFrom.setVisibility(View.VISIBLE);
                holder.disableTo.setVisibility(View.VISIBLE);
                holder.spinner.setSelection(2);
                break;
            case AppConstants.TYPE_REVISION:
                holder.disableFrom.setVisibility(View.VISIBLE);
                holder.disableTo.setVisibility(View.VISIBLE);
                holder.spinner.setSelection(3);
                break;
            default:
                holder.disableFrom.setVisibility(View.GONE);
                holder.disableTo.setVisibility(View.GONE);
                holder.spinner.setSelection(0);

        }
Log.wtf("rowid","id-->"+details.get(position).getId_class_exam());
          if(details.get(position).isTeacherAdd()){


              holder.linearBorder1.setBackgroundResource(R.drawable.border_table_teacher);
              holder.linearBorder2.setBackgroundResource(R.drawable.border_table_teacher);
              holder.linearBorder3.setBackgroundResource(R.drawable.border_table_teacher);
              holder.linearBorder4.setBackgroundResource(R.drawable.border_table_teacher);
              holder.linearBorder5.setBackgroundResource(R.drawable.border_table_teacher);
        }
         else {
              holder.linearBorder1.setBackgroundResource(R.drawable.border_table);
              holder.linearBorder2.setBackgroundResource(R.drawable.border_table);
              holder.linearBorder3.setBackgroundResource(R.drawable.border_table);
              holder.linearBorder4.setBackgroundResource(R.drawable.border_table);
              holder.linearBorder5.setBackgroundResource(R.drawable.border_table);
        }

    }

    @Override
    public int getItemCount() {
        return details.size();
    }

    public boolean isUserIsInteracting() {
        return userIsInteracting;
    }

    public void setUserIsInteracting(boolean userIsInteracting) {
        this.userIsInteracting = userIsInteracting;
    }

    public void setCheck(int check) {
        this.check = check;
    }

    public ArrayList<ScheduleExamPost> getdetails() {
        return details;
    }

    public class VHScheduleExamPost extends RecyclerView.ViewHolder implements View.OnClickListener {

        private CustomTextView ctvDate,ctvFrom,ctvTo,ctvTitle;
        private ImageView ivPickDate,ivPickTimeFrom,ivPickTimeTo;
        private Spinner spinner;
        private View disableFrom,disableTo;
        private LinearLayout linearBorder1,linearBorder2,linearBorder3,linearBorder4,linearBorder5;



        public VHScheduleExamPost(View itemView) {
            super(itemView);
            ctvDate=(CustomTextView)itemView.findViewById(R.id.ctvDate);
            ctvFrom=(CustomTextView)itemView.findViewById(R.id.ctvFrom);
            ctvTo=(CustomTextView)itemView.findViewById(R.id.ctvTo);
            ctvTitle=(CustomTextView)itemView.findViewById(R.id.ctvCourseTitle);
            ivPickDate=(ImageView)itemView.findViewById(R.id.ivPickDate);
            ivPickTimeFrom=(ImageView)itemView.findViewById(R.id.ivPickTimeFrom);
            ivPickTimeTo=(ImageView)itemView.findViewById(R.id.ivPickTimeTo);
            spinner=(Spinner)itemView.findViewById(R.id.spinner);
            disableFrom=(View)itemView.findViewById(R.id.disableFrom);
            disableTo=(View)itemView.findViewById(R.id.disableTo);

            linearBorder1=(LinearLayout) itemView.findViewById(R.id.linearBorder1);
            linearBorder2=(LinearLayout) itemView.findViewById(R.id.linearBorder2);
            linearBorder3=(LinearLayout) itemView.findViewById(R.id.linearBorder3);
            linearBorder4=(LinearLayout) itemView.findViewById(R.id.linearBorder4);
            linearBorder5=(LinearLayout) itemView.findViewById(R.id.linearBorder5);


            ivPickDate.setOnClickListener(this);
            ivPickTimeFrom.setOnClickListener(this);
            ivPickTimeTo.setOnClickListener(this);
            ctvTitle.setOnClickListener(this);



            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    longItemClickListener.onLongItemClicked(view,getLayoutPosition());
                    return false;
                }
            });

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    Log.wtf("check",isUserIsInteracting()+"");
                    //if(++check > 1) {
                    if(userIsInteracting){
                        spinnerOnSelection.onSpinnerSelected(selectedItemView,getLayoutPosition(),position);
                        userIsInteracting=false;

                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                }

            });









        }



        @Override
        public void onClick(View v) {
            itemClickListener.onItemClicked(v, getLayoutPosition());

        }
    }


}