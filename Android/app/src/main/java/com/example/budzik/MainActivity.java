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

public class MainActivity extends Activity {

    private ListView timesList;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor prefEditor;
    private JSONArray times;

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
                times = new JSONArray(sharedPref.getString("timesArray",""));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            createPref();
        }
    }

    private void setList(){
        Time time_data_test[] = new Time[]{
                new Time(20,15),
                new Time(16,59)
        };

        TimeAdapter adapter = new TimeAdapter(this,R.layout.times_list_row,time_data_test);

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

    private void createPref(){
        //Utworzenie nowego shared pref, oraz utworzenie jsona z domyślnymi ustawieniami
    }
}


//prefEditor.putString( "json", str );
      //  prefEditor.commit();