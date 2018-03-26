package com.artace.ruangbudaya.ViewPlugins;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.artace.ruangbudaya.GenerateQrCodeActivity;
import com.artace.ruangbudaya.LoginActivity;
import com.artace.ruangbudaya.MainActivity;
import com.artace.ruangbudaya.R;
import com.artace.ruangbudaya.RegisterEventOrganizerActivity;
import com.artace.ruangbudaya.RegisterSenimanActivity;
import com.artace.ruangbudaya.ScannQRCodeActivity;
import com.artace.ruangbudaya.TentangAplikasiActivity;
import com.artace.ruangbudaya.connection.DatabaseConnection;
import com.artace.ruangbudaya.constant.Field;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.squareup.picasso.Picasso;

public class DrawerMenu {

    String password, username, nama, sub_title, foto;
    boolean session;
    SharedPreferences sharedpreferences;

    public DrawerMenu(){

    }

    public void createDrawer(Context context, AppCompatActivity activity, Toolbar mToolbar){

        //get the value username and password from login
        // Cek session login jika TRUE maka langsung buka MainActivity
        sharedpreferences = activity.getSharedPreferences(Field.getLoginSharedPreferences(), Context.MODE_PRIVATE);
        session = sharedpreferences.getBoolean(Field.getSessionStatus(),false);

        if (session){
            password = sharedpreferences.getString(Field.getTagPassword(), null);
            username = sharedpreferences.getString(Field.getTagUsername(), null);
            nama = sharedpreferences.getString(Field.getNAMA(), null);
            if(sharedpreferences.getString(Field.getJenisUser(),null).equals("seniman")){
                sub_title = sharedpreferences.getString(Field.getNoHp(),null);
            }
            else{
                sub_title = sharedpreferences.getString(Field.getEMAIL(),null);
            }
            foto = sharedpreferences.getString(Field.getFOTO(),null);
        }
        else{
            nama = "Guest";
            sub_title = "Silahkan Login";
            foto = DatabaseConnection.getDirectoryFotoUserDefault();
        }

        // Create the AccountHeader

        DrawerImageLoader.init(new AbstractDrawerImageLoader() {
            @Override
            public void set(ImageView imageView, Uri uri, Drawable placeholder) {
                Picasso.with(imageView.getContext()).load(uri).placeholder(placeholder).into(imageView);
            }

            @Override
            public void cancel(ImageView imageView) {
                Picasso.with(imageView.getContext()).cancelRequest(imageView);
            }
        });

        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(activity)
                .withHeaderBackground(R.drawable.material_background)
                .addProfiles(
                        new ProfileDrawerItem().withName(nama).withEmail(sub_title).withIcon(foto)
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        return false;
                    }
                })
                .build();

        if (!session){
            buildDrawerNotLoggedIn(context,activity,mToolbar, headerResult);
        }
        else{
            if (sharedpreferences.getString(Field.getJenisUser(),null).equals("seniman")){
                buildDrawerLoggedInSeniman(context,activity,mToolbar, headerResult);
            }
            else{
                buildDrawerLoggedInEo(context,activity,mToolbar, headerResult);
            }
        }

    }

    private void buildDrawerLoggedInSeniman(Context context, AppCompatActivity activity, Toolbar mToolbar, AccountHeader headerResult){
        final Context contextFinal = context;
        final AppCompatActivity activityFinal = activity;

        //if you want to update the items at a later time it is recommended to keep it in a variable
        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(1).withName("Konfirmasi Kedatangan").withIcon(GoogleMaterial.Icon.gmd_camera);

        SecondaryDrawerItem item2 = new SecondaryDrawerItem().withIdentifier(2).withName("Tentang Aplikasi").withIcon(GoogleMaterial.Icon.gmd_info);
        SecondaryDrawerItem item3 = new SecondaryDrawerItem().withIdentifier(3).withName("Log Out").withIcon(GoogleMaterial.Icon.gmd_exit_to_app);

        //create the drawer and remember the `Drawer` result object
        Drawer result = new DrawerBuilder()
                .withActivity(activity)
                .withToolbar(mToolbar)
                .withAccountHeader(headerResult)
                .addDrawerItems(
                        item1,
                        item2,
                        new DividerDrawerItem(),
                        item3
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (drawerItem.getIdentifier() == 1){
                            Intent intent = new Intent(contextFinal, ScannQRCodeActivity.class);
                            contextFinal.startActivity(intent);
                        }

                        if (drawerItem.getIdentifier() == 2){
                            Intent intent = new Intent(contextFinal,TentangAplikasiActivity.class);
                            contextFinal.startActivity(intent);
                        }
                        if (drawerItem.getIdentifier() == 3){

                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.clear();
                            editor.apply();

                            Intent intent = new Intent(contextFinal, MainActivity.class);
                            contextFinal.startActivity(intent);
                            activityFinal.finish();
                        }
                        return false;
                    }
                })
                .build();
        if(mToolbar != null)
            result.getActionBarDrawerToggle().setDrawerIndicatorEnabled(true);
    }

    private void buildDrawerLoggedInEo(Context context, AppCompatActivity activity, Toolbar mToolbar, AccountHeader headerResult){
        final Context contextFinal = context;
        final AppCompatActivity activityFinal = activity;
        //if you want to update the items at a later time it is recommended to keep it in a variable
        SecondaryDrawerItem item1 = new SecondaryDrawerItem().withIdentifier(1).withName("Tentang Aplikasi").withIcon(GoogleMaterial.Icon.gmd_info);
        SecondaryDrawerItem item2 = new SecondaryDrawerItem().withIdentifier(2).withName("Log Out").withIcon(GoogleMaterial.Icon.gmd_exit_to_app);

        //create the drawer and remember the `Drawer` result object
        Drawer result = new DrawerBuilder()
                .withActivity(activity)
                .withToolbar(mToolbar)
                .withAccountHeader(headerResult)
                .addDrawerItems(
                        item1,
                        new DividerDrawerItem(),
                        item2
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (drawerItem.getIdentifier() == 1){
                            Intent intent = new Intent(contextFinal,TentangAplikasiActivity.class);
                            contextFinal.startActivity(intent);
                        }
                        if (drawerItem.getIdentifier() == 2){

                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.clear();
                            editor.apply();

                            Intent intent = new Intent(contextFinal, MainActivity.class);
                            contextFinal.startActivity(intent);
                            activityFinal.finish();
                        }
                        return false;
                    }
                })
                .build();
        if(mToolbar != null)
            result.getActionBarDrawerToggle().setDrawerIndicatorEnabled(true);
    }

    private void buildDrawerNotLoggedIn(Context context, AppCompatActivity activity, Toolbar mToolbar, AccountHeader headerResult){
        final Context contextFinal = context;
        final AppCompatActivity activityFinal = activity;
        //if you want to update the items at a later time it is recommended to keep it in a variable
        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(1).withName("Login").withIcon(GoogleMaterial.Icon.gmd_account_circle);
        PrimaryDrawerItem item2 = new PrimaryDrawerItem().withIdentifier(2).withName("Daftar Sebagai Seniman").withIcon(GoogleMaterial.Icon.gmd_account_box);
        PrimaryDrawerItem item3 = new PrimaryDrawerItem().withIdentifier(3).withName("Daftar Sebagai Event organizer").withIcon(GoogleMaterial.Icon.gmd_account_box);
        SecondaryDrawerItem item4 = new SecondaryDrawerItem().withIdentifier(5).withName("Tentang Aplikasi").withIcon(GoogleMaterial.Icon.gmd_info);

        //create the drawer and remember the `Drawer` result object
        Drawer result = new DrawerBuilder()
                .withActivity(activity)
                .withAccountHeader(headerResult)
                .withToolbar(mToolbar)
                .addDrawerItems(
                        item1,
                        item2,
                        item3,
                        new DividerDrawerItem(),
                        item4
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (drawerItem.getIdentifier() == 1){
                            Intent intent = new Intent(contextFinal,LoginActivity.class);
                            contextFinal.startActivity(intent);
                        }
                        if (drawerItem.getIdentifier() == 2){
                            Intent intent = new Intent(contextFinal, RegisterSenimanActivity.class);
                            contextFinal.startActivity(intent);
                        }
                        if (drawerItem.getIdentifier() == 3){
                            Intent intent = new Intent(contextFinal, RegisterEventOrganizerActivity.class);
                            contextFinal.startActivity(intent);
                        }
                        if (drawerItem.getIdentifier() == 4){
                            Intent intent = new Intent(contextFinal, TentangAplikasiActivity.class);
                            contextFinal.startActivity(intent);
                        }
                        return false;
                    }
                })
                .build();

        if(mToolbar != null)
            result.getActionBarDrawerToggle().setDrawerIndicatorEnabled(true);
    }

}
