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


    static class ViewHolder
    {
        private TextView headlineView;
        private TextView trailView;
        private ImageView publicationView;
        private TextView toneView;
        private TextView dateView;
        private ImageView imageView;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
            holder = new ViewHolder();

            holder.headlineView = (TextView) convertView.findViewById(R.id.headline);
            holder.trailView = (TextView) convertView.findViewById((R.id.trailText));
            holder.publicationView = (ImageView) convertView.findViewById(R.id.publication);
            holder.toneView = (TextView) convertView.findViewById(R.id.tone);
            holder.dateView = (TextView) convertView.findViewById(date);
            holder.imageView = (ImageView)convertView.findViewById(R.id.headline_image);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        final News current_news = getItem(position);

        holder.headlineView.setText(current_news.getmHeadline());


        if (Build.VERSION.SDK_INT >= 24) {
            holder.trailView.setText(Html.fromHtml(current_news.getmTrailText(), Html.FROM_HTML_MODE_LEGACY).toString().replaceAll("\n", "").trim());

        } else {
            holder.trailView.setText(Html.fromHtml(current_news.getmTrailText()).toString().replaceAll("\n", "").trim());
        }


        holder.publicationView.setImageResource(R.drawable.guard);

        holder.toneView.setText(current_news.getmTone());
        String tone = current_news.getmTone();
        if(tone.equalsIgnoreCase("news"))
        {
         holder.toneView.setTextColor(Color.parseColor("#F44336"));
        }
        else if(tone.equalsIgnoreCase("documentaries"))
        {
            holder.toneView.setTextColor(Color.parseColor("#FFA726"));
        }
        else if(tone.equalsIgnoreCase("features"))
        {
            holder.toneView.setTextColor(Color.parseColor("#CDDC39"));
        }
        else if(tone.equalsIgnoreCase("blogposts"))
        {
            holder.toneView.setTextColor(Color.parseColor("#4CAF50"));
        }
        else if(tone.equalsIgnoreCase("reviews"))
        {
            holder.toneView.setTextColor(Color.parseColor("#FF5722"));
        }
        else if(tone.equalsIgnoreCase("analysis"))
        {
            holder.toneView.setTextColor(Color.parseColor("#607D8B"));
        }
        else if(tone.equalsIgnoreCase("letters"))
        {
            holder.toneView.setTextColor(Color.parseColor("#26A69A"));
        }
        else if(tone.equalsIgnoreCase("obituaries"))
        {
            holder.toneView.setTextColor(Color.parseColor("#00BCD4"));
        }
        else if(tone.equalsIgnoreCase("comment"))
        {
            holder.toneView.setTextColor(Color.parseColor("#2979FF"));
        }
        else
        {
            holder.toneView.setTextColor(Color.parseColor("#E91E63"));
        }

        String dt = current_news.getmPublicationDate();
        dt = dt.split("T")[0];
        holder.dateView.setText(dt);

        Bitmap bm = current_news.getmBitmap();
        if(bm == null)
        {
         holder.imageView.setImageResource(R.drawable.noimage);
        }
        else
        {
            holder.imageView.setImageBitmap(bm);
        }

        return convertView;
    }
}
