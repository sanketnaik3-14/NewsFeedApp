package com.example.sanket.newsfeedapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.example.sanket.newsfeedapp.R.id.date;

/**
 * Created by sanket on 30/03/17.
 */

public class NewsAdapter extends ArrayAdapter<News> {

    public NewsAdapter(Context context, List<News> news) {
        super(context, 0, news);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        final News current_news = getItem(position);

        TextView headlineView = (TextView) convertView.findViewById(R.id.headline);
        headlineView.setText(current_news.getmHeadline());

        TextView trailView = (TextView) convertView.findViewById((R.id.trailText));

        if (Build.VERSION.SDK_INT >= 24) {
            trailView.setText(Html.fromHtml(current_news.getmTrailText(), Html.FROM_HTML_MODE_LEGACY).toString().replaceAll("\n", "").trim());

        } else {
            trailView.setText(Html.fromHtml(current_news.getmTrailText()).toString().replaceAll("\n", "").trim());
        }

        ImageView publicationView = (ImageView) convertView.findViewById(R.id.publication);
        publicationView.setImageResource(R.drawable.guard);

        TextView toneView = (TextView) convertView.findViewById(R.id.tone);
        toneView.setText(current_news.getmTone());
        String tone = current_news.getmTone();
        if(tone.equalsIgnoreCase("news"))
        {
         toneView.setTextColor(Color.parseColor("#F44336"));
        }
        else if(tone.equalsIgnoreCase("documentaries"))
        {
            toneView.setTextColor(Color.parseColor("#FFA726"));
        }
        else if(tone.equalsIgnoreCase("features"))
        {
            toneView.setTextColor(Color.parseColor("#CDDC39"));
        }
        else if(tone.equalsIgnoreCase("blogposts"))
        {
            toneView.setTextColor(Color.parseColor("#4CAF50"));
        }
        else if(tone.equalsIgnoreCase("reviews"))
        {
            toneView.setTextColor(Color.parseColor("#FF5722"));
        }
        else if(tone.equalsIgnoreCase("analysis"))
        {
            toneView.setTextColor(Color.parseColor("#607D8B"));
        }
        else if(tone.equalsIgnoreCase("letters"))
        {
            toneView.setTextColor(Color.parseColor("#26A69A"));
        }
        else if(tone.equalsIgnoreCase("obituaries"))
        {
            toneView.setTextColor(Color.parseColor("#00BCD4"));
        }
        else if(tone.equalsIgnoreCase("comment"))
        {
            toneView.setTextColor(Color.parseColor("#2979FF"));
        }
        else
        {
            toneView.setTextColor(Color.parseColor("#E91E63"));
        }



        TextView dateView = (TextView) convertView.findViewById(date);
        String dt = current_news.getmPublicationDate();
        dt = dt.split("T")[0];
        dateView.setText(dt);

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
