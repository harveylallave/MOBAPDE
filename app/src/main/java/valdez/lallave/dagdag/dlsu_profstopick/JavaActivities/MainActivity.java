package valdez.lallave.dagdag.dlsu_profstopick.JavaActivities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
        StudentDBHandler studentDB = new StudentDBHandler(getBaseContext());
//        studentDB.a
        final PasswordAuthentication passwordAuthentication = new PasswordAuthentication();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO Login backend

                Intent i = new Intent();        // Intent = opening new Activity

//                i.putExtra("passed", nameText.getText());

                String email = ((EditText)findViewById(R.id.usernameET)).getText().toString(),
                       pass  = ((EditText)findViewById(R.id.passwordET)).getText().toString(),
                       hashedPass = passwordAuthentication.hash(pass.toCharArray());


//                Toast.makeText(getApplicationContext(), hashedPass + "\n" +
//                               passwordAuthentication.authenticate(pass.toCharArray(),
//                               hashedPass),Toast.LENGTH_LONG).show();
                i.setClass(getBaseContext(), HomePage.class);

                startActivityForResult(i, 0);
            }
        });
    }
}
