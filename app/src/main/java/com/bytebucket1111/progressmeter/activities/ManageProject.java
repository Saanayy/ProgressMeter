package com.bytebucket1111.progressmeter.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bytebucket1111.progressmeter.R;
import com.bytebucket1111.progressmeter.UpdateAdapter;
import com.bytebucket1111.progressmeter.modal.Project;
import com.bytebucket1111.progressmeter.modal.Update;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ManageProject extends AppCompatActivity  {

    private Project currentProject;
    private Button bAddUpdate;
    private RecyclerView recyclerViewUpdate;
    private ArrayList<Update> updatesList = new ArrayList<>();
    private DatabaseReference projectRef = FirebaseDatabase.getInstance().getReference("Projects");
    private DatabaseReference updateRef = FirebaseDatabase.getInstance().getReference("Updates");
    TextView tvTitle, tvDescription,tvGeoLocation,tvStartDate,tvDuration;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_project);

        tvTitle = findViewById(R.id.manage_title);
        tvDescription = findViewById(R.id.manage_description);
        tvGeoLocation = findViewById(R.id.manage_geolocation);
        tvStartDate = findViewById(R.id.manage_startdate);
        tvDuration = findViewById(R.id.manage_duration);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle bundle = getIntent().getExtras();
        String projectData = bundle.getString("projectData");
        Gson gson = new Gson();
        Type type = new TypeToken<Project>() {
        }.getType();
        final Project currentProjectData = gson.fromJson(projectData, type);
        currentProject = currentProjectData;

        tvTitle.setText(currentProject.getTitle());
        tvDescription.setText(currentProject.getDescription());
        tvDuration.setText(currentProject.getDuration());
        tvGeoLocation.setText(currentProject.getGeolocation());
        tvStartDate.setText(currentProject.startDate);

        fetchUpdates();


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

    private void fetchUpdates() {

        projectRef.child(currentProject.getProjectId()).child("updateId").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                updatesList.clear();
                final ArrayList<String> updatesNames = (ArrayList<String>) dataSnapshot.getValue();
                for (int i = 1; i < updatesNames.size(); i++) {

                    final int finalI = i;
                    updateRef.child(updatesNames.get(i)).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Update update;
                            update = dataSnapshot.getValue(Update.class);
                            updatesList.add(update);
                            if (finalI == updatesNames.size() - 1) {
                                setRecyclerView();
                            }
                        }


                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void setRecyclerView() {
        recyclerViewUpdate = findViewById(R.id.rv_updates);
        recyclerViewUpdate.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        UpdateAdapter updateAdapter = new UpdateAdapter(this, updatesList);
        recyclerViewUpdate.setAdapter(updateAdapter);
        recyclerViewUpdate.scrollToPosition(updatesList.size()-1);
    }


}
