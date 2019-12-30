package com.example.praktikumprogmob.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.praktikumprogmob.APIHelper.UtilsApi;
import com.example.praktikumprogmob.DetailPost;
import com.example.praktikumprogmob.Model.Post;
import com.example.praktikumprogmob.R;

import java.util.ArrayList;
import java.util.List;

public class Post2Adapter extends RecyclerView.Adapter<Post2Adapter.PostViewHolder>{
    private List<Post> dataList;
    private Context mContext;

    public Post2Adapter(Context mContext, List<Post> dataList){
        this.dataList = dataList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, final int position) {
        Log.d("debug","Isi Adapter : "+dataList.get(position).getFoto());
        holder.tvJudul.setText(dataList.get(position).getJudul());
        holder.tvNama.setText(dataList.get(position).getUser_name());
        holder.tvCreated.setText(dataList.get(position).getCreated_at());
        if(mContext != null){
            Glide.with(mContext)
                    .load(UtilsApi.BASE_URL_API + dataList.get(position).getFoto())
                    .into(holder.ivFoto);
        }
        holder.cvPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle extras = new Bundle();
                extras.putInt("idPost",dataList.get(position).getId());
                Intent intent = new Intent(mContext, DetailPost.class);
                intent.putExtras(extras);
                mContext.startActivity(intent);
//                ((Activity)mContext).finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {
        private TextView tvJudul, tvNama, tvCreated;
        private ImageView ivFoto;
        private CardView cvPost;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            tvJudul = itemView.findViewById(R.id.tv_judul);
            tvNama = itemView.findViewById(R.id.tv_namauser);
            tvCreated = itemView.findViewById(R.id.tv_created);
            ivFoto = itemView.findViewById(R.id.iv_foto);
            cvPost = itemView.findViewById(R.id.cv_post);
        }
    }
}
