package com.zero.support.box.plugin.invoke;

import android.content.Context;
import android.content.Intent;

public interface IInvocationReceiver {
    void onReceive(Context context, Intent intent);
}
