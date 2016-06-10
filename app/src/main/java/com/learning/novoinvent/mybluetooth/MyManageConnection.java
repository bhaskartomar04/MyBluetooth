package com.learning.novoinvent.mybluetooth;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Bhaskar Tomar on 6/8/2016.
 */
public class MyManageConnection extends  Thread{
    private BluetoothSocket mmSocket;
    private InputStream mmInStream;
    private  OutputStream mmOutStream;
    private Handler handler;
    public MyManageConnection(BluetoothSocket socket,Handler handler) {
        mmSocket = socket;
        InputStream tmpIn = null;
        OutputStream tmpOut = null;
        this.handler = handler;
        try {
            tmpIn = socket.getInputStream();
            tmpOut = socket.getOutputStream();
        } catch (IOException e) { }
        mmInStream = tmpIn;
        mmOutStream = tmpOut;
        Log.e("message123", "In start of my  manage connection.");
    }

    public void run() {
        Log.e("message123", "In run of my  manage connection.");
        byte[] buffer = new byte[1024];
        int bytes;
        while (true) try {
            bytes = mmInStream.read(buffer);
            String msg = new String(buffer);
            Log.e("message123", msg);
            Log.e("socket",mmSocket.toString());
            handler.obtainMessage(2,bytes,-1,buffer).sendToTarget();
        } catch (IOException e) {
            break;
        }
    }
    public void write(byte[] bytes) {
        try {
            Log.e("message123", "In write of my  manage connection.");
            //Log.e("message123", bytes.toString());
            mmOutStream.write(bytes);
        } catch (IOException e) { }
    }

    /* Call this from the main activity to shutdown the connection */
    public void cancel() {
        try {
            Log.e("message123", "In cancel of my  manage connection.");
            mmSocket.close();
        } catch (IOException e) { }
    }
}
