package com.example.cpiapp.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.cpiapp.R;
import com.example.cpiapp.model.NoticeData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class NoticeActivity extends AppCompatActivity {
    private final int REQ=1;
    private Bitmap bitmap;
    private ImageView noticeimageView;
    private EditText noticetTitle;
    private DatabaseReference reference;
    private  StorageReference storageReference;
    private String downloadUrl="";
    private ProgressDialog progressDialog;
    private CardView selectNoticeImage;
    private Button noticeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);
       selectNoticeImage = findViewById(R.id.addImage);
        noticeimageView=findViewById(R.id.addNoticeImageView);
        noticetTitle=findViewById(R.id.addNoticeTitle);
        noticeButton = findViewById(R.id.addNoticebutton);

        reference= FirebaseDatabase.getInstance().getReference();
        storageReference= FirebaseStorage.getInstance().getReference();

        progressDialog=new ProgressDialog(this );

        selectNoticeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGalley();

            }
        });
        noticeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (noticetTitle.getText().toString().isEmpty()){
                    noticetTitle.setError("Empty");
                    noticetTitle.requestFocus();
                }else if (bitmap==null){
                    UploadData();
                }else{
                    UploadImage();

                }

            }
        });



    }

    private void UploadData() {

          String title=noticetTitle.getText().toString();
        reference=reference.child("Notice");
        final  String uniqueKey=reference.push().getKey();
        Calendar calendardate=Calendar.getInstance();
        SimpleDateFormat currentDate=new SimpleDateFormat("dd//MM/yy");
        String date=currentDate.format(calendardate.getTime());

        Calendar calendarTime=Calendar.getInstance();
        SimpleDateFormat currentTime=new SimpleDateFormat("hh:mm a ");
        String time=currentTime.format(calendarTime.getTime());


        NoticeData noticeData=new NoticeData(title,downloadUrl,date,time,uniqueKey);
        reference.child(uniqueKey).setValue(noticeData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                progressDialog.dismiss();
                Toast.makeText(NoticeActivity.this,"Notice Uploaded",Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(NoticeActivity.this,"Something went wrong",Toast.LENGTH_SHORT).show();


            }
        });


    }




    private void UploadImage(){
        progressDialog.setMessage("Uploading...");
        progressDialog.show();
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] finalImage=baos.toByteArray();
        final StorageReference filepath;
        filepath=storageReference.child("Notice").child(finalImage+"jpg");
        final UploadTask uploadTask=filepath.putBytes(finalImage);
        uploadTask.addOnCompleteListener(NoticeActivity.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()){
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    downloadUrl=String.valueOf(uri);
                                    UploadData();

                                }
                            });

                        }
                    });
                }else{
                    progressDialog.dismiss();
                    Toast.makeText(NoticeActivity.this,"Something went wrong",Toast.LENGTH_SHORT).show();

                }

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