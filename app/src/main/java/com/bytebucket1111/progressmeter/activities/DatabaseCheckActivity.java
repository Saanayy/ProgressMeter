package com.bytebucket1111.progressmeter.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bytebucket1111.progressmeter.NotificationTask;
import com.bytebucket1111.progressmeter.R;
import com.bytebucket1111.progressmeter.modal.Contractor;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.onesignal.OneSignal;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class DatabaseCheckActivity extends AppCompatActivity {

    static public GoogleSignInAccount account;
    public String TAG = "Tag";
    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Contractors");
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    boolean verifyFlag = false;
    private android.support.design.widget.TextInputEditText editTextName, editTextCID, editTextContact, editTextAddress;
    private android.support.design.widget.TextInputLayout ilName, ilCID, ilContact, ilAddress, ilpasscode;
    private ProgressBar progressBar;
    private boolean existence;
    private LinearLayout inputLayout, lldetailView;
    private Button btVerify;
    private android.support.design.widget.TextInputEditText etPasscode;
    private FirebaseAuth mAuth;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_check);

        account = getIntent().getParcelableExtra("account data");

        mAuth = FirebaseAuth.getInstance();
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
        //Toast.makeText(getApplicationContext(), PhoneAuthProvider.getInstance().toString(), Toast.LENGTH_SHORT).show();
        Log.d("tag",PhoneAuthProvider.getInstance().toString());
        // OnVerificationStateChangedCallbacks*/
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Log.d(TAG, "onVerificationCompleted:" + credential);
                //Toast.makeText(getApplicationContext(), "Done", Toast.LENGTH_SHORT).show();
                verifyFlag = true;
                editTextContact.getCompoundDrawables()[2].setTint(getResources().getColor(R.color.colorSecondary));
                // signInWithPhoneAuthCredential(credential);

            }


            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e);

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // ...
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // ...
                }

                // Show a message and update the UI
                // ...
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                //Log.d(TAG, "onCodeSent:" + verificationId);

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;
                //Toast.makeText(DatabaseCheckActivity.this, "Done", Toast.LENGTH_LONG).show();
                //EditText code = findViewById(R.id.code);
                //credential = PhoneAuthProvider.getCredential(verificationId, code.getText().toString());

                // ...
            }
        };
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
            editTextContact.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent event) {
                    final int DRAWABLE_LEFT = 0;
                    final int DRAWABLE_TOP = 1;
                    final int DRAWABLE_RIGHT = 2;
                    final int DRAWABLE_BOTTOM = 3;

                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        if (event.getRawX() >= (editTextContact.getRight() - editTextContact.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                            // your action here
                            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                                    "+91" + editTextContact.getText(),        // Phone number to verify
                                    60,                 // Timeout duration
                                    TimeUnit.SECONDS,   // Unit of timeout
                                    DatabaseCheckActivity.this,               // Activity (for callback binding)
                                    mCallbacks);
                            return true;
                        }
                    }
                    return false;
                }
            });
            findViewById(R.id.bt_submit).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name = String.valueOf(editTextName.getText()).trim().toLowerCase();
                    String cid = String.valueOf(editTextCID.getText()).trim().toLowerCase();
                    String contact = String.valueOf(editTextContact.getText()).trim().toLowerCase();
                    String address = String.valueOf(editTextAddress.getText()).trim().toLowerCase();
                    boolean flag = true;
                    flag = validate(name, cid, contact, address);
                    if (flag) {
                        ArrayList<String> projects = new ArrayList<>();
                        projects.add("dummy");
                        Contractor contractor = new Contractor(name, account.getEmail(), cid, account.getId(), contact, address, "", false, false, projects, 0, 0);
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
        if (name.equalsIgnoreCase("")) {
            flag = false;
            ilName.setError("Mandatory Field");
        }
        if (cid.equalsIgnoreCase("")) {
            flag = false;
            ilCID.setError("Mandatory Field");
        }
        if (contact.equalsIgnoreCase("")) {
            flag = false;
            ilContact.setError("Mandatory Field");
        }
        if (address.equalsIgnoreCase("")) {
            flag = false;
            ilAddress.setError("Mandatory Field");
        }
        if (contact.length() > 0) {
            //String regexStr = "^[0-9]$";
            if (contact.length() != 10) {
                flag = false;
                ilContact.setError("Enter a valid Phone Number");
            }
            if (!verifyFlag) {
                flag = false;
                ilContact.setError("Press tick icon to verify your number");
            }

        }

        return flag;


    }


    private void performOnExistence() {
        final Contractor[] currentContractor = new Contractor[1];
        dbRef.child(account.getId() + "").addValueEventListener(new ValueEventListener() {
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
                            if (etPasscode.length() == 0) {
                                ilpasscode.setError("Mandatory Field");
                            } else {
                                if (passcode.equalsIgnoreCase("")) {
                                    Toast.makeText(DatabaseCheckActivity.this, "It cannot be empty", Toast.LENGTH_SHORT).show();
                                } else {
                                    String fbPasscode = currentContractor[0].getPasscode().toString();
                                    if (fbPasscode.equalsIgnoreCase(passcode) == true) {
                                        startMainActivity();
                                    } else {
                                        ilpasscode.setError("Enter the correct passcode");
                                    }
                                }
                            }

                        }
                    });

                } else {
                    Toast.makeText(DatabaseCheckActivity.this, "Get Verified", Toast.LENGTH_LONG).show();
                    Toast.makeText(DatabaseCheckActivity.this, "Get Verified", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                    new NotificationTask().execute("New User", "Verification is required", "admin_pragati");
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
