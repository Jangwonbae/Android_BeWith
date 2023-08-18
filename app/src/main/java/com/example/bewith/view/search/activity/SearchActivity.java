package com.example.bewith.view.search.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.bewith.R;
import com.example.bewith.databinding.ActivitySearchBinding;
import com.example.bewith.view.search.adapter.SearchLabRecyclerViewAdapter;
import com.example.bewith.view.search.adapter.SearchRecyclerViewAdapter;
import com.example.bewith.view.search.data.ClassData;
import com.example.bewith.view.search.data.ProfessorData;
import com.unity3d.player.UnityPlayer;
import com.unity3d.player.UnityPlayerActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    private ActivitySearchBinding binding;

    private ArrayList<ClassData> classDataArraylist;
    private ArrayList<ProfessorData> professorDataArrayList;

    private SearchRecyclerViewAdapter searchRecyclerViewAdapter;
    private SearchLabRecyclerViewAdapter searchLabRecyclerViewAdapter;

    private String what;

    private String fileName;
    private String arrayName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        what = intent.getStringExtra("What");
        //확인버튼 활성화
        binding.searchView.setSubmitButtonEnabled(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager( this);


        if (what.equals("class")) {
            binding.searchView.setQueryHint("강의명으로 검색");
            classDataArraylist = new ArrayList<>();
            searchRecyclerViewAdapter = new SearchRecyclerViewAdapter(classDataArraylist);
            binding.searchListView.setLayoutManager(linearLayoutManager);
            binding.searchListView.setAdapter(searchRecyclerViewAdapter);
            fileName = "jsons/classes.json";
            arrayName = "class";


        } else {
            binding.searchView.setQueryHint("교수명으로 검색");
            professorDataArrayList = new ArrayList<>();
            searchLabRecyclerViewAdapter = new SearchLabRecyclerViewAdapter(professorDataArrayList);
            binding.searchListView.setLayoutManager(linearLayoutManager);
            binding.searchListView.setAdapter(searchLabRecyclerViewAdapter);
            fileName = "jsons/professors.json";
            arrayName = "professor";
        }

        initSearchListener();
        ininListClick();
    }


    public void parseJson(String fileName, String query, String arrayName) {
        String json = "";
        try {
            if(what.equals("class")){
                classDataArraylist.clear();
            }else {
                professorDataArrayList.clear();
            }

            InputStream is = getAssets().open(fileName); // json파일 이름
            int fileSize = is.available();

            byte[] buffer = new byte[fileSize];
            is.read(buffer);
            is.close();

            //json파일명을 가져와서 String 변수에 담음
            json = new String(buffer, "UTF-8");
            JSONObject jsonObject = new JSONObject(json);

            //배열로된 자료를 가져올때
            JSONArray Array = jsonObject.getJSONArray(arrayName);//배열의 이름
            for (int i = 0; i < Array.length(); i++) {
                JSONObject Object = Array.getJSONObject(i);
                if (arrayName.equals("class")) {
                    String className = Object.getString("className");
                    if (className.contains(query.trim())) {
                        classDataArraylist.add(new ClassData(Object.getString("department"), Object.getString("grade"), Object.getString("division"),
                                Object.getString("className"), Object.getString("professor"), Object.getString("timePlace")));
                    }
                } else {
                    String professorName = Object.getString("name");
                    if (professorName.contains(query.trim())) {
                        professorDataArrayList.add(new ProfessorData(Object.getString("name"), Object.getString("department"), Object.getString("tel"),
                                Object.getString("lab"), Object.getString("E-mail")));
                    }
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(what.equals("class")){
            searchRecyclerViewAdapter.notifyDataSetChanged();
        }
        else{
            searchLabRecyclerViewAdapter.notifyDataSetChanged();
        }

    }

    public void initSearchListener() {
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {//서치뷰 리스너
            //검색버튼을 눌렀을 경우
            @Override
            public boolean onQueryTextSubmit(String query) {//완료 버튼 시 호출
                parseJson(fileName, query, arrayName);
                if(what.equals("class")){
                    if (classDataArraylist.isEmpty()) {
                        Toast.makeText(SearchActivity.this, "검색된 정보가 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    if (professorDataArrayList.isEmpty()) {
                        Toast.makeText(SearchActivity.this, "검색된 정보가 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {//텍스트가 바뀔때마다 호출
                return true;
            }
        });
    }

    public void ininListClick() {
        if(what.equals("class")){
            searchRecyclerViewAdapter.setOnItemClickListener(new SearchRecyclerViewAdapter.OnItemClickListener(){
                //동작 구현
                @Override
                public void onItemClick(View v, int pos) {
                    String where = "";
                    for (String office : getResources().getStringArray(R.array.office_array)) {
                        if (classDataArraylist.get(pos).timePlace.contains(office)) {
                            where = office;
                        }
                    }
                    showDialog(where);
                }
            });
        }
        else {
            searchLabRecyclerViewAdapter.setOnItemClickListener(new SearchLabRecyclerViewAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View v, int pos) {
                    String where = "";
                    for (String office : getResources().getStringArray(R.array.office_array)) {
                        if (professorDataArrayList.get(pos).lab.contains(office)) {
                            where = office;
                        }
                    }
                    showDialog(where);
                }
            });
        }
        
    }
    public void showDialog(String where){
        if (where.equals("")) {
            Toast.makeText(SearchActivity.this, "정보가 없거나 해당 건물로는 안내할 수 없습니다.", Toast.LENGTH_SHORT).show();
        } else {
            AlertDialog.Builder ad = new AlertDialog.Builder(SearchActivity.this);
            ad.setTitle("길안내");
            ad.setMessage(where + "(으)로 AR네이게이션을 실행합니다.");
            ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(SearchActivity.this, UnityPlayerActivity.class);
                    UnityPlayer.UnitySendMessage("ButtonManager", "StartRoute", where);
                    startActivity(intent);
                    dialog.dismiss();
                    finish();
                }
            });
            ad.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            ad.show();
        }
    }
}