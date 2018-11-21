package com.ecode.myapplication.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DetailStoryActivity extends AppCompatActivity {
    //HELPER
    int COMMENT_COUNT=0;
    String _prefixAuthor = "By : ";
    String storyId;
    JSONObject jsonObjectDetail=new JSONObject();
    JSONArray jsonArrayComment=new JSONArray();


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
        tv_status_progress.setText("Load Story Detail...");
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
                        jsonObjectDetail=response;
                        COMMENT_COUNT = jsonObjectDetail.length();
                        tv_status_progress.setText("Load Comments...");
                        try {
                            if(response.has("kids")){
                                JSONArray jsonArray = response.getJSONArray("kids");
                                for(int i =0;i<jsonArray.length();i++){
                                    doServiceeGetComment(jsonArray.getString(i));
                                }
                            }else {
                                updatePage();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        updatePage();
                    }

                    @Override
                    public void onError(ANError anError) {
                        anError.printStackTrace();
                    }
                });
    }

    private void doServiceeGetComment(String id){
        AndroidNetworking.initialize(getApplicationContext());
        AndroidNetworking.get("https://hacker-news.firebaseio.com/v0/item/{id}.json?print=pretty")
                .addPathParameter("id", id)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("hasil", String.valueOf(response));
                        jsonArrayComment.put(response);
                        if(jsonArrayComment.length()==COMMENT_COUNT-1){
                            updatePage();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        anError.printStackTrace();
                    }
                });
    }
    private void updatePage(){
        //iv_favorite
        try {
            tv_tittle.setText(jsonObjectDetail.getString("title"));
            tv_author.setText(_prefixAuthor+jsonObjectDetail.getString("by"));
            tv_date.setText(DateProcessing.printDate(jsonObjectDetail.getLong("time"),false,DateProcessing.fDayMonthYear));
            tv_description.setText(jsonObjectDetail.getString("url"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for(int i=0;i<jsonArrayComment.length();i++){
            try {
                LayoutInflater inflater = getLayoutInflater();
                LinearLayout commentLayout = (LinearLayout) inflater.inflate(R.layout.item_list_comment, ll_comment, false);
                TextView author = commentLayout.findViewById(R.id.tv_author_item_list_comment);
                TextView date = commentLayout.findViewById(R.id.tv_date_item_list_comment);
                TextView comment = commentLayout.findViewById(R.id.tv_comment_item_list_comment);
                JSONObject jsonObject = jsonArrayComment.getJSONObject(i);
                author.setText(jsonObject.getString("by"));
                comment.setText(jsonObject.getString("text"));
                date.setText(DateProcessing.printDate(jsonObject.getLong("time"),false,DateProcessing.fDayMonthYear));
                ll_comment.addView(commentLayout);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        rl_progress.setVisibility(View.GONE);
        ll_content.setVisibility(View.VISIBLE);
    }
}
