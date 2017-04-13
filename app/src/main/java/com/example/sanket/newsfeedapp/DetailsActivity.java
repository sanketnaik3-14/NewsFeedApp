package com.example.sanket.newsfeedapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import static com.example.sanket.newsfeedapp.R.id.body;

public class DetailsActivity extends AppCompatActivity implements Html.ImageGetter {

    private final static String TAG = "TestImageGetter";
    private TextView body1;
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
        ImageView newsimage = (ImageView)findViewById(R.id.details_image);

        headline1.setText(headline);
        tone1.setText(tone);
        date1.setText(date);

        body1 = (TextView)findViewById(R.id.body);
        Spanned spanned = Html.fromHtml(body,this, null);
        body1.setText(spanned);
        body1.setMovementMethod(LinkMovementMethod.getInstance());
        
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

    @Override
    public Drawable getDrawable(String source) {
        LevelListDrawable d = new LevelListDrawable();
        Drawable empty = getResources().getDrawable(R.drawable.noimage);
        d.addLevel(0, 0, empty);
        d.setBounds(0, 0, empty.getIntrinsicWidth(), empty.getIntrinsicHeight());

        new LoadImage().execute(source, d);

        return d;
    }

    class LoadImage extends AsyncTask<Object, Void, Bitmap> {

        private LevelListDrawable mDrawable;

        @Override
        protected Bitmap doInBackground(Object... params) {
            String source = (String) params[0];
            mDrawable = (LevelListDrawable) params[1];
            Log.d(TAG, "doInBackground " + source);
            try {
                InputStream is = new URL(source).openStream();
                return BitmapFactory.decodeStream(is);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            Log.d(TAG, "onPostExecute drawable " + mDrawable);
            Log.d(TAG, "onPostExecute bitmap " + bitmap);
            if (bitmap != null) {
                BitmapDrawable d = new BitmapDrawable(bitmap);
                mDrawable.addLevel(1, 1, d);
                mDrawable.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
                mDrawable.setLevel(1);
                // i don't know yet a better way to refresh TextView
                // mTv.invalidate() doesn't work as expected
                CharSequence t = body1.getText();
                body1.setText(t);
            }
        }
    }
}
