package valdez.lallave.dagdag.dlsu_profstopick.JavaActivities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import valdez.lallave.dagdag.dlsu_profstopick.Beans_Model.Suggest;
import valdez.lallave.dagdag.dlsu_profstopick.Beans_Model.Teacher;
import valdez.lallave.dagdag.dlsu_profstopick.R;
import valdez.lallave.dagdag.dlsu_profstopick.Service.DBHandler;

/**
 * Created by Carlo Valdez on 11/15/2017.
 */

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

        etName = (EditText) findViewById(R.id.et_Name);
        etDept = (EditText) findViewById(R.id.et_Department);
        name = etName.getText().toString();
        dept = etDept.getText().toString();
        dbHandler = new DBHandler(getBaseContext());
        final Suggest s = new Suggest(name,dept,reviewer);
        findViewById(R.id.suggestProfButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(name!=null && dept!=null){
                    dbHandler.addSuggestProf(s);
                    Toast.makeText(SuggestProf.this, "Suggestion Received", Toast.LENGTH_SHORT).show();
                    finish();
                }else if(name == null)
                    etName.setError("Please Fill Up This Field");
                else if(dept == null)
                    etDept.setError("Please Fill Up This Field");
            }
        });

    }
}
