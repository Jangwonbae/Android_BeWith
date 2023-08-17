package com.example.bewith.view.set_nick;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.example.bewith.databinding.ActivitySetNickBinding;
import com.unity3d.player.UnityPlayer;
import com.unity3d.player.UnityPlayerActivity;

public class SetNickActivity extends AppCompatActivity {
    private ActivitySetNickBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivitySetNickBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initButtonClick();
    }
    public void initButtonClick(){
        String name = binding.setNameEditText.getText().toString();
        if(name.trim().equals("")){
            Toast.makeText(this, "AR SNS에서 사용 할 닉네임입니다.", Toast.LENGTH_SHORT).show();
        }
        else{
            SharedPreferences prefs = getSharedPreferences("person_name",0);
            SharedPreferences.Editor editor =prefs.edit();
            editor.putString("name",name);
            editor.apply();
            Intent intent = new Intent(SetNickActivity.this, UnityPlayerActivity.class);
            UnityPlayer.UnitySendMessage("ButtonManager", "SetNickname",name);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}