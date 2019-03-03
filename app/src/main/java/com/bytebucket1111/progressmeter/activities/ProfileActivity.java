package com.bytebucket1111.progressmeter.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bytebucket1111.progressmeter.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    TextView tvName, tvEmail, tvUserId, tvProjects, tvInProgressCount, tvCompleteCount;
    CircleImageView ivProfileImage;

    String name, id, email;
    Uri profileImage;

    Button bSignout;
    long projectCount;

    private GoogleApiClient mGoogleApiClient;

    private DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Contractors");

    @Override
    protected void onStart() {

        super.onStart();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mGoogleApiClient.connect();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //initialise all the views
        init();

        //fetch Data
        fetchUserData();

        //update the interface
        updateUI();

        bSignout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logOut();

            }
        });


        dbRef.child(id).child("projectIds").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                projectCount = dataSnapshot.getChildrenCount();
                tvProjects.setText(String.valueOf(projectCount));
                tvCompleteCount.setText(String.valueOf(0));
                tvInProgressCount.setText(String.valueOf(projectCount - 0));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return true;
    }

    void logOut() {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(ProfileActivity.this);
        builder.setTitle("Log Out");

        StringBuilder sb = new StringBuilder();
        sb.append("Do you want to Sign Out?");
        builder.setMessage(sb.toString());
        builder.setPositiveButton("Sign Out", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                        new ResultCallback<Status>() {
                            @Override
                            public void onResult(Status status) {
                                // ...
                                Toast.makeText(getApplicationContext(), "Signed Out", Toast.LENGTH_SHORT).show();
                                Intent loginIntent = new Intent(ProfileActivity.this, LoginActivity.class);
                                loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(loginIntent);
                                finish();
                            }
                        });
            }
        });
        builder.show();
    }

    private void updateUI() {
        tvName.setText(name);
        tvEmail.setText(email);
        tvUserId.setText(id);
        Glide.with(ProfileActivity.this)
                .load(profileImage)
                .fitCenter()
                .placeholder(R.drawable.ic_man)
                .into(ivProfileImage);
    }

    private void fetchUserData() {
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(ProfileActivity.this);
        if (acct != null) {
            name = acct.getDisplayName();
            email = acct.getEmail();
            id = acct.getId();
            profileImage = acct.getPhotoUrl();
        }
    }


    private void init() {
        tvName = findViewById(R.id.profile_name);
        tvEmail = findViewById(R.id.profile_email);
        tvUserId = findViewById(R.id.profile_userid);
        ivProfileImage = findViewById(R.id.profile_image);
        bSignout = findViewById(R.id.profile_signout);
        tvProjects = findViewById(R.id.profile_project_count);
        tvCompleteCount = findViewById(R.id.profile_complete_count);
        tvInProgressCount = findViewById(R.id.profile_progress_count);
    }
}
