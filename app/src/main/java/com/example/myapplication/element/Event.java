package com.example.myapplication.element;

import java.util.Comparator;

public class Event implements Comparator<Event> {

    public String eventId;
    public String eventTitle;
    public String eventDate;
    public boolean isNotify;

    public Event() {
    }

    public Event(String eventTitle, String eventDate, boolean isNotify) {
        this.eventTitle = eventTitle;
        this.eventDate = eventDate;
        this.isNotify = isNotify;
    }

    public Event(String eventId, String eventTitle, String eventDate, boolean isNotify) {
        this.eventId = eventId;
        this.eventTitle = eventTitle;
        this.eventDate = eventDate;
        this.isNotify = isNotify;
    }

    private int[] splitDate(String date) {
        int[] dateArr = new int[3];
        String[] dateStr = date.split("-");
        for (int i = 0; i < 3; i++) {
            int num = Integer.parseInt(dateStr[i]);
            dateArr[i] = num;
        }
        return dateArr;
    }

    @Override
    public int compare(Event event1, Event event2) {
        int[] dateArr1 = splitDate(event1.eventDate);
        int[] dateArr2 = splitDate(event2.eventDate);

        //year
        if (dateArr1[2] != dateArr2[2])
            return dateArr1[2] - dateArr2[2];

        //month
        if (dateArr1[0] != dateArr2[0])
            return dateArr1[0] - dateArr2[0];

        //date
        if (dateArr1[1] != dateArr2[1])
            return dateArr1[1] - dateArr2[1];

        int eventId1 = Integer.parseInt(event1.eventId);
        int eventId2 = Integer.parseInt(event2.eventId);

        return eventId1 - eventId2;
    }

    @Override
    public String toString() {
        return String.format("Event Title: %s, Event Date: %s\n", eventTitle, eventDate);
    }
}
