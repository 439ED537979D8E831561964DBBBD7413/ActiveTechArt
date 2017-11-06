package com.artace.arthub.constant;

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
    public static final String JENIS_USER = "jenis_user";

    //event organizer
    public static final String ID_EVENT_ORGANIZER = "id_event_organizer";
    public static final String EMAIL = "email";

    //seniman
    public static final String ID_SENIMAN = "id_seniman";
    public static final String ID_JENIS_SENIMAN = "id_jenis_seniman";
    public static final String JENIS_KELAMIN = "jenis_kelamin";
    public static final String PORTFOLIO = "portfolio";
    public static final String UMUR = "umur";
    public static final String KEAHLIAN_SPESIFIK = "keahlian_spesifik";
    public static final String FORMAT_SOLO_GRUP = "format_solo_grup";

    //youtube api
    public static final String YOUTUBE_API_KEY = "AIzaSyDu9rJYXDuPgMut8Ga6CMPMFUktZMkJRIQ";

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

    public static String getIdEventOrganizer() {
        return ID_EVENT_ORGANIZER;
    }

    public static String getEMAIL() {
        return EMAIL;
    }

    public static String getIdSeniman() {
        return ID_SENIMAN;
    }

    public static String getIdJenisSeniman() {
        return ID_JENIS_SENIMAN;
    }

    public static String getJenisKelamin() {
        return JENIS_KELAMIN;
    }

    public static String getPORTFOLIO() {
        return PORTFOLIO;
    }

    public static String getUMUR() {
        return UMUR;
    }

    public static String getKeahlianSpesifik() {
        return KEAHLIAN_SPESIFIK;
    }

    public static String getFormatSoloGrup() {
        return FORMAT_SOLO_GRUP;
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