package com.artace.ruangbudaya.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.artace.ruangbudaya.EoSenimanActivity;
import com.artace.ruangbudaya.OrganizerDiundangFragment;
import com.artace.ruangbudaya.R;
import com.artace.ruangbudaya.connection.DatabaseConnection;
import com.artace.ruangbudaya.controller.AppController;
import com.artace.ruangbudaya.pojo.PojoSeniman;
import com.artace.ruangbudaya.utils.StringPostRequest;
import com.artace.ruangbudaya.utils.VolleyResponseListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListDiundangAdapter extends RecyclerView.Adapter<ListDiundangAdapter.MyViewHolder> {

    private Context mContext;
    private List<PojoSeniman> senimanList;
    private PojoSeniman seniman;
    private OrganizerDiundangFragment fragment;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nama, textstatus, namaevent;
        public NetworkImageView foto;
        public LinearLayout containerStatus;
        public ImageView delete;

        public MyViewHolder(View view) {
            super(view);
            nama = (TextView) view.findViewById(R.id.item_list_diundang_nama);
            foto = (NetworkImageView) view.findViewById(R.id.item_list_diundang_foto);
            textstatus = (TextView) view.findViewById(R.id.item_list_diundang_textstatus);
            containerStatus = (LinearLayout) view.findViewById(R.id.item_list_diundang_containerstatus);
            delete = (ImageView) view.findViewById(R.id.item_list_diundang_delete);
            namaevent = (TextView) view.findViewById(R.id.item_list_diundang_namaevent);
        }
    }


    public ListDiundangAdapter(Context mContext, List<PojoSeniman> senimanList, OrganizerDiundangFragment fragment) {

        this.mContext = mContext;
        this.senimanList = senimanList;
        this.fragment = fragment;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_diundang, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        seniman = senimanList.get(position);
        holder.nama.setText(seniman.getNama());
        holder.namaevent.setText(seniman.getNamaevent());

        holder.foto.setTag(position);
        holder.foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, EoSenimanActivity.class);
                Bundle extras = new Bundle();
                int position = (Integer) view.getTag();
                seniman = senimanList.get(position);

                extras.putInt("id_kelompok_seniman",seniman.getId_kelompok_seniman());
                intent.putExtras(extras);
                mContext.startActivity(intent);
            }
        });

        holder.foto.setImageUrl(seniman.getFoto(), AppController.getInstance().getImageLoader());

        if(seniman.getStatus().equals("Pending")){
            holder.textstatus.setText("Pending");
            holder.containerStatus.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorAccent));
        }
        else if(seniman.getStatus().equals("Confirmed")){
            holder.textstatus.setText("Confirmed");
            holder.containerStatus.setBackgroundColor(ContextCompat.getColor(mContext, R.color.green_button_color));
        }
        else if(seniman.getStatus().equals("Rejected")){
            holder.textstatus.setText("Rejected");
            holder.containerStatus.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
        }

        Log.d("AdpDiundang","status : "+seniman.getStatus());

        holder.delete.setTag(position);
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final int position = (Integer) view.getTag();
                seniman = senimanList.get(position);

                Map<String,String> params = new HashMap<String, String>();
                params.put("id_tawaran_tampil",Integer.toString(seniman.getId_tawaran_tampil()));

                StringPostRequest strReq = new StringPostRequest();
                strReq.sendPost(mContext, params, DatabaseConnection.getDeleteTawaranTampil(), new VolleyResponseListener() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("ListDiundang","Berhasil : "+response);
                        senimanList.remove(position);
                        notifyDataSetChanged();
                    }

                    @Override
                    public void onError(String message) {
                        Log.e("ListDiundang","Ada error : "+message);
                    }
                });
            }
        });

    }


    @Override
    public int getItemCount() {
        return senimanList.size();
    }
}
