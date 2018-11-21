package com.ecode.myapplication.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.ecode.myapplication.R;
import com.ecode.myapplication.adapter.ListTopStoryRecyclerAdapter;
import com.ecode.myapplication.helper.DateProcessing;
import com.ecode.myapplication.helper.ItemAnimation;

import org.json.JSONException;
import org.json.JSONObject;

public class DetailStoryActivity extends AppCompatActivity {
    //HELPER
    String _prefixAuthor = "By : ";
    String storyId;

    //KONTEN
    ImageView iv_favorite;
    TextView tv_tittle;
    TextView tv_author;
    TextView tv_date;
    TextView tv_description;
    LinearLayout ll_comment;
    LinearLayout ll_content;
    RelativeLayout rl_progress;
    TextView tv_status_progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_story);
        iv_favorite=(ImageView) findViewById(R.id.iv_favorite_activity_detail_story);
        tv_tittle=(TextView) findViewById(R.id.tv_tittle_activity_detail_story);
        tv_author=(TextView) findViewById(R.id.tv_author_activity_detail_story);
        tv_date=(TextView) findViewById(R.id.tv_date_activity_detail_story);
        tv_description=(TextView) findViewById(R.id.tv_description_activity_detail_story);
        ll_comment=(LinearLayout) findViewById(R.id.ll_comment_activity_setail_story);
        ll_content=(LinearLayout) findViewById(R.id.ll_content_activity_detail_story);
        rl_progress=(RelativeLayout) findViewById(R.id.rl_progress_activity_detail_story);
        tv_status_progress=(TextView) findViewById(R.id.tv_status_activity_detail_story);

        getIntentData();
    }
    private void getIntentData(){
        storyId = getIntent().getExtras().getString("storyId");
        ll_content.setVisibility(View.GONE);
        rl_progress.setVisibility(View.VISIBLE);
        tv_status_progress.setText("Load Story Detail");
        doServiceeGetStory(storyId);
    }

    private void doServiceeGetStory(String id){
        AndroidNetworking.initialize(getApplicationContext());
        AndroidNetworking.get("https://hacker-news.firebaseio.com/v0/item/{id}.json?print=pretty")
                .addPathParameter("id", id)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("hasil", String.valueOf(response));
                        updatePage(response);
                    }

                    @Override
                    public void onError(ANError anError) {
                        anError.printStackTrace();
                    }
                });
    }

    private void updatePage(JSONObject jsonObject){
        rl_progress.setVisibility(View.GONE);
        ll_content.setVisibility(View.VISIBLE);
        //iv_favorite
        try {
            tv_tittle.setText(jsonObject.getString("title"));
            tv_author.setText(_prefixAuthor+jsonObject.getString("by"));
            tv_date.setText(DateProcessing.printDate(jsonObject.getLong("time"),false,DateProcessing.fDayMonthYear));
            tv_description.setText(jsonObject.getString("url"));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        //ll_comment
    }
}
