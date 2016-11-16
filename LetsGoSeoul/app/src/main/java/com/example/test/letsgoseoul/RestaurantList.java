package com.example.test.letsgoseoul;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
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
import java.util.List;
import java.util.Objects;


public class RestaurantList extends Activity {
    Activity act =this;
    GridView gridView;
    private List<ResolveInfo> apps;
    private PackageManager pm;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        pm = getPackageManager();
        apps = pm.queryIntentActivities(mainIntent, 0);

        setContentView(R.layout.activity_restaurant_list);

        gridView = (GridView) findViewById(R.id.gridView1);
        gridView.setAdapter(new gridAdapter());
    }

    public class gridAdapter extends BaseAdapter{
        LayoutInflater inflater;

        public gridAdapter() {
           inflater = (LayoutInflater) act.getSystemService(getApplicationContext().LAYOUT_INFLATER_SERVICE);
        }

        public final int getCount() {
            return apps.size();
        }

        public final Object getItem(int position) {
            return apps.get(position);
        }

        public final long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.activity_restaurant_list_item, parent, false);
            }
            final ResolveInfo info = apps.get(position);
            ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView1);
            TextView textView = (TextView) convertView.findViewById(R.id.textView1);
            imageView.setImageDrawable(info.activityInfo.loadIcon(getPackageManager()));
            textView.setText(info.activityInfo.loadLabel(pm).toString());
            imageView.setOnClickListener(new Button.OnClickListener() {
                                             public void onClick(View v) {
                                                 Intent intent = new Intent(RestaurantList.this, Selected_Restaurant.class);
                                                 //position이 선택된 item의 순서
                                                 intent.putExtra("SelectedPlace", "num");   //test용
                                                 //intent.putExtra("SelectedPlace", "");    여기다가 선택된 아이템의 db ID 텍스트로 뒤에 넣어주면 됌
                                                 startActivity(intent);
                                             }
                                         }
            );
            return convertView;
        }
    }
}


