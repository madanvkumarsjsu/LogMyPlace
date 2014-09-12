package com.example.app;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DisplayPlaces extends ActionBarActivity {


    ArrayList<String> al;
    Intent intent;
    ArrayList alPlace = new ArrayList();
    ArrayList alLat = new ArrayList();
    ArrayList alLon = new ArrayList();
    double lat;
    double lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_places);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.display_places, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
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
            View rootView = inflater.inflate(R.layout.fragment_display_places, container, false);
            return rootView;
        }
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        intent = getIntent();
        al = (ArrayList<String>) intent.getExtras().get("contents");
        if(al == null){
            al = new ArrayList<String>();
        }
        getData(al);
        setData(alPlace);
    }
    private ArrayList getData(ArrayList al){
        ArrayList alReturn = new ArrayList();

        String str;
        String[] strArr;
        for(int i =0;i<al.size();i++){
            str = (String) al.get(i);
            strArr = str.split("~");
            alPlace.add(strArr[0]);
            alLat.add(strArr[1]);
            alLon.add(strArr[2]);
        }
        return alReturn;
    }
    public void getAddressBtnClkHandler(View v){

        Spinner sp = (Spinner) findViewById(R.id.spinner);
        String strSelectedItem = sp.getSelectedItem().toString();
        if(strSelectedItem != null){
           // Toast.makeText(getApplicationContext(), "pos::"+sp.getSelectedItemPosition(),Toast.LENGTH_LONG).show();
            int pos = (Integer)sp.getSelectedItemPosition();
            lat = Double.parseDouble((String) alLat.get(pos));
            lon = Double.parseDouble((String)alLon.get(pos));
            getAddress(lat, lon);
        }
    }
    private void setData(ArrayList alPlace){
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, alPlace);
        spinner.setAdapter(spinnerArrayAdapter);
    }
    public void ClearLocations(View view){
        getApplicationContext().deleteDatabase("PlaceStorage.db");
        setData(new ArrayList());
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    public List<Address> getAddress(double latitude, double longitude) {
        try {
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(this, Locale.getDefault());
            if (latitude != 0 || longitude != 0) {
                addresses = geocoder.getFromLocation(latitude, longitude, 1);
                String address = addresses.get(0).getAddressLine(0);
                String city = addresses.get(0).getAddressLine(1);
                String country = addresses.get(0).getAddressLine(2);
                Log.d("TAG", "address = " + address + ", city =" + city + ", country = " + country);
                Toast.makeText(getApplicationContext(), address + " " + city + " " + country,Toast.LENGTH_LONG).show();
                return addresses;
            } else {
                Toast.makeText(getApplicationContext(), "latitude and longitude are null",Toast.LENGTH_LONG).show();
                return null;
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Service Not available currently",Toast.LENGTH_LONG).show();
            e.printStackTrace();
            return null;
        }
    }
    public void ViewMyMap(View view){
        Spinner sp = (Spinner) findViewById(R.id.spinner);
        String strSelectedItem = sp.getSelectedItem().toString();
        if(strSelectedItem != null){
            //Toast.makeText(getApplicationContext(), "pos::"+sp.getSelectedItemPosition(),Toast.LENGTH_LONG).show();
            int pos = (Integer)sp.getSelectedItemPosition();
            lat = Double.parseDouble((String) alLat.get(pos));
            lon = Double.parseDouble((String)alLon.get(pos));
            final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/?q="+lat+","+lon));
            intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
            ((Activity) this).startActivity(intent);

        }
    }

    public void MessageAFriend(View view){
        Spinner sp = (Spinner) findViewById(R.id.spinner);
        String strSelectedItem = sp.getSelectedItem().toString();
        if(strSelectedItem != null){
            // Toast.makeText(getApplicationContext(), "pos::"+sp.getSelectedItemPosition(),Toast.LENGTH_LONG).show();
            int pos = (Integer)sp.getSelectedItemPosition();
            lat = Double.parseDouble((String) alLat.get(pos));
            lon = Double.parseDouble((String)alLon.get(pos));
            //String uri= "smsto:";
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setType("vnd.android-dir/mms-sms");
            //intent.setType("text/plain");
            intent.putExtra("sms_body","http://maps.google.com/?q="+lat+","+lon);
            intent.putExtra("compose_mode", true);
            startActivity(intent);
            //startActivity(Intent.createChooser(intent, "Choose your messenger"));
            //finish();
        }

    }
}
