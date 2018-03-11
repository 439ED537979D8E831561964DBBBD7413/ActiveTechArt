package com.artace.ruangbudaya.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.artace.ruangbudaya.EoSenimanActivity;
import com.artace.ruangbudaya.R;
import com.artace.ruangbudaya.ViewPlugins.CircularImageViewPicaso;
import com.artace.ruangbudaya.controller.AppController;
import com.artace.ruangbudaya.pojo.PojoSeniman;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by USER on 2/24/2018.
 */

public class ListSenimanMainAdapater extends RecyclerView.Adapter<ListSenimanMainAdapater.MyViewHolder> {

    private Context mContext;
    private List<PojoSeniman> senimanList;
    private PojoSeniman seniman;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nama;
        public ImageView foto,bg;
        public CardView CV;

        public MyViewHolder(View view) {
            super(view);
            nama = (TextView) view.findViewById(R.id.item_list_kelompok_seniman_main_namaseniman);
            foto = (ImageView) view.findViewById(R.id.item_list_kelompok_seniman_main_imagecircleseniman);
            bg = (ImageView) view.findViewById(R.id.item_list_kelompok_seniman_main_bgimage);
            CV = (CardView) view.findViewById(R.id.item_list_kelompok_seniman_main_cardviewseniman);
        }

    }
    public ListSenimanMainAdapater(Context mContext, List<PojoSeniman> senimanList) {

        this.mContext = mContext;
        this.senimanList = senimanList;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_kelompok_seniman_main, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        seniman = senimanList.get(position);
        holder.nama.setText(seniman.getNama());

        Picasso.with(mContext).load(seniman.getFoto()).transform(new CircularImageViewPicaso()).fit().into(holder.foto);
        Picasso.with(mContext).load(R.drawable.bg_seniman).fit().into(holder.bg);


        holder.CV.setTag(position);
        holder.CV.setOnClickListener(new View.OnClickListener() {
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

    }


    @Override
    public int getItemCount() {
        return senimanList.size();
    }

}
