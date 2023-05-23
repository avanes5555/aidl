package com.example.aidl;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.widget.Toast;

import ru.atol.drivers10.service.IFptrService;



public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent();
        intent.setAction("ru.atol.drivers10.service.SERVICE");
        intent.setPackage("ru.atol.drivers10.service");
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);
    }

    private final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            fptrServiceBinder = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            fptrServiceBinder = IFptrService.Stub.asInterface(service);

            try {
                String result = fptrServiceBinder.processJson("{\n" +
                        "    \"type\": \"closeShift\",\n" +
                        "    \"operator\": {\n" +
                        "       \"name\": \"Иванов\",\n" +
                        "       \"vatin\": \"123654789507\"\n" +
                        "    }\n" +
                        "}");
                Toast.makeText(MainActivity.this,
                        result,
                        Toast.LENGTH_LONG).show();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    };
    private IFptrService fptrServiceBinder;
};