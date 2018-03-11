package com.artace.ruangbudaya.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.MyViewHolder> {

    private List<PojoEvent> eventList;
    private Context context;
    private LayoutInflater inflater;
    PojoEvent event;

    public EventAdapter(Context context, List<PojoEvent> eventList) {

        this.context = context;
        this.eventList = eventList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View rootView = inflater.inflate(R.layout.activity_item_event_list, parent, false);
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

        if (!session || !sharedpreferences.getString(Field.getJenisUser(),null).equals("event_organizer")){

            holder.btnHapus.setBackgroundColor(context.getResources().getColor(R.color.primary));
        }
        holder.btnHapus.setTag(position);

        //OnClicks

        holder.cardviewDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EventDetailActivity.class);
                Bundle extras = new Bundle();
                int position = (Integer) view.getTag();
                event = eventList.get(position);

                extras.putInt("id_acara",event.getId_acara());
                intent.putExtras(extras);
                context.startActivity(intent);
            }
        });

        holder.btnHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = (Integer) view.getTag();
                final int positionFinal = position;
                AlertDialog.Builder alertDialogBuilder =
                        new AlertDialog.Builder(context)
                                .setTitle("Apakah anda yakin ?")
                                .setMessage("Event yang terhapus tidak dapat dikembalikan lagi")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        event = eventList.get(positionFinal);
                                        deleteEvent(dialog,event.getId_acara(), context, positionFinal);
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

    public void deleteEvent(DialogInterface dialog, int id_acara, Context context, int position){

        String delete_url = DatabaseConnection.getDeleteEvent(id_acara);

        final DialogInterface dialogFinal = dialog;
        final Context contextFinal = context;
        final int positionFinal = position;
        final ProgressDialog pDialog = new ProgressDialog(contextFinal);
        pDialog.setCancelable(false);
        pDialog.setMessage("Deleting...");
        pDialog.show();

        JsonObjectRequest delete_request = new JsonObjectRequest(delete_url,
                null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {
                    int success = response.getInt("success");

                    if (success == 1) {
                        pDialog.dismiss();
                        eventList.remove(positionFinal);
                        notifyDataSetChanged();
                        dialogFinal.dismiss();
                    } else {
                        dialogFinal.dismiss();
                        Toast.makeText(contextFinal,
                                "DELETE FAILED !", Toast.LENGTH_LONG)
                                .show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(delete_request);
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView namaEvent, tanggalEvent, tempatEvent,namaEo;
        private ImageView imageEvent;
        private Button btnHapus;
        private CardView cardviewDetail;

        public MyViewHolder(View itemView) {

            super(itemView);

            namaEvent = (TextView) itemView.findViewById(R.id.item_event_list_namaEvent);
            tanggalEvent = (TextView) itemView.findViewById(R.id.item_event_list_tanggalEvent);

            namaEo = (TextView) itemView.findViewById(R.id.item_event_list_penyelenggaraAcara);
//             Volley's NetworkImageView which will load Image from URL
            imageEvent = (ImageView) itemView.findViewById(R.id.item_event_list_imageevent);
            cardviewDetail = (CardView) itemView.findViewById(R.id.item_event_list_cardView);
            btnHapus = (Button) itemView.findViewById(R.id.item_event_list_btndelete);
        }
    }

}