package com.example.bewith.javaclass;

import android.app.Application;

import com.example.bewith.view.main.data.CommentData;

import java.util.ArrayList;

public class GlobalList extends Application {

    public ArrayList<CommentData> getcData() {
        return cData;
    }

    public void setcData( CommentData commentData ) {
        cData.add(commentData);
    }

}
