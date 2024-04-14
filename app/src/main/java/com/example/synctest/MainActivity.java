package com.example.synctest;

import com.example.synctest.contacts.Contact;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.synctest.contacts.DbContact;
import com.example.synctest.database.DbHelper;
import com.example.synctest.fragments.IncidentReportFragment;
import com.example.synctest.ui.RecyclerAdapter;
import com.example.synctest.utilities.MySingleton;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    int timeHour, timeMinute;
    EditText License;
    EditText Name;
    EditText LastName;
    EditText Date;
    Button btLocation;
    TextView Address;
    TextView Time;
    Button btNext;
    FusedLocationProviderClient fusedLocationProviderClient;
    DatePickerDialog.OnDateSetListener setListener;
    RecyclerView recyclerView;
    //EditText editText;
    RecyclerView.LayoutManager layoutManager;

    RecyclerAdapter adapter;
    ArrayList<Contact> arrayList = new ArrayList<>();
    BroadcastReceiver broadcastReceiver;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        License = (EditText) findViewById(R.id.license);
        Name = (EditText) findViewById(R.id.name);
        LastName = (EditText) findViewById(R.id.lastname);

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                // Get the contact data that was swiped
                Contact contactToDelete = arrayList.get(position);
                // Remove the item from the dataset
                arrayList.remove(position);
                // Notify the adapter about the item removal
                adapter.notifyItemRemoved(position);

                // Delete the contact from the local database
                deleteFromLocalStorage(contactToDelete.getLicense() ,contactToDelete.getName(), contactToDelete.getLastName(), contactToDelete.getDate(), contactToDelete.getTime(), contactToDelete.getAddress());
            }

        };

// Attach the ItemTouchHelper to the RecyclerView
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);


        Date = (EditText) findViewById(R.id.date);

        btLocation = (Button) findViewById(R.id.locationButton);
        Address = (TextView) findViewById(R.id.address);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        btLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check permission
                if (ActivityCompat.checkSelfPermission(MainActivity.this
                        , Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    //When permission granted
                    getLocation();
                } else {
                    //When permission denied
                    ActivityCompat.requestPermissions(MainActivity.this
                            , new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
                }
            }
        });

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        MainActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month = month + 1;
                        //String date = day + "/" + month + "/" + year;
                        String date = year + "-" + month + "-" + day;
                        Date.setText(date);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        Time = (TextView) findViewById(R.id.time);

        Time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Initialize time picker dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        MainActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minuteOfDay) {
                                //initialize hour and minute
                                timeHour = hourOfDay;
                                timeMinute = minuteOfDay;
                                //store hour and minute in string
                                String time = timeHour + ":" + timeMinute;
                                //Initialize 24 hours time format
                                @SuppressLint("SimpleDateFormat") SimpleDateFormat f24Hours = new SimpleDateFormat(
                                        "HH:mm"
                                );
                                try {
                                    java.util.Date date_t = f24Hours.parse(time);
                                    //Initialize 12 hours time format
                                    @SuppressLint("SimpleDateFormat") SimpleDateFormat f12Hours = new SimpleDateFormat(
                                            "hh:mm aa"
                                    );
                                    //Set selected time on text view
                                    Time.setText(f12Hours.format(date_t));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                            }
                        }, 12, 0, false
                );
                //Set transparent background
                timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                //Displayed previous selected time
                timePickerDialog.updateTime(timeHour, timeMinute);
                //Show dialog
                timePickerDialog.show();
            }
        });

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        adapter = new RecyclerAdapter(arrayList);
        recyclerView.setAdapter(adapter);
        readFromLocalStorage();
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                readFromLocalStorage();
            }
        };

        btNext = (Button) findViewById(R.id.nextBtn);



        btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //btNext.setVisibility(View.GONE);
                findViewById(R.id.recyclerView).setVisibility(View.GONE);
                findViewById(R.id.scroll1).setVisibility(View.GONE);


                Fragment fragment = new IncidentReportFragment();

                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.activity_main, fragment).commit();
            }
        });
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                //Initialize location
                Location location = task.getResult();
                if (location != null) {

                    try {
                        //Initialize geoCoder
                        Geocoder geocoder = new Geocoder(MainActivity.this,
                                Locale.getDefault());
                        //Initialize address list
                        List<Address> addresses = geocoder.getFromLocation(
                                location.getLatitude(), location.getLongitude(), 1
                        );
                        //Set address on TextView
                        Address.setText(Html.fromHtml(
                                "<font color='#6200EE'><b>Address :</b><br></font>"
                                + addresses.get(0).getAddressLine(0)
                        ));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void submitName(View view) {
        String license = License.getText().toString();
        String  name = Name.getText().toString();
        String last_name = LastName.getText().toString();
        String date = Date.getText().toString();
        String time = Time.getText().toString();
        String address = Address.getText().toString();
        saveToAppServer(license, name, last_name, date, time, address);
        License.setText("");
        Name.setText("");
        LastName.setText("");
        Date.setText("");
        Time.setText(" Select Time");
        Address.setText("Address");
    }

    @SuppressLint("NotifyDataSetChanged")
    private void readFromLocalStorage() {
        arrayList.clear();
        DbHelper dbHelper = new DbHelper(this);
        SQLiteDatabase database = dbHelper.getReadableDatabase();

        Cursor cursor = dbHelper.readFromLocalDatabase(database);

        while (cursor.moveToNext()) {
            @SuppressLint("Range") String license = cursor.getString(cursor.getColumnIndex(DbContact.LICENSE));
            @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(DbContact.NAME));
            @SuppressLint("Range") String last_name = cursor.getString(cursor.getColumnIndex(DbContact.LAST_NAME));
            @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex(DbContact.DATE));
            @SuppressLint("Range") String time = cursor.getString(cursor.getColumnIndex(DbContact.TIME));
            @SuppressLint("Range") String address = cursor.getString(cursor.getColumnIndex(DbContact.ADDRESS));
            @SuppressLint("Range") int sync_status = cursor.getInt(cursor.getColumnIndex(DbContact.SYNC_STATUS));
            arrayList.add(new Contact(license, name, last_name, date, time, address, sync_status));
        }

        adapter.notifyDataSetChanged();
        cursor.close();
        dbHelper.close();

    }



    private void saveToAppServer(final String license, String name, String last_name, String date, String time, String address) {


        if (checkNetworkConnection()) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, DbContact.SERVER_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("Response", response);
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String Response = jsonObject.getString("response");

                                if (Response.equals("OK")) {
                                    saveToLocalStorage(license, name, last_name, date, time, address, DbContact.SYNC_STATUS_OK);
                                }
                                else {
                                    saveToLocalStorage(license, name, last_name, date, time, address, DbContact.SYNC_STATUS_FAILED);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    saveToLocalStorage(license, name, last_name, date, time, address, DbContact.SYNC_STATUS_FAILED);
                }
            })
            {
                @NonNull
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("license", license);
                    params.put("name", name);
                    params.put("last_name", last_name);
                    params.put("date", date);
                    params.put("time", time);
                    params.put("address", address);
                    return params;
                }
            };
            MySingleton.getInstance(MainActivity.this).addToRequestQue(stringRequest);
        }
        else {
            saveToLocalStorage(license, name, last_name, date, time, address, DbContact.SYNC_STATUS_FAILED);
        }

    }

    public boolean checkNetworkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnectedOrConnecting());
    }

    @SuppressLint("NotifyDataSetChanged")
    private void saveToLocalStorage(String license, String name, String last_name, String date, String time, String address, int sync) {

        DbHelper dbHelper = new DbHelper(this);
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        dbHelper.saveToLocalDatabase(license, name, last_name, date, time, address, sync, database);
        readFromLocalStorage();
        adapter.notifyDataSetChanged();
        dbHelper.close();
    }


    private void deleteFromLocalStorage(String license, String name, String lastName, String date, String time, String address) {
        DbHelper dbHelper = new DbHelper(MainActivity.this);
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        // Define the selection criteria for the row to delete
        String selection = DbContact.LICENSE + " = ? AND " + DbContact.NAME + " = ? AND " + DbContact.LAST_NAME + " = ? AND " + DbContact.DATE + " = ? AND " + DbContact.TIME + " = ? AND " + DbContact.ADDRESS +  " = ?";
        // Define the selection arguments
        String[] selectionArgs = {license, name, lastName, date, time, address};

        // Issue SQL statement to delete the selected row
        int deletedRows = database.delete(DbContact.TABLE_NAME, selection, selectionArgs);

        if (deletedRows > 0) {
            // Row deleted successfully
            Log.d("Delete", "Row deleted successfully from the database");
        } else {
            // Failed to delete row
            Log.e("Delete", "Failed to delete row from the database");
        }

        // Close the database connection
        dbHelper.close();
    }






    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    protected void onStart() {
        super.onStart();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            registerReceiver(broadcastReceiver, new IntentFilter(DbContact.UI_UPDATE_BROADCAST), Context.RECEIVER_NOT_EXPORTED);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }
}