package com.example.cpiapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cpiapp.R;
import com.example.cpiapp.model.TeacherData;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TeacherAdapter extends RecyclerView.Adapter<TeacherAdapter.TeacherViewAdapter> {
    private List<TeacherData> list;
    private Context context;

    public TeacherAdapter(List<TeacherData> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public TeacherViewAdapter onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.faculty_iteam_layout,parent,false);

        return new TeacherViewAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull TeacherAdapter.TeacherViewAdapter holder, int position) {
        TeacherData iteam=list.get(position);
        holder.name.setText(iteam.getName());
        holder.email.setText(iteam.getEmail());
        holder.post.setText(iteam.getPost());

        try {
            Picasso.get().load(iteam.getImage()).into(holder.teacher_imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"Update Teacher",Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public  class TeacherViewAdapter extends RecyclerView.ViewHolder {
        private TextView name,email,post;
        private Button update_btn;
        private ImageView teacher_imageView;

        public TeacherViewAdapter(@NonNull @NotNull View itemView) {
            super(itemView);

            name=itemView.findViewById(R.id.teachername);
            email=itemView.findViewById(R.id.teacheremail);
            post=itemView.findViewById(R.id.teacherepost);
            update_btn=itemView.findViewById(R.id.teacher_btn);
            teacher_imageView=itemView.findViewById(R.id.teacher_image);



        }
    }
}
