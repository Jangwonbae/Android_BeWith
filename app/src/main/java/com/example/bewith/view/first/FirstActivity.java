package com.example.bewith.view.first;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.bewith.R;
import com.unity3d.player.UnityPlayerActivity;

public class FirstActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        Intent intent = new Intent(FirstActivity.this, UnityPlayerActivity.class);
        startActivity(intent);
        finish();
    }
}