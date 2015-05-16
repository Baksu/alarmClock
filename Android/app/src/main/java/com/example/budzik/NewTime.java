package com.example.budzik;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TimePicker;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Baksu on 2015-05-11.
 */
public class NewTime extends Activity {

    private TimePicker timePicker;
    private EditText alarmName;
    private CheckBox ridle, vibration;
    private SeekBar volume;
    private final Boolean time24Format = true;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.options_layout);

        setValue();
    }

    private void setValue(){
        timePicker = (TimePicker)findViewById(R.id.timePicker);
        timePicker.setIs24HourView(time24Format);
        alarmName = (EditText)findViewById(R.id.alarm_name);
        ridle = (CheckBox)findViewById(R.id.puzzle_check_box);
        vibration = (CheckBox)findViewById(R.id.check_box_vibration);
        volume = (SeekBar)findViewById(R.id.seek_bar_volume);


    }

    public void bntClick(View v){
        switch(v.getId()){
            case R.id.saveBnt: saveTime(); break;
            case R.id.cancelBnt: //TODO: dorobić powrót z activity bez zmian
                break;

        }
    }

    private void createJSON(){
        JSONObject json = new JSONObject();
        try {
            json.put("Hour",timePicker.getCurrentHour());
            json.put("Minutes", timePicker.getCurrentMinute());
            json.put("Name", alarmName.getText());
            json.put("Ridle", ridle.isChecked() );
            json.put("Vibration", vibration.isChecked());
            json.put("Volume", volume.getProgress());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String str = json.toString();
    }

    private void saveTime(){
        createJSON();
    }
}




