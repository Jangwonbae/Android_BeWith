package com.example.bewith.listclass;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.bewith.R;
import com.example.bewith.view.community.data.ReplyData;

import java.util.ArrayList;

public class ReplyAdapter extends BaseAdapter {
    private Context ctx;
    private ArrayList<ReplyData> data;//원본
    private String mainUUID;
    private String myUUID;
    private ArrayList<String> UUIDArray=new ArrayList<>();

    public ReplyAdapter(Context ctx, ArrayList<ReplyData> data,String mainUUID,String myUUID){
        this.ctx=ctx;
        this.data=data;
        this.mainUUID=mainUUID;
        this.myUUID=myUUID;

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
            view = inflater.inflate(R.layout.reply_list,viewGroup,false);
        }

        TextView text1 = (TextView)view.findViewById(R.id.who);
        TextView text2 = (TextView)view.findViewById(R.id.contents);
        TextView text3 = (TextView)view.findViewById(R.id.time);
        if(data.get(i).ReplyUUID.equals(myUUID)){
            text1.setTextColor(Color.parseColor("#1E90FF"));
        }
        else{
            text1.setTextColor(Color.parseColor("#595959"));
        }
        if(data.get(i).ReplyUUID.equals(mainUUID)){
            text1.setText((data.get(i).nickname+"(작성자)"));
        }else{
            text1.setText(data.get(i).nickname);
        }
        text2.setText(data.get(i).ReplyText);
        text3.setText(data.get(i).ReplyTime);

        return view;
    }
}