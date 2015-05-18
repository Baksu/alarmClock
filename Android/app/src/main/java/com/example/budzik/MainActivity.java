package com.example.budzik;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends Activity {

    private ListView timesList;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor prefEditor;
    private JSONArray timesArray;
    private TimeAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setValue();
        setList();
    }

    private void setValue(){
        timesList = (ListView)findViewById(R.id.timesList);
        sharedPref = getSharedPreferences( "Times", Context.MODE_PRIVATE );
        prefEditor = getSharedPreferences( "Times", Context.MODE_PRIVATE ).edit();

        if(sharedPref.contains("timesArray")){
            try {
                timesArray = new JSONArray(sharedPref.getString("timesArray",""));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            createPref();
        }
    }

    private void setList(){
            Time time_data[] = new Time[timesArray.length() - 1];
            JSONObject currenJSON = null;
            for (int i = 1; i < timesArray.length(); i++) {
                try {
                    currenJSON = (JSONObject) timesArray.get(i);
                    time_data[i - 1] = new Time((int) currenJSON.get(String.valueOf(R.string.jsonHour)),
                            (int) currenJSON.get(String.valueOf(R.string.jsonMinutes)),
                            (String) currenJSON.get(String.valueOf(R.string.jsonName)),
                            (Boolean)currenJSON.get(String.valueOf(R.string.jsonOn)));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            adapter = new TimeAdapter(this, R.layout.times_list_row, time_data);
            timesList.setAdapter(adapter);
    }

    public void bntClick(View v){
        switch(v.getId()){
            case R.id.addBnt: newTime(); break;
        }
    }

    private void newTime(){
        Intent intent = new Intent (this, NewTime.class);
        startActivity(intent);
    }

    /**
     * Metoda odpowiedzialna za utworzenie odpowiedniego sharedpreferences oraz wstawienie poczatkowych wartości domyślnych
     */
    private void createPref(){
        JSONArray newArray = new JSONArray();
        JSONObject defaultValue = new JSONObject();
        try {
            defaultValue.put(String.valueOf(R.string.jsonHour),0);
            defaultValue.put(String.valueOf(R.string.jsonMinutes), 0);
            defaultValue.put(String.valueOf(R.string.jsonName), "Nowy");
            defaultValue.put(String.valueOf(R.string.jsonRidle), true );
            defaultValue.put(String.valueOf(R.string.jsonVibration), true);
            defaultValue.put(String.valueOf(R.string.jsonVolume), 100);
            defaultValue.put(String.valueOf(R.string.jsonOn), false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        newArray.put(defaultValue);
        prefEditor.putString("timesArray",newArray.toString());
        prefEditor.commit();
        timesArray = newArray;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            JSONObject currenJSON = new JSONObject(data.getStringExtra("newTime"));
            adapter.add(new Time((int) currenJSON.get(String.valueOf(R.string.jsonHour)),
                    (int) currenJSON.get(String.valueOf(R.string.jsonMinutes)),
                    (String) currenJSON.get(String.valueOf(R.string.jsonName)),
                    (Boolean)currenJSON.get(String.valueOf(R.string.jsonOn))));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        adapter.notifyDataSetChanged();
    }
}
