package com.zero.support.box.invocation;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.zero.support.box.plugin.invoke.IInvocationReceiver;

public abstract class InvocationReceiver extends BroadcastReceiver {
    private IInvocationReceiver receiver;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (receiver == null) {
            receiver = onCreateInvocationReceiver();
        }
        receiver.onReceive(this, context, intent);
    }

    protected abstract IInvocationReceiver onCreateInvocationReceiver();
}
