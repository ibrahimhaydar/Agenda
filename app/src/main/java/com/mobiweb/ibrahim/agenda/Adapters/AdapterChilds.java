package com.mobiweb.ibrahim.agenda.Adapters;



/**
 * Created by ibrahim on 4/28/2018.
 */


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mobiweb.ibrahim.agenda.Adapters.interfaces.RVOnItemClickListener;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextView;
import com.mobiweb.ibrahim.agenda.Custom.RtlGridLayoutManager;
import com.mobiweb.ibrahim.agenda.R;
import com.mobiweb.ibrahim.agenda.models.entities.Child;
import com.mobiweb.ibrahim.agenda.models.entities.Info;
import com.mobiweb.ibrahim.agenda.utils.AppConstants;

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

        private CustomTextView ctvChildName;



        public VHAllclass(View itemView) {
            super(itemView);
            ctvChildName = (CustomTextView) itemView.findViewById(R.id.ctvChildName);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onItemClicked(v, getLayoutPosition());
        }
    }
}