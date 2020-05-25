package com.example.rxjava2operator;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class ToActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        findViewById(R.id.btnInterval).setOnClickListener(v -> ToActivity.this.startActivity(new Intent(ToActivity.this, ToActivity.class)));
    }

}
