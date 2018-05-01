package com.example.iulian.newsportal.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.iulian.newsportal.Common.ISO8601Parser;
import com.example.iulian.newsportal.DetailArticle;
import com.example.iulian.newsportal.Interface.ItemClickListener;
import com.example.iulian.newsportal.Model.Article;
import com.example.iulian.newsportal.R;
import com.github.curioustechizen.ago.RelativeTimeTextView;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by iulian on 4/27/2018
 */
class ListNewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    ItemClickListener itemClickListener;
    TextView article_title;
    RelativeTimeTextView article_time;
    CircleImageView article_image;

    public ListNewsViewHolder(View itemView) {
        super(itemView);
        article_image = itemView.findViewById(R.id.article_image);
        article_time = itemView.findViewById(R.id.article_time);
        article_title = itemView.findViewById(R.id.article_title);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(),false);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}

public class ListNewsAdapter  extends RecyclerView.Adapter<ListNewsViewHolder>{
    private List<Article> articleList;
    private Context context;

    public ListNewsAdapter(List<Article> articleList, Context context) {
        this.articleList = articleList;
        this.context = context;
    }

    @NonNull
    @Override
    public ListNewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView =  inflater.inflate(R.layout.news_layout,parent,false);
        return new ListNewsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ListNewsViewHolder holder, int position) {
        Picasso.get()
                .load(articleList.get(position).getUrlToImage())
                .into(holder.article_image);

        if (articleList.get(position).getTitle().length() > 65)
        {
            holder.article_title.setText(articleList.get(position).getTitle().substring(0,65) + "...");
        }
        else
            holder.article_title.setText(articleList.get(position).getTitle());

        if(articleList.get(position).getPublishedAt() != null)
        {
            Date date = null;

            try{
                date = ISO8601Parser.parse(articleList.get(position).getPublishedAt());
                holder.article_time.setReferenceTime(date.getTime());
            }catch (ParseException e)
            {
                e.printStackTrace();
            }

        }

        //Set Event click
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                Intent detail = new Intent(context,DetailArticle.class);
                detail.putExtra("webURL", articleList.get(position).getUrl());
                context.startActivity(detail);
            }
        });
    }

    @Override
    public int getItemCount() {
        return articleList != null ? articleList.size() : 0;
    }
}
