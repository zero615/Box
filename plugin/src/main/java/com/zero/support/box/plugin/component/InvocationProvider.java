package com.zero.support.box.plugin.component;

import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.pm.ProviderInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import com.zero.support.box.plugin.invoke.IInvocationProvider;

import java.util.ArrayList;

public class InvocationProvider implements IInvocationProvider {
    private ContentProvider provider;

    public final ContentProvider requireProvider() {
        return provider;
    }

    @Override
    public Context attach(ContentProvider provider, Context context, ProviderInfo info) {
        this.provider = provider;
        return context;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        return 0;
    }

    @Override
    public ContentProviderResult[] applyBatch(String authority, ArrayList<ContentProviderOperation> operations) throws OperationApplicationException {
        return new ContentProviderResult[0];
    }

    @Override
    public boolean onCreate() {
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public Bundle call(String authority, String method, String arg, Bundle extras) {
        return null;
    }

    @Override
    public Uri canonicalize(Uri url) {
        return null;
    }

    @Override
    public Uri uncanonicalize(Uri url) {
        return null;
    }

    @Override
    public void onCallingPackageChanged() {

    }

    @Override
    public void onTrimMemory(int level) {

    }

    @Override
    public void onLowMemory() {

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {

    }
}
