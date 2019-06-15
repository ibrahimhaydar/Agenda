package com.mobiweb.ibrahim.agenda.Adapters;


import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mobiweb.ibrahim.agenda.Adapters.interfaces.RVOnItemClickListener;
import com.mobiweb.ibrahim.agenda.R;
import com.mobiweb.ibrahim.agenda.models.entities.Image;
import com.mobiweb.ibrahim.agenda.utils.RetrofitClient;

import java.io.File;
import java.util.ArrayList;


public class AdapterAddImages extends RecyclerView.Adapter<AdapterAddImages.VHPickedImages> {

    private ArrayList<Image> images;
    private RVOnItemClickListener itemClickListener;


    public AdapterAddImages(ArrayList<Image> images, RVOnItemClickListener itemClickListener) {
        this.images = images;
        this.itemClickListener = itemClickListener;
    }

    @Override
    public VHPickedImages onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VHPickedImages(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_picked_image, parent, false));
    }

    @Override
    public void onBindViewHolder(VHPickedImages holder, int position) {

if(new File(images.get(position).getImageName()).exists()){
    Glide.with(holder.itemView.getContext())
            .load(new File(images.get(position).getImageName()))
            .diskCacheStrategy(DiskCacheStrategy.ALL).dontAnimate()
            .into(holder.ivPickedImage);



}else {
try {
    Log.wtf("imagePath: ",RetrofitClient.BASE_URL + "activities/" + images.get(position).getImageName());
    Glide.with(holder.itemView.getContext())
            .load(RetrofitClient.BASE_URL + "activities/" + images.get(position).getImageName())
            .diskCacheStrategy(DiskCacheStrategy.ALL).dontAnimate()
            .into(holder.ivPickedImage);
}catch (Exception e2) {

}
}

    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public ArrayList<Image> getimages() {
        return images;
    }

    protected class VHPickedImages extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView ivPickedImage,ivClose;


        public VHPickedImages(View itemView) {
            super(itemView);
            ivPickedImage = (ImageView) itemView.findViewById(R.id.ivPickedImage);
            ivClose = (ImageView) itemView.findViewById(R.id.ivClose);


            ivClose.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onItemClicked(v, getLayoutPosition());
        }
    }
}