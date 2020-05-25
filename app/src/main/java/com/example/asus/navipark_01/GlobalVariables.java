package com.example.asus.navipark_01;

import android.app.Application;

public class GlobalVariables extends Application {
    private String str;
    private int xCount;

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public int getXCount()  {
        return setCount();
    }

    private int setCount()  {
        return getSharedPreferences("xCount", MODE_PRIVATE).getInt("xCountInt", -1);
    }
}
