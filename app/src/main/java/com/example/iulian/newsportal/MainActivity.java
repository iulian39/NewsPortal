package com.example.iulian.newsportal;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.iulian.newsportal.Adapter.ListSourceAdapter;
import com.example.iulian.newsportal.Common.Common;
import com.example.iulian.newsportal.Interface.NewsService;
import com.example.iulian.newsportal.Model.WebSite;
import com.google.gson.Gson;

import dmax.dialog.SpotsDialog;
import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView listWebsite;
    private RecyclerView.LayoutManager layoutManager;
    private NewsService newsService;
    private ListSourceAdapter adapter;
    private SpotsDialog  dialog;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Init cache
        Paper.init(this);

        //Init service
        newsService = Common.getNewsService();

        //Init view
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadWebsiteSource(true);
            }
        });

        listWebsite = (RecyclerView)findViewById(R.id.list_source);
        listWebsite.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this );
        listWebsite.setLayoutManager(layoutManager);

        dialog = new SpotsDialog(this);
        loadWebsiteSource(false);

    }

    private void loadWebsiteSource(boolean isRefreshed)
    {
        if(!isRefreshed)
        {
            String cache = Paper.book().read("cache");
            if(cache != null && !cache.equals("null") && !cache.isEmpty()) // if the cache exists
            {
                WebSite webSite = new Gson().fromJson(cache, WebSite.class); // convert cache from json
                adapter = new ListSourceAdapter(getBaseContext(), webSite);
                adapter.notifyDataSetChanged();
                listWebsite.setAdapter(adapter);
            }
            else {
                dialog.show(); // loading dialog

                newsService.getSources().enqueue(new Callback<WebSite>() {
                    @Override
                    public void onResponse(Call<WebSite> call, Response<WebSite> response) {
                        adapter = new ListSourceAdapter(getBaseContext(), response.body());
                        adapter.notifyDataSetChanged();
                        listWebsite.setAdapter(adapter);

                        Paper.book().write("cache", new Gson().toJson(response.body())); // save to cache

                        dialog.dismiss();
                    }

                    @Override
                    public void onFailure(Call<WebSite> call, Throwable t) {

                    }
                });
            }
        }
        else // if refreshing
        {
            swipeRefreshLayout.setRefreshing(true);

            newsService.getSources().enqueue(new Callback<WebSite>() {
                @Override
                public void onResponse(Call<WebSite> call, Response<WebSite> response) {
                    adapter = new ListSourceAdapter(getBaseContext(), response.body());
                    adapter.notifyDataSetChanged();
                    listWebsite.setAdapter(adapter);

                    Paper.book().write("cache", new Gson().toJson(response.body())); // save to cache

                    swipeRefreshLayout.setRefreshing(false); // stop the refresh
                }

                @Override
                public void onFailure(Call<WebSite> call, Throwable t) {

                }
            });
        }
    }
}
