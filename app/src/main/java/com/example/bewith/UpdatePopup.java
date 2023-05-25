package com.example.bewith;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class UpdatePopup extends Activity {

    private ArrayList<String> categoryList = new ArrayList<>();
    private Spinner category;
    private TextView edit_text;
    private int _id;
    private String contents;
    private int categoryNum;
    private String categoryText;
    private static String IP_ADDRESS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IP_ADDRESS= "221.147.144.65:80";
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_update_popup);
        edit_text = findViewById(R.id.edit_text);
        Intent data = getIntent();
        _id = data.getIntExtra("id",-1);
        contents = data.getStringExtra("text");
        categoryNum = data.getIntExtra("category",-1);
        edit_text.setText(contents);
        createCategorySpinner();//카테고리 스피너 생성
    }
    public void createCategorySpinner(){//카테고리 스피너 생성
        category = (Spinner)findViewById(R.id.category);//스피너 선언
        categoryList.add("리뷰");
        categoryList.add("꿀팁");
        categoryList.add("기록");
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoryList);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category.setAdapter(adapter1);
        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        categoryText = "리뷰";
                        break;
                    case 1:
                        categoryText = "꿀팁";
                        break;
                    case 2:
                        categoryText = "기록";
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        category.setSelection(categoryNum);//초기값
    }

    //확인 버튼 클릭
    public void mOnClose(View v){
        int id = v.getId();
        switch (id) {
            case R.id.okBtn:
                contents = edit_text.getText().toString().trim();//텍스트 내용
                if(contents.equals("")){
                    Toast.makeText(UpdatePopup.this,"내용을 입력하세요.",Toast.LENGTH_SHORT).show();
                }
                else{
                    ModifyComment modifyComment = new ModifyComment();//내 comment 내용 서버에 전송
                    modifyComment.execute("http://" + IP_ADDRESS + "/updateComment.php",Integer.toString(_id),categoryText,contents);//서버에 전송
                }
                break;
            case R.id.noBtn:
                finish();
                break;
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }
    public class ModifyComment extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {//작업 시작할때
            super.onPreExecute();

            progressDialog = ProgressDialog.show(UpdatePopup.this,"Please Wait", null, true, true);

        }


        @Override
        protected void onPostExecute(String result) {//작업 종료할떄
            super.onPostExecute(result);

            progressDialog.dismiss();
            //갱신메소드
            setResult(RESULT_OK);
            finish();
        }


        @Override
        protected String doInBackground(String... params) {

            String id = (String)params[1];
            String category = (String)params[2];
            String text = (String)params[3];

            String serverURL = (String)params[0];
            String postParameters = "id=" + id + "&category=" + category + "&text=" + text;

            try {

                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.connect();


                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();


                int responseStatusCode = httpURLConnection.getResponseCode();

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line = null;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }


                bufferedReader.close();
                return sb.toString();


            } catch (Exception e) {
                return new String("Error: " + e.getMessage());
            }

        }
    }
}