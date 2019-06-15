package com.mobiweb.ibrahim.agenda.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.mobiweb.ibrahim.agenda.Adapters.interfaces.IFragmentImages;
import com.mobiweb.ibrahim.agenda.R;
import com.mobiweb.ibrahim.agenda.utils.RetrofitClient;

import java.util.ArrayList;

public class AdapterImages extends PagerAdapter implements View.OnClickListener {

    private ArrayList<String> images;
    private IFragmentImages fragmentImages;
    private String foldername;

    public AdapterImages(ArrayList<String> images, IFragmentImages fragmentImages,String foldername){
        this.images = images;
        this.fragmentImages = fragmentImages;
        this.foldername=foldername;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = LayoutInflater.from(container.getContext()).inflate(R.layout.item_image, container, false);

        itemView.findViewById(R.id.ivImages).setOnClickListener(this);
        container.addView(itemView);

       loadImage(itemView.getContext(),(ImageView) itemView.findViewById(R.id.ivImages), RetrofitClient.BASE_URL+foldername+"/"+images.get(position),(ProgressBar) itemView.findViewById(R.id.progress));
       // AppHelper.setImage(itemView.getContext(),(ImageView) itemView.findViewById(R.id.ivImages), RetrofitClient.BASE_URL+foldername+"/"+images.get(position));
        return itemView;
    }

    @Override
    public void onClick(View v) {
        fragmentImages.onPageClicked(v);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        Glide.clear((View) object);
    }

    private void loadImage(Context context, final ImageView imageView, String url, final ProgressBar progressBar){
        Glide.with(context)
                .load(url)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .fitCenter()
                .crossFade()
                .dontAnimate()
                .thumbnail(
                        Glide.with(context)
                                .load(R.drawable.loading)
                              .asBitmap()



                )

/*.listener(new RequestListener<String, Bitmap>() {
    @Override
    public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
        progressBar.setVisibility(View.GONE);
        return false;
    }

    @Override
    public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
        progressBar.setVisibility(View.GONE);
        return false;
    }
})*/


                .into(new SimpleTarget<Bitmap>() {
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
