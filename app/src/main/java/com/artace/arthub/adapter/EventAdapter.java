package com.artace.arthub.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.NetworkImageView;
import com.artace.arthub.EventDetailActivity;
import com.artace.arthub.R;
import com.artace.arthub.controller.AppController;
import com.artace.arthub.pojo.PojoEvent;

import java.util.List;

/**
 * Created by AndroidNovice on 6/5/2016.
 */
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
        holder.tempatEvent.setText(event.getLokasi());
        holder.namaEo.setText(event.getEo());
        holder.imageEvent.setImageUrl(event.getFoto(), AppController.getInstance().getImageLoader());

        holder.btnDetail.setTag(position);

        //OnClicks

        holder.btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EventDetailActivity.class);
                Bundle extras = new Bundle();
                int position = (Integer) view.getTag();
                event = eventList.get(position);

                extras.putInt("id_event",event.getId_event());
                intent.putExtras(extras);
                context.startActivity(intent);
            }
        });


        holder.btnHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO : ngapain pas di click ?
                //Toast.makeText(context, "Rated By User : " + eventList.get(getAdapterPosition()).getRating(), Toast.LENGTH_SHORT).show();
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

        }
    }

}