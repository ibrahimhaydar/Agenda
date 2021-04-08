package com.mobiweb.ibrahim.agenda.Adapters;




import android.media.ThumbnailUtils;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mobiweb.ibrahim.agenda.Adapters.interfaces.RVOnItemClickListener;
import com.mobiweb.ibrahim.agenda.R;
import com.mobiweb.ibrahim.agenda.models.entities.Files;
import com.mobiweb.ibrahim.agenda.utils.AppConstants;
import com.mobiweb.ibrahim.agenda.utils.RetrofitClient;

import java.io.File;
import java.util.ArrayList;


public class AdapterFiles extends RecyclerView.Adapter<AdapterFiles.VHPickedImages> {

    private ArrayList<Files> files;
    private RVOnItemClickListener itemClickListener;


    public AdapterFiles(ArrayList<Files> files, RVOnItemClickListener itemClickListener) {
        this.files = files;
        this.itemClickListener = itemClickListener;
    }

    @Override
    public VHPickedImages onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VHPickedImages(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_picked_image, parent, false));
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(VHPickedImages holder, int position) {

        if(new File(files.get(position).getFileName()).exists()){
            holder.ivPickedFile.setVisibility(View.VISIBLE);
            holder.ivPickedVideo.setVisibility(View.GONE);
            if(files.get(position).getFileType().matches(AppConstants.FILE_TYPE_IMAGE)) {
                Glide.with(holder.itemView.getContext())
                        .load(new File(files.get(position).getFileName()))
                        .diskCacheStrategy(DiskCacheStrategy.ALL).dontAnimate()
                        .into(holder.ivPickedFile);
                holder.ivVideo.setVisibility(View.GONE);
            }else  if(files.get(position).getFileType().matches( AppConstants.FILE_TYPE_VIDEO)) {

                    holder.ivPickedFile.setImageBitmap(ThumbnailUtils.createVideoThumbnail(files.get(position).getFileName(), MediaStore.Video.Thumbnails.FULL_SCREEN_KIND));







                holder.ivVideo.setVisibility(View.VISIBLE);



            }


        }else {

            try {
                holder.ivPickedFile.setVisibility(View.VISIBLE);
                holder.ivPickedVideo.setVisibility(View.GONE);
                holder.ivVideo.setVisibility(View.GONE);

                if(files.get(position).getFileType().matches(AppConstants.FILE_TYPE_IMAGE)) {
                    Log.wtf("file_image: ", RetrofitClient.BASE_URL + "uploads/images/" + files.get(position).getFileName());
                    Glide.with(holder.itemView.getContext())
                            .load(RetrofitClient.BASE_URL + "uploads/images/" + files.get(position).getFileName())
                            .diskCacheStrategy(DiskCacheStrategy.ALL).dontAnimate()
                            .into(holder.ivPickedFile);
                    holder.ivVideo.setVisibility(View.GONE);
                }else  if(files.get(position).getFileType().matches( AppConstants.FILE_TYPE_VIDEO)) {
                    Log.wtf("file_video: ", RetrofitClient.BASE_URL + "uploads/videos/" + files.get(position).getFileName());
              /*      MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                    retriever.setDataSource(RetrofitClient.BASE_URL + "uploads/videos/" + files.get(position).getFileName(), new HashMap<String, String>());
                    Bitmap image = retriever.getFrameAtTime(2000000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
                    holder.ivPickedFile.setImageBitmap(image);*/


                    holder.ivPickedFile.setVisibility(View.GONE);

                    holder.ivPickedVideo.setVisibility(View.VISIBLE);
                    holder.ivPickedVideo.setVideoPath(RetrofitClient.BASE_URL + "uploads/videos/" + files.get(position).getFileName());
                    holder.ivPickedVideo.seekTo( 1 );
                    holder.ivPickedVideo.setZOrderOnTop(true);




       /*             holder.ivPickedFile.setImageBitmap(ThumbnailUtils.createVideoThumbnail(RetrofitClient.BASE_URL + "uploads/videos/" + files.get(position).getFileName(),
                            MediaStore.Video.Thumbnails.MICRO_KIND));
                    holder.ivVideo.setVisibility(View.VISIBLE);*/

                }
            }catch (Exception e2) {
              Log.wtf("file_exception",e2);
            }
        }

    }

    @Override
    public int getItemCount() {
        return files.size();
    }

    public ArrayList<Files> getimages() {
        return files;
    }

    protected class VHPickedImages extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView ivPickedFile,ivClose,ivVideo;
        private VideoView ivPickedVideo;


        public VHPickedImages(View itemView) {
            super(itemView);
            ivPickedVideo = (VideoView) itemView.findViewById(R.id.ivPickedVideo);
            ivPickedFile = (ImageView) itemView.findViewById(R.id.ivPickedFile);
            ivClose = (ImageView) itemView.findViewById(R.id.ivClose);
            ivVideo = (ImageView) itemView.findViewById(R.id.ivVideo);

            ivClose.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onItemClicked(v, getLayoutPosition());
        }
    }
}