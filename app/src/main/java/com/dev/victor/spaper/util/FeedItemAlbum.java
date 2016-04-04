package com.dev.victor.spaper.util;

import com.android.volley.Request;

/**
 * Created by Victor on 29/09/2015.
 */
public class FeedItemAlbum {
    private String farm;
    private String id;
    private String secret;
    private String server;
    private String titulo;

    private String ulrAlbumPhotolist;

    public String getFarm() {
        return farm;
    }

    public void setFarm(String farm) {
        this.farm = farm;
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

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }



    public String getUlrAlbumPhotolist() {
        return ulrAlbumPhotolist;
    }

    public void setUlrAlbumPhotolist(String ulrAlbumPhotolist) {
        this.ulrAlbumPhotolist = ulrAlbumPhotolist;
    }
}
