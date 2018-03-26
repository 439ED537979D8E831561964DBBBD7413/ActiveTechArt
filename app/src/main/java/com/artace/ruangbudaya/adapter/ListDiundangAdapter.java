package com.artace.ruangbudaya.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.artace.ruangbudaya.EoSenimanActivity;
import com.artace.ruangbudaya.GenerateQrCodeActivity;
import com.artace.ruangbudaya.OrganizerDiundangFragment;
import com.artace.ruangbudaya.R;
import com.artace.ruangbudaya.connection.DatabaseConnection;
import com.artace.ruangbudaya.controller.AppController;
import com.artace.ruangbudaya.pojo.PojoSeniman;
import com.artace.ruangbudaya.utils.StringPostRequest;
import com.artace.ruangbudaya.utils.VolleyResponseListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListDiundangAdapter extends RecyclerView.Adapter<ListDiundangAdapter.MyViewHolder> {

    private Context mContext;
    private List<PojoSeniman> senimanList;
    private PojoSeniman seniman;
    private OrganizerDiundangFragment fragment;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nama, textstatus, namaevent;
        public NetworkImageView foto;
        public LinearLayout containerStatus;
        public ImageView delete,qrcode;
        public Bitmap bitmap;

        public MyViewHolder(View view) {
            super(view);
            nama = (TextView) view.findViewById(R.id.item_list_diundang_nama);
            foto = (NetworkImageView) view.findViewById(R.id.item_list_diundang_foto);
            textstatus = (TextView) view.findViewById(R.id.item_list_diundang_textstatus);
            containerStatus = (LinearLayout) view.findViewById(R.id.item_list_diundang_containerstatus);
            delete = (ImageView) view.findViewById(R.id.item_list_diundang_delete);
            qrcode = (ImageView) view.findViewById(R.id.item_list_diundang_qrcode);
            namaevent = (TextView) view.findViewById(R.id.item_list_diundang_namaevent);
        }
    }


    public ListDiundangAdapter(Context mContext, List<PojoSeniman> senimanList, OrganizerDiundangFragment fragment) {

        this.mContext = mContext;
        this.senimanList = senimanList;
        this.fragment = fragment;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_diundang, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        seniman = senimanList.get(position);
        holder.nama.setText(seniman.getNama());
        holder.namaevent.setText(seniman.getNamaevent());

        holder.foto.setTag(position);
        holder.foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, EoSenimanActivity.class);
                Bundle extras = new Bundle();
                int position = (Integer) view.getTag();
                seniman = senimanList.get(position);

                extras.putInt("id_kelompok_seniman",seniman.getId_kelompok_seniman());
                intent.putExtras(extras);
                mContext.startActivity(intent);
            }
        });

        holder.foto.setImageUrl(seniman.getFoto(), AppController.getInstance().getImageLoader());

        if(seniman.getStatus().equals("Pending")){
            holder.textstatus.setText("Pending");
            holder.containerStatus.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorAccent));
        }
        else if(seniman.getStatus().equals("Confirmed")){
            if (seniman.getStatus_kehadiran() == 1){
                holder.textstatus.setText("Telah Hadir");
                holder.containerStatus.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
                holder.qrcode.setVisibility(View.GONE);
                holder.delete.setVisibility(View.VISIBLE);
            }
            else {
                holder.textstatus.setText("Confirmed");
                holder.containerStatus.setBackgroundColor(ContextCompat.getColor(mContext, R.color.green_button_color));
                holder.qrcode.setVisibility(View.VISIBLE);
                holder.delete.setVisibility(View.GONE);
            }
        }
        else if(seniman.getStatus().equals("Rejected")){
            holder.textstatus.setText("Rejected");
            holder.containerStatus.setBackgroundColor(ContextCompat.getColor(mContext, R.color.md_red_700));
        }

        Log.d("AdpDiundang","status : "+seniman.getStatus());

        try {
            holder.bitmap = TextToImageEncode(String.valueOf(seniman.getId_tawaran_tampil()));
            holder.qrcode.setImageBitmap(holder.bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
            Log.e("gamao",e.getMessage());
        }

        holder.qrcode.setTag(position);
        holder.qrcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, GenerateQrCodeActivity.class);
                Bundle extras = new Bundle();
                int position = (Integer) view.getTag();
                seniman = senimanList.get(position);

                extras.putInt("id_tawaran_tampil",seniman.getId_tawaran_tampil());
                extras.putString("namaevent", seniman.getNamaevent());
                extras.putString("namaeo", seniman.getNamaeo());
                extras.putString("tanggal", seniman.getTanggalevent());
                intent.putExtras(extras);
                mContext.startActivity(intent);
            }
        });

        holder.delete.setTag(position);
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final int position = (Integer) view.getTag();
                seniman = senimanList.get(position);

                Map<String,String> params = new HashMap<String, String>();
                params.put("id_tawaran_tampil",Integer.toString(seniman.getId_tawaran_tampil()));

                StringPostRequest strReq = new StringPostRequest();
                strReq.sendPost(mContext, params, DatabaseConnection.getDeleteTawaranTampil(), new VolleyResponseListener() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("ListDiundang","Berhasil : "+response);
                        senimanList.remove(position);
                        notifyDataSetChanged();
                    }

                    @Override
                    public void onError(String message) {
                        Log.e("ListDiundang","Ada error : "+message);
                    }
                });
            }
        });

    }

    private Bitmap TextToImageEncode(String Value) throws WriterException {
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(
                    Value,
                    BarcodeFormat.DATA_MATRIX.QR_CODE,
                    35, 35, null
            );

        } catch (IllegalArgumentException Illegalargumentexception) {

            return null;
        }
        int bitMatrixWidth = bitMatrix.getWidth();

        int bitMatrixHeight = bitMatrix.getHeight();

        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

        for (int y = 0; y < bitMatrixHeight; y++) {
            int offset = y * bitMatrixWidth;

            for (int x = 0; x < bitMatrixWidth; x++) {

                pixels[offset + x] = bitMatrix.get(x, y) ?
                        mContext.getApplicationContext().getResources().getColor(R.color.black):mContext.getApplicationContext().getResources().getColor(R.color.white);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);

        bitmap.setPixels(pixels, 0, 35, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;
    }


    @Override
    public int getItemCount() {
        return senimanList.size();
    }
}
