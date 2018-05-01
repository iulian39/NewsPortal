package com.example.iulian.newsportal.Model;

import java.util.List;

/**
 * Created by iulian on 4/27/2018
 */
public class News {
    private String status;
    private String source;
    private List<Article> articles;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }


    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }
}
