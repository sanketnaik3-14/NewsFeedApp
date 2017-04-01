package com.example.sanket.newsfeedapp;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.data;
import static android.R.attr.x;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {

    private NavigationView navigationView;
    private DrawerLayout drawer;
    private Toolbar toolbar;
    private TextView text;
    private View navHeader;
    private TextView txt;
    private SwitchCompat science;
    private SwitchCompat technology;
    private ListView listView;
    LoaderManager loaderManager;
    private static final String NEWS_LINK = "http://content.guardianapis.com/search";
    private NewsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer);

        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        loaderManager = getLoaderManager();
        loaderManager.initLoader(0,null,MainActivity.this);

        science = (SwitchCompat)navigationView.getMenu().getItem(1).getActionView().findViewById(R.id.science_switch);
        technology = (SwitchCompat)navigationView.getMenu().getItem(0).getActionView().findViewById(R.id.technology_switch);
        technology.setChecked(true);
        science.setChecked(false);

        navigationView.getMenu().setGroupVisible(R.id.temp1,false);
        navigationView.getMenu().setGroupVisible(R.id.temp2,true);
        navigationView.setCheckedItem(R.id.technologyall);

        getSupportActionBar().setTitle(R.string.technology);

        listView = (ListView)findViewById(R.id.news_list);
        adapter = new NewsAdapter(this,new ArrayList<News>());
        listView.setAdapter(adapter);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if(item.isChecked())
                {
                    item.setChecked(false);
                }
                else
                {
                    item.setChecked(true);
                }

                switch(item.getItemId()) {
                    case R.id.tech:
                        technology.setChecked(true);
                        science.setChecked(false);
                        navigationView.getMenu().setGroupVisible(R.id.temp1,false);
                        navigationView.getMenu().setGroupVisible(R.id.temp2,true);
                        txt.setText("Technology");

                        break;


                    case R.id.sci:
                        science.setChecked(true);
                        technology.setChecked(false);
                        navigationView.getMenu().setGroupVisible(R.id.temp1,true);
                        navigationView.getMenu().setGroupVisible(R.id.temp2,false);
                        txt.setText("Science");

                        break;

                    case R.id.allScience:
                        listView.setVisibility(View.GONE);
                        loaderManager.initLoader(1,null,MainActivity.this);
                        drawer.closeDrawers();
                        getSupportActionBar().setTitle(R.string.science);
                        break;

                    case R.id.aeronautics:
                        listView.setVisibility(View.GONE);
                        loaderManager.initLoader(2,null,MainActivity.this);
                        drawer.closeDrawers();
                        getSupportActionBar().setTitle(R.string.aeronautics);
                        break;

                    case R.id.agriculture:
                        listView.setVisibility(View.GONE);
                        loaderManager.initLoader(3,null,MainActivity.this);
                        drawer.closeDrawers();
                        getSupportActionBar().setTitle(R.string.agriculture);
                        break;

                    case R.id.anthropology:
                        listView.setVisibility(View.GONE);
                        loaderManager.initLoader(4,null,MainActivity.this);
                        drawer.closeDrawers();
                        getSupportActionBar().setTitle(R.string.anthropology);
                        break;

                    case R.id.archaeology:
                        listView.setVisibility(View.GONE);
                        loaderManager.initLoader(5,null,MainActivity.this);
                        drawer.closeDrawers();
                        getSupportActionBar().setTitle(R.string.archaeology);
                        break;

                    case R.id.astronomy:
                        listView.setVisibility(View.GONE);
                        loaderManager.initLoader(6,null,MainActivity.this);
                        drawer.closeDrawers();
                        getSupportActionBar().setTitle(R.string.astronomy);
                        break;

                    case R.id.biochemistry:
                        listView.setVisibility(View.GONE);
                        loaderManager.initLoader(7,null,MainActivity.this);
                        drawer.closeDrawers();
                        getSupportActionBar().setTitle(R.string.biochemistry);
                        break;

                    case R.id.biology:
                        listView.setVisibility(View.GONE);
                        loaderManager.initLoader(8,null,MainActivity.this);
                        drawer.closeDrawers();
                        getSupportActionBar().setTitle(R.string.biology);
                        break;

                    case R.id.britishsciencefestival:
                        listView.setVisibility(View.GONE);
                        loaderManager.initLoader(9,null,MainActivity.this);
                        drawer.closeDrawers();
                        getSupportActionBar().setTitle(R.string.britishsciencefestival);
                        break;

                    case R.id.chemistry:
                        listView.setVisibility(View.GONE);
                        loaderManager.initLoader(10,null,MainActivity.this);
                        drawer.closeDrawers();
                        getSupportActionBar().setTitle(R.string.chemistry);
                        break;

                    case R.id.energy:
                        listView.setVisibility(View.GONE);
                        loaderManager.initLoader(11,null,MainActivity.this);
                        drawer.closeDrawers();
                        getSupportActionBar().setTitle(R.string.energy);
                        break;

                    case R.id.foodscience:
                        listView.setVisibility(View.GONE);
                        loaderManager.initLoader(12,null,MainActivity.this);
                        drawer.closeDrawers();
                        getSupportActionBar().setTitle(R.string.foodscience);
                        break;

                    case R.id.genetics:
                        listView.setVisibility(View.GONE);
                        loaderManager.initLoader(13,null,MainActivity.this);
                        drawer.closeDrawers();
                        getSupportActionBar().setTitle(R.string.genetics);
                        break;

                    case R.id.geography:
                        listView.setVisibility(View.GONE);
                        loaderManager.initLoader(14,null,MainActivity.this);
                        drawer.closeDrawers();
                        getSupportActionBar().setTitle(R.string.geography);
                        break;

                    case R.id.geology:
                        listView.setVisibility(View.GONE);
                        loaderManager.initLoader(15,null,MainActivity.this);
                        drawer.closeDrawers();
                        getSupportActionBar().setTitle(R.string.geology);
                        break;

                    case R.id.materialsscience:
                        listView.setVisibility(View.GONE);
                        loaderManager.initLoader(16,null,MainActivity.this);
                        drawer.closeDrawers();
                        getSupportActionBar().setTitle(R.string.materialsscience);
                        break;

                    case R.id.mathematics:
                        listView.setVisibility(View.GONE);
                        loaderManager.initLoader(17,null,MainActivity.this);
                        drawer.closeDrawers();
                        getSupportActionBar().setTitle(R.string.mathematics);
                        break;

                    case R.id.medicalresearch:
                        listView.setVisibility(View.GONE);
                        loaderManager.initLoader(18,null,MainActivity.this);
                        drawer.closeDrawers();
                        getSupportActionBar().setTitle(R.string.medicalresearch);
                        break;

                    case R.id.microbiology:
                        listView.setVisibility(View.GONE);
                        loaderManager.initLoader(19,null,MainActivity.this);
                        drawer.closeDrawers();
                        getSupportActionBar().setTitle(R.string.microbiology);
                        break;

                    case R.id.nanotechnology:
                        listView.setVisibility(View.GONE);
                        loaderManager.initLoader(20,null,MainActivity.this);
                        drawer.closeDrawers();
                        getSupportActionBar().setTitle(R.string.nanotechnology);
                        return true;

                    case R.id.nobelprizes:
                        listView.setVisibility(View.GONE);
                        loaderManager.initLoader(21,null,MainActivity.this);
                        drawer.closeDrawers();
                        getSupportActionBar().setTitle(R.string.nobelprizes);
                        return true;

                    case R.id.nutrition:
                        listView.setVisibility(View.GONE);
                        loaderManager.initLoader(22,null,MainActivity.this);
                        drawer.closeDrawers();
                        getSupportActionBar().setTitle(R.string.nutrition);
                        break;

                    case R.id.physics:
                        listView.setVisibility(View.GONE);
                        loaderManager.initLoader(23,null,MainActivity.this);
                        drawer.closeDrawers();
                        getSupportActionBar().setTitle(R.string.physics);
                        break;

                    case R.id.psychology:
                        listView.setVisibility(View.GONE);
                        loaderManager.initLoader(24,null,MainActivity.this);
                        drawer.closeDrawers();
                        getSupportActionBar().setTitle(R.string.psychology);
                        break;

                    case R.id.scienceblog:
                        listView.setVisibility(View.GONE);
                        loaderManager.initLoader(25,null,MainActivity.this);
                        drawer.closeDrawers();
                        getSupportActionBar().setTitle(R.string.scienceblog);
                        break;

                    case R.id.space:
                        listView.setVisibility(View.GONE);
                        loaderManager.initLoader(26,null,MainActivity.this);
                        drawer.closeDrawers();
                        getSupportActionBar().setTitle(R.string.space);
                        break;

                    case R.id.weaponstechnology:
                        listView.setVisibility(View.GONE);
                        loaderManager.initLoader(27,null,MainActivity.this);
                        drawer.closeDrawers();
                        getSupportActionBar().setTitle(R.string.weaponstechnology);
                        break;

                    case R.id.zoology:
                        listView.setVisibility(View.GONE);
                        loaderManager.initLoader(28,null,MainActivity.this);
                        drawer.closeDrawers();
                        getSupportActionBar().setTitle(R.string.zoology);
                        break;

                    default:
                        Toast.makeText(getApplicationContext(), "Somethings Wrong", Toast.LENGTH_SHORT).show();

                }
                return true;
            }
        });

        technology.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
              if(!isChecked)
              {
                  science.setChecked(true);
                  navigationView.getMenu().setGroupVisible(R.id.temp1,true);
                  navigationView.getMenu().setGroupVisible(R.id.temp2,false);
              }
              else
              {
                  science.setChecked(false);
                  navigationView.getMenu().setGroupVisible(R.id.temp1,false);
                  navigationView.getMenu().setGroupVisible(R.id.temp2,true);
              }

            }
        });

        science.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked)
                {
                    technology.setChecked(true);
                    navigationView.getMenu().setGroupVisible(R.id.temp1,false);
                    navigationView.getMenu().setGroupVisible(R.id.temp2,true);
                }
                else
                {
                    technology.setChecked(false);
                    navigationView.getMenu().setGroupVisible(R.id.temp1,true);
                    navigationView.getMenu().setGroupVisible(R.id.temp2,false);
                }

            }
        });

        navHeader = navigationView.getHeaderView(0);
        txt = (TextView)navHeader.findViewById(R.id.title);
        txt.setText("Science");

        drawer = (DrawerLayout)findViewById(R.id.drawer);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawer,toolbar,
                R.string.openDrawer, R.string.closeDrawer) {
            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank

                super.onDrawerOpened(drawerView);

            }

        };

        drawer.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        actionBarDrawerToggle.setDrawerSlideAnimationEnabled(true);
    }

    @Override
    protected void onStop()
    {
        super.onStop();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
    }

    @Override
    public Loader<List<News>> onCreateLoader(int id, Bundle args) {

        Uri baseUri = Uri.parse(NEWS_LINK);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("show-tags","tone,contributor");
        uriBuilder.appendQueryParameter("show-fields","headline,body,trailText,publication,thumbnail");
        uriBuilder.appendQueryParameter("order-by","newest");
        uriBuilder.appendQueryParameter("page-size","10");
        uriBuilder.appendQueryParameter("api-key","test");
        switch(id)
        {
            case 0:
                uriBuilder.appendQueryParameter("section","technology");
                break;
            case 1:
                uriBuilder.appendQueryParameter("section","science");
                break;
            case 2:
                uriBuilder.appendQueryParameter("section","science");
                uriBuilder.appendQueryParameter("tag","science/aeronautics");
                break;
            case 3:
                uriBuilder.appendQueryParameter("section","science");
                uriBuilder.appendQueryParameter("tag","science/agriculture");
                break;
            case 4:
                uriBuilder.appendQueryParameter("section","science");
                uriBuilder.appendQueryParameter("tag","science/anthropology");
                break;
            case 5:
                uriBuilder.appendQueryParameter("section","science");
                uriBuilder.appendQueryParameter("tag","science/archaeology");
                break;
            case 6:
                uriBuilder.appendQueryParameter("section","science");
                uriBuilder.appendQueryParameter("tag","science/astronomy");
                break;
            case 7:
                uriBuilder.appendQueryParameter("section","science");
                uriBuilder.appendQueryParameter("tag","science/biochemistrymolecularbiology");
                break;
            case 8:
                uriBuilder.appendQueryParameter("section","science");
                uriBuilder.appendQueryParameter("tag","science/biology");
                break;
            case 9:
                uriBuilder.appendQueryParameter("section","science");
                uriBuilder.appendQueryParameter("tag","science/british-science-festival");
                break;
            case 10:
                uriBuilder.appendQueryParameter("section","science");
                uriBuilder.appendQueryParameter("tag","science/chemistry");
                break;
            case 11:
                uriBuilder.appendQueryParameter("section","science");
                uriBuilder.appendQueryParameter("tag","science/energy");
                break;
            case 12:
                uriBuilder.appendQueryParameter("section","science");
                uriBuilder.appendQueryParameter("tag","science/food-science");
                break;
            case 13:
                uriBuilder.appendQueryParameter("section","science");
                uriBuilder.appendQueryParameter("tag","science/genetics");
                break;
            case 14:
                uriBuilder.appendQueryParameter("section","science");
                uriBuilder.appendQueryParameter("tag","science/geography");
                break;
            case 15:
                uriBuilder.appendQueryParameter("section","science");
                uriBuilder.appendQueryParameter("tag","science/geology");
                break;
            case 16:
                uriBuilder.appendQueryParameter("section","science");
                uriBuilder.appendQueryParameter("tag","science/materials-science");
                break;
            case 17:
                uriBuilder.appendQueryParameter("section","science");
                uriBuilder.appendQueryParameter("tag","science/mathematics");
                break;
            case 18:
                uriBuilder.appendQueryParameter("section","science");
                uriBuilder.appendQueryParameter("tag","science/medical-research");
                break;
            case 19:
                uriBuilder.appendQueryParameter("section","science");
                uriBuilder.appendQueryParameter("tag","science/microbiology");
                break;
            case 20:
                uriBuilder.appendQueryParameter("section","science");
                uriBuilder.appendQueryParameter("tag","science/nanotechnology");
                break;
            case 21:
                uriBuilder.appendQueryParameter("section","science");
                uriBuilder.appendQueryParameter("tag","science/nobelprizes");
                break;
            case 22:
                uriBuilder.appendQueryParameter("section","science");
                uriBuilder.appendQueryParameter("tag","science/nutrition");
                break;
            case 23:
                uriBuilder.appendQueryParameter("section","science");
                uriBuilder.appendQueryParameter("tag","science/physics");
                break;
            case 24:
                uriBuilder.appendQueryParameter("section","science");
                uriBuilder.appendQueryParameter("tag","science/psychology");
                break;
            case 25:
                uriBuilder.appendQueryParameter("section","science");
                uriBuilder.appendQueryParameter("tag","science/blog");
                break;
            case 26:
                uriBuilder.appendQueryParameter("section","science");
                uriBuilder.appendQueryParameter("tag","science/space");
                break;
            case 27:
                uriBuilder.appendQueryParameter("section","science");
                uriBuilder.appendQueryParameter("tag","science/weaponstechnology");
                break;
            case 28:
                uriBuilder.appendQueryParameter("section","science");
                uriBuilder.appendQueryParameter("tag","science/zoology");
                break;

            default:
                Toast.makeText(getApplicationContext(),"No data received",Toast.LENGTH_SHORT).show();
        }


        return new NewsLoader(MainActivity.this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> data) {

        adapter.clear();
        if(data != null && !data.isEmpty()) {
            adapter.addAll(data);
        }
        listView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {

        adapter.clear();
    }
}
