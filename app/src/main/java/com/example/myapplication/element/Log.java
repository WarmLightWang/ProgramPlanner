package com.example.myapplication.element;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Log {

    public String date;
    public String content;
    public String username;
    private Calendar calendar;
    private SimpleDateFormat fmtDate;

    public Log(String date, String content, String username) {
        this.date = date;
        this.content = content;
        this.username = username;
    }

    public Log(String content, String username) {
        calendar = Calendar.getInstance();
        fmtDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);

        this.date = fmtDate.format(calendar.getTime());
        this.content = content;
        this.username = username;
    }

    @Override
    public String toString() {
        return String.format("%s:\n%s", this.date, this.content);
    }
}
