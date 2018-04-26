package com.example.iulian.newsportal.Model;

import java.util.List;

/**
 * Created by Iulian on 4/24/2018
 */
public class WebSite {
    public String status;
    private List<Source> sources;

    public WebSite() {
    }

    public WebSite(String status, List<Source> sourceList) {
        this.status = status;
        this.sources = sourceList;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Source> getSources() {
        return sources;
    }

    public void setSources(List<Source> sources) {
        this.sources = sources;
    }




}
