package com.example.cpiapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.cpiapp.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    CardView UploadNotice,UploadImage,UploadEbook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        UploadNotice=findViewById(R.id.addnotice);
        UploadImage=findViewById(R.id.addGallery);
        UploadEbook=findViewById(R.id.addEbook);
        UploadNotice.setOnClickListener(this);
        UploadImage.setOnClickListener(this);
        UploadEbook.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {


       switch (v.getId()){

           case R.id.addnotice:
               startActivity(new Intent(MainActivity.this, NoticeActivity.class));
               break;
           case R.id.addGallery:
               startActivity(new Intent(MainActivity.this, ImageActivity.class));
               break;
           case R.id.addEbook:
               startActivity(new Intent(MainActivity.this, PdfActivity.class));
               break;
       }

    }
}