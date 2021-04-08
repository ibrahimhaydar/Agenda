package com.mobiweb.ibrahim.agenda.Adapters;



/**
 * Created by ibrahim on 4/28/2018.
 */


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mobiweb.ibrahim.agenda.Adapters.interfaces.RVOnItemClickListener;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextView;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextViewAr;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextViewBoldAr;
import com.mobiweb.ibrahim.agenda.Custom.RtlGridLayoutManager;
import com.mobiweb.ibrahim.agenda.R;
import com.mobiweb.ibrahim.agenda.models.entities.Child;
import com.mobiweb.ibrahim.agenda.models.entities.Image;
import com.mobiweb.ibrahim.agenda.models.entities.Info;
import com.mobiweb.ibrahim.agenda.utils.AppConstants;
import com.mobiweb.ibrahim.agenda.utils.AppHelper;

import java.util.ArrayList;


public class AdapterChilds extends RecyclerView.Adapter<AdapterChilds.VHAllclass> implements RVOnItemClickListener{

    private ArrayList<Child> childs;
    private RVOnItemClickListener itemClickListener;
    private  AdapterScheduleDetails adapterScheduleDetails;



    public AdapterChilds(ArrayList<Child> childs, RVOnItemClickListener itemClickListener) {
        this.childs = childs;
        this.itemClickListener = itemClickListener;
    }

    @Override
    public VHAllclass onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VHAllclass(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_child, parent, false));
    }

    @Override
    public void onBindViewHolder(VHAllclass holder, int position) {
        holder.ctvChildName.setText(childs.get(position).getChildName());
        try{
            holder.ctvBirthday.setText(childs.get(position).getBirth_date());
        }catch (Exception e){}
        try{
            holder.tvClassName.setText(childs.get(position).getClassName());
        }catch (Exception e){}
        try{
            AppHelper.setRoundImage(holder.itemView.getContext(),holder.ivStudent,childs.get(position).getImage());
        }catch (Exception e){}

    }

    @Override
    public int getItemCount() {
        return childs.size();
    }

    public ArrayList<Child> getChilds() {
        return childs;
    }

    @Override
    public void onItemClicked(View view, int position) {

    }

    protected class VHAllclass extends RecyclerView.ViewHolder implements View.OnClickListener {

        private CustomTextViewBoldAr ctvChildName;
        private CustomTextView ctvBirthday;
        private CustomTextView tvClassName;
        private ImageView ivStudent;


        public VHAllclass(View itemView) {
            super(itemView);
            ctvChildName = (CustomTextViewBoldAr) itemView.findViewById(R.id.ctvChildName);
            ctvBirthday = (CustomTextView) itemView.findViewById(R.id.ctvBirthday);
            tvClassName = (CustomTextView) itemView.findViewById(R.id.tvClassName);
            ivStudent = (ImageView) itemView.findViewById(R.id.ivStudent);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onItemClicked(v, getLayoutPosition());
        }
    }
}