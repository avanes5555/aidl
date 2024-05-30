package com.example.aidl;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.content.Intent;
import android.content.ServiceConnection;
import ru.atol.drivers10.service.IFptrService;

public class MainActivity extends AppCompatActivity
{
    private IFptrService fptrServiceBinder;
    Intent aidl_intent;
    ServiceConnection serviceConnection;
    ServiceConnection testConnection;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        aidl_intent = new Intent();
        aidl_intent.setAction("ru.atol.drivers10.service.SERVICE");
        aidl_intent.setPackage("ru.atol.drivers10.service");

        // Создаём соединение для сервиса АТОЛ
        serviceConnection = new ServiceConnection()
        {
            @Override
            public void onServiceDisconnected(ComponentName name)
            {
                fptrServiceBinder = null;
                System.out.println("AIDL disconnected ");
            }

            @Override
            public void onServiceConnected(ComponentName name, IBinder service)
            {
                System.out.println("AIDL connected ");
                fptrServiceBinder = IFptrService.Stub.asInterface(service);
                try {



                    Log.d("AtolDriver", String.format("Версия драйвера ККТ - %s", fptrServiceBinder.getDriverVersion()));
                    String result = fptrServiceBinder.processJson("{\n" +
                            "    \"type\": \"closeShift\",\n" +
                            "    \"operator\": {\n" +
                            "       \"name\": \"Иванов\",\n" +
                            "       \"vatin\": \"123654789507\"\n" +
                            "    }\n" +
                            "}");
                    System.out.println("AIDL executed ");
                    TextView myTextView = (TextView) findViewById(R.id.test);
                    myTextView.setText(result);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        };


        onAIDL_bind_ButtonClick(null);
    }

    // обработчик нажатия кнопки команды АТОЛ через Intent
    public void on_send_intent_button(View v)
    {

    }


    public void onAIDL_bind_ButtonClick(View v)
    {
        TextView myTextView = (TextView) findViewById(R.id.test);
        myTextView.setText("Нажата кнопка onAIDLButtonClick");

        boolean binded = bindService(aidl_intent, serviceConnection, BIND_AUTO_CREATE);
        if (binded) System.out.println("binded ");
        else System.out.println("not binded ");
    }


}