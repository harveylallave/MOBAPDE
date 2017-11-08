package valdez.lallave.dagdag.dlsu_profstopick;

import android.content.res.Configuration;
import android.support.constraint.solver.ArrayLinkedVariables;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.widget.Toast;

import java.util.ArrayList;

public class HomePage extends AppCompatActivity {

    RecyclerView rvTeachers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);

        rvTeachers = (RecyclerView) findViewById(R.id.rv_teachers);
        RecyclerView recyclerView = new RecyclerView(getBaseContext());

        ArrayList<Teacher> teachers = new ArrayList<>();
        teachers.add(new Teacher("Ms. Ethel Ong", R.mipmap.ic_launcher));
        teachers.add(new Teacher("Ms. Charibeth Cheng", R.mipmap.ic_launcher));
        teachers.add(new Teacher("Ms. Teresita Limoanco", R.mipmap.ic_launcher));
        teachers.add(new Teacher("Ms. Jocelyn Cu", R.mipmap.ic_launcher));
        teachers.add(new Teacher("Ms. Nathalie Lim-Cheng", R.mipmap.ic_launcher));
        teachers.add(new Teacher("Mr. Ryan Dimaunahan", R.mipmap.ic_launcher));
        teachers.add(new Teacher("Mr. Miguel Cabral", R.mipmap.ic_launcher));
        teachers.add(new Teacher("Mr. Gregory Cu", R.mipmap.ic_launcher));
        teachers.add(new Teacher("Mr. Duke Delos Santos", R.mipmap.ic_launcher));
        teachers.add(new Teacher("Dr. Nelson Marcos", R.mipmap.ic_launcher));
        teachers.add(new Teacher("Dr. Conrado Ruiz, Jr", R.mipmap.ic_launcher));
        teachers.add(new Teacher("Dr. Rafael Cabredo", R.mipmap.ic_launcher));
        teachers.add(new Teacher("Dr. Remedios Bulos", R.mipmap.ic_launcher));
        teachers.add(new Teacher("Dr. Florante Salvador", R.mipmap.ic_launcher));


        TeacherAdapter ta = new TeacherAdapter(teachers);

        // Dynamic onClickListener
        ta.setOnItemClickListener(new TeacherAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Teacher t) {
                // When item in RV is clicked, code here
                Toast.makeText(getBaseContext(), t.getName() , Toast.LENGTH_SHORT).show();
            }
        });

        rvTeachers.setAdapter(ta);
        rvTeachers.setLayoutManager(new LinearLayoutManager(getBaseContext(), LinearLayoutManager.VERTICAL, false));


    }


}