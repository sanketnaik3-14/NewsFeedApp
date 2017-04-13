package com.example.sanket.newsfeedapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent i = getIntent();

        String headline = i.getStringExtra("head");
        String tone = i.getStringExtra("tone");
        String date = i.getStringExtra("date");
        date = date.split("T")[0];
        String contributor = i.getStringExtra("contributor");
        String contri = "By, "+contributor;
        String body = i.getStringExtra("body");

        byte[] byteArray = getIntent().getByteArrayExtra("image");
        Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

        TextView headline1 = (TextView)findViewById(R.id.headline1);
        TextView tone1 = (TextView)findViewById(R.id.tone1);
        TextView date1 = (TextView)findViewById(R.id.date1);
        TextView contributor1 = (TextView)findViewById(R.id.contributor);
        TextView body1 = (TextView)findViewById(R.id.body);
        ImageView newsimage = (ImageView)findViewById(R.id.details_image);

        headline1.setText(headline);
        tone1.setText(tone);
        date1.setText(date);

        if (Build.VERSION.SDK_INT >= 24) {
            body1.setText(Html.fromHtml(body, Html.FROM_HTML_MODE_LEGACY).toString());

        } else {
            body1.setText(Html.fromHtml(body).toString());
        }

        if(contributor == null)
        {
            contributor1.setText("-");
        }
        else
        {
            contributor1.setText(contri);
        }

        if(bmp == null)
        {
            newsimage.setImageResource(R.drawable.noimage);
        }
        else
        {
            newsimage.setImageBitmap(bmp);
        }

    }
}
