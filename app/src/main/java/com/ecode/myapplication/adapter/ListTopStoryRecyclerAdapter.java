package com.ecode.myapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecode.myapplication.R;
import com.ecode.myapplication.activity.DetailStoryActivity;
import com.ecode.myapplication.helper.ItemAnimation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ListTopStoryRecyclerAdapter extends RecyclerView.Adapter<ListTopStoryRecyclerAdapter.CategoryViewHolder> {
    String _prefixComment = "Comment : ";
    String _prefixScore = "Score : ";
    private Context context;
    private JSONArray list;
    private int animation_type = 0;

    public ListTopStoryRecyclerAdapter(Context context, int animation_type, JSONArray list) {
        this.context = context;
        this.list = list;
        this.animation_type = animation_type;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemRow = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_topstory,parent,false);
        return new CategoryViewHolder(itemRow);
    }

    @Override
    public void onBindViewHolder(@NonNull final CategoryViewHolder holder, int position) {
        try {
            final JSONObject jsonObject = list.getJSONObject(position);
            final String storyId=jsonObject.getString("id");

            holder.tv_tittle.setText(jsonObject.getString("title"));
            holder.tv_score.setText(_prefixScore+jsonObject.getString("score"));
            holder.tv_comment.setText(_prefixComment+jsonObject.getString("descendants"));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, DetailStoryActivity.class);
                    intent.putExtra("storyId",storyId);
                    context.startActivity(intent);
                }
            });
            setAnimation(holder.itemView, position);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return list.length();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder{
        TextView tv_tittle;
        TextView tv_comment;
        TextView tv_score;
        public CategoryViewHolder(View itemView) {
            super(itemView);
            tv_tittle=(TextView)itemView.findViewById(R.id.tv_tittle_item_list_topstory);
            tv_comment=(TextView)itemView.findViewById(R.id.tv_comment_item_list_topstory);
            tv_score=(TextView)itemView.findViewById(R.id.tv_score_item_list_topstory);
        }
    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                on_attach = false;
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
        super.onAttachedToRecyclerView(recyclerView);
    }


    private int lastPosition = -1;
    private boolean on_attach = true;

    private void setAnimation(View view, int position) {
        if (position > lastPosition) {
            ItemAnimation.animate(view, on_attach ? position : -1, animation_type);
            lastPosition = position;
        }
    }
}