package com.learning.novoinvent.mybluetooth;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Bhaskar Tomar on 6/8/2016.
 */
public class MyAdapter extends BaseAdapter {
    Context context;
    List<BluetoothDevice> devices;
    public static int count = 0;
    LayoutInflater inflater =null;

    public MyAdapter(MainActivity mainActivity,List<BluetoothDevice> devices){
        this.context = mainActivity;
        this.devices = devices;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        count++;
    }
    @Override
    public int getCount() {
        return devices.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();
        View rowView = convertView;
        if(rowView == null)
            rowView =inflater.inflate(R.layout.list_row,null);
        holder.tvDeviceName =(TextView)rowView.findViewById(R.id.tvDeviceName);
        holder.tvDeviceAddress =(TextView)rowView.findViewById(R.id.tvDeviceAddress);
        holder.tvDeviceName.setText(devices.get(position).getName());
        holder.tvDeviceAddress.setText(devices.get(position).getAddress());
        rowView.setTag(holder);

        return rowView;
    }
    private class Holder{
        TextView tvDeviceName,tvDeviceAddress;
    }
}