package com.bytebucket1111.progressmeter.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bytebucket1111.progressmeter.R;
import com.bytebucket1111.progressmeter.modal.Contractor;
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
    private android.support.design.widget.TextInputEditText editTextName, editTextCID, editTextContact, editTextAddress;
    private android.support.design.widget.TextInputLayout ilName, ilCID, ilContact, ilAddress,ilpasscode;
    private ProgressBar progressBar;
    private boolean existence;
    private LinearLayout inputLayout,lldetailView;
    private Button btVerify;
    private android.support.design.widget.TextInputEditText etPasscode;
    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Contractors");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_check);

        account = getIntent().getParcelableExtra("account data");

        progressBar = findViewById(R.id.progress_bar);
        inputLayout = findViewById(R.id.database_check_passcode_linearlayout);
        btVerify = findViewById(R.id.db_check_verifiy);
        etPasscode = findViewById(R.id.database_check_passcode);
        lldetailView = findViewById(R.id.detail_view);

        ilName = findViewById(R.id.dbcheck_name);
        ilCID = findViewById(R.id.dbcheck_cid);
        ilContact = findViewById(R.id.dbcheck_contact);
        ilAddress = findViewById(R.id.dbcheck_address);
        ilpasscode = findViewById(R.id.dbcheck_passcodeverify);


        final String id = account.getId() + "";

        //assigning tgs to user for personal notifications
        OneSignal.sendTag("key1", id);

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
            performOnExistence();

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
                    String name = String.valueOf(editTextName.getText()).trim().toLowerCase();
                    String cid = String.valueOf(editTextCID.getText()).trim().toLowerCase();
                    String contact = String.valueOf(editTextContact.getText()).trim().toLowerCase();
                    String address = String.valueOf(editTextAddress.getText()).trim().toLowerCase();
                    boolean flag = true;
                    flag = validate(name,cid,contact,address);
                    if(flag){
                        ArrayList<String> projects = new ArrayList<>();
                        projects.add("dummy");
                        Contractor contractor = new Contractor(name, account.getEmail(), cid, account.getId(), contact, address,"", false, false, projects, 0, 0);
                        FirebaseDatabase.getInstance().getReference("Contractors").child(account.getId()).setValue(contractor);
                        existence = true;
                        lldetailView.setVisibility(View.GONE);
                        progressBar.setVisibility(View.VISIBLE);
                        performOnExistence();
                    }


                }
            });

        }
    }

    private boolean validate(String name, String cid, String contact, String address) {

        boolean flag = true;
        if(name.equalsIgnoreCase("")){
            flag = false;
            ilName.setError("Mandatory Field");
        }
        if(cid.equalsIgnoreCase("")){
            flag = false;
            ilCID.setError("Mandatory Field");
        }
        if(contact.equalsIgnoreCase("")){
            flag = false;
            ilContact.setError("Mandatory Field");
        }
        if(address.equalsIgnoreCase("")){
            flag = false;
            ilAddress.setError("Mandatory Field");
        }
        if(contact.length()>0)
        {
            //String regexStr = "^[0-9]$";
            if(contact.length()!=10 ) {
                flag = false;
                ilContact.setError("Enter a valid Phone Number");
            }
        }

        return flag;



    }


    private void performOnExistence() {
        final Contractor[] currentContractor = new Contractor[1];
        dbRef.child(account.getId()+"").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentContractor[0] = dataSnapshot.getValue(Contractor.class);
                if (currentContractor[0].isVerified() == true) {
                    progressBar.setVisibility(View.GONE);
                    inputLayout.setVisibility(View.VISIBLE);

                    btVerify.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            String passcode = etPasscode.getText().toString().trim();
                            if(etPasscode.length() == 0)
                            {
                                ilpasscode.setError("Mandatory Field");
                            }
                            else
                            {
                                if (passcode.equalsIgnoreCase("")) {
                                    Toast.makeText(DatabaseCheckActivity.this, "It cannot be empty", Toast.LENGTH_SHORT).show();
                                } else {
                                    String fbPasscode = currentContractor[0].getPasscode().toString();
                                    if (fbPasscode.equalsIgnoreCase(passcode) == true) {
                                        startMainActivity();
                                    }
                                    else
                                    {
                                        ilpasscode.setError("Enter the correct passcode");
                                    }
                                }
                            }

                        }
                    });

                } else {
                    Toast.makeText(DatabaseCheckActivity.this, "Get Verified", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //Toast.makeText(this, "User exists. Going to next activity", Toast.LENGTH_SHORT).show();


    }

    private void startMainActivity() {
        Intent intent = new Intent(DatabaseCheckActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(0, 0);
    }
}
