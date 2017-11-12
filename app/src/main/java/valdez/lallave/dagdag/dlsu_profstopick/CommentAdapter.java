package valdez.lallave.dagdag.dlsu_profstopick;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;



public class CommentAdapter
        extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private ArrayList<Comment> comments;

    public CommentAdapter(ArrayList<Comment> comment) {
        this.comments = comment;
    }

    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new CommentViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final CommentViewHolder holder, int position) {

        final Comment currentComment = comments.get(position);
        holder.tvTitle.setText(currentComment.getTitle());
        holder.tvBody.setText(currentComment.getBody());
        holder.rbComment.setRating(currentComment.getRate());

        holder.itemView.setTag(currentComment);
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }


    public class CommentViewHolder extends RecyclerView.ViewHolder{

        TextView tvTitle,
                 tvBody;

        RatingBar rbComment;

        public CommentViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvBody = itemView.findViewById(R.id.tv_body);
            rbComment = itemView.findViewById(R.id.rb_comment);


        }
    }

    public interface OnItemClickListener{
        public void onItemClick(Comment t);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }
}