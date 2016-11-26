package com.example.test.letsgoseoul;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;

import android.os.Message;

import android.support.v7.app.ActionBar;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Handler;
import java.util.logging.LogRecord;


public class RestaurantList extends MenuBar {

    // Handler handler = new Handler();  url->bitmap
    //  Activity act = this;
    GridView gridView;
    gridAdapter adapter;
    private double lat;   //위도
    private double lng;   //경도
    private double resLat;
    private double resLng;
    private String buttonOption;
    private static Bitmap bm;

    private String foodUrl = "http://nodetest.iptime.org:3000/tourapi/foodlist";
    private String placeUrl = "http://nodetest.iptime.org:3000/tourapi/placelist";



    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_list);


        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Lets Go Seoul");
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = new Intent(this.getIntent());
        lat = intent.getExtras().getDouble("lat");
        lng = intent.getExtras().getDouble("lng");
        gridView = (GridView) findViewById(R.id.gridView1);
        adapter = new gridAdapter(this);

        String forUrlData = "?lat="+lat+"&lng="+lng;

        buttonOption = intent.getStringExtra("buttonOption");
        String url= null;

        CheckTypesTask task = new CheckTypesTask();
        task.execute();

        if (buttonOption.equals("restaurant"))  //레스토랑일 경우
            url = foodUrl + forUrlData;
        else  //sights 일 경우
            url = placeUrl + forUrlData;

        final String realUrl = url;
        Log.v("url", url);
        final Thread mTread = new Thread() {
            @Override
            public void run() {
                try {
                    StringRequest request = new StringRequest(Request.Method.GET, realUrl,
                            new Response.Listener<String>() {
                                public void onResponse(String response) {
                                    try {
                                        //결과 값 출력
                                        JSONArray jarr = new JSONArray(response);   // JSONArray 생성

                                        for(int i=0; i < jarr.length(); i++){
                                            JSONObject jObject = jarr.getJSONObject(i);  // JSONObject 추출
                                            String image = jObject.getString("image");
                                            String name = jObject.getString("name");

                                            int id = jObject.getInt("contentid");
                                            resLat = jObject.getDouble("lat");
                                            resLng = jObject.getDouble("lng");

                                            Log.v("list", id + " , " +  name);
                                            getBitmap(image, name, id,resLat,resLng);
                                            adapter.notifyDataSetChanged();
                                            //getBitmap("http://tong.visitkorea.or.kr/cms/resource/03/1987703_image2_1.jpg","바다",1);
                                        }

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                public void onErrorResponse(VolleyError error) {
                                    error.printStackTrace();
                                }
                            }
                    ) {
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<>();

                            return params;
                        }
                    };

                    Volley.newRequestQueue(getApplicationContext()).add(request);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        ////
        mTread.start();
        try{
            mTread.join();
            adapter.notifyDataSetChanged();
        } catch (InterruptedException e) {

        }
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {     //item 선택했을때
                Intent intent;
                intent = new Intent(RestaurantList.this, Selected_Restaurant.class);    //레스토랑 일 경우

                intent.putExtra("Selected", adapter.getItem(position).getId());   //선택된 곳 id 넘겨주기
                intent.putExtra("SelectedUrl", adapter.getItem(position).getUrl());   //선택된 곳 url 넘겨주기
                intent.putExtra("Lat",adapter.getItem(position).getmLat());
                intent.putExtra("Lng",adapter.getItem(position).getmLng());
                intent.putExtra("name",adapter.getItem(position).getData());
                //Toast.makeText(RestaurantList.this,adapter.getItem(position).getId(),Toast.LENGTH_LONG).show();
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

        public RestaurantListItem getItem(int position) {
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
                itemView = (RestaurantListView) convertView;
                itemView.setIcon(mItems.get(position).getIcon());
                itemView.setText(mItems.get(position).getData());
            }
            return itemView;
        }
    }

    public void getBitmap(String imgUrl,String imgName,int id, double lat,double lng) {
        final String urlImg =imgUrl;
        final String urlName =imgName;
        final int urlId =id;
        final double urlLat = lat;
        final double urlLng = lng;
        Thread mTread = new Thread() {
            @Override
            public void run() {
                try {
                    URL imgUrlTest = new URL(urlImg);
                    HttpURLConnection connection = (HttpURLConnection) imgUrlTest.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream is = connection.getInputStream();
                    bm = BitmapFactory.decodeStream(is);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        mTread.start();
        try {
            mTread.join();
            adapter.addItem(new RestaurantListItem(bm, urlName, urlId, urlImg,urlLat,urlLng ));
        } catch (InterruptedException e) {
        }
    }
    private class CheckTypesTask extends AsyncTask<Void, Void, Void> {

        ProgressDialog asyncDialog = new ProgressDialog(
                RestaurantList.this);

        @Override
        protected void onPreExecute() {
            asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            asyncDialog.setMessage("로딩중입니다..");

            // show dialog
            asyncDialog.show();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                for (int i = 0; i < 5; i++) {
                    //asyncDialog.setProgress(i * 30);
                    Thread.sleep(500);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            asyncDialog.dismiss();
            super.onPostExecute(result);
        }
    }
}





