package com.example.wesmt.testp2p;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

public class BroadCast extends BroadcastReceiver{

    private WifiP2pManager manager;
    private WifiP2pManager.Channel channel;
    private  MainActivity activity;
    private WifiP2pDevice device;
    private List<WifiP2pDevice> mPeers;
    private List<WifiP2pConfig> mConfigs;

public BroadCast(WifiP2pManager mManager, WifiP2pManager.Channel mChannel, MainActivity activity)
{
    super();
    this.manager = mManager;
    this.channel = mChannel;
    this.activity = activity;
}

    public void connect(int devicePosition) {

        WifiP2pConfig config = mConfigs.get(devicePosition);
        device = mPeers.get(devicePosition);

        manager.connect(channel, config, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {

                Toast.makeText(activity, "Connected Initiated",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int reason) {

                Toast.makeText(activity, "Connected Failed B: " + reason,
                        Toast.LENGTH_SHORT).show();
            }
        });

    }

@Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
            // Determine if Wifi P2P mode is enabled or not, alert
            // the Activity.
            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
            if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                activity.setIsWifiP2pEnabled(true);

            } else {
                //leave can't do anything have to true Wifi On
                activity.setIsWifiP2pEnabled(false);
                //activity.resetData();

            }
        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {

            // The peer list has changed!  We should probably do something about
            // that.
            // TODO: update UI list
            mPeers = new ArrayList<WifiP2pDevice>();
            mConfigs = new ArrayList<WifiP2pConfig>();
            if(manager != null)
            {
                WifiP2pManager.PeerListListener peerListListener = new WifiP2pManager.PeerListListener(){
                    @Override
                    public void onPeersAvailable(WifiP2pDeviceList peers) {
                        mPeers.clear();
                        mPeers.addAll(peers.getDeviceList());
                        activity.displaylist(peers);
                        mPeers.addAll(peers.getDeviceList());
                        for(int i = 0; i < peers.getDeviceList().size();i++)
                        {
                            WifiP2pConfig config = new WifiP2pConfig();
                            config.deviceAddress = mPeers.get(i).deviceAddress;
                            mConfigs.add(config);
                        }
                    }


                };

                manager.requestPeers(channel,peerListListener);
            }


        } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {

            // Connection state changed!  We should probably do something about
            // that.
            //

        }
        else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
//            DeviceListFragment fragment = (DeviceListFragment) MainActivity.getFragmentManager()
//                    .findFragmentById(R.id.frag_list);
//            fragment.updateThisDevice((WifiP2pDevice) intent.getParcelableExtra(
//                    WifiP2pManager.EXTRA_WIFI_P2P_DEVICE));

        }


    }




}


