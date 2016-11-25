package com.example.test.letsgoseoul;

import android.app.Activity;
import android.graphics.Bitmap;

public class RestaurantListItem extends Activity {

    private Bitmap mIcon;
    private String mData;
    private int mId;
    private String mUrl;
    private double mLat, mLng;
    private boolean mSelectable = true;

    public RestaurantListItem(Bitmap pic, String name,int id,String url,double lat, double lng){
        mIcon = pic;
        mData = name;
        mId = id;
        mUrl = url;
        mLat = lat;
        mLng = lng;
    }

    public boolean isSelectable() {
        return mSelectable;
    }
    public void setSelectable(boolean selectable) {
        mSelectable = selectable;
    }
    public String getData() {
        return mData;
    }
    public Bitmap getIcon() {
        return mIcon;
    }
    public int getId() {return mId;}
    public String getUrl() {return mUrl;}
    public double getmLat() {return mLat;}
    public double getmLng() {return mLng;}
    public void setData(String obj) {
        mData = obj;
    }
    public void setIcon(Bitmap icon) {
        mIcon = icon;
    }

}
