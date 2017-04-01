package com.example.sanket.newsfeedapp;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sanket on 29/03/17.
 */

public class NewsLoader extends AsyncTaskLoader<List<News>> {

    String link = "";
    public NewsLoader(Context context,String url1)
    {
        super(context);
        link = url1;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<News>loadInBackground() {

        URL url = createUrl(link);
        Log.i("NewsLoader",url.toString());
        String json = "";

        try
        {
            json = makeHttpRequest(url);
        }
        catch(IOException e)
        {
            Log.e("BooksLoader","IOException" ,e);
        }

        if(TextUtils.isEmpty(json))
        {
            return null;
        }

        List<News> news = new ArrayList<News>();

        try
        {
            JSONObject jsonObject = new JSONObject(json);
            JSONObject response = jsonObject.getJSONObject("response");
            JSONArray jsonArray = response.getJSONArray("results");
            Log.i("NewsLoader",jsonArray.toString());
            for(int i = 0; i<jsonArray.length(); i++)
            {
                String headline = "";
                String trailText = "";
                String body = "";
                String publicationDate = "";
                String publication = "";
                String tone = "";
                String contributor = "";
                Bitmap bmp = null;

                JSONObject obj = jsonArray.getJSONObject(i);
                publicationDate = obj.getString("webPublicationDate");

                JSONObject fields = obj.getJSONObject("fields");
                headline = fields.getString("headline");
                trailText = fields.getString("trailText");
                body = fields.getString("body");
                publication = fields.getString("publication");
                if(fields.has("thumbnail")) {
                    String thumbnail = fields.getString("thumbnail");
                        try {
                            URL url1 = new URL(thumbnail);
                            bmp = BitmapFactory.decodeStream(url1.openConnection().getInputStream());

                        } catch (MalformedURLException e) {
                            Log.i("BooksLoader", "URL empty or bitmap not formed");
                        } catch (IOException e1) {
                            Log.i("BooksLoader", "connection failed");
                        }

                }
                JSONArray tags = obj.getJSONArray("tags");

                if(tags.length() == 0)
                {
                    tone = "news";
                    contributor = "";
                }
                else if(tags.length() == 1)
                {
                    JSONObject obj1 = tags.getJSONObject(0);
                    tone = obj1.getString("webTitle");
                    contributor = "";
                }
                else
                {
                    JSONObject obj2 = tags.getJSONObject(0);
                    JSONObject obj3 = tags.getJSONObject(1);
                    tone = obj2.getString("webTitle");
                    contributor = obj3.getString("webTitle");
                }
                news.add(new News(headline,trailText,body,publicationDate,publication,tone,contributor,bmp));
            }

        }
        catch (JSONException e)
        {
            Log.e("BooksLoader", "Problem parsing the earthquake JSON results", e);
        }
        return news;
    }

    public static URL createUrl(String link)
    {
        URL url = null;
        try
        {
            url = new URL(link);
        }catch (MalformedURLException e) {
            Log.e("BooksLoader", "Error with creating URL ", e);
        }
        return url;
    }

    public static String makeHttpRequest(URL url) throws IOException
    {
        String jsonResponse = "";

        if(url == null)
        {
            return jsonResponse;
        }

        HttpURLConnection connection = null;
        InputStream input = null;

        try
        {
            connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod("GET");
            connection.connect();

            if(connection.getResponseCode() == 200)
            {
                input = connection.getInputStream();
                jsonResponse = readFromStream(input);
            }
            else
            {
                Log.e("BooksLoader","Error response code :" + connection.getResponseCode()+connection.getResponseMessage());
            }
        }catch (IOException e)
        {
            Log.e("BooksLoader","Problem retrieving string" ,e);
        }

        return jsonResponse;
    }

    public static String readFromStream(InputStream is) throws IOException
    {
        StringBuilder stringBuilder = new StringBuilder();
        if(is != null) {
            InputStreamReader isr = new InputStreamReader(is, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(isr);
            String line = reader.readLine();

            while (line != null) {
                stringBuilder.append(line);
                line = reader.readLine();
            }
        }
        return stringBuilder.toString();
    }
}
