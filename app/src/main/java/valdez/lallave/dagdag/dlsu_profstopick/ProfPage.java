package valdez.lallave.dagdag.dlsu_profstopick;

import android.app.Dialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.DrawerLayout;
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

public class ProfPage extends AppCompatActivity {

    RecyclerView rvComments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prof_page);

        String profName = getIntent().getExtras().getString("profName");
        ((TextView)findViewById(R.id.tv_ProfName)).setText(profName);
        ((Button)findViewById(R.id.addRateButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddRateDialog dialog = new AddRateDialog();
                dialog.show(getSupportFragmentManager(), "");
            }
        });




        rvComments = (RecyclerView) findViewById(R.id.rv_comments);
        RecyclerView recyclerView = new RecyclerView(getBaseContext());

        ArrayList<Comment> comments = new ArrayList<>();

        comments.add(new Comment("Good", "Had the prof at some subj", (float) 2.4));
        comments.add(new Comment("Bad", "Had the prof at some subj", (float) 1));
        comments.add(new Comment("Excellent", "Had the prof at some subj", (float) 5));
        CommentAdapter ta = new CommentAdapter(comments);


        rvComments.setAdapter(ta);
        rvComments.setLayoutManager(new LinearLayoutManager(getBaseContext(), LinearLayoutManager.VERTICAL, false));


    }

    public static class AddRateDialog extends DialogFragment {


        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            final View v = LayoutInflater.from(getActivity()).inflate(R.layout.add_review_dialog, null);
            final EditText  etTitle = (EditText) v.findViewById(R.id.et_titleFeedBack),
                            etBody  = (EditText) v.findViewById(R.id.et_bodyFeedBack);
            final RatingBar rbInput = (RatingBar) v.findViewById(R.id.rb_rating);

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            builder.setTitle("Rate Professor")
                    .setIcon(R.mipmap.ic_launcher)
                    .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
//                    String s = etInput.getText().toString();
//                    ((MainActivity) getActivity()).callDialog(s);
                            dismiss();
                        }}).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }}).setView(v);

            return builder.create();
        }
    }


}
