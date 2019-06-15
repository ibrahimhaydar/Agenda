package com.mobiweb.ibrahim.agenda.Adapters;


import android.content.res.TypedArray;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mobiweb.ibrahim.agenda.Adapters.interfaces.RVOnItemClickListener;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextView;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextViewBold;
import com.mobiweb.ibrahim.agenda.R;
import com.mobiweb.ibrahim.agenda.models.entities.Evaluation;


import java.util.ArrayList;

/**
 * Created by ibrahim on 11/8/2017.
 */


public class AdapterEvaluation extends RecyclerView.Adapter<AdapterEvaluation.VHteacher_evaluation> {

    private ArrayList<Evaluation> evaluations;
    private RVOnItemClickListener itemClickListener;


    public AdapterEvaluation(ArrayList<Evaluation> evaluations, RVOnItemClickListener itemClickListener) {
        this.evaluations = evaluations;
        this.itemClickListener = itemClickListener;
    }

    @Override
    public VHteacher_evaluation onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VHteacher_evaluation(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_evaluation, parent, false));
    }

    @Override
    public void onBindViewHolder(VHteacher_evaluation holder, int position) {
        holder.ctvDate.setText(evaluations.get(position).getDate());
        holder.adapterStudentInfo=new AdapterStudentInfo(evaluations.get(position).getInfoStudent());
        GridLayoutManager glm=new GridLayoutManager(holder.itemView.getContext(),1);
        holder.rvStudentInfo.setLayoutManager(glm);
        holder.rvStudentInfo.setAdapter(holder.adapterStudentInfo);


     
    }

    @Override
    public int getItemCount() {
        return evaluations.size();
    }

    public ArrayList<Evaluation> getevaluations() {
        return evaluations;
    }

    protected class VHteacher_evaluation extends RecyclerView.ViewHolder implements View.OnClickListener {

        private CustomTextViewBold ctvDate;
        private RecyclerView rvStudentInfo;
        private AdapterStudentInfo adapterStudentInfo;


        public VHteacher_evaluation(View itemView) {
            super(itemView);
            ctvDate = (CustomTextViewBold) itemView.findViewById(R.id.ctvDate);
            rvStudentInfo=(RecyclerView)itemView.findViewById(R.id.rvStudentInfo);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onItemClicked(v, getLayoutPosition());
        }
    }
}