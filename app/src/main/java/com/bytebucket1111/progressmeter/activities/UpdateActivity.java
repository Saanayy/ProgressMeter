package com.bytebucket1111.progressmeter.activities;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
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
import java.util.Calendar;
import java.util.UUID;

public class UpdateActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 1;
    private static final int REQUEST_PERMISSION = 200;

    Button createUpdate, takeImage;
    RequestQueue queue;
    private int mYear, mMonth, mDay;
    android.support.design.widget.TextInputEditText etProjectName, etLocation, etDate;
    String url = "http://api.openweathermap.org/data/2.5/weather?lat=90.00&lon=135.00&mode=json&appid=5f5f52012f486ec45ea9e55600fab0d2";
    TextView textView;
    int weatherId = 007;
    Project currentProject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        textView = findViewById(R.id.text);
        createUpdate = findViewById(R.id.update_addupdate);
        etDate = findViewById(R.id.update_date);
        etLocation = findViewById(R.id.update_location);
        etProjectName = findViewById(R.id.update_projectname);
        takeImage = findViewById(R.id.take_image);

        //External storage request
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_PERMISSION);
        }

        takeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });


        //Notification Checker
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel notificationChannel = new NotificationChannel("channelId","channelId", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        String title = "Lo Naya Notification";
        String body = "Bhagg be KamChor";



        final Spinner weatherSpinner = (Spinner) findViewById(R.id.update_spinnerweather);
        String[] weather = {"Clear Sky", "Snow", "Rain", "Drizzle", "Thunder Storm"};
        ArrayAdapter<CharSequence> spinAdapter = new ArrayAdapter<CharSequence>(UpdateActivity.this, R.layout.spinner_text, weather);
        spinAdapter.setDropDownViewResource(R.layout.spinner_dropdown);
        weatherSpinner.setAdapter(spinAdapter);

        Spinner workSpinner = (Spinner) findViewById(R.id.update_spinnerwork);
        String[] work = {"InProgress", "Slowed", "Stopped"};
        spinAdapter = new ArrayAdapter<CharSequence>(UpdateActivity.this, R.layout.spinner_text, work);
        spinAdapter.setDropDownViewResource(R.layout.spinner_dropdown);
        workSpinner.setAdapter(spinAdapter);


        Bundle bundle = getIntent().getExtras();
        String projectData = bundle.getString("projectData");
        Gson gson = new Gson();
        Type type = new TypeToken<Project>() {}.getType();
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
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                if(response!=null)
                                {
                                    try {
                                        JSONObject root = new JSONObject(response);
                                        JSONArray weather = root.getJSONArray("weather");
                                        JSONObject jsonObject = weather.getJSONObject(0);
                                        weatherId = jsonObject.getInt("id");

                                        Toast.makeText(UpdateActivity.this, ""+weatherId, Toast.LENGTH_SHORT).show();
                                        String title = "Lo Naya Notification";
                                        String body = "Bhagg be KamChor";
                                        if(weatherSpinner.getSelectedItem().toString().equalsIgnoreCase(Utilites.getWeatherString(weatherId)) == false)
                                            NotificationHelper.displayNotification(UpdateActivity.this,title,body);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }


                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        textView.setText("That didn't work!");
                    }
                });

// Add the request to the RequestQueue.
                queue.add(stringRequest);
            }
        });


    }

    @Override
    protected void onActivityResult(int reqCode, int resCode, Intent data)
    {
        super.onActivityResult(reqCode,resCode,data);
        if(resCode == RESULT_OK && reqCode ==REQUEST_CODE)
        {
            Bitmap bitmap =(Bitmap) data.getExtras().get("data");
            watermarker(bitmap);
        }
    }

    private void watermarker(Bitmap bitmap) {
        RubberStampConfig config = new RubberStampConfig.RubberStampConfigBuilder()
                .base(bitmap)
                .rubberStamp("Progress Meter")
                .rubberStampPosition(RubberStampPosition.BOTTOM_RIGHT)
                //.margin(-20, -60)
                .rotation(0)
                .textColor(Color.WHITE)
                .textBackgroundColor(Color.TRANSPARENT)
                .textShadow(0.1f, 4, 4, Color.WHITE)
                //.textSize(90)
                .textSize(12)
                .build();
        RubberStamp rubberStamp = new RubberStamp(UpdateActivity.this);
        Bitmap b = rubberStamp.addStamp(config);
        //ImageView imageView = findViewById(R.id.img_view);
        //imageView.setImageBitmap(b);

        String dirPath = Environment.getExternalStorageDirectory()+"/Progress Meter";
        File dirFile = new File(dirPath);
        if(!dirFile.exists())
            dirFile.mkdir();
        String path = Environment.getExternalStorageDirectory().toString()+"/"+"Progress Meter"+"/"+ UUID.randomUUID().toString()+".jpg";
        File file = new File(path);
        try {
            FileOutputStream out = new FileOutputStream(file);
            b.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            Log.e("file error",e+"");
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0] != PackageManager.PERMISSION_GRANTED)
        {
            Toast.makeText(this,"You did not give write external storage permission ",Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
