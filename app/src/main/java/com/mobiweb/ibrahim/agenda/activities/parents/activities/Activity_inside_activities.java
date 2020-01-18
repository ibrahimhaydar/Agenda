package com.mobiweb.ibrahim.agenda.activities.parents.activities;

import com.mobiweb.ibrahim.agenda.activities.ActivityBase; import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.mobiweb.ibrahim.agenda.Adapters.AdapterImages;
import com.mobiweb.ibrahim.agenda.Adapters.interfaces.IFragmentImages;
import com.mobiweb.ibrahim.agenda.Agenda;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextView;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextViewAr;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextViewBold;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextViewBoldAr;
import com.mobiweb.ibrahim.agenda.R;
import com.mobiweb.ibrahim.agenda.activities.director.Activity_direction_home;
import com.mobiweb.ibrahim.agenda.activities.parents.agenda.ActivityImage;
import com.mobiweb.ibrahim.agenda.utils.AppConstants;
import com.mobiweb.ibrahim.agenda.utils.AppHelper;

import java.util.ArrayList;

/**
 * Created by ibrahim on 11/11/2017.
 */

public class Activity_inside_activities extends ActivityBase implements ViewPager.OnPageChangeListener,IFragmentImages {
    private CustomTextViewBold toolbarTitle;
    private ImageView ivBack,ivRight;
    private String title,withImage,arrayImage;
    private String description;
    private String date;
    private CustomTextViewBoldAr toolbarTitleAr;

    private CustomTextView ctvDesc;
    private Activity activity;
    private ViewPager vpMedia;
    private ArrayList<String> imageUrl=new ArrayList<String>();
    private LinearLayout sliderDotspanel;
    private int dotscount;
    private ImageView[] dots;

    private CustomTextViewBold ctvtitle;
    private CustomTextViewBoldAr ctvtitleAr;
    private CustomTextView ctvdescription;
    private CustomTextViewAr ctvdescriptionAr;
    private CustomTextView ctvdate;
    private CustomTextViewAr ctvdateAr;
    private boolean isPush=false;
    private String pathImagesFolderName="";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inside_activities);
        init();

    }

    private void init(){
        activity=this;
        toolbarTitle=(CustomTextViewBold)findViewById(R.id.toolbarTitle);
        toolbarTitleAr=(CustomTextViewBoldAr)findViewById(R.id.toolbarTitleAr);
        ivBack=(ImageView)findViewById(R.id.ivBack);

        if(getIntent().getStringExtra(AppConstants.INTENT_FROM).matches(AppConstants.INTENT_ACTIVITIES)) {
            pathImagesFolderName="activities";
            toolbarTitle.setText(getString(R.string.activities));
            toolbarTitleAr.setText(getString(R.string.activitiesAr));
        }else {
            pathImagesFolderName="announcement";
            toolbarTitle.setText(getString(R.string.announcement));
            toolbarTitleAr.setText(getString(R.string.announcementAr));
        }

        try{isPush=getIntent().getBooleanExtra(AppConstants.ISPUSH,false);}catch (Exception e){isPush=false;}



        ctvdescription = (CustomTextView) findViewById(R.id.ctvDescription);
        ctvdescriptionAr = (CustomTextViewAr)findViewById(R.id.ctvDescriptionAr);
        ctvtitle= (CustomTextViewBold)findViewById(R.id.ctvTitle);
        ctvtitleAr=(CustomTextViewBoldAr)findViewById(R.id.ctvTitleAr);

        ctvdate = (CustomTextView) findViewById(R.id.ctvDate);
        ctvdateAr = (CustomTextViewAr)findViewById(R.id.ctvDateAr);



        title=getIntent().getStringExtra(AppConstants.TITLE);
        arrayImage=getIntent().getStringExtra(AppConstants.ARRAY_IMAGES);
        withImage=getIntent().getStringExtra(AppConstants.WITH_IMAGE);
        description=getIntent().getStringExtra(AppConstants.DESCRIPTION);
        date=getIntent().getStringExtra(AppConstants.DATE);
        sliderDotspanel = (LinearLayout) findViewById(R.id.SliderDots);
        imageUrl=getIntent().getStringArrayListExtra(AppConstants.HW_IMAGE);
        vpMedia = (ViewPager) findViewById(R.id.vpMedia);
       if(imageUrl.size()>0) {
           for (int i = 0; i < imageUrl.size(); i++)
               Log.wtf("array_image", "notification" + imageUrl.get(i));
       }
         if (AppHelper.isProbablyArabic(description)) {
             Log.wtf("description",description+" arabic");
             ctvdescriptionAr.setText(description);
             ctvdescriptionAr.setVisibility(View.VISIBLE);
             ctvdescription.setVisibility(View.GONE);
          } else {
             Log.wtf("description",description+" english");
             ctvdescription.setText(description);
             ctvdescription.setVisibility(View.VISIBLE);
             ctvdescriptionAr.setVisibility(View.GONE);
            }


        if(getIntent().getStringExtra(AppConstants.INTENT_FROM).matches(AppConstants.INTENT_ACTIVITIES)) {
            if (AppHelper.isProbablyArabic(date)) {
                ctvdateAr.setText(date);
                ctvdateAr.setVisibility(View.VISIBLE);
                ctvdate.setVisibility(View.GONE);
            } else {
                ctvdate.setText(date);
                ctvdate.setVisibility(View.VISIBLE);
                ctvdateAr.setVisibility(View.GONE);

            }
        }else {

            if (AppHelper.isProbablyArabic(description)) {
                ctvdateAr.setText(date);
                ctvdateAr.setVisibility(View.VISIBLE);
                ctvdate.setVisibility(View.GONE);
                ctvdateAr.setTypeface(AppHelper.getTypeFace(this));

            } else {
                ctvdate.setText(date);
                ctvdate.setVisibility(View.VISIBLE);
                ctvdateAr.setVisibility(View.GONE);
            }
        }
            if (AppHelper.isProbablyArabic(title)) {
            ctvtitleAr.setText(title);
            ctvtitleAr.setVisibility(View.VISIBLE);
            ctvtitle.setVisibility(View.GONE);


            } else {
               ctvtitle.setText(title);
               ctvtitle.setVisibility(View.VISIBLE);
               ctvtitleAr.setVisibility(View.GONE);

            }


        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isPush && ((Agenda)getApplication()).getCashedType().matches(AppConstants.LOGIN_DIRECTION) || isPush && ((Agenda)getApplication()).getCashedType().matches(AppConstants.LOGIN_PARENT))
                    startActivity(new Intent(Activity_inside_activities.this,Activity_direction_home.class));
                else
                    Activity_inside_activities.super.onBackPressed();
            }
        });
        ivRight=(ImageView)findViewById(R.id.ivRight);
        ivRight.setVisibility(View.GONE);

try{

        if(!imageUrl.isEmpty()) {
            AdapterImages adapterImages = new AdapterImages(imageUrl, this,pathImagesFolderName);
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





        if(!imageUrl.isEmpty()){
            vpMedia.setVisibility(View.VISIBLE);

        }else {
            vpMedia.setVisibility(View.GONE);
        }

    }catch (Exception e){

    vpMedia.setVisibility(View.GONE);
}
    }


    @Override
    public void onBackPressed() {
        if((isPush && ((Agenda)getApplication()).getCashedType().matches(AppConstants.LOGIN_DIRECTION)) || (isPush && ((Agenda)getApplication()).getCashedType().matches(AppConstants.LOGIN_PARENT)))
            startActivity(new Intent(Activity_inside_activities.this,Activity_direction_home.class));
        else
           super.onBackPressed();
    }

    @Override
    public void onPageClicked(View v) {
        Intent i=new Intent(Activity_inside_activities.this,ActivityImage.class);
        i.putStringArrayListExtra(AppConstants.HW_IMAGE,imageUrl);
        i.putExtra(AppConstants.INTENT_FROM,"0");
        i.putExtra(AppConstants.IMAGE_POSITION, vpMedia.getCurrentItem());
        i.putExtra(AppConstants.IMAGES_FOLDER,pathImagesFolderName);
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
