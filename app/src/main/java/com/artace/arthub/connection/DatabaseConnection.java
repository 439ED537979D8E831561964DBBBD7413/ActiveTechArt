package com.artace.arthub.connection;


public class DatabaseConnection {

//    public static String BASE_URL = "http://arthubdevelopment.000webhostapp.com/";
    public static String BASE_URL = "http://192.168.88.4/";
    public static String READ_EVENTORGANIZER_EVENTS = BASE_URL+"eventorganizer/EventOrganizerEvents.php";

    public DatabaseConnection() {
    }

    public static String getBaseUrl() {
        return BASE_URL;
    }

    public static String getReadEventorganizerEvents() {
        return READ_EVENTORGANIZER_EVENTS;
    }
}
