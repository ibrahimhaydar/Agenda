package com.mobiweb.ibrahim.agenda.Adapters;

/**
 * Created by ibrahim on 1/28/2018.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.mobiweb.ibrahim.agenda.Adapters.interfaces.RVOnItemClickListener;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextView;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextViewAr;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextViewBold;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextViewBoldAr;
import com.mobiweb.ibrahim.agenda.R;
import com.mobiweb.ibrahim.agenda.models.entities.Activities;
import com.mobiweb.ibrahim.agenda.utils.RetrofitClient;

import java.util.ArrayList;

/**
 * Created by ibrahim on 11/8/2017.
 */


public class AdapterActivities extends RecyclerView.Adapter<AdapterActivities.VHActivities> {

    private ArrayList<Activities> activities;
    private RVOnItemClickListener itemClickListener;
    private boolean isTeacher = false;


    public AdapterActivities(ArrayList<Activities> activities, RVOnItemClickListener itemClickListener) {
        this.activities = activities;
        this.itemClickListener = itemClickListener;
    }


    public AdapterActivities(ArrayList<Activities> activities, RVOnItemClickListener itemClickListener, boolean isTeacher) {
        this.activities = activities;
        this.itemClickListener = itemClickListener;
        this.isTeacher = isTeacher;
    }

    @Override
    public VHActivities onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VHActivities(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_activities, parent, false));
    }

    @Override
    public void onBindViewHolder(VHActivities holder, int position) {
        try{
            Log.wtf("imagePath",RetrofitClient.BASE_URL+"activities/"+activities.get(position).getImages().get(0).getImageName());
            // holder.ivActivity.setImageResource(R.drawable.logo_new);
            // loadImage(holder.itemView.getContext(),holder.ivActivity, RetrofitClient.BASE_URL+"activities/"+activities.get(position).getImages().get(0).getImageName());
        }catch (Exception e){

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
        }catch (Exception e){}



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
        }catch (Exception e){}




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
        private ImageView ivActivity;



        public VHActivities(View itemView) {
            super(itemView);

            description = (CustomTextView) itemView.findViewById(R.id.ctvDescription);
            descriptionAr = (CustomTextViewAr) itemView.findViewById(R.id.ctvDescriptionAr);
            title= (CustomTextViewBold) itemView.findViewById(R.id.ctvTitle);
            titleAr=(CustomTextViewBoldAr)itemView.findViewById(R.id.ctvTitleAr);
            ivActivity=(ImageView)itemView.findViewById(R.id.ivActivity);

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

    private void loadImage(Context context, final ImageView imageView, String url){
        Glide.with(context).load(url).asBitmap().diskCacheStrategy(DiskCacheStrategy.ALL).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                imageView.setImageBitmap(bitmap);
            }

            @Override
            public void onLoadCleared(Drawable placeholder) {
                super.onLoadCleared(placeholder);
                imageView.setImageBitmap(null);
            }
        });
    }
}