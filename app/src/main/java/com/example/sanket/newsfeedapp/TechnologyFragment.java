package com.example.sanket.newsfeedapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import static android.R.attr.x;
import static android.os.Build.VERSION_CODES.N;
import static android.widget.Toast.makeText;

/**
 * A simple {@link Fragment} subclass.
 */
public class TechnologyFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<News>>{
    private String search;
    private ListView listView1;
    LoaderManager loaderManager1;
    private SwipeRefreshLayout mSwipeRefreshLayout1;
    private static final String NEWS_LINK = "http://content.guardianapis.com/search";
    private NewsAdapter adapter1;
    private int load1 = 15;
    private TextView mEmptyStateTextView1;
    public boolean refresh1;
    public boolean loadMore1 = false;
    private LoaderManager.LoaderCallbacks<List<News>> mCallbacks;

    public TechnologyFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_technology,container,false);

        SearchActivity activity = (SearchActivity)getActivity();
        search = activity.getMyString();

        mEmptyStateTextView1 = (TextView)rootView.findViewById(R.id.empty_view1);
        mEmptyStateTextView1.setVisibility(View.GONE);

        mSwipeRefreshLayout1 = (SwipeRefreshLayout)rootView.findViewById(R.id.swipedown1);
        mSwipeRefreshLayout1.setRefreshing(true);
        loaderManager1 = getActivity().getSupportLoaderManager();

        loaderManager1.initLoader(0,null,this);
        refresh1 = false;

        listView1 = (ListView)rootView.findViewById(R.id.list1);
        adapter1 = new NewsAdapter(getActivity(),new ArrayList<News>());
        adapter1.clear();
        adapter1.notifyDataSetChanged();
        listView1.setAdapter(null);
        listView1.setAdapter(adapter1);
        listView1.setEmptyView(mEmptyStateTextView1);

        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                News news = (News)parent.getItemAtPosition(position);

                Bitmap bmp = news.getmBitmap();
                byte[] byteArray;
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                if(bmp!=null) {
                    bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byteArray = stream.toByteArray();
                }
                else
                {
                    byteArray = null;
                }


                Intent intent1 = new Intent(getActivity(),DetailsActivity.class);
                intent1.putExtra("head",news.getmHeadline());
                intent1.putExtra("tone",news.getmTone());
                intent1.putExtra("date",news.getmPublicationDate());
                intent1.putExtra("contributor",news.getmContributor());
                intent1.putExtra("image",byteArray);
                intent1.putExtra("body",news.getmBody());
                startActivity(intent1);
            }
        });

        listView1.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount != 0) {
                    loadMore1 = true;
                    mSwipeRefreshLayout1.setRefreshing(true);
                    createFragment();
                }
            }
        });

        mSwipeRefreshLayout1.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                mSwipeRefreshLayout1.setRefreshing(true);
                mEmptyStateTextView1.setVisibility(View.GONE);
                refresh1 = true;
                createFragment();
            }
        });

        return rootView;
    }

    public void createFragment()
    {
        loaderManager1.initLoader(0,null,this);
    }

    public boolean connect()
    {
        ConnectivityManager coMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
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
    public Loader<List<News>> onCreateLoader(int id, Bundle args) {

        SharedPreferences sharedPrefsOne = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String orderBy = sharedPrefsOne.getString(
                getString(R.string.settings_search_order_by_key),
                getString(R.string.settings_search_order_by_default));

        String sortBy = sharedPrefsOne.getString(
                getString(R.string.settings_search_sort_by_key),
                getString(R.string.settings_search_sort_by_default));

        Uri baseUri = Uri.parse(NEWS_LINK);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        if(loadMore1)
        {
            load1 = load1 + 10;
        }
        else
        {
            load1 = 15;
        }

        if(!sortBy.equalsIgnoreCase("all"))
        {
            uriBuilder.appendQueryParameter("tag","tone/"+sortBy);
        }
        uriBuilder.appendQueryParameter("q",search);
        uriBuilder.appendQueryParameter("section","technology");
        uriBuilder.appendQueryParameter("show-tags","tone,contributor");
        uriBuilder.appendQueryParameter("show-fields","headline,body,trailText,publication,thumbnail");
        uriBuilder.appendQueryParameter("order-by",orderBy);
        uriBuilder.appendQueryParameter("page-size",String.valueOf(load1));
        uriBuilder.appendQueryParameter("api-key","test");
        boolean a = connect();
        return new NewsLoader(getActivity(),uriBuilder.toString(),refresh1,a);
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> data) {

        if (data != null && !data.isEmpty()) {

            mSwipeRefreshLayout1.setRefreshing(false);
            adapter1.clear();
            adapter1.addAll(data);
            adapter1.notifyDataSetChanged();
            listView1.setVisibility(View.VISIBLE);
            mEmptyStateTextView1.setVisibility(View.GONE);
            loaderManager1.destroyLoader(loader.getId());
            refresh1 = false;

            if (loadMore1)
            {
                listView1.setSelection(load1 - 11);
                loadMore1 = false;
            } else
            {
                listView1.setSelection(0);
            }

        }
        else
        {
            if(connect())
            {
                mSwipeRefreshLayout1.setRefreshing(false);
                loaderManager1.destroyLoader(loader.getId());
                refresh1 = false;
                Toast.makeText(getActivity(),"No more data" ,Toast.LENGTH_SHORT).show();
            }
            else
            {
                final Toast toast = makeText(getActivity(),"No internet connection",Toast.LENGTH_SHORT);
                toast.show();

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        toast.cancel();
                    }
                }, 1000);

                mSwipeRefreshLayout1.setRefreshing(false);
                loaderManager1.destroyLoader(loader.getId());
                refresh1 = false;
            }

        }


    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {

        adapter1.clear();
    }
}
