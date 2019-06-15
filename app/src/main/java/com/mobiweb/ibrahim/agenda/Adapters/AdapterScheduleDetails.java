package com.mobiweb.ibrahim.agenda.Adapters;

/**
 * Created by ibrahim on 4/28/2018.
 */


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.mobiweb.ibrahim.agenda.Adapters.interfaces.RVOnItemClickListener;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextView;
import com.mobiweb.ibrahim.agenda.R;
import com.mobiweb.ibrahim.agenda.models.entities.Info;
import com.mobiweb.ibrahim.agenda.utils.AppConstants;

import java.util.ArrayList;


public class AdapterScheduleDetails extends RecyclerView.Adapter<AdapterScheduleDetails.VHAllclass> {

    private ArrayList<Info> details;
    private RVOnItemClickListener itemClickListener;


    public AdapterScheduleDetails(ArrayList<Info> details) {
        this.details = details;

    }

    @Override
    public VHAllclass onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VHAllclass(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_exam_details, parent, false));
    }

    @Override
    public void onBindViewHolder(VHAllclass holder, int position) {
      

          holder.ctvDetails.setText(details.get(position).getTitle());
          holder.ctvTo.setText(details.get(position).getTo().substring(0,details.get(position).getTo().length()-3));
          holder.ctvFrom.setText(details.get(position).getFrom().substring(0,details.get(position).getFrom().length()-3));
/*        if(details.get(position).getType().matches(AppConstants.TYPE_BREAK)) {
            holder.linearDetails.setVisibility(View.GONE);
            holder.breakView.setVisibility(View.VISIBLE);
        }
        else {*/



            holder.linearDetails.setVisibility(View.VISIBLE);
            holder.breakView.setVisibility(View.GONE);

        if(details.get(position).isBeforeIsBreak())
            holder.breakBefore.setVisibility(View.VISIBLE);
        else
            holder.breakBefore.setVisibility(View.GONE);


        if(details.get(position).isAfterIsBreak())
            holder.breakAfter.setVisibility(View.VISIBLE);
        else
            holder.breakAfter.setVisibility(View.GONE);

        if(details.get(position).getType().matches(AppConstants.TYPE_OFF) || details.get(position).getType().matches(AppConstants.TYPE_REVISION)){
            holder.ctvTo.setVisibility(View.GONE);
            holder.ctvFrom.setVisibility(View.GONE);
            holder.breakBefore.setVisibility(View.GONE);
            holder.breakAfter.setVisibility(View.GONE);
            holder.linearFrom.setVisibility(View.GONE);
            holder.linearTo.setVisibility(View.GONE);
        }else {
            holder.ctvTo.setVisibility(View.VISIBLE);
            holder.ctvFrom.setVisibility(View.VISIBLE);
            holder.linearFrom.setVisibility(View.VISIBLE);
            holder.linearTo.setVisibility(View.VISIBLE);
        }

   //     }


    }

    @Override
    public int getItemCount() {
        return details.size();
    }

    public ArrayList<Info> getclasses() {
        return details;
    }

    protected class VHAllclass extends RecyclerView.ViewHolder implements View.OnClickListener {

        private CustomTextView ctvDetails,ctvFrom,ctvTo;
        private LinearLayout linearDetails,breakView,breakBefore,breakAfter,linearFrom,linearTo;


        public VHAllclass(View itemView) {
            super(itemView);
            ctvDetails = (CustomTextView) itemView.findViewById(R.id.ctvDetais);
            ctvFrom = (CustomTextView) itemView.findViewById(R.id.ctvFrom);
            ctvTo = (CustomTextView) itemView.findViewById(R.id.ctvTo);
            linearDetails=(LinearLayout)itemView.findViewById(R.id.linearDetails);
            breakView=(LinearLayout) itemView.findViewById(R.id.Break);
            breakBefore=(LinearLayout) itemView.findViewById(R.id.BreakBefore);
            breakAfter=(LinearLayout) itemView.findViewById(R.id.BreakAfter);

            linearFrom=(LinearLayout) itemView.findViewById(R.id.linearFrom);
            linearTo=(LinearLayout) itemView.findViewById(R.id.linearTo);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
          //  itemClickListener.onItemClicked(v, getLayoutPosition());
        }
    }
}