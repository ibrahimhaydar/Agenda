package com.mobiweb.ibrahim.agenda.Adapters;
import android.content.res.TypedArray;
        import android.support.v7.widget.RecyclerView;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.LinearLayout;
        import android.widget.RadioButton;

        import com.mobiweb.ibrahim.agenda.Adapters.interfaces.RVOnItemClickListener;
        import com.mobiweb.ibrahim.agenda.Custom.CustomTextView;
        import com.mobiweb.ibrahim.agenda.Custom.CustomTextViewBold;
        import com.mobiweb.ibrahim.agenda.R;
        import com.mobiweb.ibrahim.agenda.models.Student;



        import java.util.ArrayList;

/**
 * Created by ibrahim on 11/8/2017.
 */


public class AdapterStudent extends RecyclerView.Adapter<AdapterStudent.VHteacher_infoStudent> {

    private ArrayList<Student> infoStudents;
    private RVOnItemClickListener itemClickListener;



    public AdapterStudent(ArrayList<Student> infoStudents, RVOnItemClickListener itemClickListener) {
        this.infoStudents = infoStudents;
        this.itemClickListener = itemClickListener;

    }

    @Override
    public VHteacher_infoStudent onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VHteacher_infoStudent(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student, parent, false));
    }

    @Override
    public void onBindViewHolder(VHteacher_infoStudent holder, int position) {
        try {
            holder.ctvStudentName.setText(infoStudents.get(position).getName());
        }catch (Exception e){}






    }

    @Override
    public int getItemCount() {
        return infoStudents.size();
    }

    public ArrayList<Student> getinfoStudents() {
        return infoStudents;
    }

    protected class VHteacher_infoStudent extends RecyclerView.ViewHolder  implements View.OnClickListener{

        private CustomTextView ctvStudentName;




        public VHteacher_infoStudent(View itemView) {
            super(itemView);

            ctvStudentName= (CustomTextView) itemView.findViewById(R.id.ctvStudentName);
            itemView.setId(R.id.idStudent);
         itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onItemClicked(v, getLayoutPosition());
        }
    }
}