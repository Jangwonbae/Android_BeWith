package com.example.bewith;

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

import com.example.bewith.listclass.ClassData;
import com.example.bewith.listclass.ProfessorData;
import com.example.bewith.listclass.SearchAdapter;
import com.example.bewith.listclass.SearchLabAdapter;
import com.unity3d.player.UnityPlayer;
import com.unity3d.player.UnityPlayerActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    private SearchView searchView;
    private ListView search_list;
    private String what;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Intent intent = getIntent();
        what = intent.getStringExtra("What");

        searchView = (SearchView) findViewById(R.id.searchView);
        search_list = (ListView)findViewById(R.id.search_list);
        searchView.setSubmitButtonEnabled(true);//확인버튼 활성화

        if(what.equals("class")){
            searchView.setQueryHint("강의명으로 검색");
            SearchAdapter mAdapter;
            ArrayList<ClassData> mData=new ArrayList<>();
            mAdapter = new SearchAdapter(getApplicationContext(), mData);
            search_list.setAdapter(mAdapter);
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {//서치뷰 리스너
                //검색버튼을 눌렀을 경우
                @Override
                public boolean onQueryTextSubmit(String query) {
                    mData.clear();
                    //json 자료 가져오기
                    String json = "";
                    try {
                        InputStream is = getAssets().open("jsons/classes.json"); // json파일 이름
                        int fileSize = is.available();

                        byte[] buffer = new byte[fileSize];
                        is.read(buffer);
                        is.close();

                        //json파일명을 가져와서 String 변수에 담음
                        json = new String(buffer, "UTF-8");
                        JSONObject jsonObject = new JSONObject(json);

                        //배열로된 자료를 가져올때
                        JSONArray Array = jsonObject.getJSONArray("class");//배열의 이름
                        for(int i=0; i<Array.length(); i++)
                        {
                            JSONObject Object = Array.getJSONObject(i);
                            String className =   Object.getString("className");
                            if(className.contains(query.trim())){
                                mData.add(new ClassData(Object.getString("department"),Object.getString("grade"),Object.getString("division"),
                                        Object.getString("className"),Object.getString("professor"),Object.getString("timePlace")));
                            }
                        }
                        if(mData.isEmpty()){
                            Toast.makeText(SearchActivity.this, "검색된 정보가 없습니다.", Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException ex) { ex.printStackTrace(); }
                    catch (JSONException e) { e.printStackTrace(); }
                    mAdapter.notifyDataSetChanged();
                    return true;
                }
                //텍스트가 바뀔때마다 호출
                @Override
                public boolean onQueryTextChange(String newText) {
                    return true;
                }
            });

            search_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {//리스트뷰 클릭 리스너
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String where;
                    if(mData.get(position).timePlace.contains("이공관")){
                        where = "이공관";
                    }else if(mData.get(position).timePlace.contains("경영관")){
                        where = "경영관";
                    }else if(mData.get(position).timePlace.contains("교양관")){
                        where = "교양관";
                    }else if(mData.get(position).timePlace.contains("인문사회관")){
                        where = "인문사회관";
                    }else if(mData.get(position).timePlace.contains("예술관")){
                        where = "예술관";
                    }else if(mData.get(position).timePlace.contains("예술대실습관")){
                        where = "예술대실습관";
                    }else if(mData.get(position).timePlace.contains("웨슬리관")) {
                        where = "웨슬리관";
                    }else{
                        where = "";
                    }
                    if(where.equals("")){
                        Toast.makeText(SearchActivity.this, "정보가 없거나 해당 건물로는 안내할 수 없습니다.", Toast.LENGTH_SHORT).show();
                    }else{
                        AlertDialog.Builder ad = new AlertDialog.Builder(SearchActivity.this);
                        ad.setTitle("길안내");
                        ad.setMessage(where +"(으)로 AR네이게이션을 실행합니다.");
                        ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(SearchActivity.this, UnityPlayerActivity.class);
                                UnityPlayer.UnitySendMessage("ButtonManager", "StartRoute",where);
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
        }else{
            searchView.setQueryHint("교수명으로 검색");
            SearchLabAdapter mAdapter;
            ArrayList<ProfessorData> mData=new ArrayList<>();
            mAdapter = new SearchLabAdapter(getApplicationContext(), mData);

            search_list.setAdapter(mAdapter);
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {//서치뷰 리스너
                //검색버튼을 눌렀을 경우
                @Override
                public boolean onQueryTextSubmit(String query) {
                    mData.clear();
                    //json 자료 가져오기
                    String json = "";
                    try {
                        InputStream is = getAssets().open("jsons/professors.json"); // json파일 이름
                        int fileSize = is.available();

                        byte[] buffer = new byte[fileSize];
                        is.read(buffer);
                        is.close();

                        //json파일명을 가져와서 String 변수에 담음
                        json = new String(buffer, "UTF-8");
                        JSONObject jsonObject = new JSONObject(json);

                        //배열로된 자료를 가져올때
                        JSONArray Array = jsonObject.getJSONArray("professor");//배열의 이름
                        for(int i=0; i<Array.length(); i++)
                        {
                            JSONObject Object = Array.getJSONObject(i);
                            String professorName =   Object.getString("name");
                            if(professorName.contains(query.trim())){
                                mData.add(new ProfessorData(Object.getString("name"),Object.getString("department"),Object.getString("tel"),
                                        Object.getString("lab"),Object.getString("E-mail")));
                            }
                        }
                        if(mData.isEmpty()){
                            Toast.makeText(SearchActivity.this, "검색된 정보가 없습니다.", Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException ex) { ex.printStackTrace(); }
                    catch (JSONException e) { e.printStackTrace(); }
                    mAdapter.notifyDataSetChanged();
                    return true;
                }
                //텍스트가 바뀔때마다 호출
                @Override
                public boolean onQueryTextChange(String newText) {
                    return true;
                }
            });

            search_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {//리스트뷰 클릭 리스너
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String where;
                    if(mData.get(position).lab.contains("이공관")){
                        where = "이공관";
                    }else if(mData.get(position).lab.contains("경영관")){
                        where = "경영관";
                    }else if(mData.get(position).lab.contains("교양관")){
                        where = "교양관";
                    }else if(mData.get(position).lab.contains("인문사회관")){
                        where = "인문사회관";
                    }else if(mData.get(position).lab.contains("예술관")){
                        where = "예술관";
                    }else if(mData.get(position).lab.contains("예술대실습관")){
                        where = "예술대실습관";
                    }else if(mData.get(position).lab.contains("웨슬리관")) {
                        where = "웨슬리관";
                    }else{
                        where = "";
                    }
                    if(where.equals("")){
                        Toast.makeText(SearchActivity.this, "정보가 없거나 해당 건물로는 안내할 수 없습니다.", Toast.LENGTH_SHORT).show();
                    }else{
                        AlertDialog.Builder ad = new AlertDialog.Builder(SearchActivity.this);
                        ad.setTitle("길안내");
                        ad.setMessage(where +"(으)로 AR네이게이션을 실행합니다.");
                        ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(SearchActivity.this, UnityPlayerActivity.class);
                                UnityPlayer.UnitySendMessage("ButtonManager", "StartRoute",where);
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

}