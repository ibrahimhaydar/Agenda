package com.mobiweb.ibrahim.agenda.Adapters;


import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;

import com.mobiweb.ibrahim.agenda.Custom.CustomTextView;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextViewBoldAr;
import com.mobiweb.ibrahim.agenda.R;
import com.mobiweb.ibrahim.agenda.models.entities.Info;
import com.mobiweb.ibrahim.agenda.models.json.SchedulesExamsDetail;
import com.mobiweb.ibrahim.agenda.utils.AppConstants;
import com.mobiweb.ibrahim.agenda.utils.AppHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;


public class AdapterExpandableExams extends BaseExpandableListAdapter {
    private Context ctx;
    private LinkedHashMap<SchedulesExamsDetail, List<Info>> ChildTitles;
    private List<SchedulesExamsDetail> HeaderTitles;

    public AdapterExpandableExams(Context ctx, LinkedHashMap<SchedulesExamsDetail, List<Info>> ChildTitles, List<SchedulesExamsDetail> HeaderTitles) {
        this.ctx = ctx;
        this.ChildTitles = ChildTitles;
        this.HeaderTitles = HeaderTitles;
    }

    @Override
    public int getGroupCount() {
        return HeaderTitles.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return ChildTitles.get(HeaderTitles.get(i)).size();
    }

    @Override
    public Object getGroup(int i) {
        return HeaderTitles.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return ChildTitles.get(HeaderTitles.get(i)).get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        String date =  ((SchedulesExamsDetail) this.getGroup(i)).getDate();
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) this.ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_exams_schedule_header, null);
            view.setMinimumHeight(150);
        }


        CustomTextViewBoldAr ctvDate = (CustomTextViewBoldAr) view.findViewById(R.id.ctvDate);

        Locale locale = new Locale("ar");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        ctx.getApplicationContext().getResources().updateConfiguration(config, null);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-d",locale);
        Date date3 = null;
        try {
            date3 = sdf.parse(date);
        } catch (Exception e) {

        }
        sdf = new SimpleDateFormat("EEEE,dd MMMM yyyy",locale);
        String format = sdf.format(date3);
        ctvDate.setText(AppHelper.replaceArabicMonth(format));

        try{
            if(((Info) this.getChild(i, 0)).getType().matches(AppConstants.TYPE_OFF) || ((Info) this.getChild(i, 0)).getType().matches(AppConstants.TYPE_REVISION)){
            ctvDate.setBackgroundColor(Color.parseColor("#E97778"));
        }else
            ctvDate.setBackgroundColor(Color.parseColor("#F9F9F9"));

        }catch (Exception e){
            ctvDate.setBackgroundColor(Color.parseColor("#F9F9F9"));
        }

        return view;
    }

    @Override
    public View getChildView(final int i, final int i1, boolean b, View view, ViewGroup viewGroup) {
        String from = ((Info) this.getChild(i, i1)).getFrom().substring(0,((Info) this.getChild(i, i1)).getFrom().length()-3);
        String to = ((Info) this.getChild(i, i1)).getTo().substring(0,((Info) this.getChild(i, i1)).getTo().length()-3);
        String title = ((Info) this.getChild(i, i1)).getTitle();
        String type = ((Info) this.getChild(i, i1)).getType();
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) this.ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_exams_schedule_body, null);
            //view.setMinimumHeight(120);
        }
        CustomTextView ctvFromto = (CustomTextView) view.findViewById(R.id.ctvFromTo);
        CustomTextView ctvExamTitle = (CustomTextView) view.findViewById(R.id.ctvExamTitle);

        CustomTextView ctvCenterTitle = (CustomTextView) view.findViewById(R.id.ctvCenterTitle);
        LinearLayout linearCenter = (LinearLayout) view.findViewById(R.id.linearCenter);
        LinearLayout linearData = (LinearLayout) view.findViewById(R.id.linearData);



        ctvFromto.setText(from +" - "+to);
        ctvExamTitle.setText(title);

        if(((Info) this.getChild(i, i1)).getType().matches(AppConstants.TYPE_OFF) || ((Info) this.getChild(i, i1)).getType().matches(AppConstants.TYPE_REVISION) /*|| ((Info) this.getChild(i, i1)).getType().matches(AppConstants.TYPE_BREAK)*/){
            linearData.setVisibility(View.GONE);
            linearCenter.setVisibility(View.VISIBLE);
            ctvCenterTitle.setText(title);
        }else {
            linearData.setVisibility(View.VISIBLE);
            linearCenter.setVisibility(View.GONE);
        }


        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }





}