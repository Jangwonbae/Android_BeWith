package com.example.bewith.view.search.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.bewith.R;
import com.example.bewith.databinding.ActivitySearchBinding;
import com.example.bewith.view.search.data.ClassData;
import com.example.bewith.view.search.data.ProfessorData;
import com.example.bewith.view.search.adapter.SearchAdapter;
import com.example.bewith.view.search.adapter.SearchLabAdapter;
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
    private SearchLabAdapter searchLabAdapter;
    private SearchAdapter searchAdapter;
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


        if (what.equals("class")) {
            binding.searchView.setQueryHint("강의명으로 검색");
            classDataArraylist = new ArrayList<>();
            searchAdapter = new SearchAdapter(getBaseContext(), classDataArraylist);
            binding.searchListView.setAdapter(searchAdapter);
            fileName = "jsons/classes.json";
            arrayName = "class";


        } else {
            binding.searchView.setQueryHint("교수명으로 검색");
            professorDataArrayList = new ArrayList<>();
            searchLabAdapter = new SearchLabAdapter(getBaseContext(), professorDataArrayList);
            binding.searchListView.setAdapter(searchLabAdapter);
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
            searchAdapter.notifyDataSetChanged();
        }
        else{
            searchLabAdapter.notifyDataSetChanged();
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
        binding.searchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {//리스트뷰 클릭 리스너
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String where = "";
                if(what.equals("class")){
                    for (String office : getResources().getStringArray(R.array.office_array)) {
                        if (classDataArraylist.get(position).timePlace.contains(office)) {
                            where = office;
                        }
                    }
                }else{
                    for (String office : getResources().getStringArray(R.array.office_array)) {
                        if (professorDataArrayList.get(position).lab.contains(office)) {
                            where = office;
                        }
                    }
                }


                if (where.equals("")) {
                    Toast.makeText(SearchActivity.this, "정보가 없거나 해당 건물로는 안내할 수 없습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    AlertDialog.Builder ad = new AlertDialog.Builder(SearchActivity.this);
                    ad.setTitle("길안내");
                    ad.setMessage(where + "(으)로 AR네이게이션을 실행합니다.");
                    String finalWhere = where;
                    ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(SearchActivity.this, UnityPlayerActivity.class);
                            UnityPlayer.UnitySendMessage("ButtonManager", "StartRoute", finalWhere);
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
        });
    }
}