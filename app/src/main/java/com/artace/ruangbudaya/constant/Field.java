package com.artace.ruangbudaya.constant;

public class Field {

    public static final String TAG_PASSWORD = "password";
    public static final String TAG_USERNAME = "username";
    public static final String SESSION_STATUS = "session_status";
    public static final String LOGIN_SHARED_PREFERENCES = "login_shared_preferences";
    public static final String ID_USER = "id_user";

    //
    public static final String NAMA = "nama";
    public static final String NO_HP = "no_hp";
    public static final String FOTO = "foto";
    public static final String KETERANGAN = "keterangan";
    public static final String JENIS_USER = "jenis_user";

    //event organizer
    public static final String ID_PENYELENGGARA_ACARA = "id_penyelenggara_acara";
    public static final String EMAIL = "email";

    //seniman
    public static final String ID_KELOMPOK_SENIMAN = "id_kelompok_seniman";
    public static final String ID_BIDANG_SENI = "id_bidang_seni";
    public static final String PORTFOLIO = "portfolio";

    //youtube api
    public static final String YOUTUBE_API_KEY = "AIzaSyA0KCIjEXYt79gqVdS5YpKMKKRjzE88_zY";


    public Field(){

    }

    public static String getTagPassword() {
        return TAG_PASSWORD;
    }

    public static String getJenisUser() {
        return JENIS_USER;
    }

    public static String getIdUser() {
        return ID_USER;
    }

    public static String getNAMA() {
        return NAMA;
    }

    public static String getNoHp() {
        return NO_HP;
    }

    public static String getFOTO() {
        return FOTO;
    }


    public static String getEMAIL() {
        return EMAIL;
    }


    public static String getKETERANGAN() {
        return KETERANGAN;
    }

    public static String getPORTFOLIO() {
        return PORTFOLIO;
    }

    public static String getIdPenyelenggaraAcara() {
        return ID_PENYELENGGARA_ACARA;
    }

    public static String getIdKelompokSeniman() {
        return ID_KELOMPOK_SENIMAN;
    }

    public static String getIdBidangSeni() {
        return ID_BIDANG_SENI;
    }

    public static String getTagUsername() {
        return TAG_USERNAME;
    }

    public static String getSessionStatus() {
        return SESSION_STATUS;
    }

    public static String getLoginSharedPreferences() {
        return LOGIN_SHARED_PREFERENCES;
    }

    public static String getYoutubeApiKey() {return YOUTUBE_API_KEY;}
}