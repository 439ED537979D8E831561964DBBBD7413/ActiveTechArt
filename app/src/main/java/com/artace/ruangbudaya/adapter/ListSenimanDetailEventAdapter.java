package com.artace.ruangbudaya.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.artace.ruangbudaya.EoSenimanActivity;
import com.artace.ruangbudaya.R;
import com.artace.ruangbudaya.controller.AppController;
import com.artace.ruangbudaya.pojo.PojoSeniman;

import java.util.List;

/**
 * Created by USER on 11/12/2017.
 */

public class ListSenimanDetailEventAdapter extends RecyclerView.Adapter<ListSenimanDetailEventAdapter.MyViewHolder> {
    private Context mContext;
    private List<PojoSeniman> senimanList;
    private PojoSeniman seniman;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nama;
        public NetworkImageView foto;

        public MyViewHolder(View view) {
            super(view);
            nama = (TextView) view.findViewById(R.id.item_list_seniman_detail_event_nama);
            foto = (NetworkImageView) view.findViewById(R.id.item_list_seniman_detail_event_foto);
        }
    }

    public ListSenimanDetailEventAdapter(Context mContext, List<PojoSeniman> senimanList) {

        this.mContext = mContext;
        this.senimanList = senimanList;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_seniman_detail_event, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        seniman = senimanList.get(position);
        holder.nama.setText(seniman.getNama());

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

    }


    @Override
    public int getItemCount() {
        return senimanList.size();
    }

}
