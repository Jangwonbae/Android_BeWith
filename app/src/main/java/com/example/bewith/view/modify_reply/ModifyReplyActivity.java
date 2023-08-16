package com.example.bewith.view.modify_reply;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.example.bewith.databinding.ActivityModifyReplyBinding;

public class ModifyReplyActivity extends AppCompatActivity {
    private ActivityModifyReplyBinding binding;

    private String id_;
    private String text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityModifyReplyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


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
                    Intent intent = new Intent();
                    intent.putExtra("id",id_);
                    intent.putExtra("text",text);
                    setResult(Activity.RESULT_OK,intent);
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