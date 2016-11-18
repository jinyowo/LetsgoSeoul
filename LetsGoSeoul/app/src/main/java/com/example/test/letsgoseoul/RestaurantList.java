package com.example.test.letsgoseoul;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Handler;
import java.util.logging.LogRecord;


public class RestaurantList extends Activity {

  // Handler handler = new Handler();  url->bitmap
    //  Activity act = this;
    GridView gridView;
    gridAdapter adapter;
    private double lat;   //위도
    private double lng;   //경도
    private String buttonOption;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_list);

        Intent intent = new Intent(this.getIntent());
        lat = intent.getExtras().getDouble("lat");
        lng = intent.getExtras().getDouble("lng");
        gridView = (GridView) findViewById(R.id.gridView1);
        adapter = new gridAdapter(this);

        buttonOption = intent.getStringExtra("buttonOption");
        if(buttonOption=="restaurant")  //레스토랑일 경우
        {
              //for 문을 통해 서버에서 하나씩 받아온다
        }
        else  //sights 일 경우
        {
        }
        //Bitmap bm1 = getBitmap("http://tong.visitkorea.or.kr/cms/resource/03/1987703_image2_1.jpg");
        Bitmap bm1 = BitmapFactory.decodeResource(getResources(), R.drawable.food1);
        Bitmap bm2 = BitmapFactory.decodeResource(getResources(), R.drawable.food2);
        Bitmap bm3 = BitmapFactory.decodeResource(getResources(), R.drawable.food3);
        Bitmap bm4 = BitmapFactory.decodeResource(getResources(), R.drawable.food4);
        Bitmap bm5 = BitmapFactory.decodeResource(getResources(), R.drawable.food5);
        Bitmap bm6 = BitmapFactory.decodeResource(getResources(), R.drawable.food6);
        adapter.addItem(new RestaurantListItem(bm1, "1"));
        adapter.addItem(new RestaurantListItem(bm2, "2"));
        adapter.addItem(new RestaurantListItem(bm3, "3"));
        adapter.addItem(new RestaurantListItem(bm4, "4"));
        adapter.addItem(new RestaurantListItem(bm5, "5"));
        adapter.addItem(new RestaurantListItem(bm6, "6"));


        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(RestaurantList.this, Selected_Restaurant.class);
                intent.putExtra("SelectedRestaurant", position);   //test용
                startActivity(intent);
            }
        });
    }

    public class gridAdapter extends BaseAdapter {
        private Context mContext;
        private List<RestaurantListItem> mItems = new ArrayList<RestaurantListItem>();

        public gridAdapter(Context context) {
            mContext = context;
        }

        public void addItem(RestaurantListItem it) {
            mItems.add(it);
        }

        public void setListItems(List<RestaurantListItem> lit) {
            mItems = lit;
        }

        public int getCount() {
            return mItems.size();
        }

        public Object getItem(int position) {
            return mItems.get(position);
        }

        public boolean areAllItemsSelectable() {
            return false;
        }

        public boolean isSelectable(int position) {
            try {
                return mItems.get(position).isSelectable();
            } catch (IndexOutOfBoundsException ex) {
                return false;
            }
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            RestaurantListView itemView;
            if (convertView == null) {
                itemView = new RestaurantListView(mContext, mItems.get(position));
            } else {
                itemView = ( RestaurantListView) convertView;
                itemView.setIcon(mItems.get(position).getIcon());
                itemView.setText(mItems.get(position).getData());
            }
            return itemView;
        }
    }

    private Bitmap getBitmap(String url) {
        URL imgUrl = null;
        HttpURLConnection connection = null;
        InputStream is = null;
        Bitmap retBitmap = null;
        try{
            imgUrl = new URL(url);
            connection = (HttpURLConnection) imgUrl.openConnection();
            connection.setDoInput(true);
            connection.connect();
            is = connection.getInputStream();
            retBitmap = BitmapFactory.decodeStream(is);
        }catch(Exception e) {
            e.printStackTrace();
            return null;
        }finally {
            if(connection!=null) {
                connection.disconnect();
            }
            return retBitmap;
        }
    }

}




