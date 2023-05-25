package com.example.bewith;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.MenuInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bewith.javaclass.ExampleBottomSheetDialog;
import com.example.bewith.listclass.ReplyAdapter;
import com.example.bewith.listclass.ReplyData;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.unity3d.player.UnityPlayer;
import com.unity3d.player.UnityPlayerActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.UUID;

public class CommunityActivity extends AppCompatActivity implements ExampleBottomSheetDialog.BottomSheetListener {
    private ListView replyList;
    private ReplyAdapter mAdapter;
    private ArrayList<ReplyData> mData = new ArrayList<>();
    private TextView replyCount;
    private TextView likeCount;
    private TextView mainTime;
    private TextView mainNickname;
    private TextView mainText;
    private TextView replyNickname;
    private EditText editText;
    private ImageView submit_btn;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ActivityResultLauncher<Intent> activityResultLauncher;

    private String mainId;
    private String mainUUID;
    private String myUUID;
    private String nickname;
    private int int_likeCount;
    private int int_replyCount;
    private static String IP_ADDRESS;
    private ImageView like_img;
    private boolean likeState;
    private int selectIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);
        IP_ADDRESS = "221.147.144.65:80";
        replyCount = findViewById(R.id.replyCount);
        mainNickname = findViewById(R.id.mainNickname);
        mainTime = findViewById(R.id.mainTime);
        mainText = findViewById(R.id.mainText);
        like_img = findViewById(R.id.like_img);
        likeCount = findViewById(R.id.likeCount);
        replyNickname = findViewById(R.id.replyNickname);
        submit_btn = findViewById(R.id.submit_btn);
        editText = findViewById(R.id.replyEditText);
        swipeRefreshLayout = findViewById(R.id.swiperefreshlayout);

        //유니티에서 인텐트로 받아서 세팅하기
        Intent intent = getIntent();
        //시간 , 내용, 게시물uuid, 게시물id, 사용자uuid
        mainTime.setText(intent.getStringExtra("mainTime"));
        //닉네임
        nickname = intent.getStringExtra("nickname");
        mainNickname.setText(nickname);
        //내용
        mainText.setText(intent.getStringExtra("mainText"));
        //게시물 UUID
        mainUUID = intent.getStringExtra("mainUUID");
        //나의 UUID
        myUUID = intent.getStringExtra("myUUID");
        //게시물 ID
        mainId = intent.getStringExtra("mainId");
        SharedPreferences prefs = getSharedPreferences("person_name", 0);
        replyNickname.setText(prefs.getString("name", ""));
        replyList = (ListView) findViewById(R.id.list);


        //좋아요 표시하기, 좋아요 수 표시
        GetLike getLike = new GetLike();
        getLike.execute("http://" + IP_ADDRESS + "/getLike.php", mainId);

        mAdapter = new ReplyAdapter(getApplicationContext(), mData, mainUUID, myUUID);
        replyList.setAdapter(mAdapter);

        //처음에는 에디트텍스트에 아무것도 없기 때문에 제출 버튼 감추기
        submit_btn.setVisibility(View.INVISIBLE);

        editText.setImeOptions(EditorInfo.IME_ACTION_DONE); //키보드 다음 버튼을 완료 버튼으로 바꿔줌

        swipeRefreshLayout.setOnRefreshListener(//리스트 아래로 땡겼을 때
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {

                        //댓글 받아서 넣기(서버에서 데이터 받아서 넣기)
                        GetReply getReply = new GetReply();
                        getReply.execute("http://" + IP_ADDRESS + "/getReply.php", mainId);

                        swipeRefreshLayout.setRefreshing(false);
                    }
                }
        );
        replyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {//리스트 클릭 리스너
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (mData.get(i).ReplyUUID.equals(myUUID)) {
                    ExampleBottomSheetDialog bottomSheetDialog = new ExampleBottomSheetDialog();
                    bottomSheetDialog.show(getSupportFragmentManager(), "exampleBottomSheet");
                    selectIndex = i;
                }

            }
        });

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override//완료 버튼 클릭 리스너
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {//키보드에 완료버튼을 누른 후 수행할 것
                    if (v.getText().toString().trim().equals("")) {
                        Toast.makeText(CommunityActivity.this, "댓글을 입력하세요.", Toast.LENGTH_SHORT).show();
                    } else {
                        SharedPreferences prefs = getSharedPreferences("person_name", 0);
                        String name = prefs.getString("name", "");
                        //서버에 댓글 정보 추가
                        AddReply addReply = new AddReply();//
                        addReply.execute("http://" + IP_ADDRESS + "/addReply.php", myUUID, mainId, name,
                                v.getText().toString().trim());//서버에 전송
                        editText.setText("");
                        //키보드 내리기
                        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                    }

                    return true;
                }
                return false;
            }
        });

        editText.addTextChangedListener(new TextWatcher() {//에디트텍스트 텍스트 변경 리스너
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (editText.getText().toString().equals("")) {
                    submit_btn.setVisibility(View.INVISIBLE);
                } else {
                    submit_btn.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                //텍스트 입력이 모두 끝았을때 Call back
            }

            @Override
            public void afterTextChanged(Editable s) {

                //텍스트가 입력하기 전에 Call back
            }
        });


        //댓글 수정하기 갔다왔을 때 실행
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                GetReply getReply = new GetReply();
                getReply.execute("http://" + IP_ADDRESS + "/getReply.php", mainId);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        //댓글 받아서 넣기(서버에서 데이터 받아서 넣기), 댓글 수 표시
        GetReply getReply = new GetReply();
        getReply.execute("http://" + IP_ADDRESS + "/getReply.php", mainId);
    }

    public void mOnClick(View v) {
        switch (v.getId()) {
            case R.id.like_img:
                if (likeState) {//좋아요 표시상태에서 버튼을 누르면
                    like_img.setImageResource(R.drawable.likesoff);//OFF로 변경
                    int_likeCount--;
                    likeCount.setText(int_likeCount + "");
                    likeState = false;
                    ControlLike controlLike = new ControlLike();//
                    controlLike.execute("http://" + IP_ADDRESS + "/deleteLike.php", mainId, myUUID);//서버에 전송
                } else {//좋아요가 안눌러진 상태에서 버튼을 누르면
                    like_img.setImageResource(R.drawable.likeson);//ON으로 변경
                    int_likeCount++;
                    likeCount.setText(int_likeCount + "");
                    likeState = true;
                    ControlLike controlLike = new ControlLike();
                    controlLike.execute("http://" + IP_ADDRESS + "/addLike.php", mainId, myUUID);//서버에 전송
                }
                break;
            case R.id.submit_btn:
                if (editText.getText().toString().trim().equals("")) {//빈칸 일때
                    Toast.makeText(CommunityActivity.this, "댓글을 입력하세요.", Toast.LENGTH_SHORT).show();
                } else {
                    SharedPreferences prefs = getSharedPreferences("person_name", 0);
                    String name = prefs.getString("name", "");
                    AddReply addReply = new AddReply();//댓글정보 서버에 전송
                    addReply.execute("http://" + IP_ADDRESS + "/addReply.php", myUUID, mainId, name,
                            editText.getText().toString().trim());
                    editText.setText("");
                    //키보드 내리기
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                }
                break;
        }
    }

    @Override
    public void onButtonClicked(int position) {
        switch (position) {
            case 0://수정
                Intent intent = new Intent(CommunityActivity.this, UpdateReplyActivity.class);
                intent.putExtra("id", Integer.toString(mData.get(selectIndex).id));
                intent.putExtra("text", mData.get(selectIndex).ReplyText);
                activityResultLauncher.launch(intent);
                Toast.makeText(CommunityActivity.this, "수정", Toast.LENGTH_SHORT).show();
                break;
            case 1://삭제
                AlertDialog.Builder ad = new AlertDialog.Builder(CommunityActivity.this);
                ad.setTitle("삭제");
                ad.setMessage("해당 댓글을 삭제합니다.");
                ad.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                ad.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DeleteReply deleteReply = new DeleteReply();//내 comment 내용 서버에 전송
                        deleteReply.execute("http://" + IP_ADDRESS + "/deleteReply.php", Integer.toString(mData.get(selectIndex).id));//서버에 전송
                        dialog.dismiss();
                    }
                });
                ad.show();
                break;
        }
    }

    public class AddReply extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {//작업 시작할때
            super.onPreExecute();
            progressDialog = ProgressDialog.show(CommunityActivity.this, "Please Wait", null, true, true);

        }

        @Override
        protected void onPostExecute(String result) {//작업 종료할떄
            super.onPostExecute(result);
            GetReply getReply = new GetReply();
            getReply.execute("http://" + IP_ADDRESS + "/getReply.php", mainId);
            progressDialog.dismiss();
        }


        @Override
        protected String doInBackground(String... params) {

            String UUID = (String) params[1];
            String replyId = (String) params[2];
            String name = (String) params[3];
            String text = (String) params[4];


            String serverURL = (String) params[0];
            String postParameters = "UUID=" + UUID + "&replyId=" + replyId + "&nickname=" + name + "&text=" + text;


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
                if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                } else {
                    inputStream = httpURLConnection.getErrorStream();
                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line = null;

                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }


                bufferedReader.close();


                return sb.toString();


            } catch (Exception e) {


                return new String("Error: " + e.getMessage());
            }

        }
    }

    public class GetReply extends AsyncTask<String, Void, String> {
        String errorString = null;
        private String mJsonString;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(CommunityActivity.this, "Please Wait", null, true, true);
        }

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
                mData.clear();
                int_replyCount = 0;
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject item = jsonArray.getJSONObject(i);
                    mData.add(new ReplyData(item.getInt(TAG_ID), item.getInt(TAG_ReplyID), item.getString(TAG_UUID), item.getString(TAG_time),
                            item.getString(TAG_nickname), item.getString(TAG_text)));
                    int_replyCount++;
                }
                mAdapter.notifyDataSetChanged();
                replyCount.setText(int_replyCount + "");
                progressDialog.dismiss();
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

    public class GetLike extends AsyncTask<String, Void, String> {
        String errorString = null;
        private String mJsonString;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //progressDialog = ProgressDialog.show(CommunityActivity.this,"Please Wait", null, true, true);
        }

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
                int_likeCount = 0;
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject item = jsonArray.getJSONObject(i);

                    if (item.getString(TAG_UUID).equals(myUUID)) {
                        check = true;
                    }
                    int_likeCount++;

                }
                likeCount.setText(int_likeCount + "");
                if (check) {
                    like_img.setImageResource(R.drawable.likeson);
                    likeState = true;
                } else {
                    like_img.setImageResource(R.drawable.likesoff);
                    likeState = false;
                }
                // progressDialog.dismiss();
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

    public class ControlLike extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {//작업 시작할때
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(String result) {//작업 종료할떄
            super.onPostExecute(result);
        }


        @Override
        protected String doInBackground(String... params) {

            String replyId = (String) params[1];
            String UUID = (String) params[2];


            String serverURL = (String) params[0];
            String postParameters = "replyId=" + replyId + "&UUID=" + UUID;


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
                if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                } else {
                    inputStream = httpURLConnection.getErrorStream();
                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line = null;

                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }


                bufferedReader.close();


                return sb.toString();


            } catch (Exception e) {


                return new String("Error: " + e.getMessage());
            }

        }
    }

    public class DeleteReply extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {//작업 시작할때
            super.onPreExecute();
            progressDialog = ProgressDialog.show(CommunityActivity.this, "Please Wait", null, true, true);

        }

        @Override
        protected void onPostExecute(String result) {//작업 종료할떄
            //댓글 받아서 넣기(서버에서 데이터 받아서 넣기)
            GetReply getReply = new GetReply();
            getReply.execute("http://" + IP_ADDRESS + "/getReply.php", mainId);

            progressDialog.dismiss();
            super.onPostExecute(result);
        }


        @Override
        protected String doInBackground(String... params) {

            String id = (String) params[1];


            String serverURL = (String) params[0];
            String postParameters = "id=" + id;


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
                if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                } else {
                    inputStream = httpURLConnection.getErrorStream();
                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line = null;

                while ((line = bufferedReader.readLine()) != null) {
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