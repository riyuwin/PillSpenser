package com.example.bottomnavigation;

import java.sql.Timestamp;
import java.util.Date;

public class Notification_Retriever {

    private String activity;
    private String container;
    private String current_date;
    private String current_time;
    private String data;
    private String documentId;
    private String remarks;
    private String sched_name;
    private Date timestampField;


    public String getActivity() {
        return activity;
    }

    public String getContainer() {
        return container;
    }

    public String getCurrent_date() {
        return current_date;
    }

    public String getCurrent_time() {
        return current_time;
    }

    public String getData() {
        return data;
    }

    public String getDocumentId() {
        return documentId;
    }

    public String getRemarks() {
        return remarks;
    }

    public String getSched_name() {
        return sched_name;
    }

}