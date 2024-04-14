package com.example.synctest.contacts;

public class DbContact {
    public static final int SYNC_STATUS_OK = 0;
    public static final int SYNC_STATUS_FAILED = 1;
    //public static final String SERVER_URL = "http://10.0.2.2/syncdemo/syncifo.php";
    public static final String SERVER_URL = "http://192.168.1.13/syncdemo/syncifo.php";




    public static final String UI_UPDATE_BROADCAST = "com.example.synctest.uiupdatebroadcast";

    public static final String DATABASE_NAME = "contactdb";
    public static final String TABLE_NAME = "contactinfo";
    public static final String LICENSE = "license";
    public static final String NAME = "name";
    public static final String LAST_NAME = "last_name";
    public static final String DATE = "date";
    public static final String TIME = "time";
    public static final String ADDRESS = "address";
    public static final String SYNC_STATUS = "syncstatus";
}
