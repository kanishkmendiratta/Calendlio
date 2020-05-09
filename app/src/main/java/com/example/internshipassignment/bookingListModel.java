package com.example.internshipassignment;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class bookingListModel {
    String id;
    String start;
    String end;
    String created;
    String modified;
    String description;

    public bookingListModel(String id, String start, String end, String created, String modified, String description) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.created = created;
        this.modified = modified;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String convertDateTime(String timeStamp){
        String date_Time="";
        String date=timeStamp.substring(8,10);
        String month=timeStamp.substring(5,7);
        String year=timeStamp.substring(0,4);
        String hour=timeStamp.substring(11,13);
        String min=timeStamp.substring(14,16);

        String monthString= new DateFormatSymbols().getMonths()[Integer.parseInt(month)-1];

        date_Time=date+" "+monthString.substring(0,3)+" "+year+" "+hour+":"+min;

        return date_Time;
    }
}
