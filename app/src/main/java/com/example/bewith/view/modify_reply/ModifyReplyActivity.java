package com.example.bewith.view.modify_reply;

import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.example.bewith.R;
import com.example.bewith.data.Constants;
import com.example.bewith.databinding.ActivityModifyReplyBinding;
import com.example.bewith.util.network.community.ModifyReply;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ModifyReplyActivity extends AppCompatActivity {
    private ActivityModifyReplyBinding binding;

    private String id_;
    private String text;
    private static String IP_ADDRESS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityModifyReplyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //서버 IP
        IP_ADDRESS= Constants.IP_ADDRESS;

        Intent intent = getIntent();
        id_=intent.getStringExtra("id");
        text=intent.getStringExtra("text");

        binding.modifyReplyEditText.setText(text);
        initButtonClick();

    }

    public void initButtonClick(){
        binding.okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text = binding.modifyReplyEditText.getText().toString().trim();//텍스트 내용
                if (text.equals("")) {
                    Toast.makeText(ModifyReplyActivity.this, "내용을 입력하세요.", Toast.LENGTH_SHORT).show();
                } else {

                    ModifyReply modifyReply = new ModifyReply();//내 comment 내용 서버에 전송
                    modifyReply.execute("http://" + IP_ADDRESS + "/updateReply.php",id_,text);//서버에 전송
                    setResult(RESULT_OK);
                    finish();
                }
            }
        });
        binding.noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

}