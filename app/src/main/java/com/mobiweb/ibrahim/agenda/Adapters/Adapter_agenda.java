package com.mobiweb.ibrahim.agenda.Adapters;

/**
 * Created by ibrahim on 11/8/2017.
 */

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.mobiweb.ibrahim.agenda.Adapters.interfaces.RVOnItemClickListener;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextView;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextViewAr;
import com.mobiweb.ibrahim.agenda.R;
import com.mobiweb.ibrahim.agenda.models.entities.Agenda;

import java.util.ArrayList;


public class Adapter_agenda extends RecyclerView.Adapter<Adapter_agenda.VHAgenda> {

    private ArrayList<Agenda> classes;
    private RVOnItemClickListener itemClickListener;
    private boolean isTeacher = false;


    public Adapter_agenda(ArrayList<Agenda> classes, RVOnItemClickListener itemClickListener) {
        this.classes = classes;
        this.itemClickListener = itemClickListener;
    }


    public Adapter_agenda(ArrayList<Agenda> classes, RVOnItemClickListener itemClickListener, boolean isTeacher) {
        this.classes = classes;
        this.itemClickListener = itemClickListener;
        this.isTeacher = isTeacher;
    }

    @Override
    public VHAgenda onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VHAgenda(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_agenda_table, parent, false));
    }

    @Override
    public void onBindViewHolder(VHAgenda holder, int position) {
        holder.courseName.setText(classes.get(position).getHwTitle());
        if (!classes.get(position).getHw_info().isEmpty())
            holder.linearInfo.setVisibility(View.VISIBLE);
        else
            holder.linearInfo.setVisibility(View.GONE);


        try {
            if (isProbablyArabic(classes.get(position).getHwDesc())) {
                try {
                    holder.descriptionAr.setText(classes.get(position).getHwDesc());
                } catch (Exception e) {
                }
                holder.descriptionAr.setVisibility(View.VISIBLE);
                holder.description.setVisibility(View.GONE);


            } else {
                try {
                    holder.description.setText(classes.get(position).getHwDesc());
                } catch (Exception e) {
                }
                holder.description.setVisibility(View.VISIBLE);
                holder.descriptionAr.setVisibility(View.GONE);

            }
        }catch (Exception e){}



        try {
            if (isProbablyArabic(classes.get(position).getHw_info())) {
                try {
                    holder.infoAr.setText(classes.get(position).getHw_info());
                } catch (Exception e) {
                }
                holder.infoAr.setVisibility(View.VISIBLE);
                holder.info.setVisibility(View.GONE);

            } else {
                try {
                    holder.info.setText(classes.get(position).getHw_info());
                } catch (Exception e) {
                }
                holder.info.setVisibility(View.VISIBLE);
                holder.infoAr.setVisibility(View.GONE);

            }
        }catch (Exception e){}



        if (isTeacher)
            holder.linearDelete.setVisibility(View.VISIBLE);
        else
            holder.linearDelete.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return classes.size();
    }

    public ArrayList<Agenda> getclasses() {
        return classes;
    }

    protected class VHAgenda extends RecyclerView.ViewHolder implements View.OnClickListener {

        private CustomTextView courseName;
        private CustomTextView description;
        private CustomTextViewAr descriptionAr;

        private CustomTextView info;
        private CustomTextViewAr infoAr;

        private LinearLayout linearDelete, linearDesc, linearInfo;

        public VHAgenda(View itemView) {
            super(itemView);
            courseName = (CustomTextView) itemView.findViewById(R.id.courseName);
            description = (CustomTextView) itemView.findViewById(R.id.description);
            descriptionAr = (CustomTextViewAr) itemView.findViewById(R.id.descriptionAr);
            info = (CustomTextView) itemView.findViewById(R.id.info);
            infoAr = (CustomTextViewAr) itemView.findViewById(R.id.infoAr);
            linearDelete = (LinearLayout) itemView.findViewById(R.id.linearDelete);
            linearDesc = (LinearLayout) itemView.findViewById(R.id.linearDesc);
            linearInfo = (LinearLayout) itemView.findViewById(R.id.linearInfo);

            linearDesc.setOnClickListener(this);
            linearDelete.setOnClickListener(this);
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