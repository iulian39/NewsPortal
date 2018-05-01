package com.example.iulian.newsportal;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.iulian.newsportal.Adapter.ListNewsAdapter;
import com.example.iulian.newsportal.Common.Common;
import com.example.iulian.newsportal.Interface.NewsService;
import com.example.iulian.newsportal.Model.Article;
import com.example.iulian.newsportal.Model.News;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.github.florent37.diagonallayout.DiagonalLayout;
import com.squareup.picasso.Picasso;

import java.util.List;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListNews extends AppCompatActivity {
    KenBurnsView kenBurnsView;
    DiagonalLayout diagonalLayout;
    SpotsDialog dialog;
    NewsService mService;
    TextView top_author, top_title;
    SwipeRefreshLayout swipeRefreshLayout;

    String source = "", webHotUrlNews = "";

    ListNewsAdapter adapter;
    RecyclerView lstNews;
    RecyclerView.LayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_news);

        mService = Common.getNewsService();

        dialog = new SpotsDialog(this);

        //View
        swipeRefreshLayout = findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadNews(source,true);
            }
        });

        diagonalLayout = findViewById(R.id.diagonalLayout);
        diagonalLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Click to hot/latest news

                Intent detail = new Intent(getBaseContext(),DetailArticle.class);
                detail.putExtra("webURL", webHotUrlNews);
                startActivity(detail);
            }
        });
        kenBurnsView = findViewById(R.id.top_image);
        top_author = findViewById(R.id.top_author);
        top_title = findViewById(R.id.top_title);

        lstNews = findViewById(R.id.lstNews);
        lstNews.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        lstNews.setLayoutManager(layoutManager);

        //Intent
        if(getIntent() != null)
        {
            source = getIntent().getStringExtra("source");
            if(!source.isEmpty())
            {
                loadNews(source, false);
            }
        }

    }

    public boolean isOnline()
    {

        ConnectivityManager cm = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }

    private void loadNews(String source, boolean isRefreshed) {
        if(!isOnline())
            finish();
        if(!isRefreshed)
        {
            dialog.show();
            swipeRefreshLayout.setRefreshing(false);
            mService.getNewestArticles(Common.getNewsUrl(source)).enqueue(new Callback<News>() {
                @Override
                public void onResponse(Call<News> call, Response<News> response) {
                    dialog.dismiss();
                    //get first article
                    try{
                        Picasso.get()
                                .load(response.body().getArticles().get(0).getUrlToImage())
                                .into(kenBurnsView);

                        top_title.setText(response.body().getArticles().get(0).getTitle());
                        top_author.setText(response.body().getArticles().get(0).getAuthor());

                        webHotUrlNews = response.body().getArticles().get(0).getUrl();

                        List<Article> articles = response.body().getArticles();
                        articles.remove(0); // we already put the first article
                        adapter = new ListNewsAdapter(articles,getBaseContext());
                        adapter.notifyDataSetChanged();
                        lstNews.setAdapter(adapter);
                    }catch (NullPointerException e)
                    {
                        e.printStackTrace();
                        finish();
                    }



                }

                @Override
                public void onFailure(Call<News> call, Throwable t) {

                }
            });
        }
        else
        {
            swipeRefreshLayout.setRefreshing(true);
            mService.getNewestArticles(Common.getNewsUrl(source)).enqueue(new Callback<News>() {
                @Override
                public void onResponse(Call<News> call, Response<News> response) {
                    //get first article
                    try{
                        Picasso.get()
                                .load(response.body().getArticles().get(0).getUrlToImage())
                                .into(kenBurnsView);

                        top_title.setText(response.body().getArticles().get(0).getTitle());
                        top_author.setText(response.body().getArticles().get(0).getAuthor());

                        webHotUrlNews = response.body().getArticles().get(0).getUrl();

                        List<Article> articles = response.body().getArticles();
                        articles.remove(0); // we already put the first article
                        adapter = new ListNewsAdapter(articles,getBaseContext());
                        adapter.notifyDataSetChanged();
                        lstNews.setAdapter(adapter);
                    }catch (NullPointerException e)
                    {
                        e.printStackTrace();
                        finish();
                    }


                }

                @Override
                public void onFailure(Call<News> call, Throwable t) {

                }
            });
            swipeRefreshLayout.setRefreshing(false);
        }
    }
}
