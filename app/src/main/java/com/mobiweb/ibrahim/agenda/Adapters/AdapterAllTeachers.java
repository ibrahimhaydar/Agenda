package com.mobiweb.ibrahim.agenda.Adapters;

/**
 * Created by ibrahim on 11/8/2017.
 */

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mobiweb.ibrahim.agenda.Adapters.interfaces.RVOnItemClickListener;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextViewBold;
import com.mobiweb.ibrahim.agenda.R;
import com.mobiweb.ibrahim.agenda.models.entities.Allteacher;

import java.util.ArrayList;


public class AdapterAllTeachers extends RecyclerView.Adapter<AdapterAllTeachers.VHAllteacher> {

    private ArrayList<Allteacher> teachers;
    private RVOnItemClickListener itemClickListener;


    public AdapterAllTeachers(ArrayList<Allteacher> teachers, RVOnItemClickListener itemClickListener) {
        this.teachers = teachers;
        this.itemClickListener = itemClickListener;
    }

    @Override
    public VHAllteacher onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VHAllteacher(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_teachers, parent, false));
    }

    @Override
    public void onBindViewHolder(VHAllteacher holder, int position) {
        holder.TeacherName.setText(teachers.get(position).getTeacherName());


/*        TypedArray ta = holder.itemView.getResources().obtainTypedArray(R.array.colors);
        int[] colors = new int[ta.length()];
        for (int i = 0; i < ta.length(); i++) {
            colors[i] = ta.getColor(i, 0);
        }
        ta.recycle();

        GradientDrawable bgShape = (GradientDrawable)holder.TeacherName.getBackground();
        bgShape.setColor(colors[position%4]);*/
    }

    @Override
    public int getItemCount() {
        return teachers.size();
    }

    public ArrayList<Allteacher> getteachers() {
        return teachers;
    }

    protected class VHAllteacher extends RecyclerView.ViewHolder implements View.OnClickListener {

        private CustomTextViewBold TeacherName;

        public VHAllteacher(View itemView) {
            super(itemView);
            TeacherName = (CustomTextViewBold) itemView.findViewById(R.id.ctvTeacherName);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onItemClicked(v, getLayoutPosition());
        }
    }
}