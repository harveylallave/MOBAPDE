package valdez.lallave.dagdag.dlsu_profstopick.JavaActivities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import valdez.lallave.dagdag.dlsu_profstopick.Service.Adapters.TeacherAdapter;
import valdez.lallave.dagdag.dlsu_profstopick.Beans_Model.Teacher;
import valdez.lallave.dagdag.dlsu_profstopick.R;
import valdez.lallave.dagdag.dlsu_profstopick.Service.UserDBHandler;

public class HomePage extends AppCompatActivity {

    RecyclerView rvTeachers;
    UserDBHandler userDBHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);

        rvTeachers    = (RecyclerView) findViewById(R.id.rv_teachers);
        userDBHandler = new UserDBHandler(getBaseContext());

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


        TeacherAdapter ta = new TeacherAdapter(new ArrayList<Teacher>(userDBHandler.getAllTeachers()));

        // Dynamic onClickListener
        ta.setOnItemClickListener(new TeacherAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Teacher t) {
                Intent i = new Intent();

                i.putExtra("profName", t.getName());
                i.setClass(getBaseContext(), ProfPage.class);

                startActivityForResult(i, 0);
            }
        });

        rvTeachers.setAdapter(ta);
        rvTeachers.setLayoutManager(new LinearLayoutManager(getBaseContext(), LinearLayoutManager.VERTICAL, false));


    }


}