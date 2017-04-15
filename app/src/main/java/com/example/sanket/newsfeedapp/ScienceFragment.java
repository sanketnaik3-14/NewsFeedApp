package com.example.sanket.newsfeedapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
public class ScienceFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<News>> {


    private String search2;
    private ListView listView2;
    LoaderManager loaderManager2;
    private SwipeRefreshLayout mSwipeRefreshLayout2;
    private static final String NEWS_LINK = "http://content.guardianapis.com/search";
    private NewsAdapter adapter2;
    private int load2 = 15;
    private TextView mEmptyStateTextView2;
    public boolean refresh2;
    public boolean loadMore2 = false;

    public ScienceFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_science,container,false);

        SearchActivity activity = (SearchActivity)getActivity();
        search2 = activity.getMyString();

        mEmptyStateTextView2 = (TextView)rootView.findViewById(R.id.empty_view2);
        mEmptyStateTextView2.setVisibility(View.GONE);

        mSwipeRefreshLayout2 = (SwipeRefreshLayout)rootView.findViewById(R.id.swipedown2);
        mSwipeRefreshLayout2.setRefreshing(true);
        loaderManager2 = getActivity().getSupportLoaderManager();

        loaderManager2.initLoader(1,null,this);
        refresh2 = false;

        listView2 = (ListView)rootView.findViewById(R.id.list2);
        adapter2 = new NewsAdapter(getActivity(),new ArrayList<News>());
        adapter2.clear();
        adapter2.notifyDataSetChanged();
        listView2.setAdapter(null);
        listView2.setAdapter(adapter2);
        listView2.setEmptyView(mEmptyStateTextView2);

        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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

                Intent intent2 = new Intent(getActivity(),DetailsActivity.class);
                intent2.putExtra("head",news.getmHeadline());
                intent2.putExtra("tone",news.getmTone());
                intent2.putExtra("date",news.getmPublicationDate());
                intent2.putExtra("contributor",news.getmContributor());
                intent2.putExtra("image",byteArray);
                intent2.putExtra("body",news.getmBody());
                startActivity(intent2);

            }
        });

        listView2.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {


                if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount != 0) {
                    loadMore2 = true;
                    mSwipeRefreshLayout2.setRefreshing(true);
                   createFragment();
                }

            }
        });

        mSwipeRefreshLayout2.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                mSwipeRefreshLayout2.setRefreshing(true);
                mEmptyStateTextView2.setVisibility(View.GONE);
                refresh2 = true;
                createFragment();
            }
        });

        return rootView;
    }

    public void createFragment()
    {
        loaderManager2.initLoader(1,null,this);
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

        SharedPreferences sharedPrefsTwo = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String orderBy = sharedPrefsTwo.getString(
                getString(R.string.settings_search_order_by_key),
                getString(R.string.settings_search_order_by_default));

        String sortBy = sharedPrefsTwo.getString(
                getString(R.string.settings_search_sort_by_key),
                getString(R.string.settings_search_sort_by_default));

        Uri baseUri = Uri.parse(NEWS_LINK);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        if(loadMore2)
        {
            load2 = load2 + 10;
        }
        else
        {
            load2 = 15;
        }

        if(!sortBy.equalsIgnoreCase("all"))
        {
            uriBuilder.appendQueryParameter("tag","tone/"+sortBy);
        }

        uriBuilder.appendQueryParameter("q",search2);
        uriBuilder.appendQueryParameter("section","science");
        uriBuilder.appendQueryParameter("show-tags","tone,contributor");
        uriBuilder.appendQueryParameter("show-fields","headline,body,trailText,publication,thumbnail");
        uriBuilder.appendQueryParameter("order-by",orderBy);
        uriBuilder.appendQueryParameter("page-size",String.valueOf(load2));
        uriBuilder.appendQueryParameter("api-key","test");
        boolean a = connect();
        return new NewsLoader(getActivity(),uriBuilder.toString(),refresh2,a);
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> data) {
        if (data != null && !data.isEmpty()) {

            mSwipeRefreshLayout2.setRefreshing(false);
            adapter2.clear();
            adapter2.addAll(data);
            adapter2.notifyDataSetChanged();
            listView2.setVisibility(View.VISIBLE);
            mEmptyStateTextView2.setVisibility(View.GONE);
            loaderManager2.destroyLoader(loader.getId());
            refresh2 = false;

            if (loadMore2)
            {
                listView2.setSelection(load2 - 11);
                loadMore2 = false;
            } else
            {
                listView2.setSelection(0);
            }

        }
        else
        {
            if(connect())
            {
                mSwipeRefreshLayout2.setRefreshing(false);
                loaderManager2.destroyLoader(loader.getId());
                refresh2 = false;
                Toast.makeText(getActivity(),getString(R.string.nomoredata) ,Toast.LENGTH_SHORT).show();
            }
            else
            {
                final Toast toast = makeText(getActivity(),getString(R.string.nomoreinternet),Toast.LENGTH_SHORT);
                toast.show();

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        toast.cancel();
                    }
                }, 1000);

                mSwipeRefreshLayout2.setRefreshing(false);
                loaderManager2.destroyLoader(loader.getId());
                refresh2 = false;
            }

        }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        adapter2.clear();

    }
}
