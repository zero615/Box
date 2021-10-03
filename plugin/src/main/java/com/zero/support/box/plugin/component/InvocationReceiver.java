package com.zero.support.box.plugin.component;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.zero.support.box.plugin.invoke.IInvocationReceiver;

public class InvocationReceiver implements IInvocationReceiver {
    private BroadcastReceiver receiver;

    @Override
    public final void onReceive(BroadcastReceiver receiver, Context context, Intent intent) {
        this.receiver = receiver;
        onReceiver(context, intent);
    }

    public final Bundle getResultExtras(boolean makeMap) {
        return receiver.getResultExtras(makeMap);
    }

    public final String getResultData() {
        return receiver.getResultData();
    }

    public final int getResultCode() {
        return receiver.getResultCode();
    }

    public final boolean getDebugUnregister() {
        return receiver.getDebugUnregister();
    }

    public void onReceiver(Context context, Intent intent) {

    }

    public final BroadcastReceiver requireReceiver() {
        return receiver;
    }

    public final boolean getAbortBroadcast() {
        return receiver.getAbortBroadcast();
    }
}
