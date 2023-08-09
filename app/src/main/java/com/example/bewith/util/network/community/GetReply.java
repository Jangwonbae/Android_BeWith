package com.example.bewith.util.network.community;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.example.bewith.data.Constants;
import com.example.bewith.view.community.activity.CommunityActivity;
import com.example.bewith.view.community.activity.CommunityActivityViewModel;
import com.example.bewith.view.community.data.ReplyData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetReply extends AsyncTask<String, Void, String> {
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

        String TAG_JSON = "reply";
        String TAG_ID = "id";
        String TAG_ReplyID = "replyId";
        String TAG_UUID = "UUID";
        String TAG_time = "time";
        String TAG_nickname = "nickname";
        String TAG_text = "text";


        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
            Constants.replyDataArrayList.clear();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject item = jsonArray.getJSONObject(i);
                Constants.replyDataArrayList.add(new ReplyData(item.getInt(TAG_ID), item.getInt(TAG_ReplyID), item.getString(TAG_UUID), item.getString(TAG_time),
                        item.getString(TAG_nickname), item.getString(TAG_text)));

            }



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