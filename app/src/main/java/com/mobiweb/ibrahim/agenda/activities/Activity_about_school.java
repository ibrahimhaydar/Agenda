package com.mobiweb.ibrahim.agenda.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextViewBold;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextViewBoldAr;
import com.mobiweb.ibrahim.agenda.R;


public class Activity_about_school extends AppCompatActivity implements OnMapReadyCallback {
    private CustomTextViewBold toolbarTitle;
    private CustomTextViewBoldAr toolbarTitleAr;
    private ImageView ivBack,ivRight,ivfacebook,ivfacebook_page;
    private GoogleMap map;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_school);
        init();
    }
    private void init(){
        getSupportActionBar().hide();
        toolbarTitle=(CustomTextViewBold)findViewById(R.id.toolbarTitle);
        toolbarTitleAr=(CustomTextViewBoldAr)findViewById(R.id.toolbarTitleAr);
        ivBack=(ImageView)findViewById(R.id.ivBack);
        toolbarTitle.setText(getString(R.string.welcome));
        toolbarTitleAr.setText(getString(R.string.welcome_ar));
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Activity_about_school.super.onBackPressed();
            }
        });
        ivRight=(ImageView)findViewById(R.id.ivRight);
        ivRight.setVisibility(View.GONE);
        ivfacebook=(ImageView)findViewById(R.id.ivfacebook);
        ivfacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             getOpenFacebookIntent();
            }
        });
        ivfacebook_page=(ImageView)findViewById(R.id.ivfacebookPage);
        ivfacebook_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/751309818292779"));
                    startActivity(intent);
                } catch(Exception e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.facebook.com/Lebanese-Al-Ahd-Secondary-ثانوية-العهد-اللبنانية-751309818292779/?hc_ref=ARQ2ADrrdmOZpnODpyGrV4x2O4JA0GBT3Dfas5EnyZ-3wPWthWAQQJZosouoPaAJmxc\n")));
                }


            }
        });



   /*     ivRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity_about_school.this,Activity_Home.class));
            }
        });*/
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



        final ScrollView mainScrollView = (ScrollView) findViewById(R.id.scrollView1);
        ImageView transparentImageView = (ImageView) findViewById(R.id.image_trans);

        transparentImageView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        mainScrollView.requestDisallowInterceptTouchEvent(true);
                        // Disable touch on transparent view
                        return false;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        mainScrollView.requestDisallowInterceptTouchEvent(false);
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        mainScrollView.requestDisallowInterceptTouchEvent(true);
                        return false;

                    default:
                        return true;
                }
            }
        });

    }


    public void getOpenFacebookIntent() {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://profile/100008881782987"));
            startActivity(intent);
        } catch(Exception e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.facebook.com/Lebanese-Al-Ahd-Secondary-ثانوية-العهد-اللبنانية-751309818292779")));
        }


    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        map = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng Location = new LatLng(33.8798621, 36.0857028);
        map.addMarker(new MarkerOptions().position(Location).title("Lebaneese Al-Ahd secondary school"));
        map.moveCamera(CameraUpdateFactory.newLatLng(Location));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(Location, 13));

      /*  LatLng sydney = new LatLng(-33.867, 151.206);

        map.setMyLocationEnabled(true);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 13));

        map.addMarker(new MarkerOptions()
                .title("Sydney")
                .snippet("The most populous city in Australia.")
                .position(sydney));*/
    }
}
