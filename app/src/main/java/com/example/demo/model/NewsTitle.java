package com.example.demo.model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class NewsTitle {
    private String title;
    private String source;
    private String newsDate;
    private String url;
    private String imgUrl;
    public NewsTitle(){
    };
    public NewsTitle(String title, String source, String newsDate,String url,String imgUrl){
        this.title = title;
        this.source = source;
        this.newsDate = newsDate;
        this.url=url;
        this.imgUrl=imgUrl;
    }
    public String getTitle(){
        return this.title;
    }
    public String getSource(){
        return this.source;
    }
    public String getNewsdate(){
        return this.newsDate;
    }
    public String getUrl(){
        return this.url;
    }
    public String getImgUrl(){
        return this.imgUrl;
    }
    public void setTitle(String title){
        this.title = title;
    }
    public void setSource(String src){
        this.source = src;
    }
    public void setNewsdate(String newsDate){
        this.newsDate = newsDate;
    }
    public void setUrl(String url){
        this.url = url;
    }
    public void setImgUrl(String imgUrl){
        this.imgUrl = imgUrl;
    }
    /**
     * 解析返回Json数据的方法
     */
    static public ArrayList<NewsTitle> parseNewsTitleList(String content) throws Exception {
        ArrayList<NewsTitle> newsList = new ArrayList<>();
        JSONArray array = new JSONArray(content);
        for (int i = 0; i < array.length(); i++) {
            JSONObject results = (JSONObject) array.get(i);
            NewsTitle nt = new NewsTitle();
            nt.setTitle(results.getString("title"));
            nt.setSource(results.getString("from_media") + " " + results.getString("author"));
            nt.setNewsdate(results.getString("pub_date"));
            nt.setUrl(results.getString("url"));
            nt.setImgUrl(results.getString("img_url"));
            newsList.add(nt);
        }
        return newsList;
    }
}