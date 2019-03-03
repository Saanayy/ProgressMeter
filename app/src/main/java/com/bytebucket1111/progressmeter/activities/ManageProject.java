package com.bytebucket1111.progressmeter.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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

public class ManageProject extends AppCompatActivity {

    Project currentProjectData;
    boolean isOpen = false;
    private Project currentProject;
    private Button bAddUpdate;
    private RecyclerView recyclerViewUpdate;
    private ArrayList<Update> updatesList = new ArrayList<>();
    private DatabaseReference projectRef = FirebaseDatabase.getInstance().getReference("Projects");
    private DatabaseReference updateRef = FirebaseDatabase.getInstance().getReference("Updates");
    private boolean fetchFirst = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_project);

        Bundle bundle = getIntent().getExtras();
        String projectData = bundle.getString("projectData");
        Gson gson = new Gson();
        Type type = new TypeToken<Project>() {
        }.getType();
        currentProjectData = gson.fromJson(projectData, type);
        bAddUpdate = findViewById(R.id.manage_project_addupdate);
        currentProject = currentProjectData;
        if(currentProject.isFinished())
        {
            bAddUpdate.setVisibility(View.GONE);
        }
        fetchUpdates();

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
    protected void onStart() {

        super.onStart();
        isOpen = true;
    }
    @Override
    protected void onStop() {

        super.onStop();
        isOpen = false;
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
                            if (!fetchFirst) {
                                Update update = null;
                                update = dataSnapshot.getValue(Update.class);
                                Log.d("Update Added", "True" + updatesList.size());
                                updatesList.add(0, update);
                                if (finalI == updatesNames.size() - 1) {
                                    setRecyclerView();
                                    fetchFirst = true;
                                }
                            } else {
                                if(isOpen) {
                                    Intent intent = new Intent(ManageProject.this, ManageProject.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    String projectData = new Gson().toJson(currentProjectData);
                                    intent.putExtra("projectData", projectData);
                                    startActivity(intent);
                                    overridePendingTransition(0, 0);
                                }
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
        recyclerViewUpdate.setLayoutManager(new LinearLayoutManager(ManageProject.this));
        UpdateAdapter updateAdapter = new UpdateAdapter(this, updatesList);
        recyclerViewUpdate.setAdapter(updateAdapter);
        recyclerViewUpdate.scrollToPosition(0);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ManageProject.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        //String projectData = new Gson().toJson(currentProjectData);
        //intent.putExtra("projectData",projectData);
        startActivity(intent);
    }

}
