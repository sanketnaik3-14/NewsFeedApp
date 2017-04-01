package com.example.sanket.newsfeedapp;

import android.graphics.Bitmap;

/**
 * Created by sanket on 29/03/17.
 */

public class News {
    private String mHeadline;
    private String mTrailText;
    private String mBody;
    private String mPublicationDate;
    private String mPublication;
    private String mTone;
    private String mContributor;
    private Bitmap mBmp;

    public News(String headline,String trailText,String body,String publicationDate,String publication,
                String tone,String contributor,Bitmap bmp)
    {
        mHeadline = headline;
        mTrailText = trailText;
        mBody = body;
        mPublicationDate = publicationDate;
        mPublication = publication;
        mTone = tone;
        mContributor = contributor;
        mBmp = bmp;
    }

    public String getmHeadline() {
        return mHeadline;
    }

    public String getmTrailText() {
        return mTrailText;
    }

    public String getmBody() {
        return mBody;
    }

    public String getmPublication() {
        return mPublication;
    }

    public String getmTone() {
        return mTone;
    }

    public String getmContributor() {
        return mContributor;
    }

    public String getmPublicationDate() {
        return mPublicationDate;
    }

    public Bitmap getmBitmap() {
        return mBmp;
    }
}
