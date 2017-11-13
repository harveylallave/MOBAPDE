package valdez.lallave.dagdag.dlsu_profstopick.Service.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

import valdez.lallave.dagdag.dlsu_profstopick.Beans_Model.Comment;
import valdez.lallave.dagdag.dlsu_profstopick.R;


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
        holder.tvReviewer.setText(currentComment.getReviewer());
        holder.rbComment.setRating(currentComment.getRate());

        holder.itemView.setTag(currentComment);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                onItemClickListener.onItemClick((Comment) view.getTag());
            }
        });
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }


    public class CommentViewHolder extends RecyclerView.ViewHolder{

        TextView tvTitle,
                 tvBody,
                 tvReviewer;

        RatingBar rbComment;

        public CommentViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvBody = (TextView) itemView.findViewById(R.id.tv_body);
            tvReviewer = (TextView) itemView.findViewById(R.id.tv_reviewer);
            rbComment = (RatingBar) itemView.findViewById(R.id.rb_comment);


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