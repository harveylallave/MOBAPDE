package valdez.lallave.dagdag.dlsu_profstopick.JavaActivities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import valdez.lallave.dagdag.dlsu_profstopick.Service.Adapters.TeacherAdapter;
import valdez.lallave.dagdag.dlsu_profstopick.Beans_Model.Teacher;
import valdez.lallave.dagdag.dlsu_profstopick.R;
import valdez.lallave.dagdag.dlsu_profstopick.Service.DBHandler;

public class HomePage extends AppCompatActivity {

    RecyclerView rvTeachers;
    EditText     etSearch;
    DBHandler DBHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);

        SharedPreferences SP          = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        final String  reviewer        = SP.getString("loggedStudent", "student_email");
        rvTeachers                    = (RecyclerView) findViewById(R.id.rv_teachers);
        DBHandler                     = new DBHandler(getBaseContext());
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

//
        ArrayList<Teacher> teacherArrayList = new ArrayList<>(DBHandler.getAllTeachers());
//        ArrayList<String>  teacherNames     = new ArrayList<>();
//
//        for(Teacher t : teacherArrayList)
//            teacherNames.add(t.getName());

//        String[] countries = getResources().getStringArray(R.array.countries_array);

        TeacherAdapter ta = new TeacherAdapter(teacherArrayList);
//        ArrayAdapter<String> autoCompleteAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, teacherNames);
//        autoCompleteTextView.setAdapter(autoCompleteAdapter);
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

                    TeacherAdapter ta = new TeacherAdapter(new ArrayList<>(DBHandler.searchTeacher(v.getText().toString())));
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
                    TeacherAdapter ta = new TeacherAdapter(new ArrayList<>(DBHandler.getAllTeachers()));
                    ta.setOnItemClickListener(taOnItemClickListener);

                    rvTeachers.setAdapter(ta);
                }
            }
        });


        View v = findViewById(R.id.menuPane);
        v.bringToFront();                       // <--- IMPORTANT MENUPANE IS IN THE BACK (backend)
        initializeMenuButtons(v, reviewer);
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        finish();
        startActivity(getIntent());
    }

    public static void initializeMenuButtons(View v, String reviewer){


        TextView emailMenuItem  = ((TextView)v.findViewById(R.id.tv_emailMenuItem));
        TextView logoutMenuItem = ((TextView)v.findViewById(R.id.logoutMenuItem));
        TextView changePassItem = ((TextView)v.findViewById(R.id.changePassMenuItem));
        TextView suggestProf = ((TextView)v.findViewById(R.id.suggestProfMenuItem));
        emailMenuItem.setText(reviewer);

        // TODO Link menuitems(followed profs, suggest a prof, change pass)

        changePassItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent I = new Intent(v.getContext(), ChangePassword.class);
                v.getContext().startActivity(I);
            }
        });

        suggestProf.setOnClickListener(new View.OnClickListener() {
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


    }

}