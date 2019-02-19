package com.bytebucket1111.progressmeter.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.bytebucket1111.progressmeter.R;
import com.bytebucket1111.progressmeter.modal.Contractor;
import com.bytebucket1111.progressmeter.modal.Project;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.onesignal.OneSignal;

import java.util.ArrayList;

public class DatabaseCheckActivity extends AppCompatActivity {

    public GoogleSignInAccount account;
    private EditText editTextName, editTextCID, editTextContact, editTextAddress;
    private ProgressBar progressBar;
    private boolean existence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_check);

        account = getIntent().getParcelableExtra("account data");

        progressBar = findViewById(R.id.progress_bar);

        final String id = account.getId() + "";

        //assigning tgs to user for personal notifications
        OneSignal.sendTag("key1",id);

        try {
            DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("Contractors").child(id);
            mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Contractor contractor = dataSnapshot.getValue(Contractor.class);
                    existence = contractor != null;
                    performCheck();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            //Toast.makeText(this, mRef + "", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            existence = false;
        }

    }

    private void performCheck() {
        if (existence) {
            //Toast.makeText(this, "User exists. Going to next activity", Toast.LENGTH_SHORT).show();
            startMainActivity();
        } else {
            //Toast.makeText(this, "No such user", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            findViewById(R.id.detail_view).setVisibility(View.VISIBLE);
            editTextName = findViewById(R.id.et_name);
            editTextCID = findViewById(R.id.et_cid);
            editTextContact = findViewById(R.id.et_contact);
            editTextAddress = findViewById(R.id.et_address);

            findViewById(R.id.bt_submit).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name = String.valueOf(editTextName.getText());
                    String cid = String.valueOf(editTextCID.getText());
                    String contact = String.valueOf(editTextContact.getText());
                    String address = String.valueOf(editTextAddress.getText());
                    ArrayList<String> projects = new ArrayList<>();
                    projects.add("dummy");
                    Contractor contractor = new Contractor(name, account.getEmail(), cid, account.getId(), contact, address, false, projects);
                    FirebaseDatabase.getInstance().getReference("Contractors").child(account.getId()).setValue(contractor);
                    //Toast.makeText(DatabaseCheckActivity.this,"Data pushed",Toast.LENGTH_SHORT).show();
                    startMainActivity();
                }
            });

        }
    }

    private void startMainActivity() {
        Intent intent = new Intent(DatabaseCheckActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(0, 0);
    }
}
