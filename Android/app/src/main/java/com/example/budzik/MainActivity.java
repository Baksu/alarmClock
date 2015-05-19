package com.example.budzik;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends Activity {

    private ListView timesList;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor prefEditor;
    private JSONArray timesArray;
    private TimeAdapter adapter = null;
    private ArrayList<Time> time_data;

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
            time_data = new ArrayList<Time>();
            JSONObject currenJSON;
            for (int i = 1; i < timesArray.length(); i++) {
                try {
                    currenJSON = (JSONObject) timesArray.get(i);
                    time_data.add( new Time((int) currenJSON.get(String.valueOf(R.string.jsonHour)),
                            (int) currenJSON.get(String.valueOf(R.string.jsonMinutes)),
                            (String) currenJSON.get(String.valueOf(R.string.jsonName)),
                            (Boolean)currenJSON.get(String.valueOf(R.string.jsonOn))));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            adapter = new TimeAdapter(this, R.layout.times_list_row, time_data);
            timesList.setAdapter(adapter);

            timesList.setOnItemClickListener(new AdapterView.OnItemClickListener(){

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Log.e("klikniete"," krótkie");
                    //TODO: dodać tworzenie activity z już wypełnionymi danymi
                }

            });

            timesList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                time_data.remove(position);
                adapter.notifyDataSetChanged();

                JSONArray newArray = new JSONArray();

                for(int i = 0 ; i < timesArray.length() ; i++){
                    if(i != position+1) {
                        try {
                            newArray.put(timesArray.get(i));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                timesArray = newArray;

                prefEditor.putString("timesArray",timesArray.toString());
                prefEditor.commit();

                return true;

                //TODO: Dodać usunięcie wątku budzika
            }
        });
    }

    public void bntClick(View v){
        switch(v.getId()){
            case R.id.addBnt: newTime(); break;
        }
    }

    /**
     * Metoda odpowiedzialna za tworzenie nowego activity czasu
     */
    //TODO: Utworzyć przekazywanie zmiennej do nowej klasy NewTime z informacją czy jest to nowy czas czy już istniejący
    private void newTime(){
        Intent intent = new Intent (this, NewTime.class);
        startActivityForResult(intent,0);
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

    /**
     * Akrualizacja listy po powrocie z wątków
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("weszlo "," activity end");
        try {
            JSONObject currenJSON = new JSONObject(data.getStringExtra("newTime"));
            time_data.add(new Time((int) currenJSON.get(String.valueOf(R.string.jsonHour)),
                    (int) currenJSON.get(String.valueOf(R.string.jsonMinutes)),
                    (String) currenJSON.get(String.valueOf(R.string.jsonName)),
                    (Boolean)currenJSON.get(String.valueOf(R.string.jsonOn))));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        adapter.notifyDataSetChanged();
    }
}
