package com.geodoer.bluetoothcontroler.controller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.geodoer.bluetoothcontroler.service.BluetoothLeService;
import com.geodoer.bluetoothcontroler.BcUtils;

/**
 * Created by kuyen on 2015/6/22.f
 */
public class BleActionReceiver extends BroadcastReceiver {

    private whenReceivedBleAction mWhenReceivedBleAction;

    public BleActionReceiver() {
        super();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();

        if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action))
        {
            String data = intent.getStringExtra(BluetoothLeService.EXTRA_DATA);
            if(data!=null)
            {
                Log.wtf(BcUtils.logTag, "---------- Received "+
                        data.substring(0, 2)+
                        " ----------");
                mWhenReceivedBleAction.onActionDataAvailable(data.substring(0, 2));
            }
        }
        else if (BcUtils.SERVICE_STATE.equals(action))
        {
            String data = intent.getStringExtra(BcUtils.EXTRA_DATA);

            if(data.equals("null"))
            {
                Log.wtf(BcUtils.logTag, "---------- Received null ----------");

                mWhenReceivedBleAction.onBleServiceNotConnecting();
                BcUtils.IS_SERVICE_EXSTING = false;
            }
            else
            {
                Log.wtf(BcUtils.logTag, "---------- Received "+action+" ----------");

                mWhenReceivedBleAction.onBleServiceConnecting();
                BcUtils.IS_SERVICE_EXSTING = true;
            }
        }
    }

    public void setWhenReceivedBleActionTarget(whenReceivedBleAction whenReceivedBleActionTarget){
        this.mWhenReceivedBleAction = whenReceivedBleActionTarget;
    }

    // IntentFilter
    public static IntentFilter BleIntentFilter()
    {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BcUtils.SERVICE_STATE);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }

    // interface
    public interface whenReceivedBleAction{
        void onActionDataAvailable(String actionData);

        void onBleServiceNotConnecting();

        void onBleServiceConnecting();
    }
}
