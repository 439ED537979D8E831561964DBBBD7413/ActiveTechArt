package com.artace.arthub.adapter;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.artace.arthub.R;
import com.artace.arthub.pojo.PojoSeniman;
import com.bumptech.glide.Glide;

import java.util.List;


public class ListSenimanAdapter extends RecyclerView.Adapter<ListSenimanAdapter.MyViewHolder> {

    private Context mContext;
    private List<PojoSeniman> senimanList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nama, keahlianSpesifik;
        public ImageView foto, overflow;

        public MyViewHolder(View view) {
            super(view);
            nama = (TextView) view.findViewById(R.id.item_list_seniman_nama);
            keahlianSpesifik = (TextView) view.findViewById(R.id.item_list_seniman_subtitle);
            foto = (ImageView) view.findViewById(R.id.item_list_seniman_foto);
            overflow = (ImageView) view.findViewById(R.id.item_list_seniman_overflow);
        }
    }


    public ListSenimanAdapter(Context mContext, List<PojoSeniman> senimanList) {

        this.mContext = mContext;
        this.senimanList = senimanList;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_seniman, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        PojoSeniman seniman = senimanList.get(position);
        holder.nama.setText(seniman.getNama());
        holder.keahlianSpesifik.setText(seniman.getKeahlian_spesifik());

        // loading album cover using Glide library
        Glide.with(mContext).load(seniman.getFoto()).into(holder.foto);


        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.overflow);
            }
        });
    }

    /**
     * Showing popup menu when tapping on 3 dots
     */
    private void showPopupMenu(View view) {
        // inflate menu
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_list_seniman, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
        popup.show();
    }

    /**
     * Click listener for popup menu items
     */
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        public MyMenuItemClickListener() {
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_detail:
                    Toast.makeText(mContext, "Add to favourite", Toast.LENGTH_SHORT).show();
                    return true;
                default:
            }
            return false;
        }
    }

    @Override
    public int getItemCount() {
        return senimanList.size();
    }
}
