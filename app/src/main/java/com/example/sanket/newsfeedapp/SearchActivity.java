package com.example.sanket.newsfeedapp;

import android.app.LoaderManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.http.HttpResponseCache;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.app.SearchManager.QUERY;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

public class SearchActivity extends AppCompatActivity {

    public String search="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Intent i = getIntent();
        if(i.getExtras() != null) {
            search = i.getExtras().getString("search");
        }
        else
        {
            search = "";
        }

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);

        SimpleFragmentPagerAdapter adapter = new SimpleFragmentPagerAdapter(SearchActivity.this, getSupportFragmentManager());

        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu_two, menu);

        SearchManager searchManager =
                (SearchManager)getSystemService(Context.SEARCH_SERVICE);

        SearchView searchView = (SearchView) menu.findItem(R.id.searchtwo).getActionView();

        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {


                TechnologyFragment tt = new TechnologyFragment();
                ScienceFragment ss = new ScienceFragment();
                search = query;

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container1, tt)
                        .addToBackStack(null)
                        .commit();

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container2, ss)
                        .addToBackStack(null)
                        .commit();

                try {
                    TechnologyFragment tf = (TechnologyFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_container1);

                    ScienceFragment sf = (ScienceFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_container2);
                }
                catch(NullPointerException npe)
                {
                    npe.printStackTrace();
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }



    public String getMyString()
    {
        return search;
    }

}
