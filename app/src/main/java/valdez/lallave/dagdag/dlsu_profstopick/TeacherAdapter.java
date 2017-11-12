package valdez.lallave.dagdag.dlsu_profstopick;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by G301 on 05/10/2017.
 */

public class TeacherAdapter
        extends RecyclerView.Adapter<TeacherAdapter.TeacherViewHolder> {

    ArrayList<Teacher> teachers;

    public TeacherAdapter(ArrayList<Teacher> teachers) {
        this.teachers = teachers;
    }

    @Override
    public TeacherViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // TODO duplicate template item_teacher into the RV
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_teacher, parent, false);
        return new TeacherViewHolder(v);
    }

    @Override // Adding data to the template
    public void onBindViewHolder(final TeacherViewHolder holder, int position) {

        // tvTeacher's text must be set to the data at this position
        final Teacher currentTeacher = teachers.get(position);
        holder.tvTeacher.setText(currentTeacher.getName());
//        holder.ivTeacher.setImageResource(currentTeacher.getIcon());

//         User input photo
//        holder.ivTeacher.setImageBitmap(BitmapFactory.something);

        // itemView == inflated whole linear layout (per person)
        holder.itemView.setTag(currentTeacher);

        // Different bg color
        String[] colors = {"#F44336", "#E91E63", "#9C27B0", "#673AB7", "#3f51B5"};
//        holder.tvTeacher.setBackgroundColor(Color.parseColor(colors[position % colors.length]));
    }

    @Override
    public int getItemCount() {
        return teachers.size();
    }


    public class TeacherViewHolder extends RecyclerView.ViewHolder{

        TextView tvTeacher;
//        ImageView ivTeacher;

        public TeacherViewHolder(View itemView) {
            super(itemView);
            // What is itemview?? itemview = inflated item_teacher
            tvTeacher = itemView.findViewById(R.id.tv_teacher);
//            ivTeacher = itemView.findViewById(R.id.iv_teacher);


        }
    }

    public interface OnItemClickListener{
        public void onItemClick(Teacher t);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }
}