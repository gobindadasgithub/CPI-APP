package com.example.cpiapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

import java.io.IOException;

public class NoticeActivity extends AppCompatActivity {
    private CardView addimage;
    private final int REQ=1;
    private Bitmap bitmap;
    private ImageView noticeimageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);
        addimage=findViewById(R.id.addImage);
        noticeimageView=findViewById(R.id.addNoticeImageView);

        addimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGalley();

            }
        });
    }

    private void openGalley() {
        Intent imagePick=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(imagePick,REQ);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

if (requestCode==REQ && resultCode==RESULT_OK){
    Uri uri=data.getData();
    try {
        bitmap=MediaStore.Images.Media.getBitmap(getContentResolver(),uri);

    } catch (IOException e) {
        e.printStackTrace();
    }
    noticeimageView.setImageBitmap(bitmap);


}


    }
}