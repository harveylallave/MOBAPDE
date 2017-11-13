package valdez.lallave.dagdag.dlsu_profstopick.JavaActivities;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.widget.Toast;

import java.util.ArrayList;

import valdez.lallave.dagdag.dlsu_profstopick.Beans_Model.Teacher;
import valdez.lallave.dagdag.dlsu_profstopick.Service.Adapters.CommentAdapter;
import valdez.lallave.dagdag.dlsu_profstopick.Beans_Model.Comment;
import valdez.lallave.dagdag.dlsu_profstopick.R;
import valdez.lallave.dagdag.dlsu_profstopick.Service.DBHandler;


interface OnDialogDismissListener{
    void onDialogDismiss();
}

public class ProfPage extends AppCompatActivity implements OnDialogDismissListener{

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
        final ProfPage profPage = this;

        findViewById(R.id.addRateButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddRateDialog dialog = AddRateDialog.newInstance(prof, reviewer, profPage);

                dialog.show(getSupportFragmentManager(), "");
            }
        });




        rvComments = (RecyclerView) findViewById(R.id.rv_comments);
        RecyclerView recyclerView = new RecyclerView(getBaseContext());


        CommentAdapter ta = new CommentAdapter((ArrayList<Comment>) dbHandler.getAllCommentsPerTeacher(prof.getTeacherId()));


        rvComments.setAdapter(ta);
        rvComments.setLayoutManager(new LinearLayoutManager(getBaseContext(), LinearLayoutManager.VERTICAL, false));
    }

    @Override
    public void onDialogDismiss() {

//        Toast.makeText(getApplicationContext(), "WAAAA",Toast.LENGTH_SHORT).show();
        finish();
        startActivity(getIntent());
    }

    public static class AddRateDialog extends DialogFragment {
        static OnDialogDismissListener listener;
        static Activity baseActivity;

        public static AddRateDialog newInstance(Teacher prof, String reviewer, Activity activity) {
            AddRateDialog f = new AddRateDialog();

            // Supply num input as an argument.
            Bundle args = new Bundle();
            args.putParcelable("selectedProf", prof);
            args.putString("reviewer", reviewer);
            f.setArguments(args);
            listener = (OnDialogDismissListener) activity;
            baseActivity = activity;
            return f;
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            final View          v             = LayoutInflater.from(getActivity()).inflate(R.layout.add_review_dialog, null);
            final Teacher       selectedProf  = getArguments().getParcelable("selectedProf");
            final DBHandler     dbHandler     = new DBHandler(getContext());
            final String        reviewer      = getArguments().getString("reviewer");
            AlertDialog.Builder builder       = new AlertDialog.Builder(getActivity());

            builder.setTitle("Rate Professor")
                    .setIcon(R.mipmap.ic_launcher)
                    .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String title         = ((EditText) v.findViewById(R.id.et_titleFeedBack)).getText().toString(),
                                   body          = ((EditText) v.findViewById(R.id.et_bodyFeedBack)).getText().toString();
                            float  rating        = ((RatingBar) v.findViewById(R.id.rb_rating)).getRating();
//                            dbHandler.addNewComment(new Comment(title, body, rating, reviewer, selectedProf.getTeacherId()));
                            dismiss();
                        }}).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                        }}).setView(v);

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
