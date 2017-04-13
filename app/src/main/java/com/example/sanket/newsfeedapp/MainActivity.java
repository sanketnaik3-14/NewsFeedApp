package com.example.sanket.newsfeedapp;


import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;
import android.support.v4.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.http.HttpResponseCache;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.BundleCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.makeText;
import static com.example.sanket.newsfeedapp.R.id.tone;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {

    private NavigationView navigationView;
    private DrawerLayout drawer;
    private Toolbar toolbar;
    private View navHeader;
    private TextView txt;
    private SwitchCompat science;
    private SwitchCompat technology;
    private ListView listView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    LoaderManager loaderManager;
    private static final String NEWS_LINK = "http://content.guardianapis.com/search";
    private NewsAdapter adapter;
    private final Context mContext = this;
    private int x;
    private int load = 15;
    private TextView mEmptyStateTextView;
    public boolean refresh;
    public boolean loadMore = false;
    View footer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            File httpCacheDir = new File(mContext.getCacheDir(), "http");
            long httpCacheSize = 100 * 1024 * 1024;
            HttpResponseCache.install(httpCacheDir, httpCacheSize);
        }catch(IOException e)
        {
            Log.i("NewsFeedApp" , "Http response failed to create cache:" + e);
        }
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle(R.string.technology);

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        mEmptyStateTextView.setVisibility(View.GONE);

        listView = (ListView)findViewById(R.id.news_list);
        adapter = new NewsAdapter(this,new ArrayList<News>());
        listView.setAdapter(adapter);
        listView.setEmptyView(mEmptyStateTextView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                News news = (News)parent.getItemAtPosition(position);

                Bitmap bmp = news.getmBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();

                Intent intent = new Intent(MainActivity.this,DetailsActivity.class);
                intent.putExtra("head",news.getmHeadline());
                intent.putExtra("tone",news.getmTone());
                intent.putExtra("date",news.getmPublicationDate());
                intent.putExtra("contributor",news.getmContributor());
                intent.putExtra("image",byteArray);
                intent.putExtra("body",news.getmBody());
                startActivity(intent);
            }
        });

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {


                    if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount != 0) {
                            loadMore = true;
                            mSwipeRefreshLayout.setRefreshing(true);
                            loaderManager.initLoader(x, null, MainActivity.this);
                    }
                }
        });

        drawer = (DrawerLayout) findViewById(R.id.drawer);
        mSwipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipedown);
        navigationView = (NavigationView)findViewById(R.id.navigation_view);
        navHeader = navigationView.getHeaderView(0);
        txt = (TextView)navHeader.findViewById(R.id.title);

        loaderManager = getSupportLoaderManager();
        mSwipeRefreshLayout.setRefreshing(true);

        loaderManager.initLoader(101, null, MainActivity.this);
        txt.setText(getString(R.string.technology));
        x = 101;
        refresh=false;

        science = (SwitchCompat)navigationView.getMenu().getItem(1).getActionView().findViewById(R.id.science_switch);
        technology = (SwitchCompat)navigationView.getMenu().getItem(0).getActionView().findViewById(R.id.technology_switch);
        technology.setChecked(true);
        science.setChecked(false);

        navigationView.getMenu().setGroupVisible(R.id.temp1,false);
        navigationView.getMenu().setGroupVisible(R.id.temp2,true);
        navigationView.setCheckedItem(R.id.alltechnology);

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
                        txt.setText(getString(R.string.technology));

                        break;

                    case R.id.sci:
                        science.setChecked(true);
                        technology.setChecked(false);
                        navigationView.getMenu().setGroupVisible(R.id.temp1,true);
                        navigationView.getMenu().setGroupVisible(R.id.temp2,false);
                        txt.setText(getString(R.string.science));

                        break;

                    case R.id.settings:
                        Intent intent = new Intent(MainActivity.this,SettingsActivity.class);
                        startActivity(intent);

                        break;

                    case R.id.allScience:
                        listView.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setRefreshing(true);
                        loaderManager.initLoader(1,null,MainActivity.this);
                        x=1;
                        drawer.closeDrawers();
                        getSupportActionBar().setTitle(R.string.science);
                        break;

                    case R.id.aeronautics:
                        listView.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setRefreshing(true);
                        loaderManager.initLoader(2,null,MainActivity.this);
                        x=2;
                        drawer.closeDrawers();
                        getSupportActionBar().setTitle(R.string.aeronautics);
                        break;

                    case R.id.agriculture:
                        listView.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setRefreshing(true);
                        loaderManager.initLoader(3,null,MainActivity.this);
                        x=3;
                        drawer.closeDrawers();
                        getSupportActionBar().setTitle(R.string.agriculture);
                        break;

                    case R.id.anthropology:
                        listView.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setRefreshing(true);
                        loaderManager.initLoader(4,null,MainActivity.this);
                        x=4;
                        drawer.closeDrawers();
                        getSupportActionBar().setTitle(R.string.anthropology);
                        break;

                    case R.id.archaeology:
                        listView.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setRefreshing(true);
                        loaderManager.initLoader(5,null,MainActivity.this);
                        x=5;
                        drawer.closeDrawers();
                        getSupportActionBar().setTitle(R.string.archaeology);
                        break;

                    case R.id.astronomy:
                        listView.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setRefreshing(true);
                        loaderManager.initLoader(6,null,MainActivity.this);
                        x=6;
                        drawer.closeDrawers();
                        getSupportActionBar().setTitle(R.string.astronomy);
                        break;

                    case R.id.biochemistry:
                        listView.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setRefreshing(true);
                        loaderManager.initLoader(7,null,MainActivity.this);
                        x=7;
                        drawer.closeDrawers();
                        getSupportActionBar().setTitle(R.string.biochemistry);
                        break;

                    case R.id.biology:
                        listView.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setRefreshing(true);
                        loaderManager.initLoader(8,null,MainActivity.this);
                        x=8;
                        drawer.closeDrawers();
                        getSupportActionBar().setTitle(R.string.biology);
                        break;

                    case R.id.britishsciencefestival:
                        listView.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setRefreshing(true);
                        loaderManager.initLoader(9,null,MainActivity.this);
                        x=9;
                        drawer.closeDrawers();
                        getSupportActionBar().setTitle(R.string.britishsciencefestival);
                        break;

                    case R.id.chemistry:
                        listView.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setRefreshing(true);
                        loaderManager.initLoader(10,null,MainActivity.this);
                        x=10;
                        drawer.closeDrawers();
                        getSupportActionBar().setTitle(R.string.chemistry);
                        break;

                    case R.id.energy:
                        listView.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setRefreshing(true);
                        loaderManager.initLoader(11,null,MainActivity.this);
                        x=11;
                        drawer.closeDrawers();
                        getSupportActionBar().setTitle(R.string.energy);
                        break;

                    case R.id.foodscience:
                        listView.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setRefreshing(true);
                        loaderManager.initLoader(12,null,MainActivity.this);
                        x=12;
                        drawer.closeDrawers();
                        getSupportActionBar().setTitle(R.string.foodscience);
                        break;

                    case R.id.genetics:
                        listView.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setRefreshing(true);
                        loaderManager.initLoader(13,null,MainActivity.this);
                        x=13;
                        drawer.closeDrawers();
                        getSupportActionBar().setTitle(R.string.genetics);
                        break;

                    case R.id.geography:
                        listView.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setRefreshing(true);
                        loaderManager.initLoader(14,null,MainActivity.this);
                        x=14;
                        drawer.closeDrawers();
                        getSupportActionBar().setTitle(R.string.geography);
                        break;

                    case R.id.geology:
                        listView.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setRefreshing(true);
                        loaderManager.initLoader(15,null,MainActivity.this);
                        x=15;
                        drawer.closeDrawers();
                        getSupportActionBar().setTitle(R.string.geology);
                        break;

                    case R.id.materialsscience:
                        listView.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setRefreshing(true);
                        loaderManager.initLoader(16,null,MainActivity.this);
                        x=16;
                        drawer.closeDrawers();
                        getSupportActionBar().setTitle(R.string.materialsscience);
                        break;

                    case R.id.mathematics:
                        listView.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setRefreshing(true);
                        loaderManager.initLoader(17,null,MainActivity.this);
                        x=17;
                        drawer.closeDrawers();
                        getSupportActionBar().setTitle(R.string.mathematics);
                        break;

                    case R.id.medicalresearch:
                        listView.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setRefreshing(true);
                        loaderManager.initLoader(18,null,MainActivity.this);
                        x=18;
                        drawer.closeDrawers();
                        getSupportActionBar().setTitle(R.string.medicalresearch);
                        break;

                    case R.id.microbiology:
                        listView.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setRefreshing(true);
                        loaderManager.initLoader(19,null,MainActivity.this);
                        x=19;
                        drawer.closeDrawers();
                        getSupportActionBar().setTitle(R.string.microbiology);
                        break;

                    case R.id.nanotechnology:
                        listView.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setRefreshing(true);
                        loaderManager.initLoader(20,null,MainActivity.this);
                        x=20;
                        drawer.closeDrawers();
                        getSupportActionBar().setTitle(R.string.nanotechnology);
                        return true;

                    case R.id.nobelprizes:
                        listView.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setRefreshing(true);
                        loaderManager.initLoader(21,null,MainActivity.this);
                        x=21;
                        drawer.closeDrawers();
                        getSupportActionBar().setTitle(R.string.nobelprizes);
                        return true;

                    case R.id.nutrition:
                        listView.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setRefreshing(true);
                        loaderManager.initLoader(22,null,MainActivity.this);
                        x=22;
                        drawer.closeDrawers();
                        getSupportActionBar().setTitle(R.string.nutrition);
                        break;

                    case R.id.physics:
                        listView.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setRefreshing(true);
                        loaderManager.initLoader(23,null,MainActivity.this);
                        x=23;
                        drawer.closeDrawers();
                        getSupportActionBar().setTitle(R.string.physics);
                        break;

                    case R.id.psychology:
                        listView.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setRefreshing(true);
                        loaderManager.initLoader(24,null,MainActivity.this);
                        x=24;
                        drawer.closeDrawers();
                        getSupportActionBar().setTitle(R.string.psychology);
                        break;

                    case R.id.scienceblog:
                        listView.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setRefreshing(true);
                        loaderManager.initLoader(25,null,MainActivity.this);
                        x=25;
                        drawer.closeDrawers();
                        getSupportActionBar().setTitle(R.string.scienceblog);
                        break;

                    case R.id.space:
                        listView.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setRefreshing(true);
                        loaderManager.initLoader(26,null,MainActivity.this);
                        x=26;
                        drawer.closeDrawers();
                        getSupportActionBar().setTitle(R.string.space);
                        break;

                    case R.id.weaponstechnology:
                        listView.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setRefreshing(true);
                        loaderManager.initLoader(27,null,MainActivity.this);
                        x=27;
                        drawer.closeDrawers();
                        getSupportActionBar().setTitle(R.string.weaponstechnology);
                        break;

                    case R.id.zoology:
                        listView.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setRefreshing(true);
                        loaderManager.initLoader(28,null,MainActivity.this);
                        x=28;
                        drawer.closeDrawers();
                        getSupportActionBar().setTitle(R.string.zoology);
                        break;

                    case R.id.alltechnology:
                        listView.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setRefreshing(true);
                        loaderManager.initLoader(101,null,MainActivity.this);
                        x=101;
                        drawer.closeDrawers();
                        getSupportActionBar().setTitle(R.string.technology);
                        break;

                    case R.id.threedprinting:
                        listView.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setRefreshing(true);
                        loaderManager.initLoader(102,null,MainActivity.this);
                        x=102;
                        drawer.closeDrawers();
                        getSupportActionBar().setTitle(R.string.threedprinting);
                        break;

                    case R.id.alphabet:
                        listView.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setRefreshing(true);
                        loaderManager.initLoader(103,null,MainActivity.this);
                        x=103;
                        drawer.closeDrawers();
                        getSupportActionBar().setTitle(R.string.alphabet);
                        break;

                    case R.id.amazon:
                        listView.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setRefreshing(true);
                        loaderManager.initLoader(104,null,MainActivity.this);
                        x=104;
                        drawer.closeDrawers();
                        getSupportActionBar().setTitle(R.string.amazon);
                        break;

                    case R.id.android:
                        listView.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setRefreshing(true);
                        loaderManager.initLoader(105,null,MainActivity.this);
                        x=105;
                        drawer.closeDrawers();
                        getSupportActionBar().setTitle(R.string.android);
                        break;

                    case R.id.apple:
                        listView.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setRefreshing(true);
                        loaderManager.initLoader(106,null,MainActivity.this);
                        x=106;
                        drawer.closeDrawers();
                        getSupportActionBar().setTitle(R.string.apple);
                        break;

                    case R.id.apps:
                        listView.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setRefreshing(true);
                        loaderManager.initLoader(107,null,MainActivity.this);
                        x=107;
                        drawer.closeDrawers();
                        getSupportActionBar().setTitle(R.string.apps);
                        break;

                    case R.id.artificialintelligence:
                        listView.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setRefreshing(true);
                        loaderManager.initLoader(108,null,MainActivity.this);
                        x=108;
                        drawer.closeDrawers();
                        getSupportActionBar().setTitle(R.string.arfiticialintelligence);
                        break;

                    case R.id.augmentedreality:
                        listView.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setRefreshing(true);
                        loaderManager.initLoader(109,null,MainActivity.this);
                        x=109;
                        drawer.closeDrawers();
                        getSupportActionBar().setTitle(R.string.augmentedreality);
                        break;

                    case R.id.bigdata:
                        listView.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setRefreshing(true);
                        loaderManager.initLoader(110,null,MainActivity.this);
                        x=110;
                        drawer.closeDrawers();
                        getSupportActionBar().setTitle(R.string.bigdata);
                        break;

                    case R.id.billgates:
                        listView.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setRefreshing(true);
                        loaderManager.initLoader(111,null,MainActivity.this);
                        x=111;
                        drawer.closeDrawers();
                        getSupportActionBar().setTitle(R.string.billgates);
                        break;

                    case R.id.ces:
                        listView.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setRefreshing(true);
                        loaderManager.initLoader(112,null,MainActivity.this);
                        x=112;
                        drawer.closeDrawers();
                        getSupportActionBar().setTitle(R.string.ces);
                        break;

                    case R.id.computing:
                        listView.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setRefreshing(true);
                        loaderManager.initLoader(113,null,MainActivity.this);
                        x=113;
                        drawer.closeDrawers();
                        getSupportActionBar().setTitle(R.string.computing);
                        break;

                    case R.id.datasecurity:
                        listView.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setRefreshing(true);
                        loaderManager.initLoader(114,null,MainActivity.this);
                        x=114;
                        drawer.closeDrawers();
                        getSupportActionBar().setTitle(R.string.datasecurity);
                        break;

                    case R.id.drones:
                        listView.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setRefreshing(true);
                        loaderManager.initLoader(115,null,MainActivity.this);
                        x=115;
                        drawer.closeDrawers();
                        getSupportActionBar().setTitle(R.string.drones);
                        break;

                    case R.id.fintech:
                        listView.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setRefreshing(true);
                        loaderManager.initLoader(116,null,MainActivity.this);
                        x=116;
                        drawer.closeDrawers();
                        getSupportActionBar().setTitle(R.string.fintech);
                        break;

                    case R.id.elonmusk:
                        listView.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setRefreshing(true);
                        loaderManager.initLoader(117,null,MainActivity.this);
                        x=117;
                        drawer.closeDrawers();
                        getSupportActionBar().setTitle(R.string.elonmusk);
                        break;

                    case R.id.engineering:
                        listView.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setRefreshing(true);
                        loaderManager.initLoader(118,null,MainActivity.this);
                        x=118;
                        drawer.closeDrawers();
                        getSupportActionBar().setTitle(R.string.engineering);
                        break;

                    case R.id.facebook:
                        listView.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setRefreshing(true);
                        loaderManager.initLoader(119,null,MainActivity.this);
                        x=119;
                        drawer.closeDrawers();
                        getSupportActionBar().setTitle(R.string.facebook);
                        break;

                    case R.id.gadgets:
                        listView.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setRefreshing(true);
                        loaderManager.initLoader(120,null,MainActivity.this);
                        x=120;
                        drawer.closeDrawers();
                        getSupportActionBar().setTitle(R.string.gadgets);
                        break;

                    case R.id.google:
                        listView.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setRefreshing(true);
                        loaderManager.initLoader(121,null,MainActivity.this);
                        x=121;
                        drawer.closeDrawers();
                        getSupportActionBar().setTitle(R.string.google);
                        break;

                    case R.id.ibm:
                        listView.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setRefreshing(true);
                        loaderManager.initLoader(122,null,MainActivity.this);
                        x=122;
                        drawer.closeDrawers();
                        getSupportActionBar().setTitle(R.string.ibm);
                        break;

                    case R.id.intel:
                        listView.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setRefreshing(true);
                        loaderManager.initLoader(123,null,MainActivity.this);
                        x=123;
                        drawer.closeDrawers();
                        getSupportActionBar().setTitle(R.string.intel);
                        break;

                    case R.id.internet:
                        listView.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setRefreshing(true);
                        loaderManager.initLoader(124,null,MainActivity.this);
                        x=124;
                        drawer.closeDrawers();
                        getSupportActionBar().setTitle(R.string.internet);
                        break;

                    case R.id.iot:
                        listView.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setRefreshing(true);
                        loaderManager.initLoader(125,null,MainActivity.this);
                        x=125;
                        drawer.closeDrawers();
                        getSupportActionBar().setTitle(R.string.iot);
                        break;

                    case R.id.kickstarter:
                        listView.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setRefreshing(true);
                        loaderManager.initLoader(126,null,MainActivity.this);
                        x=126;
                        drawer.closeDrawers();
                        getSupportActionBar().setTitle(R.string.kickstarter);
                        break;

                    case R.id.laptops:
                        listView.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setRefreshing(true);
                        loaderManager.initLoader(127,null,MainActivity.this);
                        x=127;
                        drawer.closeDrawers();
                        getSupportActionBar().setTitle(R.string.laptops);
                        break;

                    case R.id.microsoft:
                        listView.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setRefreshing(true);
                        loaderManager.initLoader(128,null,MainActivity.this);
                        x=128;
                        drawer.closeDrawers();
                        getSupportActionBar().setTitle(R.string.microsoft);
                        break;

                    case R.id.mobilephones:
                        listView.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setRefreshing(true);
                        loaderManager.initLoader(129,null,MainActivity.this);
                        x=129;
                        drawer.closeDrawers();
                        getSupportActionBar().setTitle(R.string.mobilephones);
                        break;

                    case R.id.mwc:
                        listView.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setRefreshing(true);
                        loaderManager.initLoader(130,null,MainActivity.this);
                        x=130;
                        drawer.closeDrawers();
                        getSupportActionBar().setTitle(R.string.mwc);
                        break;

                    case R.id.motoring:
                        listView.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setRefreshing(true);
                        loaderManager.initLoader(131,null,MainActivity.this);
                        x=131;
                        drawer.closeDrawers();
                        getSupportActionBar().setTitle(R.string.motoring);
                        break;

                    case R.id.photography:
                        listView.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setRefreshing(true);
                        loaderManager.initLoader(132,null,MainActivity.this);
                        x=132;
                        drawer.closeDrawers();
                        getSupportActionBar().setTitle(R.string.photography);
                        break;

                    case R.id.programming:
                        listView.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setRefreshing(true);
                        loaderManager.initLoader(133,null,MainActivity.this);
                        x=133;
                        drawer.closeDrawers();
                        getSupportActionBar().setTitle(R.string.programming);
                        break;

                    case R.id.research:
                        listView.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setRefreshing(true);
                        loaderManager.initLoader(134,null,MainActivity.this);
                        x=134;
                        drawer.closeDrawers();
                        getSupportActionBar().setTitle(R.string.research);
                        break;

                    case R.id.robots:
                        listView.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setRefreshing(true);
                        loaderManager.initLoader(135,null,MainActivity.this);
                        x=135;
                        drawer.closeDrawers();
                        getSupportActionBar().setTitle(R.string.robots);
                        break;

                    case R.id.samsung:
                        listView.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setRefreshing(true);
                        loaderManager.initLoader(136,null,MainActivity.this);
                        x=136;
                        drawer.closeDrawers();
                        getSupportActionBar().setTitle(R.string.samsung);
                        break;

                    case R.id.siliconvalley:
                        listView.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setRefreshing(true);
                        loaderManager.initLoader(137,null,MainActivity.this);
                        x=137;
                        drawer.closeDrawers();
                        getSupportActionBar().setTitle(R.string.siliconvalley);
                        break;

                    case R.id.software:
                        listView.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setRefreshing(true);
                        loaderManager.initLoader(138,null,MainActivity.this);
                        x=138;
                        drawer.closeDrawers();
                        getSupportActionBar().setTitle(R.string.software);
                        break;

                    case R.id.startups:
                        listView.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setRefreshing(true);
                        loaderManager.initLoader(139,null,MainActivity.this);
                        x=139;
                        drawer.closeDrawers();
                        getSupportActionBar().setTitle(R.string.startups);
                        break;

                    case R.id.jeffbezos:
                        listView.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setRefreshing(true);
                        loaderManager.initLoader(140,null,MainActivity.this);
                        x=140;
                        drawer.closeDrawers();
                        getSupportActionBar().setTitle(R.string.jeffbezos);
                        break;


                    default:
                        makeText(getApplicationContext(), "Somethings Wrong", Toast.LENGTH_SHORT).show();

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
                  txt.setText(getString(R.string.science));
              }
              else
              {
                  science.setChecked(false);
                  navigationView.getMenu().setGroupVisible(R.id.temp1,false);
                  navigationView.getMenu().setGroupVisible(R.id.temp2,true);
                  txt.setText(getString(R.string.technology));
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
                    txt.setText(getString(R.string.technology));
                }
                else
                {
                    technology.setChecked(false);
                    navigationView.getMenu().setGroupVisible(R.id.temp1,true);
                    navigationView.getMenu().setGroupVisible(R.id.temp2,false);
                    txt.setText(getString(R.string.science));
                }

            }
        });

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

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                mEmptyStateTextView.setVisibility(View.GONE);
                refresh = true;
                loaderManager.initLoader(x,null,MainActivity.this);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);

        SearchManager searchManager =
                (SearchManager)getSystemService(Context.SEARCH_SERVICE);

        SearchView searchView = (SearchView)menu.findItem(R.id.search).getActionView();

        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                Intent intent = new Intent(MainActivity.this,SearchActivity.class);
                String a = query;
                intent.putExtra("search",a);
                startActivity(intent);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    // method to flush cache contents to the filesystem
    public void flushCache() {
        HttpResponseCache cache = HttpResponseCache.getInstalled();
        if (cache != null) {
            cache.flush();
        }
    }

    public boolean connect()
    {
        ConnectivityManager coMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = coMgr.getActiveNetworkInfo();

        if (info != null && info.isConnected()) {
            return true;
        }
        else
        {
            return false;
        }
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        flushCache();
    }


    @Override
    public Loader<List<News>> onCreateLoader(int id, Bundle args) {

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String orderBy = sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default));


        String sortBy = sharedPrefs.getString(
                getString(R.string.settings_sort_by_key),
                getString(R.string.settings_sort_by_default));

        Uri baseUri = Uri.parse(NEWS_LINK);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        if(loadMore)
        {
            load = load + 10;
        }
        else
        {
            load = 15;
        }

        if(!sortBy.equalsIgnoreCase("all"))
        {
            uriBuilder.appendQueryParameter("tag","tone/"+sortBy);
        }

        uriBuilder.appendQueryParameter("show-tags","tone,contributor");
        uriBuilder.appendQueryParameter("show-fields","headline,body,trailText,publication,thumbnail");
        uriBuilder.appendQueryParameter("order-by",orderBy);
        uriBuilder.appendQueryParameter("page-size",String.valueOf(load));
        uriBuilder.appendQueryParameter("api-key","test");

        switch(id)
        {
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

            case 101:
                uriBuilder.appendQueryParameter("section","technology");
                break;

            case 102:
                uriBuilder.appendQueryParameter("section","technology");
                uriBuilder.appendQueryParameter("tag","technology/3d-printing");
                break;

            case 103:
                uriBuilder.appendQueryParameter("section","technology");
                uriBuilder.appendQueryParameter("tag","technology/alphabet");
                break;

            case 104:
                uriBuilder.appendQueryParameter("section","technology");
                uriBuilder.appendQueryParameter("tag","technology/amazon");
                break;

            case 105:
                uriBuilder.appendQueryParameter("section","technology");
                uriBuilder.appendQueryParameter("tag","technology/android");
                break;

            case 106:
                uriBuilder.appendQueryParameter("section","technology");
                uriBuilder.appendQueryParameter("tag","technology/apple");
                break;

            case 107:
                uriBuilder.appendQueryParameter("section","technology");
                uriBuilder.appendQueryParameter("tag","technology/apps");
                break;

            case 108:
                uriBuilder.appendQueryParameter("section","technology");
                uriBuilder.appendQueryParameter("tag","technology/artificialintelligenceai");
                break;

            case 109:
                uriBuilder.appendQueryParameter("section","technology");
                uriBuilder.appendQueryParameter("tag","technology/augmented-reality");
                break;

            case 110:
                uriBuilder.appendQueryParameter("section","technology");
                uriBuilder.appendQueryParameter("tag","technology/big-data");
                break;

            case 111:
                uriBuilder.appendQueryParameter("section","technology");
                uriBuilder.appendQueryParameter("tag","technology/billgates");
                break;

            case 112:
                uriBuilder.appendQueryParameter("section","technology");
                uriBuilder.appendQueryParameter("tag","technology/ces");
                break;

            case 113:
                uriBuilder.appendQueryParameter("section","technology");
                uriBuilder.appendQueryParameter("tag","technology/computing");
                break;

            case 114:
                uriBuilder.appendQueryParameter("section","technology");
                uriBuilder.appendQueryParameter("tag","technology/data-computer-security");
                break;

            case 115:
                uriBuilder.appendQueryParameter("section","technology");
                uriBuilder.appendQueryParameter("tag","technology/drones-non-military");
                break;

            case 116:
                uriBuilder.appendQueryParameter("section","technology");
                uriBuilder.appendQueryParameter("tag","technology/efinance");
                break;

            case 117:
                uriBuilder.appendQueryParameter("section","technology");
                uriBuilder.appendQueryParameter("tag","technology/elon-musk");
                break;

            case 118:
                uriBuilder.appendQueryParameter("section","technology");
                uriBuilder.appendQueryParameter("tag","technology/engineering");
                break;

            case 119:
                uriBuilder.appendQueryParameter("section","technology");
                uriBuilder.appendQueryParameter("tag","technology/facebook");
                break;

            case 120:
                uriBuilder.appendQueryParameter("section","technology");
                uriBuilder.appendQueryParameter("tag","technology/gadgets");
                break;

            case 121:
                uriBuilder.appendQueryParameter("section","technology");
                uriBuilder.appendQueryParameter("tag","technology/google");
                break;

            case 122:
                uriBuilder.appendQueryParameter("section","technology");
                uriBuilder.appendQueryParameter("tag","technology/ibm");
                break;

            case 123:
                uriBuilder.appendQueryParameter("section","technology");
                uriBuilder.appendQueryParameter("tag","technology/intel");
                break;

            case 124:
                uriBuilder.appendQueryParameter("section","technology");
                uriBuilder.appendQueryParameter("tag","technology/internet");
                break;

            case 125:
                uriBuilder.appendQueryParameter("section","technology");
                uriBuilder.appendQueryParameter("tag","technology/internet-of-things");
                break;

            case 140:
                uriBuilder.appendQueryParameter("section","technology");
                uriBuilder.appendQueryParameter("tag","technology/jeff-bezos");
                break;

            case 126:
                uriBuilder.appendQueryParameter("section","technology");
                uriBuilder.appendQueryParameter("tag","technology/kickstarter");
                break;

            case 127:
                uriBuilder.appendQueryParameter("section","technology");
                uriBuilder.appendQueryParameter("tag","technology/laptops");
                break;

            case 128:
                uriBuilder.appendQueryParameter("section","technology");
                uriBuilder.appendQueryParameter("tag","technology/microsoft");
                break;

            case 129:
                uriBuilder.appendQueryParameter("section","technology");
                uriBuilder.appendQueryParameter("tag","technology/mobilephones");
                break;

            case 130:
                uriBuilder.appendQueryParameter("section","technology");
                uriBuilder.appendQueryParameter("tag","technology/mobile-world-congress");
                break;

            case 131:
                uriBuilder.appendQueryParameter("section","technology");
                uriBuilder.appendQueryParameter("tag","technology/motoring");
                break;

            case 132:
                uriBuilder.appendQueryParameter("section","technology");
                uriBuilder.appendQueryParameter("tag","technology/photography");
                break;

            case 133:
                uriBuilder.appendQueryParameter("section","technology");
                uriBuilder.appendQueryParameter("tag","technology/programming");
                break;

            case 134:
                uriBuilder.appendQueryParameter("section","technology");
                uriBuilder.appendQueryParameter("tag","technology/research");
                break;

            case 135:
                uriBuilder.appendQueryParameter("section","technology");
                uriBuilder.appendQueryParameter("tag","technology/robots");
                break;

            case 136:
                uriBuilder.appendQueryParameter("section","technology");
                uriBuilder.appendQueryParameter("tag","technology/samsung");
                break;

            case 137:
                uriBuilder.appendQueryParameter("section","technology");
                uriBuilder.appendQueryParameter("tag","technology/silicon-valley");
                break;

            case 138:
                uriBuilder.appendQueryParameter("section","technology");
                uriBuilder.appendQueryParameter("tag","technology/software");
                break;

            case 139:
                uriBuilder.appendQueryParameter("section","technology");
                uriBuilder.appendQueryParameter("tag","technology/startups");
                break;


            default:
                makeText(getApplicationContext(),"No data received",Toast.LENGTH_SHORT).show();
        }

        boolean a = connect();
        return new NewsLoader(MainActivity.this, uriBuilder.toString(),refresh,a);
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> data) {

            if (data != null && !data.isEmpty()) {

                mSwipeRefreshLayout.setRefreshing(false);
                adapter.clear();
                adapter.addAll(data);
                adapter.notifyDataSetChanged();
                listView.setVisibility(View.VISIBLE);
                mEmptyStateTextView.setVisibility(View.GONE);
                loaderManager.destroyLoader(loader.getId());
                refresh = false;

                if (loadMore)
                {
                    listView.setSelection(load - 11);
                    loadMore = false;
                } else
                {
                    listView.setSelection(0);
                }

            }
        else
        {
            if(connect())
            {
                mSwipeRefreshLayout.setRefreshing(false);
                loaderManager.destroyLoader(loader.getId());
                refresh = false;
                Toast.makeText(MainActivity.this,getString(R.string.nomoredata) ,Toast.LENGTH_SHORT).show();
            }
            else
            {
                final Toast toast = Toast.makeText(MainActivity.this,getString(R.string.nomoreinternet),Toast.LENGTH_SHORT);
                toast.show();

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        toast.cancel();
                    }
                }, 1000);

                mSwipeRefreshLayout.setRefreshing(false);
                loaderManager.destroyLoader(loader.getId());
                refresh = false;
            }

        }

    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader)
    {
        adapter.clear();
    }


}
