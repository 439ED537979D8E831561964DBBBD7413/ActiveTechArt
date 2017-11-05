package com.artace.arthub.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Network;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.artace.arthub.EoSenimanActivity;
import com.artace.arthub.EventDetailActivity;
import com.artace.arthub.R;
import com.artace.arthub.ViewPlugins.CircularNetworkImageView;
import com.artace.arthub.YoutubeWebViewActivity;
import com.artace.arthub.connection.DatabaseConnection;
import com.artace.arthub.constant.Field;
import com.artace.arthub.controller.AppController;
import com.artace.arthub.pojo.PojoEvent;
import com.artace.arthub.pojo.PojoSeniman;
import com.artace.arthub.utils.YoutubeId;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SenimanPortfolioListAdapter extends RecyclerView.Adapter<SenimanPortfolioListAdapter.MyViewHolder> {

    private List<PojoSeniman> senimanList;
    private Context context;
    private LayoutInflater inflater;
    PojoSeniman seniman;

    public SenimanPortfolioListAdapter(Context context, List<PojoSeniman> senimanList) {

        this.context = context;
        this.senimanList = senimanList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View rootView = inflater.inflate(R.layout.activity_item_portfolio_list, parent, false);
        return new MyViewHolder(rootView);

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        seniman = senimanList.get(position);

        YoutubeId generateYoutubeId = new YoutubeId();
        String idThumbnail = generateYoutubeId.generateId(seniman.getPortfolio());

        holder.mNama.setText(seniman.getNama());
        holder.mFoto.setImageUrl(seniman.getFoto(), AppController.getInstance().getImageLoader());
        holder.mThumbnail.setImageUrl("https://img.youtube.com/vi/"+idThumbnail+"/0.jpg", AppController.getInstance().getImageLoader());
        holder.mKeahlianSpesifik.setText(seniman.getKeahlian_spesifik());
        holder.mJenisKelamin.setText(seniman.getJenis_kelamin());
        holder.mUmur.setText(seniman.getUmur());

        holder.mProfileContainer.setTag(position);
        holder.mThumbnail.setTag(position);

        SharedPreferences sharedpreferences = context.getSharedPreferences(Field.getLoginSharedPreferences(), Context.MODE_PRIVATE);
        boolean session = sharedpreferences.getBoolean(Field.getSessionStatus(),false);

        if (session || sharedpreferences.getString(Field.getJenisUser(),null) == "event_organizer"){
            holder.mProfileContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, EoSenimanActivity.class);
                    Bundle extras = new Bundle();
                    int position = (Integer) view.getTag();
                    seniman = senimanList.get(position);

                    extras.putInt("id_seniman",seniman.getId_seniman());
                    intent.putExtras(extras);
                    context.startActivity(intent);
                }
            });
        }

        holder.mThumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, YoutubeWebViewActivity.class);
                intent.putExtra("url", seniman.getPortfolio());
                context.startActivity(intent);
//                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(seniman.getPortfolio())));
            }
        });

    }

    @Override
    public int getItemCount() {
        return senimanList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView mNama, mKeahlianSpesifik, mJenisKelamin, mUmur;
        private CircularNetworkImageView mFoto;
        private NetworkImageView mThumbnail;
        private ConstraintLayout mProfileContainer;

        public MyViewHolder(View itemView) {

            super(itemView);
            mNama = (TextView) itemView.findViewById(R.id.item_portfolio_list_namaSeniman);
            mKeahlianSpesifik = (TextView) itemView.findViewById(R.id.item_portfolio_list_keahlianSeniman);
            mJenisKelamin = (TextView) itemView.findViewById(R.id.item_portfolio_list_jkSeniman);
            mUmur = (TextView) itemView.findViewById(R.id.item_portfolio_list_umurSeniman);
            mFoto = (CircularNetworkImageView) itemView.findViewById(R.id.item_portfolio_list_fotoSeniman);
            mProfileContainer = (ConstraintLayout) itemView.findViewById(R.id.item_portfolio_list_profileContainer);
            mThumbnail = (NetworkImageView) itemView.findViewById(R.id.item_portfolio_list_thumbnail);

        }
    }

}
