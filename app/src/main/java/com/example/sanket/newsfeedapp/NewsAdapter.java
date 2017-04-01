package com.example.sanket.newsfeedapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.net.HttpURLConnection;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by sanket on 30/03/17.
 */

public class NewsAdapter extends ArrayAdapter<News> {

    public NewsAdapter(Context context, List<News> news)
    {
        super(context,0,news);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);
        }

        final News current_news = getItem(position);

        TextView headlineView = (TextView)convertView.findViewById(R.id.headline);
        headlineView.setText(current_news.getmHeadline());

        TextView trailView = (TextView)convertView.findViewById((R.id.trailText));

        if (Build.VERSION.SDK_INT >= 24)
        {
            trailView.setText(Html.fromHtml(current_news.getmTrailText(),Html.FROM_HTML_MODE_LEGACY));
        }
        else
        {
            trailView.setText(Html.fromHtml(current_news.getmTrailText()));
        }

        TextView publicationView = (TextView)convertView.findViewById(R.id.publication);

        if (Build.VERSION.SDK_INT >= 24)
        {
            publicationView.setText(Html.fromHtml(current_news.getmPublication(),Html.FROM_HTML_MODE_LEGACY));
        }
        else
        {
            publicationView.setText(Html.fromHtml(current_news.getmPublication()));
        }


        TextView toneView = (TextView)convertView.findViewById(R.id.tone);
        toneView.setText(current_news.getmTone());

        TextView dateView = (TextView)convertView.findViewById(R.id.date);
        dateView.setText(current_news.getmPublicationDate());

        ImageView imageView = (ImageView)convertView.findViewById(R.id.headline_image);
        Bitmap bm = current_news.getmBitmap();
        if(bm == null)
        {
         imageView.setImageResource(R.drawable.noimage);
        }
        else
        {
            imageView.setImageBitmap(bm);
        }

        return convertView;
    }
}
