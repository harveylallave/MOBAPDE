package valdez.lallave.dagdag.dlsu_profstopick.JavaActivities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import valdez.lallave.dagdag.dlsu_profstopick.Beans_Model.Admin;
import valdez.lallave.dagdag.dlsu_profstopick.Beans_Model.Comment;
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

//        try {
//        DBHandler.addNewAdmin(new Admin("profs_to_pick@dlsu.edu.ph", PasswordAuthentication.SHA1("1234")));
//        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }

//        Temporary register
//        try {
//           DBHandler.addNewAdmin(new Admin("profs_to_pick@dlsu.edu.ph", PasswordAuthentication.SHA1("1234")));
//            DBHandler.addNewStudent(new Student("harvey_lallave@dlsu.edu.ph", PasswordAuthentication.SHA1("1234")));
/*            DBHandler.addNewTeacher(new Teacher("Ms. Ethel Ong", "CCS"));
            DBHandler.addNewTeacher(new Teacher("Ms. Charibeth Cheng", "CCS"));
            DBHandler.addNewTeacher(new Teacher("Ms. Teresita Limoanco", "CCS"));
            DBHandler.addNewTeacher(new Teacher("Ms. Jocelyn Cu", "CCS"));
            DBHandler.addNewTeacher(new Teacher("Ms. Nathalie Lim-Cheng", "CCS"));*/
//        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }


//        DBHandler.addNewComment(new Comment("Sample title", "Sample body", 3, "dan@dlsu.edu.ph", "Ms. Teresita Limoanco"));
//        DBHandler.addNewComment(new Comment("Sample title3", "Sample body3", 5, "dan_dagdag@dlsu.edu.ph", "Ms. Charibeth Cheng"));
//        DBHandler.addNewComment(new Comment("Sample title4", "Sample body4", 1, "dan_dagdag@dlsu.edu.ph", "Ms. Ethel Ong"));

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference studentDatabaseReference = databaseReference.child("student");

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = ((EditText)findViewById(R.id.usernameET)).getText().toString(),
                       pass  = ((EditText)findViewById(R.id.passwordET)).getText().toString();
                final EditText clearPass = (EditText)findViewById(R.id.passwordET);

                try {
                    final String hashedPass = PasswordAuthentication.SHA1(pass);
                    studentDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        boolean checkStudent=false;
                        String key;
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot studentSnapshot : dataSnapshot.getChildren()) {
                                if(studentSnapshot.getValue(Student.class).getEmail().equals(email) && studentSnapshot.getValue(Student.class).getHashedPass().equals(hashedPass)){
                                    checkStudent=true;
                                    key = studentSnapshot.getKey();
                                    break;
                                }else {
                                    checkStudent = false;
                                }
                            }
                            if(checkStudent){
                                SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                                SharedPreferences.Editor SPE = SP.edit();
                                SPE.putString("loggedStudent", email);
                                SPE.putString("studentkey", key);
                                SPE.apply();

                                startActivity(new Intent(getBaseContext(), HomePage.class));
                                finish();
                            }else {
                                clearPass.setText("");
                                Toast.makeText(getApplicationContext(), "Invalid email or password", Toast.LENGTH_LONG).show();
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

//                try {
//                    if(DBHandler.validateStudent(email, PasswordAuthentication.SHA1(pass))) {
//
//                        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
//                        SharedPreferences.Editor SPE = SP.edit();
//                        SPE.putString("loggedStudent", email);
//                        SPE.apply();
//
//                        startActivity(new Intent(getBaseContext(), HomePage.class));
//                        finish();
//                    } else if (DBHandler.validateAdmin(email, PasswordAuthentication.SHA1(pass))) {
//                        startActivity(new Intent(getBaseContext(), AndroidDatabaseManager.class));
//                        finish();
//                    } else {
//                        clearPass.setText("");
//                        Toast.makeText(getApplicationContext(), "Invalid email or password", Toast.LENGTH_LONG).show();
//                    }
//                } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }
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
                startActivity(new Intent(getBaseContext(), ForgotPass.class));
            }
        });
    }
}
