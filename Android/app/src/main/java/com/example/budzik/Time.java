package com.example.budzik;

/**
 * Created by Baksu on 2015-05-02.
 */
public class Time {

    int minutes;
    int second;
    String name;
    Boolean on;


    public Time(int minutes, int second, String name, Boolean on){
        this.minutes = minutes;
        this.second = second;
        this.name = name;
        this.on = on;
    }
}
