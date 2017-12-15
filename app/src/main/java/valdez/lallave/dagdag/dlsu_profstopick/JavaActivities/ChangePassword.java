package valdez.lallave.dagdag.dlsu_profstopick.JavaActivities;

import android.app.ActionBar;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
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

public class ChangePassword extends AppCompatActivity{

    DBHandler dbHandler;
    EditText etOld,
            etNew,
            etNewR,
            etEmail;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    final DatabaseReference studentDatabaseReference = databaseReference.child("student");

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_pass);
        etOld = (EditText) findViewById(R.id.et_OldPass);
        etNew = (EditText) findViewById(R.id.et_Newpass);
        etNewR = (EditText) findViewById(R.id.et_NewRpass);
        etEmail = (EditText) findViewById(R.id.et_Email);

        findViewById(R.id.changePassButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String pOld = etOld.getText().toString();
                final String pNew = etNew.getText().toString();
                final String pNewR = etNewR.getText().toString();
                SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                final String  reviewer        = SP.getString("loggedStudent", "student_email");



                studentDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    boolean valid = true;
                    Student s;
                    String key;
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot studentSnapshot : dataSnapshot.getChildren()) {
                             if (studentSnapshot.getValue(Student.class).getEmail().equals(reviewer) ) {
                               s = studentSnapshot.getValue(Student.class);
                               key = studentSnapshot.getKey();
                            }
                        }

                        Log.e("mytag", pOld);
                        Log.e("mytag", pNew);
                        Log.e("mytag", pNewR);

                       if(!validateOldPassword(pOld,s))
                            valid = false;
                        if(!validatePassword(pNew))
                            valid = false;
                        if(!validateRPassword(pNewR,pNew))
                            valid = false;

                        if(valid){

                            try {
                                s.setHashedPass(PasswordAuthentication.SHA1(pNew));
                                studentDatabaseReference.child(key).setValue(s);
                                Toast.makeText(getApplicationContext(),  "Password successfully changed!", Toast.LENGTH_SHORT).show();
                                finish();
                            } catch (NoSuchAlgorithmException e) {
                                e.printStackTrace();
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    protected boolean validateOldPassword(String password, Student s){
        try {
            if(password!=null && password.length()>=8 && s.getHashedPass().equals(PasswordAuthentication.SHA1(password))) {
                Log.e("mytag", "entered");
                return true;
            }else etOld.setError("Wrong Password");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        etNew.requestFocus();
        return false;
    }

    protected boolean validatePassword(String password){

        if(password!=null && password.length()>=8)
            return true;
        else etNew.setError("Password Must Be At Least 8 Characters");

        etNew.requestFocus();
        return false;
    }

    protected boolean validateRPassword(String password, String rPass) {

        if (rPass != null && rPass.equals(password))
            return true;
        else etNewR.setError("Password Do Not Match");

        etNewR.requestFocus();
        return false;
    }

}
