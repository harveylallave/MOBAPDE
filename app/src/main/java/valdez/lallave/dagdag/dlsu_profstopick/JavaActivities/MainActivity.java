package valdez.lallave.dagdag.dlsu_profstopick.JavaActivities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import valdez.lallave.dagdag.dlsu_profstopick.Beans_Model.Admin;
import valdez.lallave.dagdag.dlsu_profstopick.Beans_Model.Student;
import valdez.lallave.dagdag.dlsu_profstopick.Beans_Model.Teacher;
import valdez.lallave.dagdag.dlsu_profstopick.R;
import valdez.lallave.dagdag.dlsu_profstopick.Service.PasswordAuthentication;
import valdez.lallave.dagdag.dlsu_profstopick.Service.UserDBHandler;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button loginButton = (Button) findViewById(R.id.loginB);
        final UserDBHandler userDBHandler = new UserDBHandler(getBaseContext());


//        Temporary register
//        try {
//            userDBHandler.addNewAdmin(new Admin("profs_to_pick@dlsu.edu.ph", PasswordAuthentication.SHA1("1234")));
//            userDBHandler.addNewStudent(new Student("harvey_lallave@dlsu.edu.ph", PasswordAuthentication.SHA1("1234")));
//            userDBHandler.addNewTeacher(new Teacher("Ms. Ethel Ong", "CCS"));
//            userDBHandler.addNewTeacher(new Teacher("Ms. Charibeth Cheng", "CCS"));
//            userDBHandler.addNewTeacher(new Teacher("Ms. Teresita Limoanco", "CCS"));
//            userDBHandler.addNewTeacher(new Teacher("Ms. Jocelyn Cu", "CCS"));
//            userDBHandler.addNewTeacher(new Teacher("Ms. Nathalie Lim-Cheng", "CCS"));
//            userDBHandler.addNewTeacher(new Teacher("Ms. Charibeth Cheng", "CCS"));
//            userDBHandler.addNewTeacher(new Teacher("Ms. Charibeth Cheng", "CCS"));
//            userDBHandler.addNewTeacher(new Teacher("Ms. Charibeth Cheng", "CCS"));
//        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent();        // Intent = opening new Activity

//                i.putExtra("passed", nameText.getText());

                String email = ((EditText)findViewById(R.id.usernameET)).getText().toString(),
                       pass  = ((EditText)findViewById(R.id.passwordET)).getText().toString();


                i.setClass(getBaseContext(), HomePage.class);

                try {
                    if(userDBHandler.validateStudent(email, PasswordAuthentication.SHA1(pass)))
                         startActivityForResult(i, 0);
                    else if (userDBHandler.validateAdmin(email, PasswordAuthentication.SHA1(pass))) {
                        Intent dbmanager = new Intent(getBaseContext(), AndroidDatabaseManager.class);
                        startActivity(dbmanager);
                    }
                    else
                        Toast.makeText(getApplicationContext(), "Invalid email or password",Toast.LENGTH_LONG).show();

                } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
