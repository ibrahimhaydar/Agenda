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
import com.mobiweb.ibrahim.agenda.models.entities.Announcements;
import com.mobiweb.ibrahim.agenda.utils.AppHelper;
import com.mobiweb.ibrahim.agenda.utils.RetrofitClient;

import java.util.ArrayList;

/**
 * Created by ibrahim on 11/8/2017.
 */


public class AdapterDirectorAnnouncements extends RecyclerView.Adapter<AdapterDirectorAnnouncements.VHAnnouncements> {

    private ArrayList<Announcements> activities;
    private RVOnItemClickListener itemClickListener;
    private boolean isDirection = false;


    public AdapterDirectorAnnouncements(ArrayList<Announcements> activities, RVOnItemClickListener itemClickListener) {
        this.activities = activities;
        this.itemClickListener = itemClickListener;
    }


    public AdapterDirectorAnnouncements(ArrayList<Announcements> activities, RVOnItemClickListener itemClickListener, boolean isDirection) {
        this.activities = activities;
        this.itemClickListener = itemClickListener;
        this.isDirection = isDirection;
    }

    @Override
    public VHAnnouncements onCreateViewHolder(ViewGroup parent, int viewType) {
        if(isDirection)
           return new VHAnnouncements(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_director_announcement, parent, false));
        else
           return new VHAnnouncements(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_annnouncement, parent, false));


    }

    @Override
    public void onBindViewHolder(VHAnnouncements holder, int position) {


        try{
            Log.wtf("imagePath", RetrofitClient.BASE_URL+"announcement/"+activities.get(position).getImages().get(0).getImageName());
            // holder.ivAnnouncement.setImageResource(R.drawable.logo_new);
            if(activities.get(position).getImages().size()>0) {
                AppHelper.setImage(holder.itemView.getContext(), holder.ivAnnouncement, RetrofitClient.BASE_URL + "announcement/" + activities.get(position).getImages().get(0).getImageName());
                holder.ivAnnouncement.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }
            else {
                holder.ivAnnouncement.setBackgroundColor(holder.itemView.getResources().getColor(R.color.gray_video));
                holder.ivAnnouncement.setImageResource(R.drawable.logo_new);
                holder.ivAnnouncement.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            }
        }catch (Exception e){
            holder.ivAnnouncement.setBackgroundColor(holder.itemView.getResources().getColor(R.color.gray_video));
            holder.ivAnnouncement.setImageResource(R.drawable.logo_new);
            holder.ivAnnouncement.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        }
        
        
        
        try {
            if (isProbablyArabic(activities.get(position).getDescription())) {
                try {
                    holder.descriptionAr.setText(activities.get(position).getDescription());

                } catch (Exception e) {
                }
                holder.descriptionAr.setVisibility(View.VISIBLE);
                holder.description.setVisibility(View.GONE);


                try {
                    holder.dateAr.setText(activities.get(position).getAnnouncement_date().substring(0,activities.get(position).getAnnouncement_date().length()-5)+" "+activities.get(position).getAnnouncement_date().substring(activities.get(position).getAnnouncement_date().length()-2,activities.get(position).getAnnouncement_date().length()));
                } catch (Exception e) {
                }
                holder.dateAr.setVisibility(View.VISIBLE);
                holder.date.setVisibility(View.GONE);


            } else {
                try {
                    holder.description.setText(activities.get(position).getDescription());
                } catch (Exception e) {
                }
                holder.description.setVisibility(View.VISIBLE);
                holder.descriptionAr.setVisibility(View.GONE);



                try {
                    holder.date.setText(activities.get(position).getAnnouncement_date().substring(0,activities.get(position).getAnnouncement_date().length()-5)+" "+activities.get(position).getAnnouncement_date().substring(activities.get(position).getAnnouncement_date().length()-2,activities.get(position).getAnnouncement_date().length()));
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

    }

    @Override
    public int getItemCount() {
        return activities.size();
    }

    public ArrayList<Announcements> getactivities() {
        return activities;
    }

    protected class VHAnnouncements extends RecyclerView.ViewHolder implements View.OnClickListener {

        private CustomTextViewBold title;
        private CustomTextViewBoldAr titleAr;

        private CustomTextView date;
        private CustomTextViewAr dateAr;
        private CustomTextView description;
        private CustomTextViewAr descriptionAr;
        private Button btEdit;
        private Button btEditAlbum,btDelete;
        private LinearLayout linearEditAlbum;
        private LinearLayout linearButtons;
        private ImageView ivAnnouncement;

        public VHAnnouncements(View itemView) {
            super(itemView);

            description = (CustomTextView) itemView.findViewById(R.id.ctvDescription);
            descriptionAr = (CustomTextViewAr) itemView.findViewById(R.id.ctvDescriptionAr);

            date = (CustomTextView) itemView.findViewById(R.id.ctvDate);
            dateAr = (CustomTextViewAr) itemView.findViewById(R.id.ctvDateAr);
            dateAr.setTypeface(AppHelper.getTypeFace(itemView.getContext()));

            ivAnnouncement=(ImageView)itemView.findViewById(R.id.ivAnnouncement);

            title= (CustomTextViewBold) itemView.findViewById(R.id.ctvTitle);
            titleAr=(CustomTextViewBoldAr)itemView.findViewById(R.id.ctvTitleAr);
            linearEditAlbum=(LinearLayout)itemView.findViewById(R.id.linearEditAlbum);
            linearEditAlbum.setVisibility(View.GONE);

            btEdit=(Button)itemView.findViewById(R.id.btEdit);
            btEditAlbum=(Button)itemView.findViewById(R.id.btAddEditAlbum);
            btDelete=(Button)itemView.findViewById(R.id.btDeleteActivity);
            linearButtons=(LinearLayout)itemView.findViewById(R.id.linearButtons);

            if(isDirection)
                linearButtons.setVisibility(View.VISIBLE);
            else
                linearButtons.setVisibility(View.GONE);
            btEdit.setOnClickListener(this);
            btEditAlbum.setOnClickListener(this);
            btDelete.setOnClickListener(this);

            itemView.setOnClickListener(this);


            btEdit.setText("Edit Announcement");
            btDelete.setText("Delete Announcement");

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