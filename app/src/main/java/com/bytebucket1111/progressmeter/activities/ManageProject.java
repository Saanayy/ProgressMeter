package com.bytebucket1111.progressmeter.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bytebucket1111.progressmeter.ProjectInfoDialog;
import com.bytebucket1111.progressmeter.R;
import com.bytebucket1111.progressmeter.modal.Project;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class ManageProject extends AppCompatActivity implements ProjectInfoDialog.ShowProjectInfoListener {

    private Project currentProject;
    private Button bAddUpdate;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.manage_project_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.mange_info:
                displayInfoDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void displayInfoDialog() {
        ProjectInfoDialog projectInfoDialog = new ProjectInfoDialog();
        projectInfoDialog.show(getSupportFragmentManager(), "project info dialog");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_project);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle bundle = getIntent().getExtras();
        String projectData = bundle.getString("projectData");
        Gson gson = new Gson();
        Type type = new TypeToken<Project>() {}.getType();
        final Project currentProjectData = gson.fromJson(projectData, type);
        currentProject = currentProjectData;

        bAddUpdate = findViewById(R.id.manage_project_addupdate);
        bAddUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ManageProject.this, UpdateActivity.class);
                String projectData = new Gson().toJson(currentProjectData);
                intent.putExtra("projectData", projectData);
                startActivity(intent);
            }
        });


    }


    @Override
    public void showProjectDetails(TextView tvTitle, TextView tvDescription, TextView tvGeolocation, TextView tvStartDate, TextView tvDuration) {
        tvTitle.setText(currentProject.getTitle());
        tvDescription.setText(currentProject.getDescription());
        tvGeolocation.setText(currentProject.getGeolocation());
        tvDuration.setText(currentProject.getDuration());
        tvStartDate.setText(currentProject.getStartDate());

    }
}
