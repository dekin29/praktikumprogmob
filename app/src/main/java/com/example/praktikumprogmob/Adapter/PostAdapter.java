package com.example.praktikumprogmob.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.praktikumprogmob.APIHelper.UtilsApi;
import com.example.praktikumprogmob.Model.Post;
import com.example.praktikumprogmob.R;

import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder>{
    private ArrayList<Post> dataList;
    private Context mContext;

    public PostAdapter(Context mContext, ArrayList<Post> dataList){
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
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Log.d("debug","Isi Adapter : "+dataList.get(position).getFoto());
        holder.tvJudul.setText(dataList.get(position).getJudul());
        holder.tvNama.setText(dataList.get(position).getId_user());
        holder.tvCreated.setText(dataList.get(position).getCreated_at());
        Glide.with(mContext)
                .load(UtilsApi.BASE_URL_API+"/profileimages/"+dataList.get(position).getFoto())
                .into(holder.ivFoto);
    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {
        private TextView tvJudul, tvNama, tvCreated;
        private ImageView ivFoto;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            tvJudul = itemView.findViewById(R.id.tv_judul);
            tvNama = itemView.findViewById(R.id.tv_namauser);
            tvCreated = itemView.findViewById(R.id.tv_created);
            ivFoto = itemView.findViewById(R.id.iv_foto);

        }
    }
}
