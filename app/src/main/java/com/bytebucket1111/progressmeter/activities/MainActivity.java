package com.bytebucket1111.progressmeter.activities;

import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bytebucket1111.progressmeter.AddProjectDialog;
import com.bytebucket1111.progressmeter.R;
import com.bytebucket1111.progressmeter.modal.Project;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AddProjectDialog.AddProjectListener {

    //comfirmation before exit
    private boolean appExit=false;
    private FloatingActionButton fabAddProject;
    DatabaseReference dbRefProjects = FirebaseDatabase.getInstance().getReference("Projects");
    DatabaseReference dbRefContractors = FirebaseDatabase.getInstance().getReference("Contractors");
    private String userId;

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
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(MainActivity.this);
        if (acct != null) {
            String personName = acct.getDisplayName();
            userId = acct.getId();
        }

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
        final String projectKey = dbRefProjects.push().getKey();
        ArrayList<String>updateList = new ArrayList<>();
        updateList.add("dummy");
        Project project = new Project(title,desc,geolocation,startDate,duration,userId,updateList);
        dbRefProjects.child(projectKey).setValue(project);
        dbRefContractors.child(userId).child("projectIds").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String>  project = (ArrayList<String>) dataSnapshot.getValue();
                String id = project.size() + "";
                dbRefContractors.child(userId).child("projectIds").child(id).setValue(projectKey);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
