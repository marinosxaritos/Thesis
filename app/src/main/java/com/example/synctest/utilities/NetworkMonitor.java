package com.example.synctest.utilities;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.annotation.NonNull;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.synctest.contacts.DbContact;
import com.example.synctest.database.DbHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class NetworkMonitor extends BroadcastReceiver {
    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @Override
    public void onReceive(Context context, Intent intent) {
        if (checkNetworkConnection(context)) {

            DbHelper dbHelper = new DbHelper(context);
            SQLiteDatabase database = dbHelper.getReadableDatabase();

            Cursor cursor = dbHelper.readFromLocalDatabase(database);

            while (cursor.moveToNext()) {
                @SuppressLint("Range") int sync_status = cursor.getInt(cursor.getColumnIndex(DbContact.SYNC_STATUS));

                if (sync_status == DbContact.SYNC_STATUS_FAILED) {
                    @SuppressLint("Range") String Name = cursor.getString(cursor.getColumnIndex(DbContact.NAME));
                    @SuppressLint("Range") String LastName = cursor.getString(cursor.getColumnIndex(DbContact.LAST_NAME));
                    @SuppressLint("Range") String Date = cursor.getString(cursor.getColumnIndex(DbContact.DATE));
                    @SuppressLint("Range") String Time = cursor.getString(cursor.getColumnIndex(DbContact.TIME));
                    @SuppressLint("Range") String Address = cursor.getString(cursor.getColumnIndex(DbContact.ADDRESS));

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, DbContact.SERVER_URL,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        String Response = jsonObject.getString("response");

                                        if (Response.equals("OK")) {
                                            dbHelper.updateLocalDatabase(Name, LastName, Date, Time, Address, DbContact.SYNC_STATUS_OK, database);
                                            context.sendBroadcast(new Intent(DbContact.UI_UPDATE_BROADCAST));
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    })
                    {
                        @NonNull
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("name", Name);
                            params.put("last_name", LastName);
                            params.put("date", Date);
                            params.put("time", Time);
                            params.put("address", Address);
                            return params;
                        }
                    }
                            ;
                    MySingleton.getInstance(context).addToRequestQue(stringRequest);
                }
            }

            dbHelper.close();
        }

    }

    public boolean checkNetworkConnection(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnectedOrConnecting());
    }
}

