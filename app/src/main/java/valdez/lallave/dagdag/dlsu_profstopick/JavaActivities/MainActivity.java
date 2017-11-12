package valdez.lallave.dagdag.dlsu_profstopick.JavaActivities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import valdez.lallave.dagdag.dlsu_profstopick.Beans_Model.Student;
import valdez.lallave.dagdag.dlsu_profstopick.JavaActivities.HomePage;
import valdez.lallave.dagdag.dlsu_profstopick.R;
import valdez.lallave.dagdag.dlsu_profstopick.Service.PasswordAuthentication;
import valdez.lallave.dagdag.dlsu_profstopick.Service.StudentDBHandler;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button loginButton = (Button) findViewById(R.id.loginB);
        final StudentDBHandler studentDB = new StudentDBHandler(getBaseContext());
        final PasswordAuthentication passwordAuthentication = new PasswordAuthentication();

//        studentDB.addNewStudent(new Student("harvey_lallave@dlsu.edu.ph", passwordAuthentication.hash("1234".toCharArray())));
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO Login backend

                Intent i = new Intent();        // Intent = opening new Activity

//                i.putExtra("passed", nameText.getText());

                String email = ((EditText)findViewById(R.id.usernameET)).getText().toString(),
                       pass  = ((EditText)findViewById(R.id.passwordET)).getText().toString();
//                       hashedPass = passwordAuthentication.hash(pass.toCharArray());





                i.setClass(getBaseContext(), HomePage.class);

//                if(studentDB.validateStudent(email, hashedPass))
                     startActivityForResult(i, 0);
//                else Toast.makeText(getApplicationContext(), "Invalid email or password",Toast.LENGTH_LONG).show();
            }
        });
    }
}
