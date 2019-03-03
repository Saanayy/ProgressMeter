package com.bytebucket1111.progressmeter.activities;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bytebucket1111.progressmeter.R;
import com.bytebucket1111.progressmeter.Utilites;
import com.bytebucket1111.progressmeter.helper.NotificationHelper;
import com.bytebucket1111.progressmeter.modal.Project;
import com.bytebucket1111.progressmeter.modal.Update;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vinaygaba.rubberstamp.RubberStamp;
import com.vinaygaba.rubberstamp.RubberStampConfig;
import com.vinaygaba.rubberstamp.RubberStampPosition;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

public class UpdateActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 1;
    private static final int REQUEST_PERMISSION = 200;

    Button createUpdate, takeImage;
    RequestQueue queue;
    android.support.design.widget.TextInputEditText etProjectName, etLocation, etDate, etDescription;
    TextInputLayout ilDate, ilDesc;
    ImageView ivImage;
    String url = "http://api.openweathermap.org/data/2.5/weather?lat=90.00&lon=135.00&mode=json&appid=5f5f52012f486ec45ea9e55600fab0d2";
    //TextView textView;
    int weatherId = 007;
    Project currentProject;
    Spinner weatherSpinner, workSpinner;
    StorageReference storageRef = FirebaseStorage.getInstance().getReference();
    String imagePath = "none";
    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Contractors");
    DatabaseReference projectRef = FirebaseDatabase.getInstance().getReference("Projects");
    DatabaseReference updateRef = FirebaseDatabase.getInstance().getReference("Updates");
    private int mYear, mMonth, mDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        //textView = findViewById(R.id.text);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        createUpdate = findViewById(R.id.update_addupdate);
        etDate = findViewById(R.id.update_date);
        etLocation = findViewById(R.id.update_location);
        etProjectName = findViewById(R.id.update_projectname);
        takeImage = findViewById(R.id.take_image);
        etDescription = findViewById(R.id.update_desc);
        ivImage = findViewById(R.id.update_image_preview);

        ilDate = findViewById(R.id.update_date_il);
        ilDesc = findViewById(R.id.update_desc_il);


        //External storage request
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION);
        }

        takeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });


        //Notification Checker
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel("channelId", "channelId", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        String title = "Conflict Notification";
        String body = "Body of Notification";


        weatherSpinner = (Spinner) findViewById(R.id.update_spinnerweather);
        String[] weather = {"Clear Sky", "Snow", "Rain", "Drizzle", "Thunder Storm"};
        ArrayAdapter<CharSequence> spinAdapter = new ArrayAdapter<CharSequence>(UpdateActivity.this, R.layout.spinner_text, weather);
        spinAdapter.setDropDownViewResource(R.layout.spinner_dropdown);
        weatherSpinner.setAdapter(spinAdapter);

        workSpinner = (Spinner) findViewById(R.id.update_spinnerwork);
        String[] work = {"InProgress", "Slowed", "Stopped"};
        spinAdapter = new ArrayAdapter<CharSequence>(UpdateActivity.this, R.layout.spinner_text, work);
        spinAdapter.setDropDownViewResource(R.layout.spinner_dropdown);
        workSpinner.setAdapter(spinAdapter);


        Bundle bundle = getIntent().getExtras();
        final String projectData = bundle.getString("projectData");
        Gson gson = new Gson();
        Type type = new TypeToken<Project>() {
        }.getType();
        final Project currentProjectData = gson.fromJson(projectData, type);
        currentProject = currentProjectData;

        etProjectName.setText(currentProject.getTitle());
        etLocation.setText(currentProject.getGeolocation());
        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(UpdateActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                etDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();


            }
        });

        queue = Volley.newRequestQueue(UpdateActivity.this);
        createUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(UpdateActivity.this);
                builder.setTitle("Create Update");

                StringBuilder sb = new StringBuilder();
                sb.append("Are you sure you want send an update ?");
                builder.setMessage(sb.toString());

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        if (response != null) {
                                            try {
                                                JSONObject root = new JSONObject(response);
                                                JSONArray jsonWeather = root.getJSONArray("weather");
                                                JSONObject jsonObject = jsonWeather.getJSONObject(0);
                                                weatherId = jsonObject.getInt("id");
                                                boolean conflict = false;
                                                //Toast.makeText(UpdateActivity.this, "" + weatherId, Toast.LENGTH_SHORT).show();
                                                String notiTitle = currentProjectData.getTitle() + "Update";
                                                String notiBody = "Conflict detected in reason provided. We will provide further updates later.";
                                                if (!weatherSpinner.getSelectedItem().toString().equalsIgnoreCase(Utilites.getWeatherString(weatherId))) {
                                                    NotificationHelper.displayNotification(UpdateActivity.this, notiTitle, notiBody);
                                                    conflict = true;
                                                }
                                                pushUpdates(conflict);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }


                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                //textView.setText("That didn't work!");
                            }
                        });

// Add the request to the RequestQueue.
                        queue.add(stringRequest);
                    }
                }).setNegativeButton("no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.show();
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

    private void pushUpdates(boolean conflict) {
        //Pushing the Update
//        dbRef.child(DatabaseCheckActivity.account.getId()).child("updateCount").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                int count = dataSnapshot.getValue(Integer.class);
//                count++;
//                dbRef.child(DatabaseCheckActivity.account.getId()).child("updateCount").setValue(count);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

        String title = etProjectName.getText().toString().trim();
        String location = etLocation.getText().toString().trim();
        String desc = (etDescription.getText()+"").trim();
        String imageUrl = "none";
        String isStopped = workSpinner.getSelectedItem().toString();
        String weather = weatherSpinner.getSelectedItem().toString();
        String date = etDate.getText().toString().trim();
        final String updateKey = updateRef.push().getKey().toString();
        String apiWeather = Utilites.getWeatherString(weatherId);

        boolean flag = validate(desc, date);
        if (flag) {
            findViewById(R.id.progress_bar2).setVisibility(View.VISIBLE);
            findViewById(R.id.update_ll).setVisibility(View.GONE);
            if (!imagePath.equalsIgnoreCase("none")) {
                pushImagetoStorage(title, desc, imageUrl, isStopped, weather, date, apiWeather, location, updateKey, conflict);
            } else {
                Update update = new Update(title, desc, imageUrl, isStopped, weather, date, currentProject.getProjectId(), updateKey, apiWeather, location, false, conflict);
                updateRef.child(updateKey).setValue(update);
                projectRef.child(currentProject.getProjectId()).child("updateId").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        long id = dataSnapshot.getChildrenCount();
                        projectRef.child(currentProject.getProjectId()).child("updateId").child(id + "").setValue(updateKey);
                        finish();
//                        Intent intent = new Intent(UpdateActivity.this, ManageProject.class);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        }
    }

    private void pushImagetoStorage(final String title, final String desc, final String imageUrl, final String isStopped, final String weather, final String date, final String apiWeather, final String location, final String updateKey, final boolean conflict) {
        Uri file = Uri.fromFile(new File(imagePath));
        final StorageReference imagesRef = storageRef.child("images/" + file.getLastPathSegment());
        UploadTask uploadTask = imagesRef.putFile(file);
// Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
                imagesRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String imageUrl1 = uri.toString();
                        Update update = new Update(title, desc, imageUrl1, isStopped, weather, date, currentProject.getProjectId(), updateKey, apiWeather, location, false, conflict);
                        updateRef.child(updateKey).setValue(update);

                        projectRef.child(currentProject.getProjectId()).child("updateId").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                long id = dataSnapshot.getChildrenCount();
                                projectRef.child(currentProject.getProjectId()).child("updateId").child(id + "").setValue(updateKey);
                                finish();
//                                Intent intent = new Intent(UpdateActivity.this, ManageProject.class);
//                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                                startActivity(intent);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int reqCode, int resCode, Intent data) {
        super.onActivityResult(reqCode, resCode, data);
        if (resCode == RESULT_OK && reqCode == REQUEST_CODE) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            Bitmap b = watermarker(bitmap);
            ivImage.setVisibility(View.VISIBLE);
            ivImage.setImageBitmap(b);
        }
    }

    private Bitmap watermarker(Bitmap bitmap) {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yy");
        String date = dateFormat.format(Calendar.getInstance().getTime());
        RubberStampConfig config = new RubberStampConfig.RubberStampConfigBuilder()
                .base(bitmap)
                .rubberStamp(currentProject.getGeolocation().substring(0,9) + currentProject.getGeolocation().substring(14,24)+" "+ date)
                .rubberStampPosition(RubberStampPosition.CENTER)
               // .margin(-10, -30)
                .rotation(0)
                .textColor(Color.WHITE)
                .textBackgroundColor(Color.TRANSPARENT)
                //.textShadow(0.1f, 4, 4, Color.WHITE)
                //.textSize(90)
                .textSize(10)
                .build();
        RubberStamp rubberStamp = new RubberStamp(UpdateActivity.this);
        Bitmap b = rubberStamp.addStamp(config);
        String dirPath = Environment.getExternalStorageDirectory() + "/Progress Meter";
        File dirFile = new File(dirPath);
        if (!dirFile.exists())
            dirFile.mkdir();
        String path = Environment.getExternalStorageDirectory().toString() + "/" + "Progress Meter" + "/" + UUID.randomUUID().toString() + ".png";
        imagePath = path;
        File file = new File(path);
        try {
            FileOutputStream out = new FileOutputStream(file);
            b.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            Log.e("file error", e + "");
            e.printStackTrace();
        }
        return b;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "You did not give write external storage permission ", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private boolean validate(String desc, String date) {
        boolean flag = true;
        if (desc.equalsIgnoreCase("")) {
            flag = false;
            ilDesc.setError("Mandatory Field");
        }
        if (date.equalsIgnoreCase("")) {
            ilDate.setError("Mandatory");
            flag = false;
        }
        return flag;
    }

}

