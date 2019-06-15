package com.mobiweb.ibrahim.agenda.Adapters;

/**
 * Created by ibrahim on 5/12/2018.
 */


import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mobiweb.ibrahim.agenda.Adapters.interfaces.RVOnItemClickListener;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextView;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextViewAr;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextViewBold;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextViewBoldAr;
import com.mobiweb.ibrahim.agenda.R;
import com.mobiweb.ibrahim.agenda.models.entities.ViewExam;

import java.util.ArrayList;


public class AdapterExamsCoursesDetails extends RecyclerView.Adapter<AdapterExamsCoursesDetails.VHAgenda> {

    private ArrayList<ViewExam> exams;
    private RVOnItemClickListener itemClickListener;



    public AdapterExamsCoursesDetails(ArrayList<ViewExam> exams, RVOnItemClickListener itemClickListener) {
        this.exams = exams;
        this.itemClickListener = itemClickListener;
    }




    @Override
    public VHAgenda onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VHAgenda(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_exam_course_info, parent, false));
    }

    @Override
    public void onBindViewHolder(VHAgenda holder, int position) {



        try {
            if (isProbablyArabic(exams.get(position).getDesc())) {
                try {
                    holder.descriptionAr.setText(exams.get(position).getDesc());
                } catch (Exception e) {
                }
                holder.descriptionAr.setVisibility(View.VISIBLE);
                holder.description.setVisibility(View.GONE);


            } else {
                try {
                    holder.description.setText(exams.get(position).getDesc());
                } catch (Exception e) {
                }
                holder.description.setVisibility(View.VISIBLE);
                holder.descriptionAr.setVisibility(View.GONE);

            }
        }catch (Exception e){}



        try {
            if (isProbablyArabic(exams.get(position).getTitle())) {
                try {
                    holder.ctvTitleAr.setText(exams.get(position).getTitle());
                } catch (Exception e) {
                }
                holder.ctvTitleAr.setVisibility(View.VISIBLE);
                holder.ctvTitle.setVisibility(View.GONE);

            } else {
                try {
                    holder.ctvTitle.setText(exams.get(position).getTitle());
                } catch (Exception e) {
                }
                holder.ctvTitle.setVisibility(View.VISIBLE);
                holder.ctvTitleAr.setVisibility(View.GONE);

            }
        }catch (Exception e){}




    }

    @Override
    public int getItemCount() {
        return exams.size();
    }

    public ArrayList<ViewExam> getclasses() {
        return exams;
    }

    protected class VHAgenda extends RecyclerView.ViewHolder implements View.OnClickListener {
        private CustomTextViewBold ctvTitle;
        private CustomTextViewBoldAr ctvTitleAr;

        private CustomTextView description;
        private CustomTextViewAr descriptionAr;





        public VHAgenda(View itemView) {
            super(itemView);

            description = (CustomTextView) itemView.findViewById(R.id.ctvDesc);
            descriptionAr = (CustomTextViewAr) itemView.findViewById(R.id.ctvDescAr);
            ctvTitle = (CustomTextViewBold) itemView.findViewById(R.id.ctvTitle);
            ctvTitleAr = (CustomTextViewBoldAr) itemView.findViewById(R.id.ctvTitleAr);

            ctvTitle.setPaintFlags(ctvTitle.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            ctvTitleAr.setPaintFlags(ctvTitleAr.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

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