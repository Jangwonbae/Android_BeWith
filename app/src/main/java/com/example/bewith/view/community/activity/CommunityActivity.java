package com.example.bewith.view.community.activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.bewith.R;
import com.example.bewith.data.Constants;
import com.example.bewith.view.modify_reply.ModifyReplyActivity;
import com.example.bewith.databinding.ActivityCommunityBinding;
import com.example.bewith.view.community.activity.fragment.ExampleBottomSheetDialog;
import com.example.bewith.view.community.adapter.ReplyAdapter;
import com.example.bewith.view.community.data.LikeData;
import com.example.bewith.view.community.data.ReplyData;
import java.util.ArrayList;

public class CommunityActivity extends AppCompatActivity implements ExampleBottomSheetDialog.BottomSheetListener {
    private ActivityCommunityBinding binding;
    private CommunityActivityViewModel communityActivityViewModel;

    private ReplyAdapter replyAdapter;
    private ArrayList<ReplyData> replyDataArrayList = new ArrayList<>();

    private ActivityResultLauncher<Intent> activityResultLauncher;
    private String communityId;
    private String commuityUUID;
    private String myUUID;
    private String nickname;
    private int int_likeCount=0;

    private boolean likeState;
    private int selectIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //유니티에서 인텐트로 받아서 세팅하기
        Intent intent = getIntent();
        Constants.UUID = intent.getStringExtra("myUUID");
        //데이터 바인딩
        binding = DataBindingUtil.setContentView(this, R.layout.activity_community);
        //뷰모델 객체 생성
        communityActivityViewModel = new ViewModelProvider(this).get(CommunityActivityViewModel.class);
        //뷰모델 적용
        binding.setViewModel(communityActivityViewModel);

        //시간 , 내용, 게시물uuid, 게시물id, 사용자uuid
        binding.communityTimeTextView.setText(intent.getStringExtra("mainTime"));
        //닉네임
        nickname = intent.getStringExtra("nickname");
        binding.communityNickNameTextView.setText(nickname);
        //내용
        binding.communityContentsTextView.setText(intent.getStringExtra("mainText"));
        //게시물 UUID
        commuityUUID = intent.getStringExtra("mainUUID");
        //나의 UUID
        myUUID = intent.getStringExtra("myUUID");
        //게시물 ID
        communityId = intent.getStringExtra("mainId");
        //닉네임
        SharedPreferences prefs = getSharedPreferences("person_name", 0);
        binding.replyNickNameTextView.setText(prefs.getString("name", ""));

        replyAdapter = new ReplyAdapter(getBaseContext(), replyDataArrayList, commuityUUID, myUUID);
        binding.replyListView.setAdapter(replyAdapter);

        binding.submitImageView.setVisibility(View.INVISIBLE); //처음에는 에디트텍스트에 아무것도 없기 때문에 제출 버튼 감추기
        binding.replyEditText.setImeOptions(EditorInfo.IME_ACTION_DONE); //키보드 다음 버튼을 완료 버튼으로 바꿔줌
        ininEditTextListener();
        initListClick();
        initButtonClick();
        initObserver();
        initActivityResult();

        communityActivityViewModel.getReplyData(communityId);
    }

    public void ininEditTextListener(){
        binding.replyEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override//완료 버튼 클릭 리스너
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {//키보드에 완료버튼을 누른 후 수행할 것
                    if (v.getText().toString().trim().equals("")) {
                        Toast.makeText(CommunityActivity.this, "댓글을 입력하세요.", Toast.LENGTH_SHORT).show();
                    } else {
                        submitReply();
                    }
                    return true;
                }
                return false;
            }
        });
        binding.replyEditText.addTextChangedListener(new TextWatcher() {//에디트텍스트 텍스트 변경 리스너
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (binding.replyEditText.getText().toString().equals("")) {
                    binding.submitImageView.setVisibility(View.INVISIBLE);
                } else {
                    binding.submitImageView.setVisibility(View.VISIBLE);
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

    }
    public void initListClick(){
        binding.replyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {//리스트 클릭 리스너
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (replyDataArrayList.get(i).ReplyUUID.equals(myUUID)) {
                    ExampleBottomSheetDialog bottomSheetDialog = new ExampleBottomSheetDialog();
                    bottomSheetDialog.show(getSupportFragmentManager(), "exampleBottomSheet");
                    selectIndex = i;
                }
            }
        });
        binding.swipeRefreshLayout.setOnRefreshListener(//리스트 아래로 땡겼을 때
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        //댓글 받아서 넣기(서버에서 데이터 받아서 넣기)
                        communityActivityViewModel.getReplyData(communityId);
                        binding.swipeRefreshLayout.setRefreshing(false);
                    }
                }
        );
    }
    public void initButtonClick(){
        binding.likeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (likeState) {//좋아요 표시상태에서 버튼을 누르면
                    binding.likeImageView.setImageResource(R.drawable.likesoff);//OFF로 변경
                    likeState = false;
                    int_likeCount-=1;
                    binding.likeCountTextView.setText(int_likeCount+"");

                } else {//좋아요가 안눌러진 상태에서 버튼을 누르면
                    binding.likeImageView.setImageResource(R.drawable.likeson);//ON으로 변경
                    likeState = true;
                    int_likeCount+=1;
                    binding.likeCountTextView.setText(int_likeCount+"");
                }
                communityActivityViewModel.controlLikeData(likeState, communityId);
            }
        });
        binding.submitImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.replyEditText.getText().toString().trim().equals("")) {//빈칸 일때
                    Toast.makeText(CommunityActivity.this, "댓글을 입력하세요.", Toast.LENGTH_SHORT).show();
                } else {
                    submitReply();
                }
            }
        });
    }
    public void submitReply(){
        SharedPreferences prefs = getSharedPreferences("person_name", 0);
        String name = prefs.getString("name", "");
        //서버에 댓글 정보 추가
        communityActivityViewModel.addReplyData(communityId, name, binding.replyEditText.getText().toString().trim());
        binding.replyEditText.setText("");
        //키보드 내리기
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(binding.replyEditText.getWindowToken(), 0);
    }


    @Override
    public void onButtonClicked(int position) {
        switch (position) {
            case 0://수정
                Intent intent = new Intent(CommunityActivity.this, ModifyReplyActivity.class);
                intent.putExtra("id", Integer.toString(replyDataArrayList.get(selectIndex).id));
                intent.putExtra("text", replyDataArrayList.get(selectIndex).ReplyText);
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
                        communityActivityViewModel.deleteReplyData(communityId, Integer.toString(replyDataArrayList.get(selectIndex).id));
                        dialog.dismiss();
                    }
                });
                ad.show();
                break;
        }
    }
    public void initObserver(){
        //댓글 정보가 바뀌면
        communityActivityViewModel.getReplyDataArrayListLiveData().observeInOnStart(this, new Observer<ArrayList<ReplyData>>() {
            @Override
            public void onChanged(ArrayList<ReplyData> replyDataList) {
                replyDataArrayList.clear();
                for(ReplyData replyData : replyDataList){
                    replyDataArrayList.add(replyData);
                }
                replyAdapter.notifyDataSetChanged();
                binding.replyCountTextView.setText(replyDataArrayList.size() + "");
            }
        });
        //like 정보가 바뀌면
        communityActivityViewModel.getLikeDataLiveData().observeInOnStart(this, new Observer<LikeData>() {
            @Override
            public void onChanged(LikeData likeData) {
                if (likeData.getMyLike()) {
                    binding.likeImageView.setImageResource(R.drawable.likeson);
                    likeState = true;

                } else {
                    binding.likeImageView.setImageResource(R.drawable.likesoff);
                    likeState = false;
                }
                int_likeCount=likeData.getLikeCount();
                binding.likeCountTextView.setText(int_likeCount+ "");
            }
        });
    }
    public void initActivityResult(){

        //댓글 수정하기 갔다왔을 때 실행
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                String id = result.getData().getStringExtra("id");
                String text = result.getData().getStringExtra("text");
                communityActivityViewModel.modifyReply(communityId, id,text);
            }
        });

    }
}