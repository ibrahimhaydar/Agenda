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
import com.mobiweb.ibrahim.agenda.models.entities.Info;
import com.mobiweb.ibrahim.agenda.models.json.SchedulesExamsDetail;
import com.mobiweb.ibrahim.agenda.utils.AppConstants;

import java.util.ArrayList;


public class AdapterDates extends RecyclerView.Adapter<AdapterDates.VHAllclass> implements RVOnItemClickListener{

    private ArrayList<SchedulesExamsDetail> dates;
    private RVOnItemClickListener itemClickListener;
    private  AdapterScheduleDetails adapterScheduleDetails;



    public AdapterDates(ArrayList<SchedulesExamsDetail> dates, RVOnItemClickListener itemClickListener) {
        this.dates = dates;
        this.itemClickListener = itemClickListener;
    }

    @Override
    public VHAllclass onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VHAllclass(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dates, parent, false));
    }

    @Override
    public void onBindViewHolder(VHAllclass holder, int position) {
        holder.ctvDate.setText(dates.get(position).getDate());
        int breakCount=0;
        ArrayList<Info> arrayNoBreak=new ArrayList<>();
        for (int i=0;i<dates.get(position).getInfo().size();i++){
            if(dates.get(position).getInfo().get(i).getType().matches(AppConstants.TYPE_BREAK))
                breakCount++;

            try{
                if(dates.get(position).getInfo().get(i-1).getType().matches(AppConstants.TYPE_BREAK))
                    dates.get(position).getInfo().get(i).setBeforeIsBreak(true);
                else
                    dates.get(position).getInfo().get(i).setBeforeIsBreak(false);
            }catch (Exception e){

            }


            try{
                if(dates.get(position).getInfo().get(i+1).getType().matches(AppConstants.TYPE_BREAK))
                    dates.get(position).getInfo().get(i).setAfterIsBreak(true);
                else
                    dates.get(position).getInfo().get(i).setAfterIsBreak(false);
            }catch (Exception e){

            }

            if(!dates.get(position).getInfo().get(i).getType().matches(AppConstants.TYPE_BREAK))
                arrayNoBreak.add(dates.get(position).getInfo().get(i));
        }




        RtlGridLayoutManager glm=new RtlGridLayoutManager(holder.itemView.getContext(),arrayNoBreak.size());
        adapterScheduleDetails=new AdapterScheduleDetails(arrayNoBreak);
        holder.rvDetails.setLayoutManager(glm);
        holder.rvDetails.setAdapter(adapterScheduleDetails);


    }

    @Override
    public int getItemCount() {
        return dates.size();
    }

    public ArrayList<SchedulesExamsDetail> getclasses() {
        return dates;
    }

    @Override
    public void onItemClicked(View view, int position) {

    }

    protected class VHAllclass extends RecyclerView.ViewHolder implements View.OnClickListener {

        private CustomTextView ctvDate;
        private RecyclerView rvDetails;


        public VHAllclass(View itemView) {
            super(itemView);
            ctvDate = (CustomTextView) itemView.findViewById(R.id.ctvDate);
            rvDetails=(RecyclerView)itemView.findViewById(R.id.rvDetails);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onItemClicked(v, getLayoutPosition());
        }
    }
}