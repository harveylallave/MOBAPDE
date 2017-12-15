package valdez.lallave.dagdag.dlsu_profstopick.JavaActivities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import valdez.lallave.dagdag.dlsu_profstopick.Beans_Model.Suggest;
import valdez.lallave.dagdag.dlsu_profstopick.Beans_Model.Teacher;
import valdez.lallave.dagdag.dlsu_profstopick.R;
import valdez.lallave.dagdag.dlsu_profstopick.Service.DBHandler;

public class SuggestProf extends AppCompatActivity {

    EditText etName,etDept;
    String name, dept;
    DBHandler dbHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.suggest_prof);
        getSupportActionBar();

        SharedPreferences SP          = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        final String  reviewer        = SP.getString("loggedStudent", "student_email");

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference suggestDatabaseReference = databaseReference.child("suggest");

        etName = (EditText) findViewById(R.id.et_Name);
        etDept = (EditText) findViewById(R.id.et_Department);
        dbHandler = new DBHandler(getBaseContext());

        findViewById(R.id.suggestProfButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = etName.getText().toString();
                dept = etDept.getText().toString();
                boolean valid = true;
                Suggest s = new Suggest(name,dept,reviewer);
                if(name.equals("")) {
                    valid = false;
                    etName.setError("Please Fill Up This Field");
                }
                if(dept.equals("")) {
                    valid = false;
                    etDept.setError("Please Fill Up This Field");
                }

                if(valid){
                    /*dbHandler.addSuggestProf(s);*/
                    String key = suggestDatabaseReference.push().getKey();
                    suggestDatabaseReference.child(key).setValue(s);
                    Toast.makeText(SuggestProf.this, "Suggestion Received", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

    }
}
