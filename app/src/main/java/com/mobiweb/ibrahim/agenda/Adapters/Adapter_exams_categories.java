package com.mobiweb.ibrahim.agenda.Adapters;

/**
 * Created by ibrahim on 4/14/2018.
 */

import android.content.res.TypedArray;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.mobiweb.ibrahim.agenda.Adapters.interfaces.RVOnItemClickListener;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextViewBold;
import com.mobiweb.ibrahim.agenda.R;
import com.mobiweb.ibrahim.agenda.models.entities.AllCategory;

import java.util.ArrayList;

public class Adapter_exams_categories extends RecyclerView.Adapter<Adapter_exams_categories.VHAllCategory> {

    private ArrayList<AllCategory> categories;
    private RVOnItemClickListener itemClickListener;


    public Adapter_exams_categories(ArrayList<AllCategory> categories, RVOnItemClickListener itemClickListener) {
        this.categories = categories;
        this.itemClickListener = itemClickListener;
    }

    @Override
    public VHAllCategory onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VHAllCategory(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_exam_category, parent, false));
    }

    @Override
    public void onBindViewHolder(VHAllCategory holder, int position) {
        holder.title.setText(categories.get(position).getTitle());

        TypedArray ta = holder.itemView.getResources().obtainTypedArray(R.array.colors_category);
        int[] colors = new int[ta.length()];
        for (int i = 0; i < ta.length(); i++) {
            colors[i] = ta.getColor(i, 0);
        }
        ta.recycle();

        holder.linearCategory.setBackgroundColor(colors[position%4]);

    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public ArrayList<AllCategory> getcategories() {
        return categories;
    }

    protected class VHAllCategory extends RecyclerView.ViewHolder implements View.OnClickListener {

        private CustomTextViewBold title;
        private LinearLayout linearCategory;

        public VHAllCategory(View itemView) {
            super(itemView);
            title = (CustomTextViewBold) itemView.findViewById(R.id.ctvTitle);
            linearCategory=(LinearLayout) itemView.findViewById(R.id.linearCategory);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onItemClicked(v, getLayoutPosition());
        }
    }
}