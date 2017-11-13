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
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import valdez.lallave.dagdag.dlsu_profstopick.Service.Adapters.TeacherAdapter;
import valdez.lallave.dagdag.dlsu_profstopick.Beans_Model.Teacher;
import valdez.lallave.dagdag.dlsu_profstopick.R;
import valdez.lallave.dagdag.dlsu_profstopick.Service.DBHandler;

public class HomePage extends AppCompatActivity {

    RecyclerView rvTeachers;
    DBHandler DBHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);

        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        final String  reviewer = SP.getString("loggedStudent", "student_email");
        rvTeachers    = (RecyclerView) findViewById(R.id.rv_teachers);
        DBHandler     = new DBHandler(getBaseContext());

        RecyclerView recyclerView = new RecyclerView(getBaseContext());

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


        TeacherAdapter ta = new TeacherAdapter(new ArrayList<Teacher>(DBHandler.getAllTeachers()));

        // Dynamic onClickListener
        ta.setOnItemClickListener(new TeacherAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Teacher t) {
                Intent i = new Intent();

                i.putExtra("selectedProf", t);
                i.setClass(getBaseContext(), ProfPage.class);

                startActivityForResult(i, 0);
            }
        });

        rvTeachers.setAdapter(ta);
        rvTeachers.setLayoutManager(new LinearLayoutManager(getBaseContext(), LinearLayoutManager.VERTICAL, false));


        View v = findViewById(R.id.menuPane);
        v.bringToFront();                       // <--- IMPORTANT MENUPANE IS IN THE BACK (backend)
        initializeMenuButtons(v, reviewer);
    }

    public static void initializeMenuButtons(View v, String reviewer){


        TextView emailMenuItem  = ((TextView)v.findViewById(R.id.tv_emailMenuItem));
        TextView logoutMenuItem = ((TextView)v.findViewById(R.id.logoutMenuItem));

        emailMenuItem.setText(reviewer);

        Toast.makeText(v.getContext().getApplicationContext(), logoutMenuItem.getText().toString(),Toast.LENGTH_SHORT).show();
        // TODO Link menuitems(followed profs, suggest a prof, change pass)

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