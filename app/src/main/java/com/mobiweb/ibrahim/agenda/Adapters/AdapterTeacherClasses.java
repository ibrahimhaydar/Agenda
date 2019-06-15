package com.mobiweb.ibrahim.agenda.Adapters;

/**
 * Created by ibrahim on 11/14/2017.
 */

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mobiweb.ibrahim.agenda.Adapters.interfaces.RVOnItemClickListener;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextViewBold;
import com.mobiweb.ibrahim.agenda.R;
import com.mobiweb.ibrahim.agenda.models.entities.teacher_class;

import java.util.ArrayList;

/**
 * Created by ibrahim on 11/8/2017.
 */


public class AdapterTeacherClasses extends RecyclerView.Adapter<AdapterTeacherClasses.VHteacher_class> {

    private ArrayList<teacher_class> classes;
    private RVOnItemClickListener itemClickListener;


    public AdapterTeacherClasses(ArrayList<teacher_class> classes, RVOnItemClickListener itemClickListener) {
        this.classes = classes;
        this.itemClickListener = itemClickListener;
    }

    @Override
    public VHteacher_class onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VHteacher_class(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_class, parent, false));
    }

    @Override
    public void onBindViewHolder(VHteacher_class holder, int position) {
        holder.ClassName.setText(classes.get(position).getClassName());


   /*     TypedArray ta = holder.itemView.getResources().obtainTypedArray(R.array.colors);
        int[] colors = new int[ta.length()];
        for (int i = 0; i < ta.length(); i++) {
            colors[i] = ta.getColor(i, 0);
        }
        ta.recycle();

        GradientDrawable bgShape = (GradientDrawable)holder.ClassName.getBackground();
        bgShape.setColor(colors[position%11]);*/
    }

    @Override
    public int getItemCount() {
        return classes.size();
    }

    public ArrayList<teacher_class> getclasses() {
        return classes;
    }

    protected class VHteacher_class extends RecyclerView.ViewHolder implements View.OnClickListener {

        private CustomTextViewBold ClassName;

        public VHteacher_class(View itemView) {
            super(itemView);
            ClassName = (CustomTextViewBold) itemView.findViewById(R.id.className);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onItemClicked(v, getLayoutPosition());
        }
    }
}