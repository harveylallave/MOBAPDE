package valdez.lallave.dagdag.dlsu_profstopick.JavaActivities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
           password,
           rPassword;


    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    final DatabaseReference studentDatabaseReference = databaseReference.child("student");

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

                final String email = etEmail.getText().toString();
                password      = etPass.getText().toString();
                rPassword     = etRpass.getText().toString();

                studentDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    boolean checkEmail=true;
                    boolean checkPass=true;
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(!validateRPassword(password, rPassword))
                            checkPass = false;
                        if(!validatePassword((password)))
                            checkPass = false;
                        if(email.matches(emailPattern)) { //Checks if email pattern has been satisfied
                            for (DataSnapshot studentSnapshot : dataSnapshot.getChildren()) {
                                Log.e("My tag", studentSnapshot.getValue(Student.class).getEmail());
                                if(studentSnapshot.getValue(Student.class).getEmail().equals(email)){
                                    checkEmail=false;
                                    break;
                                }else
                                    checkEmail = true;
                            }

                            if(checkEmail&&checkPass) { //Success
                                try {
                                    DBHandler.addNewStudent(new Student(email, PasswordAuthentication.SHA1(password)));

                                    Toast.makeText(getApplicationContext(), email + " added to the db", Toast.LENGTH_SHORT).show();
                                    finish();
                                } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                            }else if(checkEmail==false &&checkPass==true) {
                                etEmail.setError("Email already exists");
                                etEmail.requestFocus();
                            }
                        }else{
                            etEmail.setError("Email\'s domain must be @dlsu.edu.ph");
                            etEmail.requestFocus();
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });
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


