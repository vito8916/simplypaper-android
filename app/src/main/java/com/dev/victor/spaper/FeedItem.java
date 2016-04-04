package com.dev.victor.spaper;

/**
 * Created by Victor on 21/09/2015.
 */
public class FeedItem {
    private String title;
    private String description;
    private String farm;
    private String server;
    private String id;
    private String secret;
    private String numFotos;

    public String getNumFotos(){
        return numFotos;
    }

    public void setNumFotos(String numFotos){
        this.numFotos = numFotos;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFarm() {
        return farm;
    }

    public void setFarm(String farm) {
        this.farm = farm;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
}