package com.artace.ruangbudaya.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.artace.ruangbudaya.R;
import com.artace.ruangbudaya.SenimanListTawaranFragment;
import com.artace.ruangbudaya.ViewPlugins.CircularImageViewPicaso;
import com.artace.ruangbudaya.connection.DatabaseConnection;
import com.artace.ruangbudaya.pojo.PojoListTawaran;
import com.artace.ruangbudaya.utils.StringPostRequest;
import com.artace.ruangbudaya.utils.VolleyResponseListener;
import com.squareup.picasso.Picasso;

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
        holder.hargaEvent.setText("IDR "+event.getHarga()+",-");
        holder.namaEo.setText(event.getEo());

        Picasso.with(context).load(event.getFoto()).transform(new CircularImageViewPicaso()).fit().into(holder.imageEvent);

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

        holder.btnTerima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = (Integer) view.getTag();
                final int positionFinal = position;
                String status = "Confirmed";

                event = eventList.get(positionFinal);
                updateEvent(event.getId_tawaran_tampil(), context, status, positionFinal);

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
                                        updateEvent(event.getId_tawaran_tampil(), context, status, positionFinal);
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
                                        updateEvent(event.getId_tawaran_tampil(), context, status, positionFinal);

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

    public void updateEvent(int id_tawaran_tampil, Context context, final String status, int position){

        final Context contextFinal = context;
        final int positionFinal = position;

        Map<String,String> params = new HashMap<String, String>();
        params.put("id_tawaran_tampil",Integer.toString(id_tawaran_tampil));
        params.put("status",status);

        StringPostRequest strReq = new StringPostRequest();
        strReq.sendPost(contextFinal, params, DatabaseConnection.getUpdateTerimaTolakTawaranTampil(), new VolleyResponseListener() {
            @Override
            public void onResponse(String response) {
                if(status == "Rejected"){
                    eventList.remove(positionFinal);
                    notifyDataSetChanged();
                }
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

        private TextView namaEvent, tanggalEvent, namaEo,hargaEvent;
        private ImageView imageEvent;
        private Button btnTerima, btnTolak, btnCancel;
        private CardView listCard;

        public MyViewHolder(View itemView) {

            super(itemView);
            listCard = (CardView) itemView.findViewById(R.id.item_list_tawaran_cardView);
            namaEvent = (TextView) itemView.findViewById(R.id.item_event_kttNamaEvent);
            tanggalEvent = (TextView) itemView.findViewById(R.id.item_list_tawaran_tanggalevent);

            namaEo = (TextView) itemView.findViewById(R.id.item_list_tawaran_penyelenggaraevent);
            hargaEvent = (TextView) itemView.findViewById(R.id.item_list_tawaran_harga);
            // Volley's NetworkImageView which will load Image from URL
            imageEvent = (ImageView) itemView.findViewById(R.id.item_list_tawaran_image);
            btnTerima = (Button) itemView.findViewById(R.id.item_list_tawaran_btnTerima);
            btnTolak = (Button) itemView.findViewById(R.id.item_list_tawaran_btnTolak);
            btnCancel = (Button) itemView.findViewById(R.id.item_list_tawaran_btnCancel);
        }
    }
}