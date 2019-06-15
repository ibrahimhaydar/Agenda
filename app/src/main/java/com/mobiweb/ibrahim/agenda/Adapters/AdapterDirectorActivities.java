package com.mobiweb.ibrahim.agenda.Adapters;

/**
 * Created by ibrahim on 1/28/2018.
 */

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.mobiweb.ibrahim.agenda.Adapters.interfaces.RVOnItemClickListener;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextView;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextViewAr;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextViewBold;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextViewBoldAr;
import com.mobiweb.ibrahim.agenda.R;
import com.mobiweb.ibrahim.agenda.models.entities.Activities;
import com.mobiweb.ibrahim.agenda.utils.AppHelper;
import com.mobiweb.ibrahim.agenda.utils.RetrofitClient;

import java.util.ArrayList;

/**
 * Created by ibrahim on 11/8/2017.
 */


public class AdapterDirectorActivities extends RecyclerView.Adapter<AdapterDirectorActivities.VHActivities> {

    private ArrayList<Activities> activities;
    private RVOnItemClickListener itemClickListener;
    private boolean isDirection = false;


    public AdapterDirectorActivities(ArrayList<Activities> activities, RVOnItemClickListener itemClickListener) {
        this.activities = activities;
        this.itemClickListener = itemClickListener;
    }


    public AdapterDirectorActivities(ArrayList<Activities> activities, RVOnItemClickListener itemClickListener, boolean isDirection) {
        this.activities = activities;
        this.itemClickListener = itemClickListener;
        this.isDirection = isDirection;
    }

    @Override
    public VHActivities onCreateViewHolder(ViewGroup parent, int viewType) {
        if(isDirection)
            return new VHActivities(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_director_activities, parent, false));
        else
            return new VHActivities(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_activities, parent, false));

    }


    @Override
    public void onBindViewHolder(VHActivities holder, int position) {


        try{
           Log.wtf("imagePath", RetrofitClient.BASE_URL+"activities/"+activities.get(position).getImages().get(0).getImageName());
            // holder.ivActivity.setImageResource(R.drawable.logo_new);
            if(activities.get(position).getImages().size()>0)
                AppHelper.setImage(holder.itemView.getContext(),holder.ivActivity, RetrofitClient.BASE_URL+"activities/"+activities.get(position).getImages().get(0).getImageName());
             else
                holder.ivActivity.setBackgroundColor(holder.itemView.getResources().getColor(R.color.gray_video));
        }catch (Exception e){
            holder.ivActivity.setBackgroundColor(holder.itemView.getResources().getColor(R.color.gray_video));

        }


        try {
            if (isProbablyArabic(activities.get(position).getDescription())) {
                try {
                    holder.descriptionAr.setText(activities.get(position).getDescription());
                } catch (Exception e) {
                }
                holder.descriptionAr.setVisibility(View.VISIBLE);
                holder.description.setVisibility(View.GONE);


            } else {
                try {
                    holder.description.setText(activities.get(position).getDescription());
                } catch (Exception e) {
                }
                holder.description.setVisibility(View.VISIBLE);
                holder.descriptionAr.setVisibility(View.GONE);

            }
        } catch (Exception e) {
        }



        try {
            //if (isProbablyArabic(activities.get(position).getActivity_date())) {
            if (isProbablyArabic(activities.get(position).getDescription()) || isProbablyArabic(activities.get(position).getActivity_date())) {
                try {
                    holder.dateAr.setText(activities.get(position).getActivity_date());
                } catch (Exception e) {
                }
                holder.dateAr.setVisibility(View.VISIBLE);
                holder.date.setVisibility(View.GONE);


            } else {
                try {
                    holder.date.setText(activities.get(position).getActivity_date());
                } catch (Exception e) {
                }
                holder.date.setVisibility(View.VISIBLE);
                holder.dateAr.setVisibility(View.GONE);

            }
        } catch (Exception e) {
        }



        try {
            if (isProbablyArabic(activities.get(position).getTitle())) {
                try {
                    holder.titleAr.setText(activities.get(position).getTitle());
                } catch (Exception e) {
                }
                holder.titleAr.setVisibility(View.VISIBLE);
                holder.title.setVisibility(View.GONE);


            } else {
                try {
                    holder.title.setText(activities.get(position).getTitle());
                } catch (Exception e) {
                }
                holder.title.setVisibility(View.VISIBLE);
                holder.titleAr.setVisibility(View.GONE);

            }
        } catch (Exception e) {
        }
        try {


        if (activities.get(position).getImages().isEmpty())
            holder.btEditAlbum.setText("Add Album");
        else
            holder.btEditAlbum.setText("Edit Album");
    }catch (Exception e){}
        holder.setIsRecyclable(false);
    }

    @Override
    public int getItemCount() {
        return activities.size();
    }

    public ArrayList<Activities> getactivities() {
        return activities;
    }

    protected class VHActivities extends RecyclerView.ViewHolder implements View.OnClickListener {

        private CustomTextViewBold title;
        private CustomTextViewBoldAr titleAr;
        private CustomTextView description;
        private CustomTextViewAr descriptionAr;

        private CustomTextView date;
        private CustomTextViewAr dateAr;

        private Button btEdit;
        private Button btEditAlbum,btDelete;
        private LinearLayout linearButtons;
        private ImageView ivActivity;


        public VHActivities(View itemView) {
            super(itemView);

            description = (CustomTextView) itemView.findViewById(R.id.ctvDescription);
            descriptionAr = (CustomTextViewAr) itemView.findViewById(R.id.ctvDescriptionAr);
            date = (CustomTextView) itemView.findViewById(R.id.ctvDate);
            dateAr = (CustomTextViewAr) itemView.findViewById(R.id.ctvDateAr);
            title= (CustomTextViewBold) itemView.findViewById(R.id.ctvTitle);
            titleAr=(CustomTextViewBoldAr)itemView.findViewById(R.id.ctvTitleAr);
            btEdit=(Button)itemView.findViewById(R.id.btEdit);
            btEditAlbum=(Button)itemView.findViewById(R.id.btAddEditAlbum);
            btDelete=(Button)itemView.findViewById(R.id.btDeleteActivity);
            linearButtons=(LinearLayout)itemView.findViewById(R.id.linearButtons);
            ivActivity=(ImageView)itemView.findViewById(R.id.ivActivity);

           if(isDirection)
               linearButtons.setVisibility(View.VISIBLE);
            else
                linearButtons.setVisibility(View.GONE);

            btEdit.setOnClickListener(this);
            btEditAlbum.setOnClickListener(this);
            btDelete.setOnClickListener(this);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            itemClickListener.onItemClicked(v, getLayoutPosition());
        }
    }


    public static boolean isProbablyArabic(String s) {
        for (int i = 0; i < s.length(); ) {
            int c = s.codePointAt(i);
            if (c >= 0x0600 && c <= 0x06E0)
                return true;
            i += Character.charCount(c);
        }
        return false;
    }

}