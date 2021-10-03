package com.zero.support.box.invoke;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public interface IInvocationReceiver {
    void onReceive(BroadcastReceiver receiver,Context context, Intent intent);
}
