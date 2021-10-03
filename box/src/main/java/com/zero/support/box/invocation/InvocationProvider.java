package com.zero.support.box.invocation;


import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.zero.support.box.plugin.invoke.IInvocationProvider;

public abstract class InvocationProvider extends ContentProvider {

    private IInvocationProvider provider;

    @Override
    public void attachInfo(Context context, ProviderInfo info) {
        provider = onCreateInvocationProvider();
        context = provider.attach(this, context, info);
        super.attachInfo(context, info);
    }

    protected abstract IInvocationProvider onCreateInvocationProvider();

    @Override
    public boolean onCreate() {
        return provider.onCreate();
    }

    @Nullable
    @Override
    public Bundle call(@NonNull String method, @Nullable String arg, @Nullable Bundle extras) {
        return provider.call(null, method, arg, extras);
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        return provider.query(uri, strings, s, strings1, s1);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
