package com.example.iulian.newsportal.Interface;
import com.example.iulian.newsportal.Common.Common;
import com.example.iulian.newsportal.Model.WebSite;

import retrofit2.Call;
import retrofit2.http.GET;


/**
 * Created by Iulian on 4/24/2018
 */

public interface NewsService {
    @GET("v2/sources?language=en&apiKey=" + Common.API_KEY)
    Call<WebSite> getSources();
}
