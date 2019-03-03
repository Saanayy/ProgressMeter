package com.bytebucket1111.progressmeter.activities;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.bytebucket1111.progressmeter.R;
import com.bytebucket1111.progressmeter.SlideAdapter;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import me.relex.circleindicator.CircleIndicator;

public class SlideImagesActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private SlideAdapter myadapter;
    static public GoogleSignInAccount account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_images);

        getSupportActionBar().hide();
        account = getIntent().getParcelableExtra("account data");

        viewPager = findViewById(R.id.viewpager);
        myadapter = new SlideAdapter(this);
        viewPager.setAdapter(myadapter);

        CircleIndicator indicator = findViewById(R.id.indicator);
        indicator.setViewPager(viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int i) {
                if(i==2)
                {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            doWork();
                            Intent intent = new Intent(SlideImagesActivity.this,DatabaseCheckActivity.class);
                            intent.putExtra("account data", account);
                            startActivity(intent);
                            finish();
                            overridePendingTransition(0,0);
                        }
                    }).start();

                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

// optional
        myadapter.registerDataSetObserver(indicator.getDataSetObserver());
    }
    private void doWork() {
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
