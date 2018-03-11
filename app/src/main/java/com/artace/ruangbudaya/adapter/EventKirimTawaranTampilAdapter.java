package com.artace.ruangbudaya.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.artace.ruangbudaya.EventKirimTawaranTampilActivty;
import com.artace.ruangbudaya.R;
import com.artace.ruangbudaya.connection.DatabaseConnection;
import com.artace.ruangbudaya.constant.Field;
import com.artace.ruangbudaya.controller.AppController;
import com.artace.ruangbudaya.pojo.PojoEvent;
import com.artace.ruangbudaya.utils.StringPostRequest;
import com.artace.ruangbudaya.utils.VolleyResponseListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventKirimTawaranTampilAdapter extends RecyclerView.Adapter<EventKirimTawaranTampilAdapter.MyViewHolder> {

    private List<PojoEvent> eventList;
    private Context context;
    private LayoutInflater inflater;
    PojoEvent event;
    EventKirimTawaranTampilActivty activity;

    public EventKirimTawaranTampilAdapter(Context context, List<PojoEvent> eventList, EventKirimTawaranTampilActivty activity) {

        this.activity = activity;
        this.context = context;
        this.eventList = eventList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View rootView = inflater.inflate(R.layout.item_event_kirim_tawaran_tampil, parent, false);
        return new MyViewHolder(rootView);

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        event = eventList.get(position);
        //Pass the values of feeds object to Views
        holder.namaEvent.setText(event.getNama());
        holder.tanggalEvent.setText(event.getTanggal());
        holder.namaEo.setText(event.getEo());

        Picasso.with(context).load(event.getFoto()).fit().into(holder.imageEvent);

        SharedPreferences sharedpreferences = context.getSharedPreferences(Field.getLoginSharedPreferences(), Context.MODE_PRIVATE);
        boolean session = sharedpreferences.getBoolean(Field.getSessionStatus(),false);

        holder.cardViewDetail.setTag(position);
        holder.cardViewDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = (Integer) view.getTag();
                final int positionFinal = position;

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                final View dialogView = inflater.inflate(R.layout.dialog_harga_tawaran_tampil, null);

                builder.setView(dialogView)

                        .setPositiveButton("kirim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                event = eventList.get(positionFinal);
                                final EditText nominal = (EditText) dialogView.findViewById(R.id.dialog_harga_tawaran_tampil_text);
                                kirimTawaranTampil(event.getId_acara(), nominal.getText().toString(), positionFinal);
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                builder.show();

            }
        });

    }

    public void kirimTawaranTampil(int id_acara, String harga, int position){
        final int positionFinal = position;

        Map<String,String> params = new HashMap<String, String>();
        params.put("id_acara",String.valueOf(id_acara));
        params.put("id_kelompok_seniman",activity.id_kelompok_seniman);
        params.put("harga",harga);
        params.put("status","Pending");

        StringPostRequest strReq = new StringPostRequest();
        strReq.sendPost(context, params, DatabaseConnection.getInsertTawaranTampil(), new VolleyResponseListener() {
            @Override
            public void onResponse(String response) {
                Log.d("EventKirim","Berhasil : "+response);
                eventList.remove(positionFinal);
                notifyDataSetChanged();
            }

            @Override
            public void onError(String message) {
                Log.e("ListDiundang","Ada error : "+message);
            }
        });
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView namaEvent, tanggalEvent, namaEo;
        private ImageView imageEvent;
        private CardView cardViewDetail;

        public MyViewHolder(View itemView) {

            super(itemView);
            namaEvent = (TextView) itemView.findViewById(R.id.item_event_kttNamaEvent);
            tanggalEvent = (TextView) itemView.findViewById(R.id.item_event_ktttanggalevent);
            namaEo = (TextView) itemView.findViewById(R.id.item_event_kttpenyelenggaraacara);
            // Volley's NetworkImageView which will load Image from URL
            imageEvent = (ImageView) itemView.findViewById(R.id.item_event_kttimage);
            cardViewDetail = (CardView) itemView.findViewById(R.id.item_event_kttcardView);
        }
    }

}
