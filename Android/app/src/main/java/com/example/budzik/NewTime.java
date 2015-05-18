package com.example.budzik;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TimePicker;

import org.json.JSONArray;
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
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor prefEditor;
    private JSONArray timesArray;


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

        sharedPref = getSharedPreferences( "Times", Context.MODE_PRIVATE );
        prefEditor = getSharedPreferences( "Times", Context.MODE_PRIVATE ).edit();
        try {
            timesArray = new JSONArray(sharedPref.getString("timesArray",""));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void bntClick(View v){
        switch(v.getId()){
            case R.id.saveBnt: saveTime(); break;
            case R.id.cancelBnt: //TODO: dorobić powrót z activity bez zmian
                break;

        }
    }

    private JSONObject createJSON(){
        JSONObject json = new JSONObject();
        try {
            json.put(String.valueOf(R.string.jsonHour),timePicker.getCurrentHour());
            json.put(String.valueOf(R.string.jsonMinutes), timePicker.getCurrentMinute());
            json.put(String.valueOf(R.string.jsonName), alarmName.getText());
            json.put(String.valueOf(R.string.jsonRidle), ridle.isChecked() );
            json.put(String.valueOf(R.string.jsonVibration), vibration.isChecked());
            json.put(String.valueOf(R.string.jsonVolume), volume.getProgress());
            json.put(String.valueOf(R.string.jsonOn), true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        timesArray.put(json);
        prefEditor.putString("timesArray",timesArray.toString());
        prefEditor.commit();

        return json;
    }
//TODO: zastanowić się czy ma to tak wyglądać
    private void saveTime(){
        JSONObject newTime = createJSON();
        Intent intent = new Intent();
        intent.putExtra("newTime",newTime.toString());
        setResult(RESULT_OK, intent);
        this.finish();
    }
}




