package com.learning.novoinvent.mybluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by Bhaskar Tomar on 6/8/2016.
 */
public class MyServerThread extends Thread {
    private final BluetoothServerSocket mmServerSocket;
    Handler handler;

    public MyServerThread(BluetoothAdapter myBluetoothAdapter, String NAME, UUID MY_UUID, Handler handler) {
        BluetoothServerSocket tmp = null;
        this.handler = handler;
        try {
            tmp = myBluetoothAdapter.listenUsingRfcommWithServiceRecord(NAME, MY_UUID);
        } catch (IOException e) { }
        mmServerSocket = tmp;
        Log.e("message123","My Server start.");
    }

    public void run() {
        BluetoothSocket[] socket = new BluetoothSocket[10];
        int socket_no = 0;
        Log.e("message123","In server run.");
        while (true) try {
            socket[socket_no] = mmServerSocket.accept();
            Log.e("message123", " New socked recieved");
            if (socket != null) {
                handler.obtainMessage(1,socket[socket_no]).sendToTarget();
                socket_no++;
            }
        } catch (IOException e) {
            break;
        }
    }
    public void cancel() {
        try {
            mmServerSocket.close();
            Log.e("message123","In server cancel.");
        } catch (IOException e) { }
    }


}