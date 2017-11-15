package valdez.lallave.dagdag.dlsu_profstopick.JavaActivities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import valdez.lallave.dagdag.dlsu_profstopick.Beans_Model.Admin;
import valdez.lallave.dagdag.dlsu_profstopick.Beans_Model.Student;
import valdez.lallave.dagdag.dlsu_profstopick.Beans_Model.Teacher;
import valdez.lallave.dagdag.dlsu_profstopick.R;
import valdez.lallave.dagdag.dlsu_profstopick.Service.DBHandler;
import valdez.lallave.dagdag.dlsu_profstopick.Service.PasswordAuthentication;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button loginButton = (Button) findViewById(R.id.loginB);
        final DBHandler DBHandler = new DBHandler(getBaseContext());
        final TextView registerView = (TextView) findViewById(R.id.createAccTV);
        final TextView forgotPassView = (TextView) findViewById(R.id.forgotPassTV);

        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String loggedStudent = SP.getString("loggedStudent", null);

        if(loggedStudent != null){
            Intent I = new Intent(getBaseContext(), HomePage.class);
            startActivity(I);
            finish();
        }

//        Temporary register
//        try {
//            DBHandler.addNewAdmin(new Admin("profs_to_pick@dlsu.edu.ph", PasswordAuthentication.SHA1("1234")));
//            DBHandler.addNewStudent(new Student("harvey_lallave@dlsu.edu.ph", PasswordAuthentication.SHA1("1234")));
//            DBHandler.addNewTeacher(new Teacher("Ms. Ethel Ong", "CCS"));
//            DBHandler.addNewTeacher(new Teacher("Ms. Charibeth Cheng", "CCS"));
//            DBHandler.addNewTeacher(new Teacher("Ms. Teresita Limoanco", "CCS"));
//            DBHandler.addNewTeacher(new Teacher("Ms. Jocelyn Cu", "CCS"));
//            DBHandler.addNewTeacher(new Teacher("Ms. Nathalie Lim-Cheng", "CCS"));
//        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = ((EditText)findViewById(R.id.usernameET)).getText().toString(),
                       pass  = ((EditText)findViewById(R.id.passwordET)).getText().toString();
                EditText clearPass = (EditText)findViewById(R.id.passwordET);

                try {
                    if(DBHandler.validateStudent(email, PasswordAuthentication.SHA1(pass))) {
                        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                        SharedPreferences.Editor SPE = SP.edit();
                        SPE.putString("loggedStudent", email);
                        SPE.apply();

                        startActivity(new Intent(getBaseContext(), HomePage.class));
                        finish();
                    } else if (DBHandler.validateAdmin(email, PasswordAuthentication.SHA1(pass))) {
                        startActivity(new Intent(getBaseContext(), AndroidDatabaseManager.class));
                        finish();
                    } else {
                        clearPass.setText("");
                        Toast.makeText(getApplicationContext(), "Invalid email or password", Toast.LENGTH_LONG).show();
                    }
                } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
        registerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getBaseContext(), RegisterActivity.class));
            }
        });
        forgotPassView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getBaseContext(),ForgotPass.class ));
            }
        });
    }
}
