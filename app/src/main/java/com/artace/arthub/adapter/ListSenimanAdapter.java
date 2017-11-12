package com.artace.arthub.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.NetworkImageView;
import com.artace.arthub.EoSenimanActivity;
import com.artace.arthub.R;
import com.artace.arthub.controller.AppController;
import com.artace.arthub.pojo.PojoSeniman;

import java.util.List;


public class ListSenimanAdapter extends RecyclerView.Adapter<ListSenimanAdapter.MyViewHolder> {

    private Context mContext;
    private List<PojoSeniman> senimanList;
    private PojoSeniman seniman;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nama, keahlianSpesifik;
        public NetworkImageView foto;

        public MyViewHolder(View view) {
            super(view);
            nama = (TextView) view.findViewById(R.id.item_list_seniman_nama);
            keahlianSpesifik = (TextView) view.findViewById(R.id.item_list_seniman_subtitle);
            foto = (NetworkImageView) view.findViewById(R.id.item_list_seniman_foto);
        }
    }


    public ListSenimanAdapter(Context mContext, List<PojoSeniman> senimanList) {

        this.mContext = mContext;
        this.senimanList = senimanList;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_seniman, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        seniman = senimanList.get(position);
        holder.nama.setText(seniman.getNama());
        holder.keahlianSpesifik.setText(seniman.getKeahlian_spesifik());

        holder.foto.setTag(position);
        holder.foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, EoSenimanActivity.class);
                Bundle extras = new Bundle();
                int position = (Integer) view.getTag();
                seniman = senimanList.get(position);

                extras.putInt("id_seniman",seniman.getId_seniman());
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
