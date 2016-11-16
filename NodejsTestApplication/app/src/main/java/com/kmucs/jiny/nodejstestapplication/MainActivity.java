package com.kmucs.jiny.nodejstestapplication;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    TextView textView;

    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView)findViewById(R.id.textView);

    }

    public void onButton1Clicked(View v) {
        String url = "http://nodetest.iptime.org:3000/facebook/listlocation";

        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.activity_main);
        final StringBuffer sb = new StringBuffer();
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    public void onResponse(String response) {
                        try {
                            //결과 값 출력
                            JSONArray jarr = new JSONArray(response);   // JSONArray 생성

                            for(int i=0; i < jarr.length(); i++){
                                JSONObject jObject = jarr.getJSONObject(i);  // JSONObject 추출
                                String name = jObject.getString("name");
                                int checkins = jObject.getInt("checkins");
                                int id = jObject.getInt("id");

                                println(
                                        "체크인:" + checkins +
                                                "이름:" + name +
                                                "id:" + id
                                );
                            }

                            //textView.setText(sb.toString());
                            //println("onResponse() 호출됨 : " + response);
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

        Volley.newRequestQueue(this).add(request);
        println("웹서버에 요청함 : " + url);
    }

    public void println(final String data) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                textView.append(data + '\n');

            }
        });
    }
}
