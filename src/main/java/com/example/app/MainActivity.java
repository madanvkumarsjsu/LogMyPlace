package com.example.app;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import java.lang.reflect.Array;
import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

    GPSTracker gps;
    Button btn;
    double Lat;
    double Lon;
    SQLiteHelper sqLiteHelper ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        /*if (id == R.id.action_settings) {
            return true;
        }*/
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }
    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        btn = (Button) findViewById(R.id.btnshowLoc);

    }

    public void onShowLocClick(View v) {

        gps = new GPSTracker(getApplicationContext());
        if(gps.canGetLocation()){
            Lat = gps.getLatitude();
            Lon = gps.getLongitude();
            StoreMyLatLong();
            Toast.makeText(getApplicationContext(), "Storing your Location - \nLat: " + Lat + "\nLong: " + Lon, Toast.LENGTH_LONG).show();
        }
        else{
            gps.showSettingsAlert();
        }
    }
    public void StoreMyLatLong(){

        sqLiteHelper = new SQLiteHelper(this);
        SQLiteDatabase sqLiteDatabase = sqLiteHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        EditText et = (EditText) findViewById(R.id.etname);
        String name = et.getText().toString();
        if(name == null || "".equals(name)){
            name = "New Place";
        }
        //contentValues.put(SQLiteHelper.name, "Place1");
        contentValues.put(SQLiteHelper.name, name);
        contentValues.put(SQLiteHelper.lat, Lat);
        contentValues.put(SQLiteHelper.lon, Lon);
        Long newRowID;
        newRowID = sqLiteDatabase.insert(SQLiteHelper.TABLE_PLACE, null,contentValues);
        Log.i("RowID@@@@@@@@@@@@", newRowID.toString());
    }

    public void getPlaces(View view){


        if(sqLiteHelper == null){
            sqLiteHelper = new SQLiteHelper(this);
        }
        SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
        String[] projections = {SQLiteHelper.placeid, SQLiteHelper.name, SQLiteHelper.lat, SQLiteHelper.lon};
        Cursor cursor = db.query(SQLiteHelper.TABLE_PLACE, projections, null,null,null,null,null);
        cursor.moveToFirst();
        ArrayList<String> al = new ArrayList<String>();
        String row;
        while (!cursor.isAfterLast())
        {
            row = cursor.getString(1) + "~" + cursor.getString(2) + "~" + cursor.getString(3);
            al.add(row);
            cursor.moveToNext();
        }
        cursor.close();
        if(al.size() > 0){
            Intent intent = new Intent(this, DisplayPlaces.class);
            intent.putExtra("contents", al);
            startActivity(intent);
        }
        else{
            Toast.makeText(getApplicationContext(), "No Places Saved", Toast.LENGTH_LONG).show();
        }

    }
    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        if(gps != null)
            gps.stopUsingGPS();
    }
}
