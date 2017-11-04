package com.artace.arthub.connection;


public class DatabaseConnection {

    public static String BASE_URL = "http://arthubdevelopment.000webhostapp.com/";
//    public static String BASE_URL = "http://192.168.43.206/arthub/";
    public static String READ_EVENTORGANIZER_EVENTS = BASE_URL+"eventorganizer/EventOrganizerEvents.php";
    public static String READ_EVENT_DETAIL = BASE_URL+"event/DetailEvent.php";
    public static String CREATE_EVENT_ORGANIZER = BASE_URL+"eventorganizer/CreateEventOrganizer.php";
    //DELETE
    public static String DELETE_EVENT = BASE_URL+"event/DeleteEvent.php";
    public static String LOGIN = BASE_URL+"login/Login.php";

    public DatabaseConnection() {
    }

    public static String getDeleteEvent(int id_event) {
        return DELETE_EVENT + "?id_event=" + id_event;
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
}
