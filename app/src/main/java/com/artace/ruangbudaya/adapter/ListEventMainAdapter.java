package com.artace.ruangbudaya.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.artace.ruangbudaya.EventDetailActivity;
import com.artace.ruangbudaya.R;
import com.artace.ruangbudaya.ViewPlugins.CircularImageViewPicaso;
import com.artace.ruangbudaya.connection.DatabaseConnection;
import com.artace.ruangbudaya.constant.Field;
import com.artace.ruangbudaya.controller.AppController;
import com.artace.ruangbudaya.pojo.PojoEvent;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by USER on 2/24/2018.
 */

public class ListEventMainAdapter extends RecyclerView.Adapter<ListEventMainAdapter.MyViewHolder>{

    private List<PojoEvent> eventList;
    private Context context;
    private LayoutInflater inflater;
    PojoEvent event;

    public ListEventMainAdapter(Context context, List<PojoEvent> eventList) {

        this.context = context;
        this.eventList = eventList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View rootView = inflater.inflate(R.layout.item_list_acara_main, parent, false);
        return new MyViewHolder(rootView);

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        event = eventList.get(position);
        //Pass the values of feeds object to Views
        holder.namaEvent.setText(event.getNama());
        holder.tanggalEvent.setText(event.getTanggal());

        Picasso.with(context).load(event.getFoto()).fit().into((holder.imageEvent));

        holder.imageEvent.setTag(position);
        //OnClicks

        holder.imageEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = (Integer) view.getTag();
                event = eventList.get(position);

                Intent intent = new Intent(context, EventDetailActivity.class);
                Bundle extras = new Bundle();
                extras.putInt("id_acara",event.getId_acara());
                intent.putExtras(extras);
                context.startActivity(intent);
            }
        });

    }



    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView namaEvent, tanggalEvent;
        private ImageView imageEvent;

        public MyViewHolder(View itemView) {

            super(itemView);
            namaEvent = (TextView) itemView.findViewById(R.id.item_list_acara_main_namaacara);
            tanggalEvent = (TextView) itemView.findViewById(R.id.item_list_acara_main_tanggalacara);
//             Volley's NetworkImageView which will load Image from URL
            imageEvent = (ImageView) itemView.findViewById(R.id.item_list_acara_main_imageacara);
        }
    }
}
