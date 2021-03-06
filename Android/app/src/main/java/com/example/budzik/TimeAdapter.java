package com.example.budzik;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Baksu on 2015-05-02.
 */
public class TimeAdapter extends ArrayAdapter<Time>{

    Context context;
    int layoutResourceId;
    ArrayList<Time> data = null;

    public TimeAdapter(Context context, int layoutResourceId, ArrayList<Time> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View row = convertView;
        TimeHolder holder;

        if (row == null){
            LayoutInflater inflater =((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId,parent,false);

            holder = new TimeHolder();
            holder.time = (TextView)row.findViewById(R.id.time);
            holder.days = (TextView)row.findViewById(R.id.days);
            holder.name = (TextView)row.findViewById(R.id.name);
            holder.turnOn = (CheckBox)row.findViewById(R.id.checkbox);

            row.setTag(holder);
        }else{
            holder = (TimeHolder)row.getTag();
        }

        Time time = data.get(position);
        holder.time.setText(Integer.toString(time.minutes)+":"+Integer.toString(time.second));
        holder.name.setText(time.name);
        holder.turnOn.setChecked(time.on);

        return row;
    }

    static class TimeHolder{
        TextView time;
        TextView days;
        TextView name;
        CheckBox turnOn;
    }


   /* @Override
    public boolean areAllItemsEnabled()
    {
        return true;
    }

    @Override
    public boolean isEnabled(int arg0)
    {
        return true;
    }*/
}
