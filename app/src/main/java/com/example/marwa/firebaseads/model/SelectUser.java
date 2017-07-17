package com.example.marwa.firebaseads.model;

import android.graphics.Bitmap;

/**
 * Created by Marwa on 7/15/2017.
 */

public class SelectUser {
    String name;
    Bitmap thumb;
    String phone;

    public Bitmap getThumb() {
        return thumb;
    }

    public void setThumb(Bitmap thumb) {
        this.thumb = thumb;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}


