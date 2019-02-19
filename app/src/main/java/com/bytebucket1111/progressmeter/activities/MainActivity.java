package com.bytebucket1111.progressmeter.activities;

import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bytebucket1111.progressmeter.AddProjectDialog;
import com.bytebucket1111.progressmeter.R;

public class MainActivity extends AppCompatActivity implements AddProjectDialog.AddProjectListener {

    //comfirmation before exit
    private boolean appExit=false;
    private FloatingActionButton fabAddProject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fabAddProject = findViewById(R.id.main_add_project);
        fabAddProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddProjectDialog();
            }
        });

    }

    private void openAddProjectDialog() {
        AddProjectDialog addProjectDialog = new AddProjectDialog();
        addProjectDialog.show(getSupportFragmentManager(), "add project dialog");
    }

    //waits for three seconds incase back is pressed again before the 3 second timer expires app exits.
    @Override
    public void onBackPressed() {
        if(appExit){
            System.gc();
            System.exit(0);
        }
        else {
            Toast.makeText(this, "Press Back again to Exit.",
                    Toast.LENGTH_SHORT).show();
            appExit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    appExit = false;
                }
            }, 3 * 1000);
        }
    }

    @Override
    public void addProjectToFirebase(String title, String desc, String geolocation, String startDate, String duration) {
        Toast.makeText(this, "Added", Toast.LENGTH_SHORT).show();
        

    }
}