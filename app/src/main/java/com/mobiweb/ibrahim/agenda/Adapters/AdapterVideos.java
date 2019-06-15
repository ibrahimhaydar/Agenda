package com.mobiweb.ibrahim.agenda.Adapters;

/**
 * Created by ibrahim on 3/5/2018.
 */

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.github.ybq.android.spinkit.SpinKitView;
import com.mobiweb.ibrahim.agenda.Adapters.interfaces.RVOnItemClickListener;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextView;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextViewAr;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextViewBold;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextViewBoldAr;
import com.mobiweb.ibrahim.agenda.R;
import com.mobiweb.ibrahim.agenda.models.entities.Videos;
import com.mobiweb.ibrahim.agenda.utils.AppHelper;
import com.mobiweb.ibrahim.agenda.utils.RetrofitClient;

import java.util.ArrayList;
import java.util.HashMap;


public class AdapterVideos extends RecyclerView.Adapter<AdapterVideos.VHVideos> {

    private ArrayList<Videos> videos;
    private RVOnItemClickListener itemClickListener;
    private boolean isDirection = false;



    public AdapterVideos(ArrayList<Videos> videos, RVOnItemClickListener itemClickListener) {
        this.videos = videos;
        this.itemClickListener = itemClickListener;
    }


    public AdapterVideos(ArrayList<Videos> videos, RVOnItemClickListener itemClickListener, boolean isDirection) {
        this.videos = videos;
        this.itemClickListener = itemClickListener;
        this.isDirection = isDirection;
    }

    @Override
    public VHVideos onCreateViewHolder(ViewGroup parent, int viewType) {
       if(isDirection)
         return new VHVideos(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video_direction, parent, false));
        else
           return new VHVideos(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video, parent, false));


    }

    @Override
    public void onBindViewHolder(final VHVideos holder, int position) {



        try {
            if (isProbablyArabic(videos.get(position).getDescription())) {
                try {
                    holder.descriptionAr.setText(videos.get(position).getDescription());
                } catch (Exception e) {
                }
                holder.descriptionAr.setVisibility(View.VISIBLE);
                holder.description.setVisibility(View.GONE);


            } else {
                try {
                    holder.description.setText(videos.get(position).getDescription());
                } catch (Exception e) {
                }
                holder.description.setVisibility(View.VISIBLE);
                holder.descriptionAr.setVisibility(View.GONE);

            }
        }catch (Exception e){}



        try {
            if (isProbablyArabic(videos.get(position).getTitle())) {
                try {
                    holder.titleAr.setText(videos.get(position).getTitle());
                } catch (Exception e) {
                }
                holder.titleAr.setVisibility(View.VISIBLE);
                holder.title.setVisibility(View.GONE);


            } else {
                try {
                    holder.title.setText(videos.get(position).getTitle());
                } catch (Exception e) {
                }
                holder.title.setVisibility(View.VISIBLE);
                holder.titleAr.setVisibility(View.GONE);

            }
        }catch (Exception e){}

     //   holder.ivThumb.setImageBitmap(ThumbnailUtils.createVideoThumbnail(RetrofitClient.BASE_URL+"videos/"+videos.get(position).getUrl(), MediaStore.Video.Thumbnails.FULL_SCREEN_KIND));

/*        try {
            holder.ivThumb.setImageBitmap(retriveVideoFrameFromVideo(RetrofitClient.BASE_URL+"videos/"+videos.get(position).getUrl()));
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }*/

        try {

            Glide.with(holder.itemView.getContext())
                    .load(RetrofitClient.BASE_URL+"videos/"+videos.get(position).getThumb())
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            holder.ivPlay.setVisibility(View.GONE);
                            holder.spin_kit.setVisibility(View.VISIBLE);

                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            holder.spin_kit.setVisibility(View.GONE);
                            holder.ivPlay.setVisibility(View.VISIBLE);
                            return false;
                        }
                    })
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    //.thumbnail(Glide.with(holder.itemView.getContext()).load(R.drawable.loading))
                    .dontAnimate().into(holder.ivThumb);
        } catch (Throwable throwable) {
           holder.ivThumb.setImageResource(R.drawable.logo_new);
        }

      /*  if(videos.get(position).getThumb().isEmpty())
            holder.ivPlay.setVisibility(View.GONE);
        else
            holder.ivPlay.setVisibility(View.VISIBLE);*/


        holder.setIsRecyclable(false);


        //  AppHelper.setImage(holder.itemView.getContext(),holder.ivThumb,);
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    public ArrayList<Videos> getvideos() {
        return videos;
    }

    protected class VHVideos extends RecyclerView.ViewHolder implements View.OnClickListener {

        private CustomTextViewBold title;
        private CustomTextViewBoldAr titleAr;
        private CustomTextView description;
        private CustomTextViewAr descriptionAr;
        private ImageView ivThumb;
        private SpinKitView spin_kit;

        private Button btEdit;
        private Button btEditAlbum,btDelete;
        private LinearLayout linearButtons;
        private ImageView ivPlay;



        public VHVideos(View itemView) {
            super(itemView);
            spin_kit=(SpinKitView)itemView.findViewById(R.id.spin_kit) ;
            description = (CustomTextView) itemView.findViewById(R.id.ctvDescription);
            descriptionAr = (CustomTextViewAr) itemView.findViewById(R.id.ctvDescriptionAr);
            title= (CustomTextViewBold) itemView.findViewById(R.id.ctvTitle);
            titleAr=(CustomTextViewBoldAr)itemView.findViewById(R.id.ctvTitleAr);
            ivThumb=(ImageView) itemView.findViewById(R.id.ivVideo);
            btEdit=(Button)itemView.findViewById(R.id.btEdit);
            btDelete=(Button)itemView.findViewById(R.id.btDeleteActivity);
            linearButtons=(LinearLayout)itemView.findViewById(R.id.linearButtons);
            ivPlay=(ImageView)itemView.findViewById(R.id.ivPlay);
            if(isDirection)
                linearButtons.setVisibility(View.VISIBLE);
            else
                linearButtons.setVisibility(View.GONE);

            btEdit.setOnClickListener(this);
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



    public static Bitmap retriveVideoFrameFromVideo(String videoPath) throws Throwable
    {
        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try
        {
            mediaMetadataRetriever = new MediaMetadataRetriever();
            if (Build.VERSION.SDK_INT >= 14)
                mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());
            else
                mediaMetadataRetriever.setDataSource(videoPath);
            //   mediaMetadataRetriever.setDataSource(videoPath);
            bitmap = mediaMetadataRetriever.getFrameAtTime();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Throwable("Exception in retriveVideoFrameFromVideo(String videoPath)" + e.getMessage());

        } finally {
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release();
            }
        }
        return bitmap;
    }




}