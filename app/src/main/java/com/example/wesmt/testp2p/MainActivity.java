package com.example.wesmt.testp2p;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.Context;
import android.content.IntentFilter;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.pm.PackageManager;
import android.database.DataSetObserver;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.provider.MediaStore;
import android.database.Cursor;
import android.view.MenuItem;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.io.File;
import java.util.List;
import android.widget.Toast;

import java.lang.Object;


import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnClickListener {


    private boolean IsWifiP2pEnabled = false;
    Channel mChannel;
    private Boolean hostdevice = false;

    private ListView lv;
    private ListView songList;
    WifiP2pManager mManager;

    //WifiP2pDevice device;
    private final IntentFilter intentFilter = new IntentFilter();
    private BroadCast receiver = new BroadCast(mManager,mChannel,this) {
        @Override
        public void onReceive(Context context, Intent intent) {

        }
    };

    private ArrayAdapter<String> wifiAdapter;
    private ArrayAdapter<String> songAdapter;
    private int deviceNumber;
    //private BroadCast peers = new BroadCast(mManager,mChannel,this);

    //arraylist for songs
    public ArrayList<Song> arrayList = new ArrayList<Song>();





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        //  Indicates a change in the Wi-Fi P2P status.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);

        // Indicates a change in the list of available peers.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);

        // Indicates the state of Wi-Fi P2P connectivity has changed.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);

        // Indicates this device's details have changed.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);


        mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(this, getMainLooper(), null);
        lv = (ListView) findViewById(R.id.mylist);
        wifiAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        lv.setAdapter(wifiAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView parent, View view, int pos, long id)
            {
                Object listItem = lv.getItemAtPosition(pos);
                //int test = Integer.valueOf((String) listItem);
                receiver.connect(pos);
            }


        });
        //peers.onPeersAvailable((WifiP2pDeviceList) peers.peers);
        Button host = (Button) findViewById(R.id.listen);
        host.setOnClickListener(this);
        Button join = (Button) findViewById((R.id.join));
        join.setOnClickListener(this);

        //list view of songs
        getSongs();
        //Song k = new Song();

        songList = (ListView) findViewById(R.id.songs);
        songAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        songList.setAdapter(songAdapter);

        for(Song k : arrayList)
        {
            songAdapter.add(k.getTitle());
        }
        songList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView parent, View view, int pos, long id)
            {
                Object listItem = songList.getItemAtPosition(pos);
                //int test = Integer.valueOf((String) listItem);
                Song k = arrayList.get(pos);
                Toast.makeText(MainActivity.this, String.format("%s was chose. %s", k.getTitle(),k.getSongPath()),Toast.LENGTH_SHORT).show();
                // TODO: we need to send this song to any connected device

                // TODO: we need to go back and get the songs actual data to send
            }


        });



        Log.d("TAG","i'm back");
       /* String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";

        String[] projection = {
                //MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.TITLE,
                //MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.DURATION
        };

            ContentResolver cr = this.getContentResolver();
        Cursor cursor = cr.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                null,
                null);


        while(cursor.moveToNext()){
            songs.add(cursor.getString(0) + "||" + cursor.getString(1) + "||" +   cursor.getString(2) + "||" +   cursor.getString(3) + "||" +  cursor.getString(4) + "||" +  cursor.getString(5));
        }

            cursor.close();
            Log.d("value is: ", songs.get(5));*/



    }


    public void getSongs()
    {


        ContentResolver contentResolver = getContentResolver();
        String selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0";
        Uri songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)==
                PackageManager.PERMISSION_GRANTED) {

            //do the things
            } else {
            requestPermissions(new String[] { Manifest.permission.READ_EXTERNAL_STORAGE },
                    10);}
        Cursor songCursor = contentResolver.query(songUri,null,selection,null,null);
        if(songCursor != null && songCursor.moveToFirst())
        {
            int songId = songCursor.getColumnIndex(MediaStore.Audio.Media._ID);
            int songTitle = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int data = songCursor.getColumnIndex(MediaStore.Audio.Media.DATA);

            do{
                long currentId = songCursor.getLong(songId);
                String currentTitle = songCursor.getString(songTitle);
                String path = songCursor.getString(data);
                arrayList.add(new Song(currentId,currentTitle,path));
            }while(songCursor.moveToNext());
        }
        songCursor.close();


    }

    public void onClick(View v)
    {
        switch(v.getId()){
            case R.id.listen: {

                mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {
                    @Override
                    public void onSuccess() {

                        hostdevice = true;
                        Toast.makeText(MainActivity.this, "Discovery Initiated",
                                Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onFailure(int reasonCode) {
                        Toast.makeText(MainActivity.this, "Discovery Failed : " + reasonCode,
                                Toast.LENGTH_SHORT).show();
                    }
                });


            }
            case R.id.join: {

                break;
            }

        }
    }

    public void displaylist(WifiP2pDeviceList peerList)
    {
        wifiAdapter.clear();
        for(WifiP2pDevice peer : peerList.getDeviceList()){
            wifiAdapter.add(peer.deviceName + "\n" + peer.deviceAddress);
        }


    }



    @Override
    public void onResume() {
        super.onResume();
        receiver = new BroadCast(mManager, mChannel, this);
        registerReceiver(receiver, intentFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }






    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setIsWifiP2pEnabled(boolean b) {
        this.IsWifiP2pEnabled = b;
    }
}

