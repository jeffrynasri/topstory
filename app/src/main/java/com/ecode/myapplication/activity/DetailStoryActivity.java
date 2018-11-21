package com.ecode.myapplication.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecode.myapplication.R;

public class DetailStoryActivity extends AppCompatActivity {
    //HELPER
    String _prefixAuthor = "By : ";
    Long id;

    //KONTEN
    ImageView iv_favorite;
    TextView tv_tittle;
    TextView tv_author;
    TextView tv_date;
    TextView tv_description;
    LinearLayout ll_comment;
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
    }


}
