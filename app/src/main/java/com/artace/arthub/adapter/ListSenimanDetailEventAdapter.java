package com.artace.arthub.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.artace.arthub.EoSenimanActivity;
import com.artace.arthub.R;
import com.artace.arthub.controller.AppController;
import com.artace.arthub.pojo.PojoSeniman;

import java.util.List;

/**
 * Created by USER on 11/12/2017.
 */

public class ListSenimanDetailEventAdapter extends RecyclerView.Adapter<ListSenimanDetailEventAdapter.MyViewHolder> {
    private Context mContext;
    private List<PojoSeniman> senimanList;
    private PojoSeniman seniman;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nama, keahlianSpesifik;
        public NetworkImageView foto;

        public MyViewHolder(View view) {
            super(view);
            nama = (TextView) view.findViewById(R.id.item_list_seniman_detail_event_nama);
            keahlianSpesifik = (TextView) view.findViewById(R.id.item_list_seniman_detail_event_subtitle);
            foto = (NetworkImageView) view.findViewById(R.id.item_list_seniman_detail_event_foto);
        }
    }

    public ListSenimanDetailEventAdapter(Context mContext, List<PojoSeniman> senimanList) {

        this.mContext = mContext;
        this.senimanList = senimanList;

    }

    @Override
    public ListSenimanDetailEventAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_seniman_detail_event, parent, false);

        return new ListSenimanDetailEventAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ListSenimanDetailEventAdapter.MyViewHolder holder, int position) {
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
