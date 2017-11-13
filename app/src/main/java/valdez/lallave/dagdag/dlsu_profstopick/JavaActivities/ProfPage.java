package valdez.lallave.dagdag.dlsu_profstopick.JavaActivities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

import valdez.lallave.dagdag.dlsu_profstopick.Beans_Model.Teacher;
import valdez.lallave.dagdag.dlsu_profstopick.Service.Adapters.CommentAdapter;
import valdez.lallave.dagdag.dlsu_profstopick.Beans_Model.Comment;
import valdez.lallave.dagdag.dlsu_profstopick.R;
import valdez.lallave.dagdag.dlsu_profstopick.Service.DBHandler;

public class ProfPage extends AppCompatActivity {

    RecyclerView rvComments;
    DBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prof_page);
        dbHandler = new DBHandler(getBaseContext());

        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        final Teacher prof     = getIntent().getParcelableExtra("selectedProf");
        final String  reviewer = SP.getString("loggedStudent", "student_email");


        ((TextView)findViewById(R.id.tv_ProfName)).setText(prof.getName());
        ((TextView)findViewById(R.id.tv_department)).setText(prof.getDepartment());

        View v = findViewById(R.id.menuPane);
        v.bringToFront();
        HomePage.initializeMenuButtons(v, reviewer);

        findViewById(R.id.addRateButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddRateDialog dialog = AddRateDialog.newInstance(prof, reviewer);
                dialog.show(getSupportFragmentManager(), "");
            }
        });




        rvComments = (RecyclerView) findViewById(R.id.rv_comments);
        RecyclerView recyclerView = new RecyclerView(getBaseContext());

        ArrayList<Comment> comments = new ArrayList<>();

        comments.add(new Comment("Good", "Had the prof at some subj", (float) 2.4, reviewer, prof.getTeacherId()));
        CommentAdapter ta = new CommentAdapter((ArrayList<Comment>) dbHandler.getAllCommentsPerTeacher(prof.getTeacherId()));


        rvComments.setAdapter(ta);
        rvComments.setLayoutManager(new LinearLayoutManager(getBaseContext(), LinearLayoutManager.VERTICAL, false));
    }

    public static class AddRateDialog extends DialogFragment {

        public static AddRateDialog newInstance(Teacher prof, String reviewer) {
            AddRateDialog f = new AddRateDialog();

            // Supply num input as an argument.
            Bundle args = new Bundle();
            args.putParcelable("selectedProf", prof);
            args.putString("reviewerMail", reviewer);
            f.setArguments(args);

            return f;
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            final View          v             = LayoutInflater.from(getActivity()).inflate(R.layout.add_review_dialog, null);
            final String        title         = ((EditText) v.findViewById(R.id.et_titleFeedBack)).getText().toString(),
                                body          = ((EditText) v.findViewById(R.id.et_bodyFeedBack)).getText().toString(),
                                reviewer      = getArguments().getString("reviewerMail");
            final float         rating        = ((RatingBar) v.findViewById(R.id.rb_rating)).getRating();
            final Teacher       selectedProf  = getArguments().getParcelable("selectedProf");
            final DBHandler     dbHandler     = new DBHandler(getContext());
            AlertDialog.Builder builder       = new AlertDialog.Builder(getActivity());

            builder.setTitle("Rate Professor")
                    .setIcon(R.mipmap.ic_launcher)
                    .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dbHandler.addNewComment(new Comment(title, body, rating, reviewer, selectedProf.getTeacherId()));
                            dismiss();
                        }}).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }}).setView(v);

            return builder.create();
        }
    }


}
