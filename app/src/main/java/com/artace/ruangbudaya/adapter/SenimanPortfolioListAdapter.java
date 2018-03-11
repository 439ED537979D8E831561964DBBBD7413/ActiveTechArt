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
        final String idThumbnail = generateYoutubeId.generateId(seniman.getPortfolio());

        holder.mNama.setText(seniman.getNama());
        holder.mFoto.setImageUrl(seniman.getFoto(), AppController.getInstance().getImageLoader());
        holder.mThumbnail.setImageUrl("https://img.youtube.com/vi/"+idThumbnail+"/0.jpg", AppController.getInstance().getImageLoader());

        holder.mProfileContainer.setTag(position);
        holder.mThumbnail.setTag(position);

        SharedPreferences sharedpreferences = context.getSharedPreferences(Field.getLoginSharedPreferences(), Context.MODE_PRIVATE);
        boolean session = sharedpreferences.getBoolean(Field.getSessionStatus(),false);

//        if (session || sharedpreferences.getString(Field.getJenisUser(),null) == "event_organizer"){
            holder.mProfileContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, EoSenimanActivity.class);
                    Bundle extras = new Bundle();
                    int position = (Integer) view.getTag();
                    seniman = senimanList.get(position);

                    extras.putInt("id_kelompok_seniman",seniman.getId_kelompok_seniman());
                    intent.putExtras(extras);
                    context.startActivity(intent);
                }
            });
        //}

        holder.mThumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, YoutubePlayerViewActivity.class);
                intent.putExtra("idThumbnailExtra", idThumbnail);
                context.startActivity(intent);

//                Intent intent = YouTubeStandalonePlayer.createVideoIntent(this, "<<YOUTUBE_API_KEY>>", "<<Youtube Video ID>>", 0, true, false);
//                context.startActivity(intent);

//                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(seniman.getPortfolio())));
            }
        });

    }

    @Override
    public int getItemCount() {
        return senimanList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView mNama;
        private CircularNetworkImageView mFoto;
        private NetworkImageView mThumbnail;
        private ConstraintLayout mProfileContainer;

        public MyViewHolder(View itemView) {

            super(itemView);
            mNama = (TextView) itemView.findViewById(R.id.item_portfolio_list_namaSeniman);
            mFoto = (CircularNetworkImageView) itemView.findViewById(R.id.item_portfolio_list_fotoSeniman);
            mProfileContainer = (ConstraintLayout) itemView.findViewById(R.id.item_portfolio_list_profileContainer);
            mThumbnail = (NetworkImageView) itemView.findViewById(R.id.item_portfolio_list_thumbnail);

        }
    }

}
