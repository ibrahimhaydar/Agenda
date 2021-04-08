package com.mobiweb.ibrahim.agenda.Adapters;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.mobiweb.ibrahim.agenda.Adapters.interfaces.IFragmentImages;
import com.mobiweb.ibrahim.agenda.Custom.TouchImageView;
import com.mobiweb.ibrahim.agenda.R;
import com.mobiweb.ibrahim.agenda.activities.parents.agenda.ActivityFiles;
import com.mobiweb.ibrahim.agenda.models.entities.Files;
import com.mobiweb.ibrahim.agenda.utils.AppConstants;
import com.mobiweb.ibrahim.agenda.utils.RetrofitClient;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterPagerFiles extends PagerAdapter implements View.OnClickListener {

    private ArrayList<Files> files;
    private IFragmentImages fragmentImages;
    private String foldername;
    private Boolean showFullScreen;

    public Long currentPosition;
    public Boolean isPlaying=false;


    public AdapterPagerFiles(ArrayList<Files> files, IFragmentImages fragmentImages, String foldername, Boolean showFullScreen, Long currentPosition){
        this.files = files;
        this.fragmentImages = fragmentImages;
        this.foldername=foldername;
        this.showFullScreen=showFullScreen;
        this.currentPosition=currentPosition;
    }

    @Override
    public int getCount() {
        return files.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        final View itemView = LayoutInflater.from(container.getContext()).inflate(R.layout.item_pager_files, container, false);

        itemView.findViewById(R.id.ivImages).setOnClickListener(this);
        container.addView(itemView);
        if(showFullScreen && files.get(position).getFileType().matches(AppConstants.FILE_TYPE_VIDEO))
            ((ImageView) itemView.findViewById(R.id.btFullScreen)).setVisibility(View.VISIBLE);
        else
            ((ImageView) itemView.findViewById(R.id.btFullScreen)).setVisibility(View.INVISIBLE);




        if(files.get(position).getFileType().matches(AppConstants.FILE_TYPE_VIDEO)) {
            Log.wtf("file_video_url","file : "+ RetrofitClient.BASE_URL + foldername + "/" + files.get(position));
            ((SimpleExoPlayerView) itemView.findViewById(R.id.epView)).setVisibility(View.VISIBLE);
            ((TouchImageView) itemView.findViewById(R.id.ivImages)).setVisibility(View.GONE);
            foldername="uploads/videos";
            loadVideo(itemView.getContext(), (SimpleExoPlayerView) itemView.findViewById(R.id.epView), RetrofitClient.BASE_URL + foldername + "/" + files.get(position).getFileName(), (ProgressBar) itemView.findViewById(R.id.progress),(ImageView) itemView.findViewById(R.id.btFullScreen),position);

        }else {
            Log.wtf("file_image_url","file : "+ RetrofitClient.BASE_URL + foldername + "/" + files.get(position));

            ((SimpleExoPlayerView) itemView.findViewById(R.id.epView)).setVisibility(View.GONE);
            ((TouchImageView) itemView.findViewById(R.id.ivImages)).setVisibility(View.VISIBLE);
            foldername="uploads/images";
            loadImage(itemView.getContext(), (TouchImageView) itemView.findViewById(R.id.ivImages), RetrofitClient.BASE_URL + foldername + "/" + files.get(position).getFileName(), (ProgressBar) itemView.findViewById(R.id.progress));

        }
       // AppHelper.setImage(itemView.getContext(),(ImageView) itemView.findViewById(R.id.ivImages), RetrofitClient.BASE_URL+foldername+"/"+files.get(position));
        return itemView;
    }

    @Override
    public void onClick(View v) {
        fragmentImages.onPageClicked(v);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        try {
            currentPosition=0l;


            ((SimpleExoPlayerView) container.getChildAt(position).findViewById(R.id.epView)).getPlayer().setPlayWhenReady(false);
            ((SimpleExoPlayerView) container.getChildAt(position).findViewById(R.id.epView)).getPlayer().getPlaybackState();




        }catch (Exception e){
               Log.wtf("exception",e.toString());
        }

        try{
            container.removeView((View) object);
            Glide.clear((View) object);
        }catch (Exception e){}
    }

    private void loadImage(final Context context, final TouchImageView imageView, final String url, final ProgressBar progressBar){
  /*      Glide.with(context)
                .load(url)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .fitCenter()
                .crossFade()


                .thumbnail(
                       *//* Glide.with(context)
                                .load(R.drawable.loading)
                                .asBitmap()*//*

      //  Glide.with(context).load(R.raw.loading_gif).into(new GlideDrawableImageViewTarget(imageView))
                        Glide.with(context).load(R.drawable.loading_gif)

                )

                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                        imageView.setImageBitmap(bitmap);
                        imageView.setZoom(1);
                        Log.wtf("loaded","loaded");


                    }

                    @Override
                    public void onLoadCleared(Drawable placeholder) {
                        super.onLoadCleared(placeholder);
                        imageView.setImageBitmap(null);
                        Log.wtf("loaded","clear");

                    }
                });*/





        Picasso.get()
                .load( url )
                .placeholder( R.drawable.progress_animation )
                .into( imageView );


    }



    private void loadVideo(final Context context, final SimpleExoPlayerView exoPlayerView, String url, final ProgressBar progressBar, final ImageView ivFullScreen, final int position){

       Log.wtf("load_vid",url);
        exoPlayerView.setUseController(true);
        exoPlayerView.showController();
        exoPlayerView.requestFocus();




     //   if(player == null) {
      final SimpleExoPlayer player = ExoPlayerFactory.newSimpleInstance(new DefaultRenderersFactory(context),
                    new DefaultTrackSelector(), new DefaultLoadControl());
            // Prepare the player with the source.
            player.prepare(new ExtractorMediaSource(Uri.parse(url), new DefaultHttpDataSourceFactory("ua"),
                    new DefaultExtractorsFactory(), null, null), true, false);
           // player.seekTo(-1);
            player.seekTo(currentPosition);
         //   if(currentPosition==-1L)
                player.setPlayWhenReady(false);
        /*    else
                player.setPlayWhenReady(true);*/

     //   }
        exoPlayerView.setPlayer(player);


        ivFullScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(context, ActivityFiles.class);
                i.putExtra(AppConstants.IMAGES_FOLDER,foldername);
                i.putExtra(AppConstants.INTENT_FROM,"0");
                i.putExtra(AppConstants.IMAGE_POSITION, position);
                i.putExtra(AppConstants.SEEK_POSITION, player.getCurrentPosition());
                player.setPlayWhenReady(false);
                player.getPlaybackState();
                context.startActivity(i);



          /*      player.setPlayWhenReady(false);
                player.getPlaybackState();*/

                  //  player.seekTo(0);


            }
        });

    }

    public void pausePlayer(){
        isPlaying=true;

    }


}
