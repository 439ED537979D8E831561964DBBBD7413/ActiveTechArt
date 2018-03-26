package com.artace.ruangbudaya.connection;


public class DatabaseConnection {

    public static String BASE_URL = "http://ruangbudayadevelopment.000webhostapp.com/";
//    public static String BASE_URL = "http://192.168.43.206/arthub/";
    public static String READ_EVENTORGANIZER_EVENTS = BASE_URL+"eventorganizer/EventOrganizerEvents.php";
    public static String READ_SENIMAN_LIST = BASE_URL+"seniman/ReadListSeniman.php";
    public static String READ_SENIMAN_LIST_MAIN = BASE_URL+"seniman/ReadListSenimanMain.php";
    public static String READ_EVENT_DETAIL = BASE_URL+"event/DetailEvent.php";
    public static String READ_LIST_TAWARAN_TAMPIL = BASE_URL+"tawaran_tampil/ReadListTawaranTampil.php";
    public static String READ_EVENT_LIST_MAIN = BASE_URL+"event/ReadListEventMain.php";
    //DELETE
    public static String DELETE_EVENT = BASE_URL+"event/DeleteEvent.php";
    public static String DELETE_TAWARAN_TAMPIL = BASE_URL+"tawaran_tampil/DeleteTawaranTampil.php";
    //LOGIN
    public static String LOGIN = BASE_URL+"login/Login.php";
    //READ JENIS SENIMAN
    public  static String READ_JENIS_SENIMAN = BASE_URL+"jenis_seniman/JenisSeniman.php";

    //UPLOAD
    public static String UPLOAD_FOTO_EO = BASE_URL+"upload/foto/Eo.php";

    //UPDATE
    public static String UPDATE_SENIMAN = BASE_URL+"seniman/UpdateSeniman.php";

    //INSERT
    public static String INSERT_REGISTER_SENIMAN = BASE_URL+"seniman/RegisterSeniman.php";
    public static String CREATE_EVENT_ORGANIZER = BASE_URL+"eventorganizer/CreateEventOrganizer.php";
    public static String INSERT_EVENT = BASE_URL+"event/InsertEvent.php";
    public static String INSERT_TAWARAN_TAMPIL = BASE_URL+"tawaran_tampil/InsertTawaranTampil.php";

    //READ SENIMAN LIST TAWARAN
    public static String READ_TAWARAN_TAMPIL = BASE_URL+"seniman/SenimanListTawaranTampil.php";

    //READ LIST SENIMAN DETAIL EVENT
    public static String READ_LIST_SENIMAN_DETAIL_EVENT = BASE_URL+"seniman/ListSenimanEventDetail.php";

    //READ DETAIL SENIMAN
    public static String READ_DETAIL_SENIMAN = BASE_URL+"seniman/ReadDetailSeniman.php";

    //UPDATE TERIMA TOLAK LIST TAWARAN
    public static String UPDATE_TERIMA_TOLAK_TAWARAN_TAMPIL = BASE_URL+"seniman/SenimanUpdateTerimaTolakTawaranTampil.php";

    //DIRECTORY
    public static String DIRECTORY_FOTO_USER = BASE_URL+"images/user/";
    public static String DIRECTORY_FOTO_USER_DEFAULT = BASE_URL+"images/user/default.png";

    public DatabaseConnection() {
    }

    public static String getInsertTawaranTampil() {
        return INSERT_TAWARAN_TAMPIL;
    }

    public static String getReadListSenimanDetailEvent() {
        return READ_LIST_SENIMAN_DETAIL_EVENT;
    }

    public static String getInsertEvent() {
        return INSERT_EVENT;
    }

    public static String getUpdateTerimaTolakTawaranTampil() {
        return UPDATE_TERIMA_TOLAK_TAWARAN_TAMPIL;
    }

    public static String getReadEventListMain() {
        return READ_EVENT_LIST_MAIN;
    }

    public static String getReadListTawaranTampil() {
        return READ_LIST_TAWARAN_TAMPIL;
    }

    public static String getDeleteTawaranTampil() {
        return DELETE_TAWARAN_TAMPIL;
    }

    public static String getReadTawaranTampil() {
        return READ_TAWARAN_TAMPIL;
    }

    public static String getReadSenimanList() {
        return READ_SENIMAN_LIST;
    }

    public static String getInsertRegisterSeniman() {
        return INSERT_REGISTER_SENIMAN;
    }

    public static String getUpdateSeniman() {
        return UPDATE_SENIMAN;
    }

    public static String getCreateEventOrganizer() {
        return CREATE_EVENT_ORGANIZER;
    }

    public static String getReadJenisSeniman() {
        return READ_JENIS_SENIMAN;
    }

    public static String getReadDetailSeniman() {
        return READ_DETAIL_SENIMAN;
    }

    public static String getDeleteEvent(int id_event) {
        return DELETE_EVENT + "?id_acara=" + id_event;
    }

    public static String getDirectoryFotoUserDefault() {
        return DIRECTORY_FOTO_USER_DEFAULT;
    }

    public static String getDirectoryFotoUser() {
        return DIRECTORY_FOTO_USER;
    }

    public static String getBaseUrl() {
        return BASE_URL;
    }

    public static String getReadEventDetail() {
        return READ_EVENT_DETAIL;
    }

    public static String getReadEventorganizerEvents() {
        return READ_EVENTORGANIZER_EVENTS;
    }

    public static String getLogin(String username, String password) {
        return LOGIN + "?username="+username+"&password="+password;
    }

    public static String getReadSenimanListMain() {
        return READ_SENIMAN_LIST_MAIN;
    }
}
