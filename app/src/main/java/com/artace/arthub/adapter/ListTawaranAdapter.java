package com.artace.arthub.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.artace.arthub.EventDetailActivity;
import com.artace.arthub.R;
import com.artace.arthub.SenimanListTawaranFragment;
import com.artace.arthub.ViewPlugins.CircularNetworkImageView;
import com.artace.arthub.connection.DatabaseConnection;
import com.artace.arthub.constant.Field;
import com.artace.arthub.controller.AppController;
import com.artace.arthub.pojo.PojoListTawaran;
import com.artace.arthub.utils.StringPostRequest;
import com.artace.arthub.utils.VolleyResponseListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListTawaranAdapter extends RecyclerView.Adapter<ListTawaranAdapter.MyViewHolder> {

    private List<PojoListTawaran> eventList;
    private Context context;
    private LayoutInflater inflater;
    PojoListTawaran event;
    SenimanListTawaranFragment fragment;

    public ListTawaranAdapter(Context context, List<PojoListTawaran> eventList, SenimanListTawaranFragment fragment) {

        this.context = context;
        this.eventList = eventList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.fragment = fragment;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View rootView = inflater.inflate(R.layout.item_list_tawaran, parent, false);
        return new MyViewHolder(rootView);

    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        event = eventList.get(position);
        //Pass the values of feeds object to Views
        holder.namaEvent.setText(event.getNama());
        holder.tanggalEvent.setText(event.getTanggal());
        holder.tempatEvent.setText(event.getLokasi());
        holder.hargaEvent.setText("IDR "+event.getHarga()+",-");
        holder.namaEo.setText(event.getEo());
        holder.imageEvent.setImageUrl(event.getFoto(), AppController.getInstance().getImageLoader());
        holder.keterangan.setText(event.getKeterangan());
        holder.btnTerima.setTag(position);
        holder.btnTolak.setTag(position);
        holder.btnCancel.setTag(position);

        if (event.getStatus().equals("Confirmed")){
            holder.btnTerima.setVisibility(View.GONE);
            holder.btnTolak.setVisibility(View.GONE);
            holder.btnCancel.setVisibility(View.VISIBLE);
        }
        else if (event.getStatus().equals("Pending")){
            holder.btnTerima.setVisibility(View.VISIBLE);
            holder.btnTolak.setVisibility(View.VISIBLE);
            holder.btnCancel.setVisibility(View.GONE);
        }
        //OnClicks

        holder.listCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (holder.topKeterangan.getVisibility() == View.VISIBLE && holder.keterangan.getVisibility() == View.VISIBLE){
                    holder.topKeterangan.setVisibility(view.GONE);
                    holder.keterangan.setVisibility(view.GONE);
                    holder.namaEo.setText(event.getEo());
                }
                else{
                    holder.namaEo.setText(event.getNama_eo());
                    holder.topKeterangan.setVisibility(view.VISIBLE);
                    holder.keterangan.setVisibility(view.VISIBLE);
                }
            }
        });

        holder.btnTerima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = (Integer) view.getTag();
                final int positionFinal = position;
                String status = "Confirmed";

                event = eventList.get(positionFinal);
                updateEvent(event.getId_tawaran_tampil(), context, status);

                holder.btnTerima.setVisibility(View.GONE);
                holder.btnTolak.setVisibility(View.GONE);
                holder.btnCancel.setVisibility(View.VISIBLE);
            }
        });

        holder.btnTolak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = (Integer) view.getTag();
                final int positionFinal = position;
                final String status = "Rejected";

                AlertDialog.Builder alertDialogBuilder =
                        new AlertDialog.Builder(context)
                                .setTitle("Apakah anda yakin ?")
                                .setMessage("Event yang anda TOLAK tidak akan bisa di lihat lagi")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        event = eventList.get(positionFinal);
                                        updateEvent(event.getId_tawaran_tampil(), context, status);
                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });

                alertDialogBuilder.show();
            }
        });

        holder.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = (Integer) view.getTag();
                final int positionFinal = position;
                final String status = "Pending";

                AlertDialog.Builder alertDialogBuilder =
                        new AlertDialog.Builder(context)
                                .setTitle("Apakah anda yakin ?")
                                .setMessage("Event yang anda cancel dapat di terima lagi")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        event = eventList.get(positionFinal);
                                        updateEvent(event.getId_tawaran_tampil(), context, status);
                                        holder.btnTerima.setVisibility(View.VISIBLE);
                                        holder.btnTolak.setVisibility(View.VISIBLE);
                                        holder.btnCancel.setVisibility(View.GONE);
                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });

                alertDialogBuilder.show();
            }
        });
    }

    public void updateEvent(int id_tawaran_tampil, Context context, String status){

        final Context contextFinal = context;

        Map<String,String> params = new HashMap<String, String>();
        params.put("id_tawaran_tampil",Integer.toString(id_tawaran_tampil));
        params.put("status",status);

        StringPostRequest strReq = new StringPostRequest();
        strReq.sendPost(contextFinal, params, DatabaseConnection.getUpdateTerimaTolakTawaranTampil(), new VolleyResponseListener() {
            @Override
            public void onResponse(String response) {
            }

            @Override
            public void onError(String message) {
                Log.e("UpdateTerima","Ada error : "+message);
            }
        });
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private View topKeterangan;
        private TextView namaEvent, tanggalEvent, tempatEvent,namaEo,hargaEvent,keterangan;
        private CircularNetworkImageView imageEvent;
        private Button btnTerima, btnTolak, btnCancel;
        private CardView listCard;

        public MyViewHolder(View itemView) {

            super(itemView);
            listCard = (CardView) itemView.findViewById(R.id.item_list_tawaran_cardView);
            namaEvent = (TextView) itemView.findViewById(R.id.item_list_tawaran_namaEvent);
            tanggalEvent = (TextView) itemView.findViewById(R.id.item_list_tawaran_tanggalEvent);
            tempatEvent = (TextView) itemView.findViewById(R.id.item_list_tawaran_tempatEvent);
            namaEo = (TextView) itemView.findViewById(R.id.item_list_tawaran_eo);
            keterangan = (TextView) itemView.findViewById(R.id.item_list_tawaran_keterangan);
            topKeterangan = (View) itemView.findViewById(R.id.item_list_tawaran_viewKeterangan);
            hargaEvent = (TextView) itemView.findViewById(R.id.item_list_tawaran_hargaEvent);
            // Volley's NetworkImageView which will load Image from URL
            imageEvent = (CircularNetworkImageView) itemView.findViewById(R.id.item_list_tawaran_imageEvent);
            btnTerima = (Button) itemView.findViewById(R.id.item_list_tawaran_btnTerima);
            btnTolak = (Button) itemView.findViewById(R.id.item_list_tawaran_btnTolak);
            btnCancel = (Button) itemView.findViewById(R.id.item_list_tawaran_btnCancel);
        }
    }
}