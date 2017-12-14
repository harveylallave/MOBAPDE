package valdez.lallave.dagdag.dlsu_profstopick.JavaActivities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import valdez.lallave.dagdag.dlsu_profstopick.Service.Adapters.TeacherAdapter;
import valdez.lallave.dagdag.dlsu_profstopick.Beans_Model.Teacher;
import valdez.lallave.dagdag.dlsu_profstopick.R;
import valdez.lallave.dagdag.dlsu_profstopick.Service.DBHandler;

public class HomePage extends AppCompatActivity {

    RecyclerView rvTeachers;
    EditText     etSearch;
    DrawerLayout drawerLayout;
    private ActionBarDrawerToggle mToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);

        drawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final SharedPreferences SP    = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        final String  reviewer        = SP.getString("loggedStudent", "student_email");
        rvTeachers                    = (RecyclerView) findViewById(R.id.rv_teachers);
        etSearch                      = (EditText) findViewById(R.id.et_searchProf) ;


//        AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.autoComplete_searchProf);

//        ArrayList<Teacher> teachers = new ArrayList<>();

//        teachers.add(new Teacher("Ms. Ethel Ong", R.mipmap.ic_launcher));
//        teachers.add(new Teacher("Ms. Charibeth Cheng", R.mipmap.ic_launcher));
//        teachers.add(new Teacher("Ms. Teresita Limoanco", R.mipmap.ic_launcher));
//        teachers.add(new Teacher("Ms. Jocelyn Cu", R.mipmap.ic_launcher));
//        teachers.add(new Teacher("Ms. Nathalie Lim-Cheng", R.mipmap.ic_launcher));
//        teachers.add(new Teacher("Mr. Ryan Dimaunahan", R.mipmap.ic_launcher));
//        teachers.add(new Teacher("Mr. Miguel Cabral", R.mipmap.ic_launcher));
//        teachers.add(new Teacher("Mr. Gregory Cu", R.mipmap.ic_launcher));
//        teachers.add(new Teacher("Mr. Duke Delos Santos", R.mipmap.ic_launcher));
//        teachers.add(new Teacher("Dr. Nelson Marcos", R.mipmap.ic_launcher));
//        teachers.add(new Teacher("Dr. Conrado Ruiz, Jr", R.mipmap.ic_launcher));
//        teachers.add(new Teacher("Dr. Rafael Cabredo", R.mipmap.ic_launcher));
//        teachers.add(new Teacher("Dr. Remedios Bulos", R.mipmap.ic_launcher));
//        teachers.add(new Teacher("Dr. Florante Salvador", R.mipmap.ic_launcher));


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference teacherDatabaseReference = databaseReference.child("teacher");
        final DatabaseReference commentDatabaseReference = databaseReference.child("comment");
        final ArrayList<Teacher> teacherArrayList = new ArrayList<>();

        teacherDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot teacherSnapshot : dataSnapshot.getChildren()){
                    Teacher teacher = teacherSnapshot.getValue(Teacher.class);
                    teacherArrayList.add(teacher);
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

                // Dynamic onClickListener
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

        View v = findViewById(R.id.menuPane);
        v.bringToFront();                       // <--- IMPORTANT MENUPANE IS IN THE BACK (backend)
        initializeMenuButtons(v, reviewer);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        if(mToggle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onRestart() {
        super.onRestart();

        finish();
        startActivity(getIntent());
    }

    public static void initializeMenuButtons(View v, String reviewer){


        TextView emailMenuItem  = ((TextView)v.findViewById(R.id.tv_emailMenuItem)),
                followedProfMenuItem  = ((TextView)v.findViewById(R.id.followedProfMenuItem)),
                logoutMenuItem = ((TextView)v.findViewById(R.id.logoutMenuItem)),
                changePassItem = ((TextView)v.findViewById(R.id.changePassMenuItem)),
                suggestProfMenuItem = ((TextView)v.findViewById(R.id.suggestProfMenuItem));

        LinearLayout menuLinearLayout = (LinearLayout) v.findViewById(R.id.menuLinearLayout);
        emailMenuItem.setText(reviewer);

        // TODO Link menuitems(followed profs, suggest a prof, change pass)

        changePassItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent I = new Intent(v.getContext(), ChangePassword.class);
                v.getContext().startActivity(I);
            }
        });

        followedProfMenuItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent I = new Intent(v.getContext(), FollowedProf.class);
                v.getContext().startActivity(I);
            }
        });

        suggestProfMenuItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent I = new Intent(v.getContext(), SuggestProf.class);
                v.getContext().startActivity(I);
            }
        });

        logoutMenuItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent I = new Intent(v.getContext(), MainActivity.class);
                v.getContext().startActivity(I);

                SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(v.getContext());
                SharedPreferences.Editor SPE = SP.edit();
                SPE.remove("loggedStudent");
                SPE.apply();

                ((Activity) v.getContext()).finish();
            }
        });

        menuLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {}
        });


    }

}