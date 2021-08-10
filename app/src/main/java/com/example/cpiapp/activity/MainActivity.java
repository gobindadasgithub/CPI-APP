package com.example.cpiapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.cpiapp.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    CardView UploadNotice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        UploadNotice=findViewById(R.id.addnotice);
        UploadNotice.setOnClickListener(this);



    }

    @Override
    public void onClick(View v) {


       switch (v.getId()){

           case R.id.addnotice:
               startActivity(new Intent(MainActivity.this, NoticeActivity.class));
               break;
       }

    }
}