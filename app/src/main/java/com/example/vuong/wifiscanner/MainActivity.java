package com.example.vuong.wifiscanner;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.vuong.wifiscanner.adapters.ListViewAdapter;
import com.example.vuong.wifiscanner.objects.Wifi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_LOCATION_RESULT = 0;

    WifiManager mainWifi;
    WifiReceiver receiverWifi;

    StringBuilder sb = new StringBuilder();

    private final Handler handler = new Handler();

    Button btnScan;
    ListView listWifi;
    EditText txtPassword;

    List<Wifi> listData = new ArrayList<Wifi>();
    ListViewAdapter listViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtPassword = (EditText) findViewById(R.id.txtPassword);
        listViewAdapter = new ListViewAdapter(this, listData);
        listWifi = (ListView) findViewById(R.id.listWifi);
        btnScan = (Button) findViewById(R.id.btnScan);
        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermssion();
            }
        });
        mainWifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        receiverWifi = new WifiReceiver();
        registerReceiver(receiverWifi, new IntentFilter(
                WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        if(mainWifi.isWifiEnabled()==false)
        {
            mainWifi.setWifiEnabled(true);
        }



    }

    public void doInback()
    {
        // TODO Auto-generated method stub
        mainWifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        receiverWifi = new WifiReceiver();
        registerReceiver(receiverWifi, new IntentFilter(
                WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        mainWifi.startScan();
//        doInback();

//        handler.postDelayed(new Runnable() {
//
//            @Override
//            public void run()
//            {
//                // TODO Auto-generated method stub
//                mainWifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
//
//                receiverWifi = new WifiReceiver(MainActivity.this);
//                registerReceiver(receiverWifi, new IntentFilter(
//                        WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
//                mainWifi.startScan();
//                doInback();
//            }
//        }, 1000);

    }

    private void checkPermssion() {

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mainWifi.startScan();

            } else {

                requestPermissions(new String[] {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_RESULT);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_LOCATION_RESULT) {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mainWifi.startScan();
                doInback();
            }
        }

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 0, 0, "Refresh");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onPause()
    {
        unregisterReceiver(receiverWifi);
        super.onPause();
    }

    @Override
    protected void onResume()
    {
        registerReceiver(receiverWifi, new IntentFilter(
                WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        super.onResume();

        listWifi.setAdapter(listViewAdapter);
    }

    class WifiReceiver extends BroadcastReceiver {

        public WifiReceiver() {
        }

        public void onReceive(Context c, Intent intent)
        {

//            ArrayList<String> connections=new ArrayList<String>();
//            ArrayList<Float> Signal_Strenth= new ArrayList<Float>();

            boolean isConnected = false;
            String networkPass = txtPassword.getText().toString();

            WifiConfiguration conf = new WifiConfiguration();
            conf.status = WifiConfiguration.Status.ENABLED;
            conf.priority = 40;
            conf.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
            conf.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
            conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            conf.preSharedKey = "\"" + networkPass + "\"";

            sb = new StringBuilder();
            List<ScanResult> wifiList;
            wifiList = mainWifi.getScanResults();
            listData.clear();
            for(int i = 0; i < wifiList.size(); i++)
            {
                Wifi wifi = new Wifi(wifiList.get(i).SSID, wifiList.get(i).BSSID, false);

                if(!isConnected) {
                    conf.SSID = "\"" + wifiList.get(i).SSID + "\"";

                    int networkId = mainWifi.addNetwork(conf);
                    if (mainWifi.disconnect() && mainWifi.enableNetwork(networkId, true) && mainWifi.reconnect()) {
                        isConnected = true;
                    }

                    wifi.setConnected(true);
                }

                if(!wifiList.get(i).SSID.equals("") && !listData.contains(wifi)) {
                    listData.add(wifi);
                }
            }

            listViewAdapter.notifyDataSetChanged();
        }
    }
}
