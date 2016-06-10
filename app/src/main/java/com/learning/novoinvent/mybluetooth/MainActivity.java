package com.learning.novoinvent.mybluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;


public class MainActivity extends AppCompatActivity {
    ListView listView;
    TextView tvprintMsg;
    EditText etvMessage;
    Button btnSend;
    BluetoothAdapter myBluetoothAdapther;
    BluetoothSocket socket;
    MyManageConnection mythread;
    String bluetoothAdapter;
    int REQUEST_ENABLE_BT = 1;
    List<BluetoothDevice> devices;
    public BroadcastReceiver mReceiver;
    private final String NAME = new String("bhaskartomar04");
    private final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    Handler handler = new Handler() {
        @Override
        public void handleMessage(final Message msg) {
            super.handleMessage(msg);
            String mess = null;
            switch (msg.what) {
                case 1:
                    Log.e("message123", "Writing thread has been called.");
                    Toast.makeText(getApplicationContext(), "Write your message", Toast.LENGTH_SHORT).show();
                    socket = (BluetoothSocket) msg.obj;
                    mythread = new MyManageConnection(socket, handler);
                    mythread.start();
                    btnSend.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mythread.write(etvMessage.getText().toString().getBytes());
                            etvMessage.setText("");
                        }
                    });

                    break;
                case 2:
                    byte[] readbuff = (byte[]) msg.obj;
                    mess = new String(readbuff);
                    tvprintMsg.setText(mess);
                    break;
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.list_row);
        devices = new ArrayList<BluetoothDevice>();
        bluetoothAdapter = "bluetoothAdapter";
        myBluetoothAdapther = BluetoothAdapter.getDefaultAdapter();
        tvprintMsg = (TextView) findViewById(R.id.printMsg);
        etvMessage = (EditText) findViewById(R.id.etvSend);
        btnSend = (Button) findViewById(R.id.btnSend);

        //  pairedDeviceList();
        //   unpairedDeviceList();

    }


    private void unpairedDeviceList() {
        // Create a BroadcastReceiver for ACTION_FOUND
        Log.e("unpairedList", "Inside list");
        mReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                Log.e("unpairedList", "Inside receiver");
                String action = intent.getAction();
                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    devices.add(device);
                    listView.setAdapter(new MyAdapter(MainActivity.this, devices));
                    Toast.makeText(getApplicationContext(), action + " 00", Toast.LENGTH_LONG).show();
                }
                if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                    Toast.makeText(getApplicationContext(), action + " 11", Toast.LENGTH_LONG).show();
                } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                    Toast.makeText(getApplicationContext(), action + " 22", Toast.LENGTH_LONG).show();
                } else if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                    if (myBluetoothAdapther.getState() == myBluetoothAdapther.STATE_OFF) {
                        Toast.makeText(getApplicationContext(), action + " 33", Toast.LENGTH_LONG).show();
                        enableBluetoothDevices();
                    }
                }
            }
        };
        // Register the BroadcastReceiver
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter);
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        registerReceiver(mReceiver, filter);
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(mReceiver, filter);
        filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(mReceiver, filter);
        if (myBluetoothAdapther.isDiscovering())
            myBluetoothAdapther.cancelDiscovery();
       /* myBluetoothAdapther.startDiscovery();*/
    }

    private void enableBluetoothDevices() {
        if (myBluetoothAdapther == null) {
            Log.e(bluetoothAdapter, "Could not get adapter");
            finish();
        } else {
            Log.e(bluetoothAdapter, "Got adpater.");
            if (!myBluetoothAdapther.isEnabled()) {
                Intent enableBluetooh = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBluetooh, REQUEST_ENABLE_BT);
                Log.e(bluetoothAdapter, "Ask user about turning bluetooth on.");

            }
        }

    }


    private void pairedDeviceList() {
        Set<BluetoothDevice> pairedDevices = myBluetoothAdapther.getBondedDevices();
        devices.clear();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                devices.add(device);
                Log.e("device name", device.getName());
                for (int i = 0; i < device.getUuids().length; i++) {
                    Log.e("UUID", device.getUuids()[i].getUuid().toString());
                }
            }
        }
        listView.setAdapter(new MyAdapter(MainActivity.this, devices));

    }

    public void selectDevice() throws InterruptedException {
        Log.e("message123", "started");
        if (myBluetoothAdapther.isEnabled()) {
            MyServerThread thread = new MyServerThread(myBluetoothAdapther, NAME, MY_UUID, handler);
            thread.start();
        }
    }

    public void onResume() {
        super.onResume();
        enableBluetoothDevices();
        pairedDeviceList();
        unpairedDeviceList();
        try {
            selectDevice();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
