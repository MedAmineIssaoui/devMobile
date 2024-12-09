package com.example.logtracking;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.telephony.CellInfo;
import android.telephony.CellInfoLte;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements LocationListener {

    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private LocationManager locationManager;
    private TelephonyManager telephonyManager;
    private boolean isLogging = false;
    private TextView statusTextView;
    private TextView latitudeTextView;
    private TextView longitudeTextView;
    private TextView altitudeTextView;
    private TextView signalTextView;
    private TextView batteryTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        statusTextView = findViewById(R.id.statusTextView);
        latitudeTextView = findViewById(R.id.latitudeTextView);
        longitudeTextView = findViewById(R.id.longitudeTextView);
        altitudeTextView = findViewById(R.id.altitudeTextView);
        signalTextView = findViewById(R.id.signalTextView);
        batteryTextView = findViewById(R.id.batteryTextView);

        Button startButton = findViewById(R.id.startButton);
        Button stopButton = findViewById(R.id.stopButton);

        startButton.setOnClickListener(
                new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLogging();
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopLogging();
            }
        });

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION_PERMISSION);
            return;
        }
    }

    private void startLogging() {
        if (!isLogging) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION_PERMISSION);
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            isLogging = true;
            statusTextView.setText("Status: Logging");
        }
    }

    private void stopLogging() {
        if (isLogging) {
            locationManager.removeUpdates(this);
            isLogging = false;
            statusTextView.setText("Status: Not Logging");
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        double altitude = location.getAltitude();
        int signalStrength = getSignalStrength();
        int batteryLevel = getBatteryLevel();

        Log.d("MainActivity", "Location changed: " + latitude + ", " + longitude + ", " + altitude);
        Log.d("MainActivity", "Signal strength: " + signalStrength);
        Log.d("MainActivity", "Battery level: " + batteryLevel);

        latitudeTextView.setText("Latitude: " + latitude);
        longitudeTextView.setText("Longitude: " + longitude);
        altitudeTextView.setText("Altitude: " + altitude + " meters");
        signalTextView.setText("Signal Strength: " + signalStrength + " dBm");
        batteryTextView.setText("Battery Level: " + batteryLevel + "%");

        String logEntry = String.format(Locale.getDefault(), "%tF %<tT; %f; %f; %f; %d; %d\n",
                System.currentTimeMillis(), latitude, longitude, altitude, signalStrength, batteryLevel);

        writeLog(logEntry);
    }

    private int getSignalStrength() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return 0;
        }
        List<CellInfo> cellInfoList = telephonyManager.getAllCellInfo();
        for (CellInfo cellInfo : cellInfoList) {
            if (cellInfo instanceof CellInfoLte) {
                return ((CellInfoLte) cellInfo).getCellSignalStrength().getDbm();
            }
        }
        return 0;
    }

    private int getBatteryLevel() {
        BatteryManager batteryManager = (BatteryManager) getSystemService(BATTERY_SERVICE);
        return batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
    }

    private void writeLog(String logEntry) {
        try (FileWriter writer = new FileWriter(getExternalFilesDir(null) + "/LogTracking.csv", true)) {
            writer.append(logEntry);
        } catch (IOException e) {
            Log.e("MainActivity", "Error writing log", e);
        }
    }
    
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLogging();
            } else {
                Log.e("MainActivity", "Location permission denied");
            }
        }
    }
}