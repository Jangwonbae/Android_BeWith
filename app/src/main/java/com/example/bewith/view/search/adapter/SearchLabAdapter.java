package com.example.bewith.view.search.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.bewith.R;
import com.example.bewith.view.search.data.ProfessorData;

import java.util.ArrayList;

public class SearchLabAdapter extends BaseAdapter {
    private Context ctx;
    private ArrayList<ProfessorData> data;//원본
    private String text;

    public SearchLabAdapter(Context ctx, ArrayList<ProfessorData> data){
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
            view = inflater.inflate(R.layout.search_lab_list,viewGroup,false);
        }
        TextView name = (TextView)view.findViewById(R.id.name);
        TextView department = (TextView)view.findViewById(R.id.department);
        TextView tel = (TextView)view.findViewById(R.id.tel);
        TextView lab = (TextView)view.findViewById(R.id.lab);
        TextView email = (TextView)view.findViewById(R.id.email);

        name.setText(data.get(i).name);
        department.setText("소속 : "+data.get(i).department);
        tel.setText("tel : " +data.get(i).tel);
        lab.setText("연구실 : " +data.get(i).lab);
        email.setText("E-mail : "+data.get(i).email);

        return view;
    }
}