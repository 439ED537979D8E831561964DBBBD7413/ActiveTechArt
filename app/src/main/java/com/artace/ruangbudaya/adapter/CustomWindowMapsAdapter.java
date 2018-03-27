package com.artace.ruangbudaya.adapter;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.artace.ruangbudaya.EventMapsFragment;
import com.artace.ruangbudaya.R;
import com.artace.ruangbudaya.pojo.PojoEvent;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class CustomWindowMapsAdapter implements GoogleMap.InfoWindowAdapter {

    private Context context;

    public CustomWindowMapsAdapter(Context ctx){
        context = ctx;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = ((Activity)context).getLayoutInflater()
                .inflate(R.layout.custom_info_window, null);

        TextView nama = view.findViewById(R.id.custom_info_nama);
        TextView tanggal = view.findViewById(R.id.custom_info_tanggal);
        ImageView img = view.findViewById(R.id.custom_info_image);
        TextView keterangan = view.findViewById(R.id.custom_info_keterangan);

        PojoEvent event = (PojoEvent) marker.getTag();

        Picasso.with(context)
                .load(event.getFoto()).error(R.drawable.profile_img_circle)
                .into(img,new CustomWindowMapsAdapter.MarkerCallback(marker));

        nama.setText(event.getNama());
        tanggal.setText(event.getTanggal());
        keterangan.setText(event.getKeterangan());

        return view;
    }

    public static class MarkerCallback implements Callback {
        Marker marker=null;

        MarkerCallback(Marker marker) {
            this.marker=marker;
        }

        @Override
        public void onError() {
            Log.e(getClass().getSimpleName(), "Error loading thumbnail!");
        }

        @Override
        public void onSuccess() {
            if (marker != null && marker.isInfoWindowShown()) {
                marker.hideInfoWindow();
                marker.showInfoWindow();
            }
        }
    }
}

