package com.bytebucket1111.progressmeter.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.bytebucket1111.progressmeter.R;

public class AboutUs extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return true;
    }
}
