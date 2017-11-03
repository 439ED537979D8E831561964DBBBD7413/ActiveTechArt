package com.artace.arthub.connection;


public class DatabaseConnection {

//    public static String BASE_URL = "http://arthubdevelopment.000webhostapp.com/";
    public static String BASE_URL = "http://192.168.43.84/ArtHub/";
    public static String READ_EVENTORGANIZER_EVENTS = BASE_URL+"eventorganizer/EventOrganizerEvents.php";
    public static String READ_EVENT_DETAIL = BASE_URL+"event/DetailEvent.php";

    public DatabaseConnection() {
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
}
