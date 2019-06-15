package com.mobiweb.ibrahim.agenda.Adapters;

/**
 * Created by ibrahim on 11/8/2017.
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
import com.mobiweb.ibrahim.agenda.models.entities.Allclass;

import java.util.ArrayList;


public class AdapterAllClasses extends RecyclerView.Adapter<AdapterAllClasses.VHAllclass> {

    private ArrayList<Allclass> classes;
    private RVOnItemClickListener itemClickListener;


    public AdapterAllClasses(ArrayList<Allclass> classes, RVOnItemClickListener itemClickListener) {
        this.classes = classes;
        this.itemClickListener = itemClickListener;
    }

    @Override
    public VHAllclass onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VHAllclass(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_class, parent, false));
    }

    @Override
    public void onBindViewHolder(VHAllclass holder, int position) {
       holder.ClassName.setText(classes.get(position).getClassName());


       TypedArray ta = holder.itemView.getResources().obtainTypedArray(R.array.colors);
        int[] colors = new int[ta.length()];
        for (int i = 0; i < ta.length(); i++) {
            colors[i] = ta.getColor(i, 0);
        }
        ta.recycle();

       // GradientDrawable bgShape = (GradientDrawable)holder.ClassName.getBackground();
        holder.linear_class.setBackgroundColor(colors[position%4]);
    }

    @Override
    public int getItemCount() {
        return classes.size();
    }

    public ArrayList<Allclass> getclasses() {
        return classes;
    }

    protected class VHAllclass extends RecyclerView.ViewHolder implements View.OnClickListener {

        private CustomTextViewBold ClassName;
        private LinearLayout linear_class;

        public VHAllclass(View itemView) {
            super(itemView);
            ClassName = (CustomTextViewBold) itemView.findViewById(R.id.className);
            linear_class=(LinearLayout) itemView.findViewById(R.id.linear_class);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onItemClicked(v, getLayoutPosition());
        }
    }
}