package com.example.bewith.util.network.community;


import android.os.AsyncTask;

import com.example.bewith.R;
import com.example.bewith.data.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetLike extends AsyncTask<String, Void, String> {
    String errorString = null;
    private String mJsonString;


    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if (result == null) {
        } else {
            mJsonString = result;
            showResult();
        }

    }

    private void showResult() {

        String TAG_JSON = "likes";
        String TAG_UUID = "UUID";


        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
            boolean check = false;
            int likeCount = 0;
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject item = jsonArray.getJSONObject(i);
                if (item.getString(TAG_UUID).equals(Constants.UUID)) {
                    check = true;
                }
                likeCount++;
            }
            Constants.likeData.setMyLike(check);
            Constants.likeData.setLikeCount(likeCount);


        } catch (JSONException e) {
        }
    }


    @Override
    protected String doInBackground(String... params) {

        String replyId = (String) params[1];


        String serverURL = (String) params[0];
        String postParameters = "replyId=" + replyId;

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

            InputStream inputStream;
            if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                inputStream = httpURLConnection.getInputStream();
            } else {
                inputStream = httpURLConnection.getErrorStream();
            }
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }

            bufferedReader.close();

            return sb.toString().trim();


        } catch (Exception e) {

            errorString = e.toString();

            return null;
        }

    }
}