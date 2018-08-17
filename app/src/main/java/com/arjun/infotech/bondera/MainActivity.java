package com.arjun.infotech.bondera;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.arjun.infotech.bondera.MyPreference.MyPref;
import com.arjun.infotech.bondera.Webservice.VolleyResponseListener;
import com.arjun.infotech.bondera.Webservice.VolleyUtils;
import com.arjun.infotech.bondera.Webservice.WebCallid;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.fabric.sdk.android.Fabric;

import static com.arjun.infotech.bondera.KeyboardVisibility.setKeyboardVisibilityListener;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, VolleyResponseListener,
        GoogleMap.OnMapClickListener, View.OnClickListener, AdapterView.OnItemSelectedListener, KeyboardVisibility.KeyboardVisibilityListener, LocationListener {
    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 14;
    private static final int PLACE_AUTOCOMPLETE_REQUEST = 15;

    Polyline line;
    //    public static ProgressDialog progressBar;
    MyPref myPref;
    GoogleMap mMap;
    Marker mFrom;
    Marker mTo;
    MarkerOptions from;
    MarkerOptions to;
    RelativeLayout rl_mapfrag;
    Button btn_Go;
    RequestPermissionHandler mRequestPermissionHandler;
    Spinner et_unit, et_type_of_gas, et_currency, sp_liter, sp_gallon;
    TextView txt_price_per, txt_Consumption_Rate;
    EditText et_no_of_people, et_type_of_car, et_price_per, et__Service, /*et_from,*/ /*et_to,*/
            et_when, et_Consumption_Rate, et_custom_cr;
    TextView et_from;
    TextView et_to;
    String str_price_per, unit_type, cons_rate, country;
    float kilometer, servicefees;
    int no_of_people;
    LatLng origin = null;
    LatLng destination = null;
    String LastString = "";
    String Loading = "";
    Gson gson;
    ListView list_view;
    LatLng mPosition;
    SupportMapFragment mapFragment;
    boolean checkedittedxt;
    private InterstitialAd mInterstitialAd;
    MobileAds mobileAds;
    private int mYear, mMonth, mDay, hourOfDay, mMinute;
    String current_date, path, yr, mth, daysd, crate_unit;
    DatePickerDialog datePickerDialog;
    List<String> Liter_list, Gallon_list;
    private ArrayAdapter<String> myAdapterGallon, myAdapterLiter;
    boolean third = false;
    LocationManager locationManager;
    boolean check_route, next_page;
    String Address;

    private ImageButton ib_my_location;
    public static boolean isRecursionEnable = true;
    LatLng lt;
    LatLng lt2;
    float answer;
    float priceperliter;
    float consuptionRate;
    float costPerPerson;
    ProgressBar progress_bar;
    public static ProgressDialog progress;

    /**
     * Code used in requesting runtime permissions.
     */
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;

    /**
     * Constant used in the location settings dialog.
     */
    private static final int REQUEST_CHECK_SETTINGS = 0x1;

    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;

    /**
     * The fastest rate for active location updates. Exact. Updates will never be more frequent
     * than this value.
     */
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    // Keys for storing activity state in the Bundle.
    private final static String KEY_REQUESTING_LOCATION_UPDATES = "requesting-location-updates";
    private final static String KEY_LOCATION = "location";
    private final static String KEY_LAST_UPDATED_TIME_STRING = "last-updated-time-string";

    /**
     * Provides access to the Fused Location Provider API.
     */
    private FusedLocationProviderClient mFusedLocationClient;

    /**
     * Provides access to the Location Settings API.
     */
    private SettingsClient mSettingsClient;

    /**
     * Stores parameters for requests to the FusedLocationProviderApi.
     */
    private LocationRequest mLocationRequest;

    /**
     * Stores the types of location services the client is interested in using. Used for checking
     * settings to determine if the device has optimal location settings.
     */
    private LocationSettingsRequest mLocationSettingsRequest;

    /**
     * Callback for Location events.
     */
    private LocationCallback mLocationCallback;

    /**
     * Represents a geographical location.
     */
    private Location mCurrentLocation;
    /**
     * Tracks the status of the location updates request. Value changes when the user presses the
     * Start Updates and Stop Updates buttons.
     */
    private Boolean mRequestingLocationUpdates;

    /**
     * Time when the location was updated represented as a String.
     */
    private String mLastUpdateTime;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Fabric.with(this, new Crashlytics());

        MobileAds.initialize(this, "ca-app-pub-9036845665585837~5388160880");
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-9036845665585837/5555272722");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                // Load the next interstitial.
//                progress.dismiss();
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }

        });

        mRequestPermissionHandler = new RequestPermissionHandler();
        mRequestPermissionHandler.requestPermission(this, new String[]{
                Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
        }, 123, new RequestPermissionHandler.RequestPermissionListener() {
            @Override
            public void onSuccess() {
            }

            @Override
            public void onFailed() {
            }
        });

        mRequestingLocationUpdates = true;
        mLastUpdateTime = "";
        myPref = new MyPref(this);
        // Update values using data stored in the Bundle.
        updateValuesFromBundle(savedInstanceState);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mSettingsClient = LocationServices.getSettingsClient(this);

        // Kick off the process of building the LocationCallback, LocationRequest, and
        // LocationSettingsRequest objects.
        createLocationCallback();
        createLocationRequest();
        buildLocationSettingsRequest();
        getLocation();
        initList();
        setKeyboardVisibilityListener(this, this);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.blue));
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(this);
        Loading = "Loading...";
        btn_Go = (Button) findViewById(R.id.btn_Go);
        btn_Go.setOnClickListener(this);
        txt_Consumption_Rate = findViewById(R.id.txt_Consumption_Rate);
        et_unit = findViewById(R.id.et_unit);
        et_type_of_gas = findViewById(R.id.et_type_of_gas);
        et_currency = findViewById(R.id.et_currency);
        et_no_of_people = findViewById(R.id.et_no_of_people);
        et_type_of_car = findViewById(R.id.et_type_of_car);
        et_price_per = findViewById(R.id.et_price_per);
        et__Service = findViewById(R.id.et__Service);
        et_Consumption_Rate = findViewById(R.id.et_Consumption_Rate);
        et_custom_cr = findViewById(R.id.et_custom_cr);
        list_view = (ListView) findViewById(R.id.list_view);
        et_when = findViewById(R.id.et_when);
        et_from = findViewById(R.id.et_from);
        et_from.setOnClickListener(this);
        rl_mapfrag = findViewById(R.id.rl_mapfrag);
        sp_gallon = findViewById(R.id.sp_gallon);
        sp_gallon.setOnItemSelectedListener(this);
        sp_liter = findViewById(R.id.sp_liter);
        myPref = new MyPref(getApplicationContext());
        et_Consumption_Rate.setOnClickListener(this);
        et_price_per.setText(myPref.getPref(MyPref.PricePerLiter, ""));
        et_currency.setSelection(myPref.getPref(MyPref.Current, 0));
        et_Consumption_Rate.setText("10");
        et_type_of_car.setText(myPref.getPref(MyPref.TypeOfCar, ""));

        et_unit.setSelection(myPref.getPref(MyPref.Units, 0));
        gson = new Gson();
        et_type_of_car.clearFocus();
        et_no_of_people.setText("1");
        DateFormat df = new SimpleDateFormat("EEE d/MM/yy hh:mm a", Locale.getDefault());
        String date = df.format(Calendar.getInstance().getTime());
        et_when.setText(date);
        ib_my_location = findViewById(R.id.ib_my_location);
        ib_my_location.setOnClickListener(this);
        progress_bar = findViewById(R.id.progress_bar);
        progress_bar.setVisibility(View.GONE);

        //Gallon
        myAdapterGallon = new ArrayAdapter<String>(MainActivity.this, R.layout.support_simple_spinner_dropdown_item, Gallon_list) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    return false;
                }
                return true;
            }


            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View spinnerview = super.getDropDownView(position, convertView, parent);

                TextView spinnertextview = (TextView) spinnerview;
                if (position == 0) {
                    spinnertextview.setTextColor(getResources().getColor(R.color.drop_down_uline));
                }
                return spinnerview;
            }
        };
        myAdapterGallon.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        sp_gallon.setAdapter(myAdapterGallon);

        //Liter

        myAdapterLiter = new ArrayAdapter<String>(MainActivity.this, R.layout.support_simple_spinner_dropdown_item, Liter_list) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    return false;
                }
//                if (position == 1) {
//                    return true;
//                }
//                if (position == 2) {
//                    return true;
//                }
//                if (position == 3) {
//                    return true;
//                }

                return true;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View spinnerview = super.getDropDownView(position, convertView, parent);

                TextView spinnertextview = (TextView) spinnerview;
                if (position == 0) {
                    spinnertextview.setTextColor(getResources().getColor(R.color.drop_down_uline));
                }
                return spinnerview;
            }
        };
        myAdapterLiter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        sp_liter.setAdapter(myAdapterLiter);


        et_when.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                hourOfDay = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);
                final TimePickerDialog timePickerDialog = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        int hour = hourOfDay;
                        int minutes = minute;
                        String timeSet = "";
                        if (hour > 12) {
                            hour -= 12;
                            timeSet = "PM";
                        } else if (hour == 0) {
                            hour += 12;
                            timeSet = "AM";
                        } else if (hour == 12) {
                            timeSet = "PM";
                        } else {
                            timeSet = "AM";
                        }

                        String min = "";
                        if (minutes < 10)
                            min = "0" + minutes;
                        else
                            min = String.valueOf(minutes);

                        // Append in a StringBuilder
                        String aTime = new StringBuilder().append(hour).append(':')
                                .append(min).append(" ").append(timeSet).toString();
                        SimpleDateFormat simpledateformat = new SimpleDateFormat("EEE");
                        SimpleDateFormat simpledateformat1 = new SimpleDateFormat("dd/MM/yy");

                        Date date = new Date(Integer.parseInt(yr), Integer.parseInt(mth), Integer.parseInt(daysd) - 1);
                        String dayOfWeek = simpledateformat.format(date);
                        Date dates = new Date(Integer.parseInt(yr), Integer.parseInt(mth), Integer.parseInt(daysd));
                        String day = simpledateformat1.format(dates);
                        et_when.setText(dayOfWeek + " " + day + " " + aTime);
                    }
                }, hourOfDay, mMinute, false);
                datePickerDialog = new DatePickerDialog(MainActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                yr = year + "";
                                daysd = dayOfMonth + "";
                                mth = monthOfYear + "";

                                current_date = dayOfMonth + "/" + monthOfYear + "/" + year;

                                et_when.setText(current_date);
                                timePickerDialog.show();
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }

        });

        et_to = findViewById(R.id.et_to);
        et_to.setOnClickListener(this);
        txt_price_per = findViewById(R.id.txt_price_per);


        et_to.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return false;
            }
        });
        et_unit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (et_unit.getSelectedItemId() == 0) {
                    et_Consumption_Rate.setText("10");
                    txt_price_per.setText("Price per Litre");
                    txt_Consumption_Rate.setText("Consumption Rate L/100KM");
                    str_price_per = txt_price_per.getText().toString().trim();
                    unit_type = "/L";
                    cons_rate = "L/100KM";
                    crate_unit = "Liters";
                } else {
                    et_Consumption_Rate.setText("2.6");
                    txt_price_per.setText("Price par Gallons");
                    txt_Consumption_Rate.setText("Consumption Rate G/100KM");
                    str_price_per = txt_price_per.getText().toString().trim();
                    unit_type = "/G";
                    cons_rate = "G/100KM";
                    crate_unit = "Gallons";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        et_type_of_gas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (et_type_of_gas.getSelectedItemId() == 0 && et_currency.getSelectedItemId() == 0) {
                    et_price_per.setText("6.75");
                } else if (et_type_of_gas.getSelectedItemId() == 1 && et_currency.getSelectedItemId() == 0) {
                    et_price_per.setText("7.75");
                } else if (et_type_of_gas.getSelectedItemId() == 2 && et_currency.getSelectedItemId() == 0) {
                    et_price_per.setText("5.5");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        et_currency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (et_type_of_gas.getSelectedItemId() == 0 && et_currency.getSelectedItemId() == 0) {
                    et_price_per.setText("6.75");
                } else if (et_type_of_gas.getSelectedItemId() == 1 && et_currency.getSelectedItemId() == 0) {
                    et_price_per.setText("7.75");
                } else if (et_type_of_gas.getSelectedItemId() == 2 && et_currency.getSelectedItemId() == 0) {
                    et_price_per.setText("5.5");
                } else {
                    et_price_per.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp_liter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onItemSelected(AdapterView<?> parent, final View view, int position, long id) {
                if (sp_liter.getSelectedItemId() == 1) {
                    et_Consumption_Rate.setText("10");
                } else if (sp_liter.getSelectedItemId() == 2) {
                    et_Consumption_Rate.setText("12");
                } else if (sp_liter.getSelectedItemId() == 3) {
                    et_Consumption_Rate.setVisibility(View.INVISIBLE);
                    et_custom_cr.setVisibility(View.VISIBLE);
                    et_custom_cr.requestFocus();
                    et_custom_cr.setFocusable(true);
                    et_custom_cr.setRawInputType(Configuration.KEYBOARD_12KEY);
                    et_custom_cr.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            if (!hasFocus) {
                                et_Consumption_Rate.setVisibility(View.VISIBLE);
                                et_Consumption_Rate.setText(et_custom_cr.getText().toString().trim());
                                et_custom_cr.setVisibility(View.GONE);
//                                et_Consumption_Rate.setText("");
                                sp_liter.setSelection(0);
                            }
                        }
                    });
                }
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    private void updateValuesFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            // Update the value of mRequestingLocationUpdates from the Bundle, and make sure that
            // the Start Updates and Stop Updates buttons are correctly enabled or disabled.
            if (savedInstanceState.keySet().contains(KEY_REQUESTING_LOCATION_UPDATES)) {
                mRequestingLocationUpdates = savedInstanceState.getBoolean(
                        KEY_REQUESTING_LOCATION_UPDATES);
            }

            // Update the value of mCurrentLocation from the Bundle and update the UI to show the
            // correct latitude and longitude.
            if (savedInstanceState.keySet().contains(KEY_LOCATION)) {
                // Since KEY_LOCATION was found in the Bundle, we can be sure that mCurrentLocation
                // is not null.
                mCurrentLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            }

            // Update the value of mLastUpdateTime from the Bundle and update the UI.
            if (savedInstanceState.keySet().contains(KEY_LAST_UPDATED_TIME_STRING)) {
                mLastUpdateTime = savedInstanceState.getString(KEY_LAST_UPDATED_TIME_STRING);
            }
        }
    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();

        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    }

    private void createLocationCallback() {
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
//                mMap.clear();
                mCurrentLocation = locationResult.getLastLocation();
                if (mMap != null)
                    if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
                LatLng latLng;
                latLng = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
                Address = ConstantsMethod.getFullAddress(MainActivity.this, mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
                et_from.setText(Address);
//                et_to.setText("");
                mFusedLocationClient.removeLocationUpdates(mLocationCallback);
                mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());

            }

        };
    }

//    public Location getCurrentLocation() {
//        if (mCurrentLocation != null) {
//            return mCurrentLocation;
//        }
//        return null;
//    }

    private void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        builder.setAlwaysShow(true);
        mLocationSettingsRequest = builder.build();
    }

    private void startLocationUpdates() {
        // Begin by checking if the device has the necessary location settings.
        mSettingsClient.checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        //noinspection MissingPermission
                        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                                mLocationCallback, Looper.myLooper());

                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                try {
                                    // Show the dialog by calling startResolutionForResult(), and check the
                                    // result in onActivityResult().

                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult(MainActivity.this, REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException sie) {
                                    Log.i(MainActivity.class.getSimpleName(), "PendingIntent unable to execute request.");
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                String errorMessage = "Location settings are inadequate, and cannot be " +
                                        "fixed here. Fix in Settings.";
                                Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                                mRequestingLocationUpdates = false;
                        }

                    }
                });
    }

    private void stopLocationUpdates() {

        if (!mRequestingLocationUpdates) {
            return;
        }

        // It is a good practice to remove location requests when the activity is in a paused or
        // stopped state. Doing so helps battery performance and is especially
        // recommended in applications that request frequent location updates.
        mFusedLocationClient.removeLocationUpdates(mLocationCallback)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        mRequestingLocationUpdates = false;
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();

//        createLocationCallback();
//        createLocationRequest();
//        buildLocationSettingsRequest();
//        getLocation();
        // Within {@code onPause()}, we remove location updates. Here, we resume receiving
        // location updates if the user has requested them.
        if (!checkPermissions()) {
            requestPermissions();
        }
        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

    }

    @Override
    protected void onPause() {
        super.onPause();
        // Remove location updates to save battery.
        stopLocationUpdates();
    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean(KEY_REQUESTING_LOCATION_UPDATES, mRequestingLocationUpdates);
        savedInstanceState.putParcelable(KEY_LOCATION, mCurrentLocation);
        savedInstanceState.putString(KEY_LAST_UPDATED_TIME_STRING, mLastUpdateTime);
        super.onSaveInstanceState(savedInstanceState);
    }

    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION);

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {

            // Request permission
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);

        } else {
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates();
            } else {
                // Permission denied.

                // Notify the user via a SnackBar that they have rejected a core permission for the
                // app, which makes the Activity useless. In a real app, core permissions would
                // typically be best requested during a welcome-screen flow.

                // Additionally, it is important to remember that a permission might have been
                // rejected without asking the user for permission (device policy or "Never ask
                // again" prompts). Therefore, a user interface affordance is typically implemented
                // when permissions are denied. Otherwise, your app could appear unresponsive to
                // touches or interactions which have required permissions.

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(getResources().getString(R.string.app_name));
                builder.setMessage("Please allow location permission in order to work application properly.");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.setAction(
                                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package",
                                BuildConfig.APPLICATION_ID, null);
                        intent.setData(uri);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
                builder.setCancelable(false);
                builder.show();
            }
        }
    }

    @Override
    public void onError(String message) {

    }

    @Override
    public void onResponse(Object response, WebCallid webcallid) {

        switch (webcallid) {
            case SEND_SMS:
                String s = response.toString();
                JsonObject obj = new JsonParser().parse(s).getAsJsonObject();
                Log.d("Object is : ", "" + obj);
                JsonArray jsonArray = (JsonArray) obj.get("routes");
                if (jsonArray.size() > 0) {
                    JsonObject jsonObject = (JsonObject) jsonArray.get(0);
                    JsonObject object = (JsonObject) jsonObject.getAsJsonArray("legs").get(0);
                    JsonObject object1 = (JsonObject) object.get("distance");
                    String mileS = object1.get("text").toString();
                    String mileString = mileS.replace("\"", "");
                    String[] separated = mileString.split(" ");
                    if (separated[1].equals("km")) {
                        String kmparse = separated[0];
                        kmparse = kmparse.replaceAll(",", "");
                        kilometer = Float.parseFloat(kmparse);
                        Log.d("kilomiter", "" + kilometer);
                    } else {
                        kilometer = (float) (Float.parseFloat(separated[0]) * 1.60934);
                        Log.d("kilomiter", "" + kilometer);
                    }
                } else {
                    Toast.makeText(this, "Route not found.", Toast.LENGTH_SHORT).show();

                }

                mMap.clear();

                if (mFrom == null) {
                    LatLng latLng;
                    latLng = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
                    Address = ConstantsMethod.getFullAddress(MainActivity.this, mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
                    et_from.setText(Address);
                    mMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.defaultMarker()));
                    mFrom = mMap.addMarker(new MarkerOptions()
                            .icon(BitmapDescriptorFactory.defaultMarker())
                            .position(latLng)
                            .title(Address));
                } else {
                    mMap.addMarker(new MarkerOptions().position(mFrom.getPosition()).icon(
                            BitmapDescriptorFactory.defaultMarker()));
                }
                mMap.addMarker(new MarkerOptions().position(mTo.getPosition()).icon(
                        BitmapDescriptorFactory.defaultMarker()));
                try {
                    // Tranform the string into a json object
                    check_route = true;
                    final JSONObject json = new JSONObject(response.toString());
                    JSONArray routeArray = json.getJSONArray("routes");
                    JSONObject routes = routeArray.getJSONObject(0);
                    JSONObject overviewPolylines = routes
                            .getJSONObject("overview_polyline");
                    String encodedString = overviewPolylines.getString("points");
                    List<LatLng> list = decodePoly(encodedString);

                    for (int z = 0; z < list.size() - 1; z++) {
                        LatLng src = list.get(z);
                        LatLng dest = list.get(z + 1);
                        line = mMap.addPolyline(new PolylineOptions()
                                .add(new LatLng(src.latitude, src.longitude),
                                        new LatLng(dest.latitude, dest.longitude))
                                .width(10).color(Color.RED).geodesic(true));
                    }
                    if (list.isEmpty()) return;

                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                    builder.include(mFrom.getPosition());
                    builder.include(mTo.getPosition());

                    LatLngBounds bounds = builder.build();
                    int padding = 250;
                    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);

                    mMap.animateCamera(cu);

                } catch (Exception e) {
                    e.printStackTrace();
                    check_route = false;
                    Toast.makeText(this, "Route Not Found", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();

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


        Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
        if (location != null) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 13));
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(location.getLatitude(), location.getLongitude()))      // Sets the center of the map to location user
                    .zoom(11)                   // Sets the zoom
                    .build();                   // Creates a CameraPosition from the builder
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        }
        mMap.clear();

    }


    @Override
    public void onMapClick(LatLng latLng) {

    }

    private void moveToCurrentLocation(LatLng currentLocation) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
        // Zoom in, animating the camera.
        mMap.animateCamera(CameraUpdateFactory.zoomIn());
        // Zoom out to zoom level 10, animating with a duration of 2 seconds.
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
    }


    void initList() {
        Liter_list = new ArrayList<String>();
        Liter_list.add("\t \t \t Select Liter");
        Liter_list.add("Economy - 10 L/100km");
        Liter_list.add("Luxury - 12 L/100km");
        Liter_list.add("Custom");


        Gallon_list = new ArrayList<String>();
        Gallon_list.add("\t \t \t Select Gallon ");
        Gallon_list.add("Economy - 2.6 G/100km");
        Gallon_list.add("Luxury - 3.1 G/100km");
        Gallon_list.add("Custom");
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_Go:

//                lt = ConstantsMethod.getLocationFromAddress(getApplicationContext(), et_from.getText().toString());
//                if (lt == null) {
//                    next_page = false;
//                }
//                lt2 = ConstantsMethod.getLocationFromAddress(getApplicationContext(), et_to.getText().toString());
//                if (lt2 == null) {
//                    next_page = false;
//                }
                if (!et_from.getText().toString().isEmpty() && !et_to.getText().toString().trim().isEmpty() && !check_route) {
                    Toast.makeText(MainActivity.this, "Route not found!", Toast.LENGTH_SHORT).show();
                } else {
                    if (et_from.getText().toString().isEmpty()) {
                        Toast.makeText(MainActivity.this, "Please enter route (From,To)", Toast.LENGTH_SHORT).show();
                    } else if (et_to.getText().toString().isEmpty()) {
                        Toast.makeText(MainActivity.this, "Please enter to text", Toast.LENGTH_SHORT).show();
                    } else if (et_no_of_people.getText().toString().trim().length() == 0 || Integer.parseInt(et_no_of_people.getText().toString().trim()) <= 0) {
                        Toast.makeText(MainActivity.this, "Please enter how many people you are?", Toast.LENGTH_SHORT).show();
                    } else if (et_type_of_car.getText().toString().trim().length() == 0) {
                        Toast.makeText(MainActivity.this, "Please enter type of car!", Toast.LENGTH_SHORT).show();
                    } else if (et_price_per.getText().toString().trim().length() == 0 || Float.parseFloat(et_price_per.getText().toString().trim()) <= 0) {
                        Toast.makeText(MainActivity.this, "Please pnter price per unit > 0", Toast.LENGTH_SHORT).show();
                    } else if (et_Consumption_Rate.getText().toString().trim().length() == 0 || Float.parseFloat(et_Consumption_Rate.getText().toString().trim()) <= 0) {
                        Toast.makeText(MainActivity.this, "Please enter consumption rate > 0", Toast.LENGTH_SHORT).show();
                    } else if (et_custom_cr.getVisibility() != View.GONE) {
                        if (et_custom_cr.getText().equals("")) {
                            Toast.makeText(MainActivity.this, "Please enter consumption rate > 0", Toast.LENGTH_SHORT).show();
                        }
                    } else {
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                progress = new ProgressDialog(MainActivity.this);
//                                progress.setTitle("Loading");
//                                progress.setMessage("Wait while loading...");
//                                progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
//                                progress.show();
//                            }
//                        });

                        priceperliter = Float.parseFloat(et_price_per.getText().toString());
                        if (!et__Service.getText().toString().equals("")) {
                            servicefees = Float.parseFloat(et__Service.getText().toString());
                        } else {
                            servicefees = 0;
                        }
                        consuptionRate = Float.parseFloat(et_Consumption_Rate.getText().toString());
                        if (!et_no_of_people.getText().toString().equals("")) {
                            no_of_people = Integer.parseInt(et_no_of_people.getText().toString());
                        } else {
                            no_of_people = 1;
                        }
                        myPref.setPref(MyPref.PricePerLiter, et_price_per.getText().toString().trim());
                        myPref.setPref(MyPref.ConsumptionRate, et_Consumption_Rate.getText().toString().trim());
                        myPref.setPref(MyPref.Current, et_currency.getSelectedItemPosition());
                        myPref.setPref(MyPref.TypeOfCar, et_type_of_car.getText().toString().trim());
                        myPref.setPref(MyPref.Currencys, et_currency.getSelectedItem().toString());
                        myPref.setPref(MyPref.Units, et_unit.getSelectedItemPosition());
                        myPref.setPref(MyPref.journy_date, et_when.getText().toString().trim());
                        answer = (kilometer * (consuptionRate / 100) * priceperliter) + (servicefees * kilometer);
                        costPerPerson = answer / no_of_people;
//                        progress_bar.setVisibility(View.VISIBLE);

                        Intent i = new Intent(getApplicationContext(), Detail_Activity.class);
//                        getScreen(rl_mapfrag);
                        i.putExtra("total", "" + String.format("%.1f", answer) + " " + et_currency.getSelectedItem().toString());
                        i.putExtra("from", et_from.getText().toString().trim());
                        i.putExtra("to", et_to.getText().toString().trim());
                        i.putExtra("costPerPerson", "" + String.format("%.1f", costPerPerson) + " " + et_currency.getSelectedItem().toString());
                        i.putExtra("Number_of_people", no_of_people + " Person(s)");
                        i.putExtra("servicefee", et__Service.getText().toString().trim());
                        if (et_Consumption_Rate.getText().toString().equals("10") || et_Consumption_Rate.getText().toString().equals("2.6")) {
                            i.putExtra("Consumption_Rate", "Economy");
                        } else if (et_Consumption_Rate.getText().toString().equals("12") || et_Consumption_Rate.getText().toString().equals("3.1")) {
                            i.putExtra("Consumption_Rate", "Luxury");
                        } else if (sp_gallon.getSelectedItemId() == 0 || sp_liter.getSelectedItemId() == 0) {
                            i.putExtra("Consumption_Rate", et_Consumption_Rate.getText().toString().trim() + " " + cons_rate);
                        }
                        i.putExtra("type_of_car", et_type_of_car.getText().toString().trim());
                        i.putExtra("type_of_gas", et_type_of_gas.getSelectedItem().toString());
                        i.putExtra("price_per", et_price_per.getText().toString().trim() + " " + et_currency.getSelectedItem().toString() + "" + unit_type);
                        i.putExtra("total_distance", "" + String.format("%.1f", kilometer));
                        i.putExtra("crate", consuptionRate + "");
                        i.putExtra("unit", "" + unit_type);
                        i.putExtra("crate_unit", crate_unit);
                        startActivity(i);
                        if (mInterstitialAd.isLoaded()) {
                            mInterstitialAd.show();
                        } else {
                            Log.d("TAG", "The interstitial wasn't loaded yet.");
                        }
                    }
                }
                break;
            case R.id.ib_my_location:
//                et_from.setText(Address);
                Criteria criteria = new Criteria();
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)

                {
                    return;
                }

                Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
                if (location != null)

                {
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 13));
                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(new LatLng(location.getLatitude(), location.getLongitude()))      // Sets the center of the map to location user
                            .zoom(11)                   // Sets the zoom
                            // Sets the tilt of the camera to 30 degrees
                            .build();                   // Creates a CameraPosition from the builder
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                }

                break;
            case R.id.et_Consumption_Rate:
                et_Consumption_Rate.setCursorVisible(false);
                if (et_unit.getSelectedItemId() == 0)

                {
                    sp_liter.performClick();
                } else if (et_unit.getSelectedItemId() == 1)

                {
                    sp_gallon.performClick();
                }

                break;
            case R.id.et_from:
                try

                {
                    Intent intent =
                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                                    .build(this);
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                } catch (
                        GooglePlayServicesRepairableException e)

                {
                    // TODO: Handle the error.
                } catch (
                        GooglePlayServicesNotAvailableException e)

                {
                    // TODO: Handle the error.
                }
                break;
            case R.id.et_to:

                try

                {
                    Intent intent =
                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                                    .build(this);
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST);
                } catch (
                        GooglePlayServicesRepairableException e)

                {
                    // TODO: Handle the error.
                } catch (
                        GooglePlayServicesNotAvailableException e)

                {
                    // TODO: Handle the error.
                }
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                if (et_to.getText().toString().equals(place.getAddress())) {
                    Toast.makeText(this, "Source and Destination can not be same", Toast.LENGTH_SHORT).show();
                } else {
                    et_from.setText(place.getAddress());
                    LatLng lt1 = place.getLatLng();

                    if (mFrom != null)
                        mFrom.remove();

                    mFrom = mMap.addMarker(new MarkerOptions()
                            .icon(BitmapDescriptorFactory.defaultMarker())
                            .position(new LatLng(lt1.latitude, lt1.longitude))
                            .title(place.getName().toString()));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(lt1, 14));
                }


            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                Toast.makeText(this, "" + status.getStatusMessage(), Toast.LENGTH_SHORT).show();

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                if (et_from.getText().toString().equals(place.getAddress())) {
                    Toast.makeText(this, "Source and Destination can not be same", Toast.LENGTH_SHORT).show();
                } else {
                    et_to.setText(place.getAddress());
                    LatLng lt1 = place.getLatLng();

                    if (mTo != null)
                        mTo.remove();

                    mTo = mMap.addMarker(new MarkerOptions()
                            .icon(BitmapDescriptorFactory.defaultMarker())
                            .position(new LatLng(lt1.latitude, lt1.longitude))
                            .title(place.getName().toString()));
                }
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                Toast.makeText(this, "" + status.getStatusMessage(), Toast.LENGTH_SHORT).show();

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }

        //add condition of solve error of get current latlong
        if (!et_from.getText().toString().trim().isEmpty()) {
            if (mFrom == null && mTo != null) {
                VolleyUtils.GET_METHOD(getApplicationContext(), "https://maps.googleapis.com/maps/api/directions/json?origin=" + mCurrentLocation.getLatitude() + "," + mCurrentLocation.getLongitude() + "&destination=" + mTo.getPosition().latitude + "," + mTo.getPosition().longitude + "&sensor=false&mode=driving&key=AIzaSyAtXizjmXibFeYN5y5NMW2bkSe19y0SSUI", MainActivity.this, WebCallid.SEND_SMS, false);
            }

            if (mFrom != null && mTo != null) {
                VolleyUtils.GET_METHOD(getApplicationContext(), "https://maps.googleapis.com/maps/api/directions/json?origin=" + mFrom.getPosition().latitude + "," + mFrom.getPosition().longitude + "&destination=" + mTo.getPosition().latitude + "," + mTo.getPosition().longitude + "&sensor=false&mode=driving&key=AIzaSyAtXizjmXibFeYN5y5NMW2bkSe19y0SSUI", MainActivity.this, WebCallid.SEND_SMS, false);
            }
        }
        else {
            Toast.makeText(this, "Please enter route (From,To)", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (sp_gallon.getSelectedItemId() == 1 && et_unit.getSelectedItemId() == 1) {
            et_Consumption_Rate.setText("2.6");
            et_custom_cr.setText("");
        }
        if (sp_gallon.getSelectedItemId() == 2 && et_unit.getSelectedItemId() == 1) {
            et_Consumption_Rate.setText("3.1");
            et_custom_cr.setText("");
        }
        if (sp_gallon.getSelectedItemId() == 3 && et_unit.getSelectedItemId() == 1) {
            third = true;

            et_Consumption_Rate.setVisibility(View.INVISIBLE);
            et_custom_cr.setVisibility(View.VISIBLE);
            et_custom_cr.setFocusable(true);
            et_custom_cr.requestFocus();
            et_custom_cr.setRawInputType(Configuration.KEYBOARD_12KEY);
            et_custom_cr.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        et_Consumption_Rate.setVisibility(View.VISIBLE);
                        et_Consumption_Rate.setFocusable(true);
                        et_Consumption_Rate.setText(et_custom_cr.getText().toString().trim());
                        et_custom_cr.setVisibility(View.GONE);
                        sp_gallon.setSelection(0);
                    }
                }
            });

        }

    }

    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    @Override
    public void onKeyboardVisibilityChanged(boolean keyboardVisible) {
        if (!keyboardVisible) {
            et_Consumption_Rate.setVisibility(View.VISIBLE);
            et_custom_cr.setVisibility(View.GONE);
            et_custom_cr.setText("");
        }
    }


    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    void getLocation() {
        if (mMap != null) {
            mMap.clear();
        }
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                moveTaskToBack(true);
                return true;
        }
        return false;
    }
}
