package com.mobiweb.ibrahim.agenda.activities.parents.agenda;

import com.mobiweb.ibrahim.agenda.activities.ActivityBase; import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.mobiweb.ibrahim.agenda.Adapters.AdapterImages;
import com.mobiweb.ibrahim.agenda.Adapters.interfaces.IFragmentImages;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextView;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextViewBold;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextViewBoldAr;
import com.mobiweb.ibrahim.agenda.R;
import com.mobiweb.ibrahim.agenda.utils.AppConstants;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by ibrahim on 11/11/2017.
 */

public class Activity_inside_agenda extends Activity implements ViewPager.OnPageChangeListener,IFragmentImages {
    private CustomTextViewBold toolbarTitle,ctvdate,ctvHwTitle;
    private ImageView ivBack,ivRight;
    private String title;
    private String description,date,info;
    private CustomTextViewBoldAr toolbarTitleAr;

    private CustomTextView ctvDesc;
    private Activity activity;
    private LinearLayout linearInfo;
    private CustomTextView ctvInfo;
    private ViewPager vpMedia;
    private ArrayList<String> imageUrl=new ArrayList<String>();
    private LinearLayout sliderDotspanel;
    private int dotscount;
    private ImageView[] dots;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inside_agenda);
        init();

        Locale locale = new Locale("en");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        this.getApplicationContext().getResources().updateConfiguration(config, null);
    }

    private void init(){
        activity=this;
        toolbarTitle=(CustomTextViewBold)findViewById(R.id.toolbarTitle);
        toolbarTitleAr=(CustomTextViewBoldAr)findViewById(R.id.toolbarTitleAr);
        ivBack=(ImageView)findViewById(R.id.ivBack);
        ctvInfo=(CustomTextView)findViewById(R.id.ctvInfo);
        linearInfo=(LinearLayout)findViewById(R.id.linearInfo);
        toolbarTitle.setText(getString(R.string.homework));
        toolbarTitleAr.setText(getString(R.string.homework_ar));
        ctvHwTitle=(CustomTextViewBold)findViewById(R.id.title);
        ctvdate=(CustomTextViewBold)findViewById(R.id.date);
        ctvDesc=(CustomTextView)findViewById(R.id.description);
        title=getIntent().getStringExtra(AppConstants.HW_TITLE);
        description=getIntent().getStringExtra(AppConstants.HW_DESC);
        date=getIntent().getStringExtra(AppConstants.HW_DATE);
        info=getIntent().getStringExtra(AppConstants.HW_INFO);
        sliderDotspanel = (LinearLayout) findViewById(R.id.SliderDots);


        imageUrl=getIntent().getStringArrayListExtra(AppConstants.HW_IMAGE);
        vpMedia = (ViewPager) findViewById(R.id.vpMedia);



       if(!imageUrl.isEmpty()) {
           AdapterImages adapterImages = new AdapterImages(imageUrl, this,"uploads");
           vpMedia.setAdapter(adapterImages);
           vpMedia.addOnPageChangeListener(this);
           dotscount = adapterImages.getCount();
           dots = new ImageView[dotscount];
           for (int i = 0; i < dotscount; i++) {
               dots[i] = new ImageView(this);
               dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));
               LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
               params.setMargins(8, 0, 8, 0);
               sliderDotspanel.addView(dots[i], params);

           }
           dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));
           vpMedia.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
               @Override
               public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
               }

               @Override
               public void onPageSelected(int position) {
                   for (int i = 0; i < dotscount; i++) {
                       dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));
                   }
                   dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));

               }

               @Override
               public void onPageScrollStateChanged(int state) {

               }
           });

       }




        ctvHwTitle.setText(title);
        ctvDesc.setText(description);
        ctvdate.setText(date);
        if(!info.matches("")) {
            ctvInfo.setText(info);
            linearInfo.setVisibility(View.VISIBLE);
        }else
            linearInfo.setVisibility(View.GONE);
            ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Locale locale = new Locale("ar_LB");
                Locale.setDefault(locale);
                Configuration config = new Configuration();
                config.locale = locale;
                activity.getApplicationContext().getResources().updateConfiguration(config, null);

                Activity_inside_agenda.super.onBackPressed();
            }
        });
        ivRight=(ImageView)findViewById(R.id.ivRight);
        ivRight.setVisibility(View.GONE);
        if(!imageUrl.isEmpty()){
            vpMedia.setVisibility(View.VISIBLE);
       /*     Glide.with(this)
                    .load(RetrofitClient.BASE_URL+"uploads/"+imageUrl.get(0))
                    .diskCacheStrategy(DiskCacheStrategy.ALL).dontAnimate()
                    .into(ivHw);
            ivHw.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                 Intent i=new Intent(Activity_inside_agenda.this,ActivityImage.class);
                    i.putExtra(AppConstants.HW_IMAGE,imageUrl.get(0));
                    startActivity(i);
                }
            });*/
        }else {
            vpMedia.setVisibility(View.GONE);
        }

    }


    @Override
    public void onBackPressed() {

        Locale locale = new Locale("ar_LB");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        this.getApplicationContext().getResources().updateConfiguration(config, null);
        super.onBackPressed();
    }

    @Override
    public void onPageClicked(View v) {
        Intent i=new Intent(Activity_inside_agenda.this,ActivityImage.class);
        i.putStringArrayListExtra(AppConstants.HW_IMAGE,imageUrl);
        i.putExtra(AppConstants.IMAGES_FOLDER,"uploads");
        i.putExtra(AppConstants.INTENT_FROM,"0");
        i.putExtra(AppConstants.IMAGE_POSITION, vpMedia.getCurrentItem());
        startActivity(i);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }



}
