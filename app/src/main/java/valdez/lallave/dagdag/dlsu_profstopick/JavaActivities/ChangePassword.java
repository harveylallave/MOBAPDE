package valdez.lallave.dagdag.dlsu_profstopick.JavaActivities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_pass);

        etOld = (EditText) findViewById(R.id.et_OldPass);
        etNew = (EditText) findViewById(R.id.et_Newpass);
        etNewR = (EditText) findViewById(R.id.et_NewRpass);
        etEmail = (EditText) findViewById(R.id.et_Email);

        findViewById(R.id.rButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String pOld = etOld.getText().toString();
                String pNew = etNew.getText().toString();
                String pNewR = etNewR.getText().toString();
                String email = etEmail.getText().toString();

                boolean valid = true;
                dbHandler = new DBHandler(getBaseContext());

                Student s = dbHandler.getStudent(email);

                if(!validateOldPassword(pOld,s))
                    valid = false;
                if(!validatePassword(pNew))
                    valid = false;
                if(!validateRPassword(pNewR,pNew))
                    valid = false;

                if(valid){
                    try {
                        s.setHashedPass(PasswordAuthentication.SHA1(pNew));
                        dbHandler.updateStudentInfo(s);
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }





            }
        });
    }

    protected boolean validateOldPassword(String password, Student s){

        try {
            if(password!=null && password.length()>=8 && s.getHashedPass().equals(PasswordAuthentication.SHA1(password)))
                return true;
            else etNew.setError("Wrong Password");
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
