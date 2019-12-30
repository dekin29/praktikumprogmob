package com.example.praktikumprogmob.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.praktikumprogmob.APIHelper.BaseApiService;
import com.example.praktikumprogmob.APIHelper.UtilsApi;
import com.example.praktikumprogmob.Model.Komentar;
import com.example.praktikumprogmob.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KomentarAdapter extends RecyclerView.Adapter<KomentarAdapter.KomentarViewHolder> {
    private ArrayList<Komentar> dataList;
    private Context mContext;
    private SharedPreferences profile;
    BaseApiService mApiService;
    ProgressDialog loading;

    public KomentarAdapter(Context mContext, ArrayList<Komentar> dataList) {
        this.dataList = dataList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public KomentarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_komentar, parent, false);
        return new KomentarAdapter.KomentarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull KomentarViewHolder holder, final int position) {
        holder.tvUser.setText(dataList.get(position).getUser_name());
        holder.tvKomentar.setText(dataList.get(position).getKomentar());
        profile = mContext.getSharedPreferences("profile", Context.MODE_PRIVATE);
        int id = profile.getInt("id", 0);
        if (id == dataList.get(position).getId_user()){
            holder.btnHapus.setVisibility(View.VISIBLE);
        }

        holder.btnHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);
                String token = "Bearer " + profile.getString("access_token", null);
                mApiService = UtilsApi.getAPIServiceWithToken(token);
                mApiService.deleteKomentar(dataList.get(position).getId())
                        .enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if (response.isSuccessful()){
                                    loading.dismiss();
                                    try {
                                        JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                        if (jsonRESULTS.getString("pesan").equals("berhasil")){
                                            Toast.makeText(mContext, "Komentar Dihapus!", Toast.LENGTH_SHORT).show();
                                            notifyDataSetChanged();
                                        } else {
                                            String error_message = jsonRESULTS.getString("error_msg");
                                            Toast.makeText(mContext, error_message, Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    loading.dismiss();
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Log.e("debug", "onFailure: ERROR > " + t.toString());
                                loading.dismiss();
                            }
                        });
            }
        });

    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }

    public class KomentarViewHolder extends RecyclerView.ViewHolder {
        private TextView tvUser, tvKomentar, tvCreated;
        private ImageButton btnHapus;

        public KomentarViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUser = itemView.findViewById(R.id.namauser);
            tvKomentar = itemView.findViewById(R.id.komentar);
            btnHapus = itemView.findViewById(R.id.btn_delete_komen);
        }
    }
}
