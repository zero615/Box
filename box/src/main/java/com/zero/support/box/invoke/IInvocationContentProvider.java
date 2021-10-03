package com.zero.support.box.invoke;

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

import java.util.ArrayList;

public interface IInvocationContentProvider {
    @BoxName("attach")
    Context attach(ContentProvider provider, Context context, ProviderInfo info);

    @BoxName("bulkInsert")
    public int bulkInsert(Uri uri, ContentValues[] values);

    @BoxName("applyBatch")
    public ContentProviderResult[] applyBatch(String authority, ArrayList<ContentProviderOperation> operations) throws OperationApplicationException;

    @BoxName("onCreate")
    boolean onCreate();

    @BoxName("query")
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder);


    @BoxName("getType")
    public String getType(Uri uri);

    @BoxName("insert")
    public Uri insert(Uri uri, ContentValues values);


    @BoxName("delete")
    public int delete(Uri uri, String selection, String[] selectionArgs);


    @BoxName("update")
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs);


    @BoxName("call")
    public Bundle call(String authority, String method, String arg, Bundle extras);


    @BoxName("canonicalize")
    public Uri canonicalize(Uri url);

    @BoxName("uncanonicalize")
    public Uri uncanonicalize(Uri url);


    @BoxName("onCallingPackageChanged")
    public void onCallingPackageChanged();

    @BoxName("onTrimMemory")
    public void onTrimMemory(int level);


    @BoxName("onLowMemory")
    public void onLowMemory();


    @BoxName("onConfigurationChanged")
    public void onConfigurationChanged(Configuration newConfig);
}
