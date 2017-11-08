package valdez.lallave.dagdag.dlsu_profstopick;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import valdez.lallave.dagdag.dlsu_profstopick.HomePage;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button loginButton = (Button) findViewById(R.id.loginB);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO Login backend

                Intent i = new Intent();        // Intent = opening new Activity

//                i.putExtra("passed", nameText.getText());
                i.setClass(getBaseContext(), HomePage.class);

                startActivityForResult(i, 0);
            }
        });
    }
}
