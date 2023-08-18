package com.example.bewith.view.main.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.bewith.R;
import com.example.bewith.view.main.data.CommentData;

import java.util.ArrayList;

public class MyAdapter extends BaseAdapter {
    private Context ctx;
    private ArrayList<CommentData> data;//원본
    private String text;

    public MyAdapter(Context ctx,ArrayList<CommentData> data){
        this.ctx=ctx;
        this.data=data;
    }
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view ==null){
            LayoutInflater inflater = LayoutInflater.from(ctx);
            view = inflater.inflate(R.layout.comment_list,viewGroup,false);
        }

        TextView text1 = view.findViewById(R.id.category_text_view);
        TextView text2 = view.findViewById(R.id.contents_text_view);

        text1.setText(data.get(i).category);
        text2.setText(data.get(i).text);

        return view;
    }
}