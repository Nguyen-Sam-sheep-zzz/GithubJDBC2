package com.example.colabjdbcmysqlthaycan.Class;

public class Session {
    private static String loggedInCustomerId;

    public static void setLoggedInCustomer(String customerId) {
        loggedInCustomerId = customerId;
    }

    public static String getLoggedInCustomerId() {
        return loggedInCustomerId;
    }

    public static void setLoggedInCustomerId(String loggedInCustomerId) {
        Session.loggedInCustomerId = loggedInCustomerId;
    }
    public static void clearSession() {
        loggedInCustomerId = null;
    }
}
