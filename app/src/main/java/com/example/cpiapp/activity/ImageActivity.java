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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.cpiapp.R;
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

public class ImageActivity extends AppCompatActivity {
    private CardView selectImage;
    private Spinner imageCategory;
    private Button imageUploadbtn;
    private ImageView galleryImageView;
    private String category;
    private Bitmap bitmap;
    private ProgressDialog progressDialog;
    private DatabaseReference reference;
    private StorageReference storageReference;
    private String downloadUrl="";

    private final  int  REQ=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);


        selectImage=findViewById(R.id.addGalleryImage);
        imageCategory=findViewById(R.id.Spinner_Gallery);
        imageUploadbtn=findViewById(R.id.addGallerybutton);
        galleryImageView=findViewById(R.id.addGalleryImageView);
        progressDialog=new ProgressDialog(this);
        reference= FirebaseDatabase.getInstance().getReference().child("gallery");
        storageReference= FirebaseStorage.getInstance().getReference().child("gallery");

        String[] itemas=new String[]{"Select Category","Convocation","Independence Day","Others Events"};



      imageCategory.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,itemas));
imageCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        category=imageCategory.getSelectedItem().toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
});
selectImage.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        openGallery();
    }
});
imageUploadbtn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
       if (bitmap==null){
           Toast.makeText(ImageActivity.this, "Please Upload Image", Toast.LENGTH_SHORT).show();
       }
       else if (category.equals("Select Category")){
           Toast.makeText(ImageActivity.this, "Please Select Image Category", Toast.LENGTH_SHORT).show();
       }
       else{
           progressDialog.setMessage("Uploading");
           progressDialog.show();
           uplaodImage();

       }

    }
});


    }

    private void uplaodImage() {
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] finalImage=baos.toByteArray();
        final StorageReference filepath;
        filepath=storageReference.child(finalImage+"jpg");
        final UploadTask uploadTask=filepath.putBytes(finalImage);
        uploadTask.addOnCompleteListener(ImageActivity.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
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
                    Toast.makeText(ImageActivity.this,"Something went wrong",Toast.LENGTH_SHORT).show();

                }

            }
        });


    }

    private void UploadData() {
        reference=reference.child(category);
        final String uniqueKey=reference.push().getKey();
        reference.child(uniqueKey).setValue(downloadUrl).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                progressDialog.dismiss();
                Toast.makeText(ImageActivity.this,"Image Uploaded Successfull",Toast.LENGTH_SHORT).show();


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(ImageActivity.this,"Something went wrong",Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void openGallery() {
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
            galleryImageView.setImageBitmap(bitmap);


        }


    }


}