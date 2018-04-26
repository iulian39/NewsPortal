package com.example.iulian.newsportal.Common;

import com.example.iulian.newsportal.Interface.FaviconService;
import com.example.iulian.newsportal.Interface.NewsService;
import com.example.iulian.newsportal.Remote.FaviconClient;
import com.example.iulian.newsportal.Remote.RetrofitClient;

/**
 * Created by Iulian on 4/24/2018
 */

public class Common {
    private static final String BASE_URL = "https://newsapi.org/";
    private static final String FAVICONFINDER_URL = "https://besticon-demo.herokuapp.com";
    public static final String API_KEY = "664b959b57e84668a0c018550138afe0";

    public static NewsService getNewsService()
    {
        return RetrofitClient.getClient(BASE_URL).create(NewsService.class);
    }

    public static FaviconService getIconService()
    {
        return FaviconClient.getClient(FAVICONFINDER_URL).create(FaviconService.class);
    }
}
