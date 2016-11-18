package com.example.test.letsgoseoul;

import android.app.Activity;
import android.graphics.Bitmap;

public class RestaurantListItem extends Activity {

    private Bitmap mIcon;
    private String mData;
    private boolean mSelectable = true;

    public RestaurantListItem(Bitmap pic, String name){
        mIcon = pic;
        mData = name;
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
    public void setData(String obj) {
        mData = obj;
    }
    public void setIcon(Bitmap icon) {
        mIcon = icon;
    }

}
