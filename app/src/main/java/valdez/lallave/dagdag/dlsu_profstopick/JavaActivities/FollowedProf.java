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
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

import valdez.lallave.dagdag.dlsu_profstopick.Beans_Model.Follow;
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

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference followDatabaseReference = databaseReference.child("following");
        final DatabaseReference teacherDatabaseReference = databaseReference.child("teacher");



        final ArrayList<Follow> followArrayList = new ArrayList<>();
        followDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() { //Gets all the teachers followed by the logged in user.
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot followSnapshot : dataSnapshot.getChildren()){
                    if(followSnapshot.getValue(Follow.class).getStudent().equals(reviewer)){
                        followArrayList.add(followSnapshot.getValue(Follow.class));
                    }
                }
                teacherDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    final ArrayList<Teacher> teacherArrayList = new ArrayList<>();
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(int i = 0; i<followArrayList.size(); i++){
                            for(DataSnapshot teacherSnapshot:dataSnapshot.getChildren()){
                                if (teacherSnapshot.getValue(Teacher.class).getName().equals(followArrayList.get(i).getTeacher())){
                                    teacherArrayList.add(teacherSnapshot.getValue(Teacher.class));
                                }
                            }
                        }
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

                        ta.setOnItemClickListener(taOnItemClickListener);

                        rvTeachers.setAdapter(ta);
                        rvTeachers.setLayoutManager(new LinearLayoutManager(getBaseContext(), LinearLayoutManager.VERTICAL, false));
                        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                            @Override
                            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                                String key = v.getText().toString().toLowerCase(); //converts the key to all lowercase
                                key = ".*"+key+".*";    //converts the key into "LIKE"
                                boolean handled = false;

                                ArrayList<Teacher> searchList = new ArrayList<>();  //New array for the search results

                                if (actionId == EditorInfo.IME_ACTION_SEARCH || event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                                    for(int i=0; i <teacherArrayList.size(); i++){
                                        if(teacherArrayList.get(i).getName().toLowerCase().matches(key)){
                                            searchList.add(teacherArrayList.get(i));
                                        }
                                    }
                                    TeacherAdapter ta = new TeacherAdapter(searchList);
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
                                    TeacherAdapter ta = new TeacherAdapter(teacherArrayList);
                                    ta.setOnItemClickListener(taOnItemClickListener);

                                    rvTeachers.setAdapter(ta);
                                }
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