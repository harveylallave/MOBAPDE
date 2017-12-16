package valdez.lallave.dagdag.dlsu_profstopick.JavaActivities;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

import valdez.lallave.dagdag.dlsu_profstopick.Beans_Model.Comment;
import valdez.lallave.dagdag.dlsu_profstopick.Beans_Model.Follow;
import valdez.lallave.dagdag.dlsu_profstopick.Beans_Model.Student;
import valdez.lallave.dagdag.dlsu_profstopick.Beans_Model.Teacher;
import valdez.lallave.dagdag.dlsu_profstopick.Service.Adapters.CommentAdapter;

import valdez.lallave.dagdag.dlsu_profstopick.R;
import valdez.lallave.dagdag.dlsu_profstopick.Service.DBHandler;


interface OnDialogDismissListener{
    void onDialogDismiss();
}

public class ProfPage extends AppCompatActivity implements OnDialogDismissListener{

    RecyclerView rvComments;
    DBHandler dbHandler;
    Teacher prof;
    Button rateButton;

    String reviewer;

    final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    final DatabaseReference commentDatabaseReference = databaseReference.child("comment");
    final DatabaseReference followDatabaseReference = databaseReference.child("following");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prof_page);
        dbHandler = new DBHandler();


        getSupportActionBar();

        SharedPreferences SP   = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        final String  reviewer = SP.getString("loggedStudent", "student_email");

        prof                   = getIntent().getParcelableExtra("selectedProf");
        rateButton             = (Button) findViewById(R.id.rateButton);

        final ImageView ivFollowProf = (ImageView) findViewById(R.id.iv_profPage_followProf);
        final TextView  tvFollowProf = (TextView) findViewById(R.id.tv_profPage_followProf);
        final ProfPage profPage = this;

        commentDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            Comment ownComment=null;
            float sum=0;
            int cmtCtr;
            float ave;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot commentSnapshot : dataSnapshot.getChildren()){
                    if(commentSnapshot.getValue(Comment.class).getTeacher().equals(prof.getName())){ //Computes for the average of ratings, and gets the number of comments
                        sum=sum+commentSnapshot.getValue(Comment.class).getRate();
                        cmtCtr++;
                    }
                    if(commentSnapshot.getValue(Comment.class).getReviewer().equals(reviewer) && commentSnapshot.getValue(Comment.class).getTeacher().equals(prof.getName())){ //If the logged user already made a comment
                        Comment comment = new Comment();
                        comment.setTitle(commentSnapshot.getValue(Comment.class).getTitle());
                        comment.setBody(commentSnapshot.getValue(Comment.class).getBody());
                        comment.setRate(commentSnapshot.getValue(Comment.class).getRate());
                        comment.setReviewer(commentSnapshot.getValue(Comment.class).getReviewer());
                        comment.setTeacher(commentSnapshot.getValue(Comment.class).getTeacher());
                        ownComment = comment;
                    }
                }

                ave = sum/cmtCtr;
                ((RatingBar)findViewById(R.id.rb_profPage_aveRating)).setRating(ave);
                ((TextView)findViewById(R.id.tv_profPage_aveRating)).setText(String.format("%.1f", ave));
                ((TextView)findViewById(R.id.tv_profPage_nReviews)).setText(cmtCtr+"");

                if(ownComment == null){
                    rateButton.setText("Rate");
                } else {
                    rateButton.setText("Update Rating");
                }

                View v = findViewById(R.id.menuPane);
                v.bringToFront();
                HomePage.initializeMenuButtons(v, reviewer);


                rateButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AddRateDialog dialog = AddRateDialog.newInstance(prof, reviewer, profPage, ownComment);
                        dialog.show(getSupportFragmentManager(), "");

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        ((TextView)findViewById(R.id.tv_ProfName)).setText(prof.getName());
        ((TextView)findViewById(R.id.tv_department)).setText(prof.getDepartment());

        final Follow follow = new Follow(null, null);
        follow.setTeacher(prof.getName());
        follow.setStudent(reviewer);

        followDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() { //Updates the notification button if prof is followed/unfollowed.
           boolean found = false;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot followDatasnapshot: dataSnapshot.getChildren()){
                   if (followDatasnapshot.getValue(Follow.class).getStudent().equals(reviewer) && followDatasnapshot.getValue(Follow.class).getTeacher().equals(prof.getName())){
                       found = true;
                    }
                }
                if(found){
                  ivFollowProf.setColorFilter(Color.parseColor("#e98b5b"),
                          android.graphics.PorterDuff.Mode.SRC_IN);
                  tvFollowProf.setTextColor(Color.parseColor("#e98b5b"));
                  tvFollowProf.setText("Following");
                }else{
                  ivFollowProf.setColorFilter(Color.parseColor("#686b68"),
                          android.graphics.PorterDuff.Mode.SRC_IN);
                  tvFollowProf.setTextColor(Color.parseColor("#686b68"));
                  tvFollowProf.setText("Follow");
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        rvComments = (RecyclerView) findViewById(R.id.rv_comments);
        final ArrayList<Comment> commentArrayList = new ArrayList<>();

        commentDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() { //THIS GETS ALL THE COMMENTS FOR A CERTAIN PROF
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot commentSnapshot: dataSnapshot.getChildren()){
                    if(commentSnapshot.getValue(Comment.class).getTeacher().equals(prof.getName())){
                        Comment comment = commentSnapshot.getValue(Comment.class);
                        commentArrayList.add(comment);
                    }
                }

                CommentAdapter ca = new CommentAdapter(commentArrayList);
                rvComments.setAdapter(ca);
                rvComments.setLayoutManager(new LinearLayoutManager(getBaseContext(), LinearLayoutManager.VERTICAL, false));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onDialogDismiss() {
        finish();
        startActivity(getIntent());
    }

    public void followProfButton(View view) {  //Toggle to follow/unfollow prof

        final ImageView ivFollowProf = (ImageView) view.findViewById(R.id.iv_profPage_followProf);
        final TextView  tvFollowProf = (TextView) view.findViewById(R.id.tv_profPage_followProf);

        SharedPreferences SP   = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        final String  reviewer = SP.getString("loggedStudent", "student_email");
        final Follow follow = new Follow(null, null);

        follow.setTeacher(prof.getName());
        follow.setStudent(reviewer);

        followDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            boolean found=false;
            String key;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot followDatasnapshot: dataSnapshot.getChildren()){
                    if (followDatasnapshot.getValue(Follow.class).getStudent().equals(reviewer) && followDatasnapshot.getValue(Follow.class).getTeacher().equals(prof.getName())){
                        key = followDatasnapshot.getKey();
                        found = true;
                    }
                }
                if(found) {
                    followDatabaseReference.child(key).setValue(null);
                    ivFollowProf.setColorFilter(Color.parseColor("#686b68"),
                            android.graphics.PorterDuff.Mode.SRC_IN);
                    tvFollowProf.setTextColor(Color.parseColor("#686b68"));
                    tvFollowProf.setText("Follow");
                }else {
                    ivFollowProf.setColorFilter(Color.parseColor("#e98b5b"),
                            android.graphics.PorterDuff.Mode.SRC_IN);
                    tvFollowProf.setTextColor(Color.parseColor("#e98b5b"));
                    tvFollowProf.setText("Following");
                    key = followDatabaseReference.push().getKey();
                    followDatabaseReference.child(key).setValue(follow);
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static class AddRateDialog extends DialogFragment {
        static OnDialogDismissListener listener;
        static Activity baseActivity;
        static Comment ownComment;

        static DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        static  DatabaseReference editCommentDatabaseReference = databaseReference.child("comment");
        static  DatabaseReference teacherDatabaseReference = databaseReference.child("teacher");

        public static AddRateDialog newInstance(Teacher prof, String reviewer, Activity activity, Comment comment) {
            AddRateDialog f = new AddRateDialog();

            // Supply num input as an argument.
            Bundle args = new Bundle();
            args.putParcelable("selectedProf", prof);
            args.putString("reviewer", reviewer);
            f.setArguments(args);
            listener = (OnDialogDismissListener) activity;
            baseActivity = activity;
            ownComment = comment;
            return f;
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            final View          v             = LayoutInflater.from(getActivity()).inflate(R.layout.add_review_dialog, null);
            final Teacher       selectedProf  = getArguments().getParcelable("selectedProf");
            final DBHandler     dbHandler     = new DBHandler();
            final String        reviewer      = getArguments().getString("reviewer");
            AlertDialog.Builder builder       = new AlertDialog.Builder(getActivity());


            ((RatingBar)v.findViewById(R.id.rb_rating)).setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

                @Override public void onRatingChanged(RatingBar ratingBar,
                                                      float rating,
                                                      boolean fromUser) {
                    if(rating<1.0f)
                        ratingBar.setRating(1.0f);
                }
            });

            if(ownComment != null){
                ((EditText) v.findViewById(R.id.et_titleFeedBack)).setText(ownComment.getTitle());
                ((EditText) v.findViewById(R.id.et_bodyFeedBack)).setText(ownComment.getBody());
                ((RatingBar) v.findViewById(R.id.rb_rating)).setRating(ownComment.getRate());
            }

            builder.setTitle("Rate Professor")
                    .setIcon(R.mipmap.ic_launcher)
                    .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String title         = ((EditText) v.findViewById(R.id.et_titleFeedBack)).getText().toString(),
                                    body          = ((EditText) v.findViewById(R.id.et_bodyFeedBack)).getText().toString();
                            float  rating        = ((RatingBar) v.findViewById(R.id.rb_rating)).getRating();
                            if(ownComment != null) { //Checks if the user already made a comment
                                ownComment.setTitle(title);
                                ownComment.setBody(body);
                                ownComment.setRate(rating);
                                ownComment.setReviewer(reviewer);
                                ownComment.setTeacher(selectedProf.getName());
                               editCommentDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() { //Updates existing data
                                    String key;
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for(DataSnapshot commentSnapshot : dataSnapshot.getChildren()){
                                            if(commentSnapshot.getValue(Comment.class).getReviewer().equals(ownComment.getReviewer())){
                                                key = commentSnapshot.getKey();
                                            }
                                        }
                                        editCommentDatabaseReference.child(key).setValue(ownComment);
                                    }
                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                    }
                                });
                            } else{ //IF NO EXISTING COMMENT CREATE A NEW ONE
                                dbHandler.addNewComment(new Comment(title, body, rating, reviewer, selectedProf.getName())); //Create new comment
                                teacherDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() { //It updates the number of reviews in teacher table after posting new comment
                                    String key;
                                    int nReviews=0;
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for(DataSnapshot teacherSnapshot:dataSnapshot.getChildren()){
                                            if(teacherSnapshot.getValue(Teacher.class).getName().equals(selectedProf.getName())){
                                                key=teacherSnapshot.getKey();
                                                nReviews = teacherSnapshot.getValue(Teacher.class).getnReviews() + 1;
                                            }
                                        }
                                        teacherDatabaseReference.child(key).child("nReviews").setValue(nReviews);
                                        editCommentDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() { //It updates the average of rating after posting the new comment
                                            int cmtCtr=0;
                                            float ave =0;
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                for(DataSnapshot commentSnapshot:dataSnapshot.getChildren()){
                                                    if(commentSnapshot.getValue(Comment.class).getTeacher().equals(selectedProf.getName())){
                                                        ave = ave + commentSnapshot.getValue(Comment.class).getRate();
                                                        cmtCtr++;
                                                    }
                                                }
                                                ave = ave/cmtCtr;
                                                teacherDatabaseReference.child(key).child("aveRating").setValue(ave);
                                            }
                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {
                                            }
                                        });
                                    }
                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                    }
                                });
                            }
                            dismiss();
                        }}).setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }}).setView(v);

            if(ownComment != null)
                builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        editCommentDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() { //It deletes the comment from the database
                            String key;
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for(DataSnapshot commentSnapshot: dataSnapshot.getChildren()){
                                    if(commentSnapshot.getValue(Comment.class).getTeacher().equals(ownComment.getTeacher()) && commentSnapshot.getValue(Comment.class).getReviewer().equals(ownComment.getReviewer())){
                                        key = commentSnapshot.getKey();
                                    }
                                }
                                editCommentDatabaseReference.child(key).setValue(null); //deletes from the database
                                teacherDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    int nReviews=0;
                                    String key;
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) { //It updates the new number of reviews after deleting
                                        for(DataSnapshot teacherSnapshot : dataSnapshot.getChildren()){
                                            if(teacherSnapshot.getValue(Teacher.class).getName().equals(selectedProf.getName())){
                                                nReviews = teacherSnapshot.getValue(Teacher.class).getnReviews();
                                                key = teacherSnapshot.getKey();
                                            }
                                        }
                                        nReviews--;
                                        teacherDatabaseReference.child(key).child("nReviews").setValue(nReviews);

                                        editCommentDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() { //It updates the new average rating after deleting
                                            float ave = 0;
                                            int cmtCtr = 0;
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                for(DataSnapshot commentSnapshot : dataSnapshot.getChildren()){
                                                    if(commentSnapshot.getValue(Comment.class).getTeacher().equals(selectedProf.getName())){
                                                        ave = ave + commentSnapshot.getValue(Comment.class).getRate();
                                                        cmtCtr++;
                                                    }
                                                }
                                                if(cmtCtr!=0) {
                                                    ave = ave / cmtCtr;
                                                    teacherDatabaseReference.child(key).child("aveRating").setValue(ave);
                                                }else
                                                    teacherDatabaseReference.child(key).child("aveRating").setValue(0);
                                            }
                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {
                                            }
                                        });
                                    }
                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                    }
                                });
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });
                        dismiss();
                    }
                }).setView(v);
            return builder.create();
        }


        @Override
        public void onCancel(DialogInterface dialog) {
            dialog.dismiss();
        }

        @Override
        public void onDismiss(DialogInterface dialog) {
            dialog.dismiss();
            // TODO ratingbar limit to 1-5 && not continue to listener if not complete field
            if(true)
                listener.onDialogDismiss();
        }
    }


}
