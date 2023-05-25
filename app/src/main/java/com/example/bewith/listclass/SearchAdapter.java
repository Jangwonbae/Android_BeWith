package com.example.bewith.listclass;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.bewith.R;

import java.util.ArrayList;

public class SearchAdapter extends BaseAdapter {
    private Context ctx;
    private ArrayList<ClassData> data;//원본
    private String text;

    public SearchAdapter(Context ctx,ArrayList<ClassData> data){
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
            view = inflater.inflate(R.layout.search_list,viewGroup,false);
        }
        TextView department = (TextView)view.findViewById(R.id.department);
        TextView grade = (TextView)view.findViewById(R.id.grade);
        TextView division = (TextView)view.findViewById(R.id.division);
        TextView className = (TextView)view.findViewById(R.id.className);
        TextView professor = (TextView)view.findViewById(R.id.professor);
        TextView timePlace = (TextView)view.findViewById(R.id.timePlace);

        department.setText(data.get(i).department);
        grade.setText(data.get(i).grade);
        division.setText(data.get(i).division);
        className.setText(data.get(i).className);
        professor.setText(data.get(i).professor);
        timePlace.setText(data.get(i).timePlace);

        return view;
    }
}