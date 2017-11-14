package valdez.lallave.dagdag.dlsu_profstopick.JavaActivities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import valdez.lallave.dagdag.dlsu_profstopick.Beans_Model.Student;
import valdez.lallave.dagdag.dlsu_profstopick.R;
import valdez.lallave.dagdag.dlsu_profstopick.Service.DBHandler;
import valdez.lallave.dagdag.dlsu_profstopick.Service.PasswordAuthentication;

/**
 * Created by Carlo Valdez on 11/14/2017.
 */

public class RegisterActivity extends AppCompatActivity {

    EditText etEmail,
             etPass,
             etRpass;
    Button rButton;
    DBHandler DBHandler;
    String emailPattern = "[a-zA-Z0-9._-]+@dlsu+\\.+edu+\\.+ph+",
           email,
           password,
           rPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_register);
        getSupportActionBar();

        etEmail = (EditText) findViewById(R.id.et_Email);
        etPass = (EditText) findViewById(R.id.et_Pass);
        etRpass = (EditText) findViewById(R.id.et_Rpass);
        rButton = (Button) findViewById(R.id.rButton);
        DBHandler = new DBHandler(getBaseContext());


        rButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                email         = etEmail.getText().toString();
                password      = etPass.getText().toString();
                rPassword     = etRpass.getText().toString();
                boolean valid = true;

                if(!validateRPassword(password, rPassword))
                    valid = false;
                if(!validatePassword((password)))
                    valid = false;
                if(!validateEmail(email))
                    valid = false;

                if (valid) {
                    try {
                        DBHandler.addNewStudent(new Student(email, PasswordAuthentication.SHA1(password)));

                        Toast.makeText(getApplicationContext(), email + " added to the db", Toast.LENGTH_SHORT).show();
                        finish();
                    } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    protected boolean validateEmail(String email){
        if(email.matches(emailPattern))
            if(!DBHandler.validateStudent(email))
                 return true;
            else etEmail.setError("Email already exists");
        else     etEmail.setError("Email\'s domain must be @dlsu.edu.ph");


        etEmail.requestFocus();
        return false;
    }

    protected boolean validatePassword(String password){

        if(password!=null && password.length()>=8)
             return true;
        else etPass.setError("Password Must Be At Least 8 Characters");

        etPass.requestFocus();
        return false;
    }

     protected boolean validateRPassword(String password, String rPass) {

         if (rPass != null && rPass.equals(password))
              return true;
         else etRpass.setError("Password Do Not Match");

         etRpass.requestFocus();
         return false;
     }

}


