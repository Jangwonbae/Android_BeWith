package com.example.bewith.util.network;

import android.os.AsyncTask;
import android.util.Log;

import com.example.bewith.view.main.data.CommentData;
import com.example.bewith.view.main.data.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetComment extends AsyncTask<String, Void, String> {
    private static final String TAG = "GetComment";
    String errorString = null;
    private String mJsonString;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if (result == null){
        }
        else {
            mJsonString = result;
            showResult();
        }

    }
    private void showResult(){

        String TAG_JSON="comment";
        String TAG_ID = "id";
        String TAG_UUID = "UUID";
        String TAG_time= "time";
        String TAG_category = "category";
        String TAG_text = "text";
        String TAG_STR_LATITUDE = "str_latitude";
        String TAG_STR_LONGITUDE ="str_longitude";

        Constants.commnentDataArrayList.clear();


        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
            for(int i=0;i<jsonArray.length();i++){

                JSONObject item = jsonArray.getJSONObject(i);
                int id = item.getInt(TAG_ID);
                String UUID = item.getString(TAG_UUID);
                int category=0;
                switch (item.getString(TAG_category)){
                    case "리뷰":
                        category = 0;
                        break;
                    case "꿀팁":
                        category = 1;
                        break;
                    case "기록":
                        category = 2;
                        break;

                }
                String time = item.getString(TAG_time);
                String text = item.getString(TAG_text);
                String str_latitude = item.getString(TAG_STR_LATITUDE);
                String str_longitude = item.getString(TAG_STR_LONGITUDE);

                Constants.commnentDataArrayList.add(new CommentData(id,UUID,time,category,text,str_latitude,str_longitude));

            }

        } catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
        }
    }

    @Override
    protected String doInBackground(String... params) {

        String serverURL = params[0];
        String postParameters = params[1];


        try {

            URL url = new URL(serverURL);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


            httpURLConnection.setReadTimeout(5000);
            httpURLConnection.setConnectTimeout(5000);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);
            httpURLConnection.connect();


            OutputStream outputStream = httpURLConnection.getOutputStream();
            outputStream.write(postParameters.getBytes("UTF-8"));
            outputStream.flush();
            outputStream.close();


            int responseStatusCode = httpURLConnection.getResponseCode();
            Log.d(TAG, "response code - " + responseStatusCode);

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
            String line;

            while((line = bufferedReader.readLine()) != null){
                sb.append(line);
            }

            bufferedReader.close();

            return sb.toString().trim();


        } catch (Exception e) {

            Log.d(TAG, "GetData : Error ", e);
            errorString = e.toString();

            return null;
        }

    }
}