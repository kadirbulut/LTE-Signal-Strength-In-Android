package com.example.signalstrength;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.CellIdentityLte;
import android.telephony.CellInfo;
import android.telephony.CellInfoLte;
import android.telephony.CellSignalStrengthLte;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import im.delight.android.location.SimpleLocation;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private Float DEFAULT_ZOOM = 15f;
    private TextView text_info;
    private String info = "";
    LocationManager locationManager;
    LocationListener locationListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        text_info = findViewById(R.id.text_info);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        getPermissions();
        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);
        SimpleLocation simpleLocation = new SimpleLocation(this);
        LatLng location = new LatLng(simpleLocation.getLatitude(), simpleLocation.getLongitude());
        MarkerOptions marker = new MarkerOptions().position(location).title("");
        marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_red));
        mMap.addMarker(marker);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, DEFAULT_ZOOM));
        info += ("Latitude:" + simpleLocation.getLatitude() + "\nLongitude:" + simpleLocation.getLongitude() + "\n");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setInfos();
        }

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
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                updateMap();
            }
        });

    }

    private void updateMap() {
        SimpleLocation simpleLocation = new SimpleLocation(this);
        LatLng location = new LatLng(simpleLocation.getLatitude(), simpleLocation.getLongitude());
        MarkerOptions marker = new MarkerOptions().position(location).title("");
        marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_red));
        mMap.addMarker(marker);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, DEFAULT_ZOOM));
        info += ("Latitude:" + simpleLocation.getLatitude() + "\nLongitude:" + simpleLocation.getLongitude() + "\n");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setInfos();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setInfos() {

        //operator name
        // Get System TELEPHONY service reference
        TelephonyManager tManager = (TelephonyManager) getBaseContext()
                .getSystemService(Context.TELEPHONY_SERVICE);

        // Get carrier name (Network Operator Name)
        String carrierName = tManager.getNetworkOperatorName();
        info += "Sim Operator:" + carrierName + "\n";
        info += "Network Operator:"+tManager.getNetworkOperatorName()+"\n";
        // Get Phone model and manufacturer name
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        info += "Device:" + manufacturer + "-" + model + "\n";


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        List<CellInfo> cellInfoList = tManager.getAllCellInfo();
        if (cellInfoList != null){
            for (final CellInfo cellInfo : cellInfoList){
                if (cellInfo instanceof CellInfoLte){
                    final CellSignalStrengthLte lte = ((CellInfoLte) cellInfo).getCellSignalStrength();
                    String temp = "";
                    temp+="Rsrp:"+ String.valueOf(lte.getRsrp())+",";
                    temp+="Rsrq:"+ String.valueOf(lte.getRsrq())+",";
                    temp+="Rssnr:"+ String.valueOf(lte.getRssnr())+"\n";

                    info+=temp;
                }
            }
        }

        text_info.setText(info);

    }


    private void getPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {


        return false;
    }
}
