package com.artace.ruangbudaya.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.artace.ruangbudaya.EoSenimanActivity;
import com.artace.ruangbudaya.R;
import com.artace.ruangbudaya.ViewPlugins.CircularNetworkImageView;
import com.artace.ruangbudaya.YoutubePlayerViewActivity;
import com.artace.ruangbudaya.constant.Field;
import com.artace.ruangbudaya.controller.AppController;
import com.artace.ruangbudaya.pojo.PojoSeniman;
import com.artace.ruangbudaya.utils.YoutubeId;

import java.util.List;


public class ListSenimanAdapter extends RecyclerView.Adapter<ListSenimanAdapter.MyViewHolder> {

    private Context mContext;
    private List<PojoSeniman> senimanList;
    private PojoSeniman seniman;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView mNama, mKeahlianSpesifik, mJenisKelamin, mUmur;
        private CircularNetworkImageView mFoto;
        private NetworkImageView mThumbnail;
        private ConstraintLayout mProfileContainer;

        public MyViewHolder(View view) {
            super(view);
            mNama = (TextView) itemView.findViewById(R.id.item_portfolio_list_namaSeniman);
            mFoto = (CircularNetworkImageView) itemView.findViewById(R.id.item_portfolio_list_fotoSeniman);
            mProfileContainer = (ConstraintLayout) itemView.findViewById(R.id.item_portfolio_list_profileContainer);
            mThumbnail = (NetworkImageView) itemView.findViewById(R.id.item_portfolio_list_thumbnail);
        }
    }


    public ListSenimanAdapter(Context mContext, List<PojoSeniman> senimanList) {

        this.mContext = mContext;
        this.senimanList = senimanList;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_item_portfolio_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        seniman = senimanList.get(position);

        YoutubeId generateYoutubeId = new YoutubeId();
        final String idThumbnail = generateYoutubeId.generateId(seniman.getPortfolio());

        holder.mNama.setText(seniman.getNama());
        holder.mFoto.setImageUrl(seniman.getFoto(), AppController.getInstance().getImageLoader());
        holder.mThumbnail.setImageUrl("https://img.youtube.com/vi/"+idThumbnail+"/0.jpg", AppController.getInstance().getImageLoader());

        holder.mProfileContainer.setTag(position);
        holder.mThumbnail.setTag(position);

        SharedPreferences sharedpreferences = mContext.getSharedPreferences(Field.getLoginSharedPreferences(), Context.MODE_PRIVATE);
        boolean session = sharedpreferences.getBoolean(Field.getSessionStatus(),false);

//        if (session || sharedpreferences.getString(Field.getJenisUser(),null) == "event_organizer"){
        holder.mProfileContainer.setOnClickListener(new View.OnClickListener() {
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
        //}

        holder.mThumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, YoutubePlayerViewActivity.class);
                intent.putExtra("idThumbnailExtra", idThumbnail);
                mContext.startActivity(intent);

//                Intent intent = YouTubeStandalonePlayer.createVideoIntent(this, "<<YOUTUBE_API_KEY>>", "<<Youtube Video ID>>", 0, true, false);
//                mContext.startActivity(intent);

//                mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(seniman.getPortfolio())));
            }
        });

    }


    @Override
    public int getItemCount() {
        return senimanList.size();
    }
}
