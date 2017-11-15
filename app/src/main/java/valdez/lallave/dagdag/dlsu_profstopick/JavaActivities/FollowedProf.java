package valdez.lallave.dagdag.dlsu_profstopick.JavaActivities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import valdez.lallave.dagdag.dlsu_profstopick.Beans_Model.Student;
import valdez.lallave.dagdag.dlsu_profstopick.Service.Adapters.TeacherAdapter;
import valdez.lallave.dagdag.dlsu_profstopick.Beans_Model.Teacher;
import valdez.lallave.dagdag.dlsu_profstopick.R;
import valdez.lallave.dagdag.dlsu_profstopick.Service.DBHandler;

public class FollowedProf extends AppCompatActivity {

    RecyclerView rvTeachers;
    EditText     etSearch;
    DBHandler DBHandler;
    Student student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.followed_prof);

        SharedPreferences SP          = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        final String  reviewer        = SP.getString("loggedStudent", "student_email");
        rvTeachers                    = (RecyclerView) findViewById(R.id.rv_followedTeachers);
        DBHandler                     = new DBHandler(getBaseContext());
        etSearch                      = (EditText) findViewById(R.id.et_followedProf_searchProf);
        student                       = DBHandler.getStudent(reviewer);
        ArrayList<Teacher> teacherArrayList =
                new ArrayList<>(DBHandler.getAllFollowedTeachers(student));

        TeacherAdapter ta = new TeacherAdapter(teacherArrayList);
        final TeacherAdapter.OnItemClickListener taOnItemClickListener = new TeacherAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Teacher t) {
                Intent i = new Intent();

                i.putExtra("selectedProf", t);
                i.setClass(getBaseContext(), ProfPage.class);

                startActivityForResult(i, 0);
            }
        };

        // Dynamic onClickListener
        ta.setOnItemClickListener(taOnItemClickListener);

        rvTeachers.setAdapter(ta);
        rvTeachers.setLayoutManager(new LinearLayoutManager(getBaseContext(), LinearLayoutManager.VERTICAL, false));


        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEARCH || event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {

                    TeacherAdapter ta = new TeacherAdapter(new ArrayList<>(DBHandler.getTeacher(v.getText().toString())));
                    ta.setOnItemClickListener(taOnItemClickListener);

                    rvTeachers.setAdapter(ta);
                    handled = true;
                }

                return handled;
            }
        });

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().trim().isEmpty()){
                    TeacherAdapter ta = new TeacherAdapter(new ArrayList<>(DBHandler.getAllFollowedTeachers(student)));
                    ta.setOnItemClickListener(taOnItemClickListener);

                    rvTeachers.setAdapter(ta);
                }
            }
        });


        View v = findViewById(R.id.menuPane);
        v.bringToFront();                       // <--- IMPORTANT MENUPANE IS IN THE BACK (backend)
        HomePage.initializeMenuButtons(v, reviewer);
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        finish();
        startActivity(getIntent());
    }


}