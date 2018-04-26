package com.example.iulian.newsportal.Interface;

import com.example.iulian.newsportal.Model.Favicon;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by Iulian on 4/24/2018
 */
public interface FaviconService {
    @GET
    Call<Favicon> getIconUrl(@Url String url);
}
