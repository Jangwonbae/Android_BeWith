package com.example.bewith.view.main.activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.bewith.R;
import com.example.bewith.UpdatePopup;
import com.example.bewith.databinding.ActivityMainBinding;
import com.example.bewith.util.location.DistanceCalculator;
import com.example.bewith.util.network.DeleteComment;
import com.example.bewith.view.main.data.Constants;
import com.example.bewith.view.main.data.CommentData;
import com.example.bewith.listclass.MyAdapter;
import com.example.bewith.util.location.LocationProviderManager;
import com.example.bewith.view.main.util.map_item.MarkerCreator;
import com.example.bewith.view.main.util.swipe_menu_list.SwipeMenuListCreator;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


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

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    private ActivityMainBinding binding;
    private MainActivityViewModel mainActivityViewModel;
    private GoogleMap mMap;
    public static double myLatitude;
    public static double myLogitude;

    private TextView noDataTextView;
    private SwipeMenuListView myCommentListView;
    private ListView commentListView;
    private MyAdapter swipeMenuListAdapter;
    private MyAdapter listAdapter;

    private ArrayList<CommentData> spinnerArrayList = new ArrayList<>();
    public int radiusIndex;
    private static String IP_ADDRESS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //데이터 바인딩
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        //뷰모델 객체 생성
        mainActivityViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        //뷰모델 적용
        binding.setViewModel(mainActivityViewModel);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        radiusIndex = 0;
        //서버 IP
        IP_ADDRESS = Constants.IP_ADDRESS;
        //유니티로 부터 받는 정보
        Intent intent = getIntent();
        Constants.UUID = intent.getStringExtra("UUID");
        myLatitude = Double.parseDouble(intent.getStringExtra("Lat"));
        myLogitude = Double.parseDouble(intent.getStringExtra("Lng"));

        noDataTextView = binding.noDataTextView;
        myCommentListView = binding.myCommnentListView;
        commentListView = binding.commentListView;
        //땡길 수 있는 리스트뷰 설정
        myCommentListView.setMenuCreator(new SwipeMenuListCreator(getResources()).getCreator(getBaseContext()));

        swipeMenuListAdapter = new MyAdapter(MainActivity.this, spinnerArrayList);//어뎁터에 어레이리스트를 붙임
        myCommentListView.setAdapter(swipeMenuListAdapter);//땡길 수 있는 리스트를 어뎁터에 붙임

        listAdapter = new MyAdapter(MainActivity.this, spinnerArrayList);//어뎁터에 어레이리스트를 붙임
        commentListView.setAdapter(listAdapter);//리스트를 어뎁터에 붙임
        commentListView.setVisibility(View.GONE);

        initListClick();
        initFloatButtonCLick();//플로팅버튼 생성(go to ar)
        createSpinner();//스피너 생성


        //스피너 목록에 따라 보여지는 리스트가 변경되면
        mainActivityViewModel.getSpinnerCommentArrayListLiveData().observeInOnStart(this, new Observer<ArrayList<CommentData>>() {
            @Override
            public void onChanged(ArrayList<CommentData> CommentDataList) {
                if (CommentDataList.isEmpty()) {
                    noDataTextView.setVisibility(View.VISIBLE);//데이터 없음 표시
                } else {
                    noDataTextView.setVisibility(View.INVISIBLE);
                }
                spinnerArrayList.clear();//비우고 다시 채우기
                for (CommentData commentData : CommentDataList) {
                    spinnerArrayList.add(commentData);
                }//why? notifyDataSetChanged() 얘는 friends= friendDatalist 이런식으로 하면 갱신이 안되더라
                if (radiusIndex == 0) {
                    swipeMenuListAdapter.notifyDataSetChanged();
                } else {
                    listAdapter.notifyDataSetChanged();
                    Log.d("ssssssssssss","ssssssssssssssssssssssssssssssssssss");
                    for(CommentData commentData: spinnerArrayList){
                        Log.d("sssssssss",commentData.text);
                    }
                }
            }
        });

        mainActivityViewModel.getComment(radiusIndex);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mainActivityViewModel.getComment(radiusIndex);
    }


    @Override
    public void onMapReady(final GoogleMap googleMap) {//지도가 준비되면 실행됨
        mMap = googleMap;//구글맵을 전역변수 저장
        mainActivityViewModel.getCommentArrayListLiveData().observeInOnStart(this, new Observer<ArrayList<CommentData>>() {
            @Override
            public void onChanged(ArrayList<CommentData> CommentDataList) {
                for (CommentData commentData : CommentDataList) {
                    //마커 생성
                    new MarkerCreator().addMarker(mMap, commentData);
                }
            }
        });

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        LatLng myLocation = new LatLng(myLatitude, myLogitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 18));

    }


    public void createSpinner() {

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.radius_array));
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.radiusSpinner.setAdapter(arrayAdapter);
        binding.radiusSpinner.setSelection(radiusIndex);//초기값

        initSpinnerClick();
    }

    public void initSpinnerClick() {
        binding.radiusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {//스피너가 선택되었을 때
                radiusIndex = position;
                mainActivityViewModel.onSeleteSpinner(radiusIndex);
                if (radiusIndex == 0) {//반경 리스트가 My Comment면
                    myCommentListView.setVisibility(View.VISIBLE);//땡길 수 있는 리스트를 보이게
                    commentListView.setVisibility(View.GONE);//일반 리스트를 안보이게
                } else {//다른게 선택되면
                    myCommentListView.setVisibility(View.GONE);//땡길 수 있는 리스트를 안보이게
                    commentListView.setVisibility(View.VISIBLE);//일반리스트를 보이게
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {//무시하면됨(아무 것도 선택 안됐을 때)
            }
        });
    }

    public void initFloatButtonCLick() {//플로팅버튼 클릭 메소드
        binding.reloadFbtn.setOnClickListener(new View.OnClickListener() {//새로고침 버튼
            @Override
            public void onClick(View v) {
                mainActivityViewModel.getComment(radiusIndex);
            }
        });
    }

    public void initListClick() {
        //전체 사용자 comment 리스트 클릭 이벤트
        commentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(spinnerArrayList.get(position).latitude),
                        Double.parseDouble(spinnerArrayList.get(position).logitude)), mMap.getCameraPosition().zoom));

            }
        });
        //swipeMenuListView 리스트 열었다 닫았다 메소드
        myCommentListView.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {
            @Override
            public void onSwipeStart(int position) {
                // swipe start
                myCommentListView.smoothOpenMenu(position);
            }

            @Override
            public void onSwipeEnd(int position) {
                // swipe end
                myCommentListView.smoothOpenMenu(position);
            }
        });
        //나의 comment 클릭 이벤트
        myCommentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(spinnerArrayList.get(i).latitude),
                        Double.parseDouble(spinnerArrayList.get(i).logitude)), mMap.getCameraPosition().zoom));
            }
        });
        //열려있을때 메뉴 클릭 메소드
        myCommentListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0://수정
                        //코멘트 수정후 돌아왔을 때 실행
                        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                            if (result.getResultCode() == RESULT_OK) {
                                mainActivityViewModel.getComment(radiusIndex);
                            }
                        });
                        Intent intent = new Intent(MainActivity.this, UpdatePopup.class);
                        intent.putExtra("id", spinnerArrayList.get(position)._id);
                        intent.putExtra("category", spinnerArrayList.get(position).category);
                        intent.putExtra("text", spinnerArrayList.get(position).text);
                        activityResultLauncher.launch(intent);

                        break;
                    case 1://삭제
                        //동기 처리 진행
                        DeleteComment deleteComment = new DeleteComment();
                        deleteComment.execute("http://" + IP_ADDRESS + "/deleteComment.php", Integer.toString(spinnerArrayList.get(position)._id));
                        //삭제되고 난 후 진행되어야 함
                        mainActivityViewModel.getComment(radiusIndex);
                        break;
                }
                return true;
            }
        });
    }
}

// ProgressDialog progressDialog;
// progressDialog = ProgressDialog.show(MainActivity.this,"Please Wait", null, true, true);
//progressDialog.dismiss();


