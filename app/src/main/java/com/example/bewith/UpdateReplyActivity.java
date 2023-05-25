package com.example.bewith;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class UpdateReplyActivity extends AppCompatActivity {
    private EditText editText;
    private String id_;
    private String text;
    private static String IP_ADDRESS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_reply);
        Intent intent = getIntent();
        id_=intent.getStringExtra("id");
        text=intent.getStringExtra("text");
        IP_ADDRESS= "221.147.144.65:80";

        editText=findViewById(R.id.edit_text);
        editText.setText(text);


    }
    public void mOnClose(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.okBtn://확인버튼 클릭시
                text = editText.getText().toString().trim();//텍스트 내용
                if (text.equals("")) {
                    Toast.makeText(UpdateReplyActivity.this, "내용을 입력하세요.", Toast.LENGTH_SHORT).show();
                } else {

                    ModifyReply modifyReply = new ModifyReply();//내 comment 내용 서버에 전송
                    modifyReply.execute("http://" + IP_ADDRESS + "/updateReply.php",id_,editText.getText().toString().trim());//서버에 전송
                }
                break;
            case R.id.noBtn:
                finish();
                break;
        }
    }
    public class ModifyReply extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {//작업 시작할때
            super.onPreExecute();

            progressDialog = ProgressDialog.show(UpdateReplyActivity.this,"Please Wait", null, true, true);

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
            String text = (String)params[2];

            String serverURL = (String)params[0];
            String postParameters = "id=" + id  + "&text=" + text;

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