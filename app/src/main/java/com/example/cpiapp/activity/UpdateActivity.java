package com.example.cpiapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cpiapp.R;
import com.example.cpiapp.adapter.TeacherAdapter;
import com.example.cpiapp.model.TeacherData;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class UpdateActivity extends AppCompatActivity {
    private FloatingActionButton floatingActionButton;
    private RecyclerView cse_Department,civil_Department,eee_Department,rac_Department;
     private LinearLayout cse_nodataound, civil_nodataound, eee_nodataound, rac_nodataound;
     private List<TeacherData> list1,list2,list3,list4;
     private DatabaseReference databaseReference,dbRef;
     private TeacherAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        floatingActionButton=findViewById(R.id.fbaction_btn);

        cse_Department=findViewById(R.id.cse_department);
        civil_Department=findViewById(R.id.civil_department);
        eee_Department=findViewById(R.id.eee_department);
        rac_Department=findViewById(R.id.rac_department);

        cse_nodataound=findViewById(R.id.cse_no_datafound);
        civil_nodataound=findViewById(R.id.civil_no_datafound);
        eee_nodataound=findViewById(R.id.eee_no_datafound);
        rac_nodataound=findViewById(R.id.rac_no_datafound);

        databaseReference= FirebaseDatabase.getInstance().getReference().child("teacher");


        cseDeapartment();
        civilDepartment();
        eeeDepartement();
        racDeapartment();
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(UpdateActivity.this, TeacherActivity.class);
                startActivity(intent);
            }
        });


    }

    private void racDeapartment() {
        dbRef=databaseReference.child("Rac Technology");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                list4=new ArrayList<>();

                if (!snapshot.exists()){
                    rac_Department.setVisibility(View.VISIBLE);
                    rac_nodataound.setVisibility(View.GONE);
                }else{
                    rac_Department.setVisibility(View.GONE);
                    rac_nodataound.setVisibility(View.VISIBLE);
                    for (DataSnapshot snapshot1:snapshot.getChildren()){
                        TeacherData data=snapshot1.getValue(TeacherData.class);
                        list4.add(data);
                    }
                    rac_Department.setHasFixedSize(true);
                    rac_Department.setLayoutManager(new LinearLayoutManager(UpdateActivity.this));
                    adapter=new TeacherAdapter(list4,UpdateActivity.this);
                    rac_Department.setAdapter(adapter);

                }

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Toast.makeText(UpdateActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void eeeDepartement() {

        dbRef=databaseReference.child("Electrical Technology");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                list3=new ArrayList<>();

                if (!snapshot.exists()){
                    eee_Department.setVisibility(View.VISIBLE);
                    eee_nodataound.setVisibility(View.GONE);
                }else{
                    eee_Department.setVisibility(View.GONE);
                    eee_nodataound.setVisibility(View.VISIBLE);
                    for (DataSnapshot snapshot1:snapshot.getChildren()){
                        TeacherData data=snapshot1.getValue(TeacherData.class);
                        list3.add(data);
                    }
                    eee_Department.setHasFixedSize(true);
                    eee_Department.setLayoutManager(new LinearLayoutManager(UpdateActivity.this));
                    adapter=new TeacherAdapter(list3,UpdateActivity.this);
                    eee_Department.setAdapter(adapter);

                }

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Toast.makeText(UpdateActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void civilDepartment() {
        dbRef=databaseReference.child("Civil Technology");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                list2=new ArrayList<>();
                if (!snapshot.exists()){
                    civil_Department.setVisibility(View.VISIBLE);
                    civil_nodataound.setVisibility(View.GONE);
                }else{
                    civil_Department.setVisibility(View.GONE);
                    civil_nodataound.setVisibility(View.VISIBLE);
                    for (DataSnapshot snapshot1:snapshot.getChildren()){
                        TeacherData data=snapshot1.getValue(TeacherData.class);
                        list2.add(data);
                    }
                    civil_Department.setHasFixedSize(true);
                    civil_Department.setLayoutManager(new LinearLayoutManager(UpdateActivity.this));
                    adapter=new TeacherAdapter(list2,UpdateActivity.this);
                    civil_Department.setAdapter(adapter);
                }

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Toast.makeText(UpdateActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void cseDeapartment() {
        dbRef=databaseReference.child("Computer Technology");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                list1=new ArrayList<>();
                if (!snapshot.exists()){
                    cse_Department.setVisibility(View.VISIBLE);
                    cse_nodataound.setVisibility(View.GONE);
                }else{
                    cse_Department.setVisibility(View.GONE);
                    cse_nodataound.setVisibility(View.VISIBLE);
                    for (DataSnapshot snapshot1:snapshot.getChildren()){
                    TeacherData data=snapshot1.getValue(TeacherData.class);
                    list1.add(data);
                    }
                    cse_Department.setHasFixedSize(true);
                    cse_Department.setLayoutManager(new LinearLayoutManager(UpdateActivity.this));
                    adapter=new TeacherAdapter(list1,UpdateActivity.this);
                    cse_Department.setAdapter(adapter);
                }

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Toast.makeText(UpdateActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });
    }
}