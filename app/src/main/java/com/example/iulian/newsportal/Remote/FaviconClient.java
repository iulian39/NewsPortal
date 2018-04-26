package com.example.iulian.newsportal.Remote;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Iulian on 4/24/2018
 */
public class FaviconClient {
    private static Retrofit retrofit = null;

    public static Retrofit getClient(String url)
    {
        if(retrofit == null)
        {
            retrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
