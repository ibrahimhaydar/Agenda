package com.mobiweb.ibrahim.agenda.Adapters;


import android.content.res.TypedArray;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.mobiweb.ibrahim.agenda.Adapters.interfaces.RVOnItemClickListener;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextView;
import com.mobiweb.ibrahim.agenda.Custom.CustomTextViewBold;
import com.mobiweb.ibrahim.agenda.R;
import com.mobiweb.ibrahim.agenda.models.entities.Allclass;

import java.util.ArrayList;


public class AdapterPopupClasses extends RecyclerView.Adapter<AdapterPopupClasses.VHAllclass> {

    private ArrayList<Allclass> classes;
    private RVOnItemClickListener itemClickListener;


    public AdapterPopupClasses(ArrayList<Allclass> classes, RVOnItemClickListener itemClickListener) {
        this.classes = classes;
        this.itemClickListener = itemClickListener;
    }

    @Override
    public VHAllclass onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VHAllclass(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_popup_classes, parent, false));
    }

    @Override
    public void onBindViewHolder(final VHAllclass holder, final int position) {
        holder.ClassName.setText(classes.get(position).getClassName());

        if(classes.get(position).getSelected())
            holder.btSelect.setBackgroundResource(R.drawable.border_selected);
        else
            holder.btSelect.setBackgroundResource(R.drawable.border_table);




    }

    @Override
    public int getItemCount() {
        return classes.size();
    }

    public ArrayList<Allclass> getclasses() {
        return classes;
    }

    protected class VHAllclass extends RecyclerView.ViewHolder implements View.OnClickListener {

        private CustomTextView ClassName;
        private Button btSelect;


        public VHAllclass(View itemView) {
            super(itemView);
            ClassName = (CustomTextView) itemView.findViewById(R.id.ctvPopupClasses);
            btSelect=(Button)itemView.findViewById(R.id.btSelect);


            btSelect.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onItemClicked(v, getLayoutPosition());
        }
    }
}