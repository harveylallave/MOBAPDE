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
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+\\.+[a-z]+",
           email,
           password,
           rPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_register);

        etEmail = (EditText) findViewById(R.id.et_Email);
        etPass = (EditText) findViewById(R.id.et_Pass);
        etRpass = (EditText) findViewById(R.id.et_Rpass);
        rButton = (Button) findViewById(R.id.rButton);
        DBHandler = new DBHandler(getBaseContext());


        rButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                email = etEmail.getText().toString();
                password = etPass.getText().toString();
                rPassword = etRpass.getText().toString();

                if(!validateEmail(email)){
                    etEmail.setError("Invalid Email");
                    etEmail.requestFocus();
                }else if(!validatePassword((password))){
                    etPass.setError("Invalid Password");
                    etPass.requestFocus();
                }else if(!validateRPassword(password, rPassword))
                    etRpass.setError("Password Do Not Match");
                else
                    try {
                        DBHandler.addNewStudent(new Student(email, PasswordAuthentication.SHA1(password)));

                        Toast.makeText(getApplicationContext(), "Added to the db",Toast.LENGTH_SHORT).show();
                        finish();
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
            }
        });
    }

    protected boolean validatePassword(String password){
        if(password!=null && password.length()>=8)
            return true;
        else
            Toast.makeText(getApplicationContext(),"Password Must Be At Least 8 Characters", Toast.LENGTH_SHORT).show();
            return false;
    }
    protected boolean validateEmail(String email){
        if(email.matches(emailPattern) && email.contains("@dlsu.edu.ph"))
            return true;
        else
            Toast.makeText(getApplicationContext(),"Invalid Email Address: " + email + email.matches(emailPattern), Toast.LENGTH_SHORT).show();
            return false;

    }
     protected boolean validateRPassword(String password, String rPass){
         if(rPass!=null && rPass.length()>=8 && rPass.equals(password))
             return true;
         else
             return false;
     }


}


