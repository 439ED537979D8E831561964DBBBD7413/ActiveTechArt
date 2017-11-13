package com.artace.arthub.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.artace.arthub.EventDetailActivity;
import com.artace.arthub.EventKirimTawaranTampilActivty;
import com.artace.arthub.R;
import com.artace.arthub.connection.DatabaseConnection;
import com.artace.arthub.constant.Field;
import com.artace.arthub.controller.AppController;
import com.artace.arthub.pojo.PojoEvent;
import com.artace.arthub.utils.StringPostRequest;
import com.artace.arthub.utils.VolleyResponseListener;

import org.json.JSONException;
import org.json.JSONObject;

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
        holder.tempatEvent.setText(event.getLokasi());
        holder.namaEo.setText(event.getEo());
        holder.imageEvent.setImageUrl(event.getFoto(), AppController.getInstance().getImageLoader());

        holder.btnDetail.setTag(position);

        SharedPreferences sharedpreferences = context.getSharedPreferences(Field.getLoginSharedPreferences(), Context.MODE_PRIVATE);
        boolean session = sharedpreferences.getBoolean(Field.getSessionStatus(),false);

        holder.btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = (Integer) view.getTag();
                final int positionFinal = position;

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                final View dialogView = inflater.inflate(R.layout.item_event_kirim_tawaran_tampil, null);
                builder.setView(dialogView);

                final EditText nominal = (EditText) dialogView.findViewById(R.id.dialog_harga_tawaran_tampil_text);

                builder.setView(inflater.inflate(R.layout.item_event_kirim_tawaran_tampil, null))

                        .setPositiveButton("kirim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                event = eventList.get(positionFinal);
                                kirimTawaranTampil(event.getId_event(), nominal.getText().toString(), positionFinal);
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

    public void kirimTawaranTampil(int id_event, String harga, int position){
        final int positionFinal = position;

        Map<String,String> params = new HashMap<String, String>();
        params.put("id_event",String.valueOf(id_event));
        params.put("id_seniman",activity.id_seniman);
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

        private TextView namaEvent, tanggalEvent, tempatEvent,namaEo;
        private NetworkImageView imageEvent;
        private Button btnDetail, btnHapus;
        private FrameLayout deleteContainer;

        public MyViewHolder(View itemView) {

            super(itemView);
            namaEvent = (TextView) itemView.findViewById(R.id.item_event_list_namaEvent);
            tanggalEvent = (TextView) itemView.findViewById(R.id.item_event_list_tanggalEvent);
            tempatEvent = (TextView) itemView.findViewById(R.id.item_event_list_tempatEvent);
            namaEo = (TextView) itemView.findViewById(R.id.item_event_list_eo);
            // Volley's NetworkImageView which will load Image from URL
            imageEvent = (NetworkImageView) itemView.findViewById(R.id.item_event_list_imageEvent);
            btnDetail = (Button) itemView.findViewById(R.id.item_event_list_btnDetail);
            btnHapus = (Button) itemView.findViewById(R.id.item_event_list_btnHapus);
            deleteContainer = (FrameLayout) itemView.findViewById(R.id.item_event_list_hapusContainer);
        }
    }

}
