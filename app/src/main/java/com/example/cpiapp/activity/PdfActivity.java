package com.example.cpiapp.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

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

import java.io.File;
import java.util.HashMap;

public class PdfActivity extends AppCompatActivity {
    private final int REQ=1;
    private Uri pdfdata;
    private EditText pdftTitle;
    private DatabaseReference databasereference;
    private StorageReference storageReference;
    private final String downloadUrl="";
    private ProgressDialog progressDialog;
    private CardView selecPdfImage;
    private Button pdfbtn;
    private TextView pdf_tv;
    private  String pdfname,title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);

            selecPdfImage = findViewById(R.id.addPdf);

        pdftTitle=findViewById(R.id.addPdfTitle);
        pdfbtn = findViewById(R.id.addPdfbutton);
        pdf_tv=findViewById(R.id.pdf_tv);

        databasereference= FirebaseDatabase.getInstance().getReference();
        storageReference= FirebaseStorage.getInstance().getReference();
        progressDialog=new ProgressDialog(this);

        selecPdfImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
        pdfbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title=pdftTitle.getText().toString();
                if (title.isEmpty()){
                    pdftTitle.setError("Empty");
                    pdftTitle.requestFocus();
                }else if (pdfdata==null){
                    Toast.makeText(PdfActivity.this,"Please Upload Pdf",Toast.LENGTH_SHORT).show();
                }else{
                    uploadPdf();
                }

            }
        });



    }

    private void uploadPdf() {
        progressDialog.setTitle("Please wait....");
        progressDialog.setMessage("Uploading pdf");
        progressDialog.show();
        StorageReference reference=storageReference.child("file/"+pdfname+"-"+System.currentTimeMillis()+".pdf");
        reference.putFile(pdfdata)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask=taskSnapshot.getStorage().getDownloadUrl();
                while(!uriTask.isComplete()){
                    Uri uri=uriTask.getResult();
                    uploadData(String.valueOf(uri));

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toast.makeText(PdfActivity.this,"Something Went Wrong",Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void uploadData(String downloadUrl) {
        String uniquekey=databasereference.child("pdf").push().getKey();
        HashMap data=new HashMap();
        data.put("pdftitle",title);
        data.put("pdfurl",downloadUrl);

        databasereference.child("pdf").child("uniquekey").setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                progressDialog.dismiss();
                Toast.makeText(PdfActivity.this,"Pdf Uploaded successfully",Toast.LENGTH_SHORT).show();


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                progressDialog.dismiss();

                Toast.makeText(PdfActivity.this,"Failed Upload to pdf",Toast.LENGTH_SHORT).show();

            }
        });



    }

    private void openGallery() {
       Intent intent=new Intent();
       intent.setType("pdf/docs/ppt");
       intent.setAction(Intent.ACTION_GET_CONTENT);
       startActivityForResult(Intent.createChooser(intent,"Select Pdf File"),REQ);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
if (requestCode==REQ && resultCode==RESULT_OK){
    pdfdata=data.getData();

    if (pdfdata.toString().startsWith("content://")){
        Cursor cursor=null;
        try {
            cursor=PdfActivity.this.getContentResolver().query(pdfdata,null,null,null,null);
            if (cursor!=null && cursor.moveToFirst()){
                pdfname=cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));

            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    else if (pdfdata.toString().startsWith("file;//")){
        pdfname=new File(pdfdata.toString()).getName();
    }
    pdf_tv.setText(pdfname);
}
    }
}