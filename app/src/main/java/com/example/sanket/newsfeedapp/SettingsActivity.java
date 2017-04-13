package com.example.sanket.newsfeedapp;

import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ViewPager viewPager2 = (ViewPager) findViewById(R.id.viewpager2);

        SimpleFragmentPagerAdapter2 adapter2 = new SimpleFragmentPagerAdapter2(SettingsActivity.this, getSupportFragmentManager());

        viewPager2.setAdapter(adapter2);

        TabLayout tabLayout2 = (TabLayout) findViewById(R.id.tabs2);
        tabLayout2.setupWithViewPager(viewPager2);

    }

}
