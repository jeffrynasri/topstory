package com.ecode.myapplication.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.ecode.myapplication.R;
import com.ecode.myapplication.adapter.ListTopStoryRecyclerAdapter;
import com.ecode.myapplication.helper.ItemAnimation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    //HELPER
    int REQUEST_FOR_ACTIVITY_CODE =24;
    int TOPSTORY_COUNT=0;
    JSONArray jsonArray = new JSONArray();
    ListTopStoryRecyclerAdapter listTopStoryRecyclerAdapter;

    //KONTEN
    TextView tv_tittle_favorite;
    RecyclerView rv;
    ProgressBar pb;
    TextView tv_progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_tittle_favorite=(TextView) findViewById(R.id.tv_tittle_activity_main);
        rv=(RecyclerView) findViewById(R.id.rv_activity_main);
        pb = (ProgressBar) findViewById(R.id.pb_activity_main);
        tv_progress = (TextView) findViewById(R.id.tv_progress_activity_main);

        initRecyclerView();
        doServiceeGetTopStory();
//        Intent intent = new Intent(this, DetailStoryActivity.class);
//        intent.putExtra("storyId","18501327");
//        startActivity(intent);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("soy", String.valueOf(requestCode));
        if (requestCode == REQUEST_FOR_ACTIVITY_CODE) {
            if(resultCode == Activity.RESULT_OK){
                String result=data.getStringExtra("favorite");
                Log.d("soy", String.valueOf(result));
                tv_tittle_favorite.setText(result);
            }else if (resultCode == Activity.RESULT_CANCELED) {
                Log.d("soy", "cancel");
                //Write your code if there's no result
            }
        }
    }//onActivityResult
    private void doServiceeGetTopStory(){
        AndroidNetworking.initialize(getApplicationContext());
        AndroidNetworking.get("https://hacker-news.firebaseio.com/v0/topstories.json?print=pretty")
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            Log.d("hasil", String.valueOf(response));
                            TOPSTORY_COUNT=response.length();
                            for(int i=0;i<response.length();i++){
                                doServiceeGetStory(response.getString(i));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        error.printStackTrace();
                    }
                });
    }
    private void doServiceeGetStory(String id){
        AndroidNetworking.initialize(getApplicationContext());
        AndroidNetworking.get("https://hacker-news.firebaseio.com/v0/item/{id}.json?print=pretty")
                .addPathParameter("id", id)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("hasil2", String.valueOf(response));
                        jsonArray.put(response);
                        tv_progress.setText(jsonArray.length()+" / "+String.valueOf(TOPSTORY_COUNT));
                        if(jsonArray.length() == TOPSTORY_COUNT-1){
                            pb.setVisibility(View.GONE);
                            tv_progress.setVisibility(View.GONE);
                            rv.setVisibility(View.VISIBLE);
                            listTopStoryRecyclerAdapter = new ListTopStoryRecyclerAdapter(MainActivity.this, ItemAnimation.RIGHT_LEFT,jsonArray);
                            rv.setAdapter(listTopStoryRecyclerAdapter);
                            listTopStoryRecyclerAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        anError.printStackTrace();
                    }
                });
    }
    private void initRecyclerView() {
        rv.setLayoutManager(new LinearLayoutManager(MainActivity.this));
    }
}
