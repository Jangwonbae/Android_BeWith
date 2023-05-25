package com.example.bewith;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.unity3d.player.UnityPlayer;
import com.unity3d.player.UnityPlayerActivity;

public class SetNickActivity extends AppCompatActivity {
    private EditText nameEdit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_nick);

        nameEdit = (EditText)findViewById(R.id.myName);
    }
    public void mOnClick(View v){
        String name = nameEdit.getText().toString();
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
}