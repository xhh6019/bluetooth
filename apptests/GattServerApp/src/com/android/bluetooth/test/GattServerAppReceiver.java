/*
* Copyright (c) 2012, Code Aurora Forum. All rights reserved.
*
* Redistribution and use in source and binary forms, with or without
* modification, are permitted provided that the following conditions are
* met:
*    * Redistributions of source code must retain the above copyright
*      notice, this list of conditions and the following disclaimer.
*    * Redistributions in binary form must reproduce the above
*      copyright notice, this list of conditions and the following
*      disclaimer in the documentation and/or other materials provided
*      with the distribution.
*    * Neither the name of Code Aurora Forum, Inc. nor the names of its
*      contributors may be used to endorse or promote products derived
*       from this software without specific prior written permission.

* THIS SOFTWARE IS PROVIDED "AS IS" AND ANY EXPRESS OR IMPLIED
* WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
* MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT
* ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS
* BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
* CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
* SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR
* BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
* WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
* OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN
* IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package com.android.bluetooth.test;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.app.Activity;

/**
* This class is the broadcast receiver class for the Gatt Server application.
* This receiver handles the intents and starts the Gatt Server service when the
* phone boots up and when Bluetooth is turned on
*/
public class GattServerAppReceiver extends BroadcastReceiver{
    String TAG = "GattServerAppReceiver";
    private static final int REQUEST_ENABLE_BT = 1;
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(action != null && action.equalsIgnoreCase("android.intent.action.BOOT_COMPLETED")) {
            if (mBluetoothAdapter != null) {
                // Device supports Bluetooth
                if (!mBluetoothAdapter.isEnabled()) {
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    enableBtIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(enableBtIntent);

                    Intent serviceIntent = new Intent();
                    serviceIntent.setAction("com.android.bluetooth.test.GattServerAppService");
                    Log.d(TAG, "Going to start service from BT Server app Broadcast Receiver::");
                    context.startService(serviceIntent);
                }
                else if(mBluetoothAdapter.isEnabled()) {
                    Intent serviceIntent = new Intent();
                    serviceIntent.setAction("com.android.bluetooth.test.GattServerAppService");
                    Log.d(TAG, "Going to start service from BT Server app Broadcast Receiver::");
                    context.startService(serviceIntent);
                }
            }
        }
        else if(action != null && action.equalsIgnoreCase(BluetoothAdapter.ACTION_STATE_CHANGED)) {
            if (intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR) ==
                BluetoothAdapter.STATE_ON) {
                Intent serviceIntent = new Intent();
                serviceIntent.setAction("com.android.bluetooth.test.GattServerAppService");
                Log.d(TAG, "Going to start service from BT Server app Broadcast Receiver::");
                context.startService(serviceIntent);
            }
        }
    }
}