package com.example.test.letsgoseoul;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class RestaurantListView extends LinearLayout {

    private ImageView mIcon;
    private TextView mText;
    public RestaurantListView(Context context, RestaurantListItem aItem) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.activity_restaurant_list_item, this, true);

        mIcon = (ImageView) findViewById(R.id.imageView1);
        mIcon.setImageBitmap(aItem.getIcon());

        mText = (TextView) findViewById(R.id.textView1);
        mText.setText(aItem.getData());
    }
    public void setText(String data) {
            mText.setText(data);
    }
    public void setIcon(Bitmap icon) {
        mIcon.setImageBitmap(icon);
    }

}