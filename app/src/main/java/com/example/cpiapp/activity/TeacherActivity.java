package com.example.cpiapp.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.cpiapp.R;
import com.example.cpiapp.model.TeacherData;
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

public class TeacherActivity extends AppCompatActivity {
    private ImageView addTeaherImage;
    private EditText nameTeacher,emailTeacher,postTeacher;
    private Spinner addTeacherCategory;
    private Button addTeacherbtn;
    private final  int  REQ=1;
    private Bitmap bitmap=null;
    private  String category;
    private  String name,email,post,download_Url="";
    private  StorageReference storageReference;
    private DatabaseReference reference,dbRef;
    private ProgressDialog progressDialog;
    private  String downloadUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);
        addTeaherImage=findViewById(R.id.teacherImageview);
        nameTeacher=findViewById(R.id.addTeacherName);
        emailTeacher=findViewById(R.id.addTeacherEmail);
        postTeacher=findViewById(R.id.addTeacherPost);
        addTeacherCategory=findViewById(R.id.Spinner_Teacher);
        addTeacherbtn=findViewById(R.id.addteacherbutton);
        progressDialog=new ProgressDialog(this);
        reference= FirebaseDatabase.getInstance().getReference().child("teacher");
        storageReference= FirebaseStorage.getInstance().getReference().child("teacher");



        String[] itemas=new String[]{"Select Category","Computer Technology","Civil Technology","Electrical Technology","Rac Technology"};



        addTeacherCategory.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,itemas));
        addTeacherCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category=addTeacherCategory.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        addTeaherImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
        addTeacherbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                teacherCheckValidation();
            }
        });



    }

    private void teacherCheckValidation() {
        name=nameTeacher.getText().toString();
        email=emailTeacher.getText().toString();
        post=postTeacher.getText().toString();

if (name.isEmpty()){
    nameTeacher.setError("Empty");
    nameTeacher.requestFocus();

}else if (email.isEmpty()){
    emailTeacher.setError("Empty");
    emailTeacher.requestFocus();

}else if (post.isEmpty()){
    postTeacher.setError("Empty");
    postTeacher.requestFocus();

}else if(category.equals("Select Category")){
    Toast.makeText(TeacherActivity.this,"Please provide Teacher Category",Toast.LENGTH_SHORT);

}else if (bitmap==null){
    insertdata();
}else{
    insertImage();
}


    }
    private void insertdata() {


        dbRef=reference.child("category");
        final  String uniqueKey=dbRef.push().getKey();
        TeacherData teacherData=new TeacherData(name,email,post,download_Url,uniqueKey);
        dbRef.child(uniqueKey).setValue(teacherData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                progressDialog.dismiss();
                Toast.makeText(TeacherActivity.this,"Teacher Added",Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(TeacherActivity.this,"Something went wrong",Toast.LENGTH_SHORT).show();


            }
        });



    }

    private void insertImage() {


        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,50,baos);
        byte[] finalImage=baos.toByteArray();
        final StorageReference filepath;
        filepath=storageReference.child("teacher");
        final UploadTask uploadTask=filepath.putBytes(finalImage);
        uploadTask.addOnCompleteListener(TeacherActivity.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
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
                                    insertdata();

                                }
                            });

                        }
                    });
                }else{
                    progressDialog.dismiss();
                    Toast.makeText(TeacherActivity.this,"Something went wrong",Toast.LENGTH_SHORT).show();

                }

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
            addTeaherImage.setImageBitmap(bitmap);


        }


    }

}