package com.xu.liferpg;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.OnMapReadyCallback;
import android.support.v4.app.Fragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.microsoft.projectoxford.vision.VisionServiceClient;
import com.microsoft.projectoxford.vision.VisionServiceRestClient;
import com.microsoft.projectoxford.vision.contract.AnalysisResult;
import com.microsoft.projectoxford.vision.contract.Caption;
import com.microsoft.projectoxford.vision.contract.LanguageCodes;
import com.microsoft.projectoxford.vision.contract.Line;
import com.microsoft.projectoxford.vision.contract.OCR;
import com.microsoft.projectoxford.vision.contract.Region;
import com.microsoft.projectoxford.vision.contract.Word;
import com.microsoft.projectoxford.vision.rest.VisionServiceException;
import com.xu.liferpg.data.QuestListContract;
import com.xu.liferpg.data.QuestListDBHelper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static android.R.color.black;
import static com.xu.liferpg.R.color.black_alpha_40;
import static com.xu.liferpg.R.color.white;

//TODO 2507 initial map code copied from BombVenture
//TODO 2907 added in SQLite DB data folder, along with addition and retrieval methods
//Need to add in navdrawer, tabs and detailed activity launching capability.
//Incorporate Image Recognition capability
//https://stackoverflow.com/questions/14746582/how-to-store-the-latlng-variable-in-android-in-an-sql-lite-database-for-use-late
//TODO 3007 first thing is to get map working and properly reacting to our touches, perhaps with toast.
//TODO 3007 look into mapview markers with details- launching detailed activiity, like this
//TODO https://stackoverflow.com/questions/16677929/android-google-map-v2-starting-activity-when-clicking-on-marker-infowindow
//For the moment, fuck having tabs, simply launching detailActivity will suffice. Dynamic Navigation Drawer is probably better anyhow

//TODO 0208 added nav drawer, opens and enters with marker click, closes with map click -aim to have some kind of expandable listview
/*Dynamic Navigation Drawers
https://stackoverflow.com/questions/33284812/android-change-navigation-drawer-menu-items-text-programatically
https://stackoverflow.com/questions/23399954/android-refresh-item-on-a-navigation-drawer-menu
https://stackoverflow.com/questions/23982472/how-to-refresh-list-of-the-navigation-drawer-as-soon-as-its-open-closed-in-andro
https://stackoverflow.com/questions/27152645/drawer-navigation-change-list-of-items-dynamically-after-creation
https://stackoverflow.com/questions/21985268/android-navigation-drawer-how-to-update-the-counter-value
https://stackoverflow.com/questions/27947068/how-to-update-already-opened-navigation-drawer
*/

//TODO navigationView advanced tutorial http://www.technotalkative.com/part-4-playing-with-navigationview/
//TODO navigationView updating needs to be tested and properly implemented. Also need to consider in navdrawer when clicking marker
// TODO 0308 Implemented general view vs quest view in navheader

//TODO must implement custom widgets
//TODO filled out detailActivity, first thing to do in app is to search through and return all quests for population
//https://stackoverflow.com/questions/32967513/how-to-add-checkbox-to-material-navigation-drawer

//TODO SIF 2018 add button to exhibit also QR capabilities

//TODO For reason we arent inserting to the SQLite database anymore, dunno wtf is wrong. But for mockup simply
// TODO fill the testHolder with whatever we need.
public class MapActivity extends AppCompatActivity implements
        OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener, GoogleMap.OnInfoWindowClickListener,
        DetailFragment.OnFragmentInteractionListener, QuestionFragment.OnType2InteractionListener, QuestCompleteFragment.OnQuestCompleteListener, FriendFragment.OnFriendChosenListener {

    GoogleMap googleMap;
    LocationManager locationManager;
    PendingIntent pendingIntent;
    SharedPreferences sharedPreferences;
    int locationCount=0;
    private static final String PROX_ALERT_INTENT ="com.xu.LifeRPG";
    public static final int REQUEST_GOOGLE_PLAY_SERVICES = 1972;

    //Code for GoogleApiClient, and LocationListener
    Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    double lat, lon;
    double userlat, userlon;
    Location userloc;
    //DB related initialization
    public SQLiteDatabase db;
    public QuestListDBHelper helper;
    //Boolean that is used to simulate double click of a marker.
    boolean doubleBackToExitPressedOnce=false;
    private DrawerLayout mDrawerLayout;
    NavigationView navigationView;
    //Boolean called on Marke click to indiciate arrival
    Boolean arrived = false;
    ArrayList<Marker>allMarkers;
    Marker currentLocationMarker;
    //Test ArrayList
    ArrayList<String>testQuests;
    ArrayList<String>testPlayer;
    Button toDetails;

    Button loadData;
    ArrayList<Quest>testHolder;
    Boolean firstZoom =false;

    //global arrayist for holding objectives, to be displayed for both navigationDrawer and detailfragment setargs
    ArrayList<String>objectives;
    //global variables for holding ACTIVE quest data
    String questName;
    String questDescription;
    String questID;
    String questKey1;
    String questKey2;
    String questKey3;
    int questLevel;
    Marker activeMarker;
    //PlayerData
    int playerLevel;
    int totalEXP;
    int currentEXP;

    SharedPreferences playerData;
    SharedPreferences.Editor dataEditor;
    //Alternative SharedPreferences for quest completion
    SharedPreferences questData;
    SharedPreferences.Editor questEditor;
    boolean obj1Completion;
    boolean obj2Completion;
    boolean obj3Completion;
    boolean obj4Completion;
    //---------------------------------------------------
    //Following variables are for image recognotion Computer Vision API by Microsoft
    //Source: https://github.com/Microsoft/Cognitive-Vision-Android/blob/master/Sample/app/src/main/java/com/microsoft/projectoxford/visionsample/RecognizeActivity.java
    public VisionServiceClient client;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int QR_REQUEST=2;
    Bitmap mBitmap;
    Uri mImageUri;
    //TODO need to crosscheck resulttaggs with our keys. but first, we need the API key from ms
    ArrayList<String>resultTags;
    //For identifying location
    ArrayList<String>addressDetails;
    String cityCode;
    public EditText resultEdit;

    private Uri mPhotoUri;
    boolean isNight;
    Toolbar toolbar;
    ArrayList<String>address;
    Bitmap user;
    FloatingActionButton fab;
    FragmentManager chatman;






    @Override
        protected void onCreate(Bundle savedInstanceState) {


            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_map);


        toolbar = (Toolbar)  findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(this.getResources().getColor(black_alpha_40));

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        Window window = this.getWindow();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }

        fab = (FloatingActionButton)findViewById(R.id.floatingActionButtonHELP);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //21032020 Moved Loading here
                addressDetails=getAddress(MapActivity.this,mLastLocation.getLatitude(),mLastLocation.getLongitude());
                //cityCode=returnCityCode(addressDetails);

                //TODO for SIF2018, createFakeHolder will work fine
                testHolder = createFakeHolder();
                //testHolder=FetchAllQuestsFromRegion(cityCode);
                PopulateMapWithMarkers(testHolder);

                //FragmentManager fm = getSupportFragmentManager();


                //HelpFragment helpfrag = HelpFragment.newInstance("test","test2");
                //helpfrag.show(fm, "QuestCompleteFragment");
            }
        });

            checkLocationPermission();
            chatman= getSupportFragmentManager();
        //Microsoft Computer Vision API client initialization
        if (MapActivity.this.client==null){
            MapActivity.this.client = new VisionServiceRestClient(getString(R.string.subscription_key),"https://westcentralus.api.cognitive.microsoft.com/vision/v1.0");
        }

            playerData = getSharedPreferences("playerData", Context.MODE_PRIVATE);
            dataEditor= playerData.edit();

            questData=getSharedPreferences("questData",Context.MODE_PRIVATE);
            questEditor=questData.edit();


            loadPlayerData();


            testHolder = new ArrayList<Quest>();
            allMarkers = new ArrayList<Marker>();

            //Check if Google Play is available
            boolean status = isGooglePlayServicesAvailable(this);

            this.helper = new QuestListDBHelper(this);
            this.db = this.helper.getWritableDatabase();
        //During onCreate, insert data into DB- this should be duplicate proof
        //TODO this method here is for mockup purposes
        InsertQuestDataIntoDB();
        /*
        TODO make sharedpreferences playerstorage to store player status
         */
            //Create and initialize Navigation and DrawerLayouts
            navigationView= (NavigationView)findViewById(R.id.nav_view);
            mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer);
            mDrawerLayout.setScrimColor(Color.TRANSPARENT);
            mDrawerLayout.setLayoutTransition(null);
            navigationView.setNavigationItemSelectedListener(
                    new NavigationView.OnNavigationItemSelectedListener() {
                        @Override
                        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                            //TODO checked status not needed
                            //item.setChecked(true);
                            //First step is to get the quest title TODO 19082017 THIS WORKS
                            String title  = (String)item.getTitle();
                            //now that we have the Title, we can fetch the correct QuestID using a custom method, by iterating through testHolder
                            //TODO 19082017 ID WORKS
                            String navigationID = getIDFromTitle(title,testHolder);
                            Marker navigationMarker = getMarkerFromID(navigationID,allMarkers);
                            //get correct position and navigate there
                            //TODO 19082017 Marker fetch and autonav WORKS
                            googleMap.animateCamera(CameraUpdateFactory.newLatLng(navigationMarker.getPosition()),1000,null);
                            //Toast.makeText(MapActivity.this,navigationMarker.getTitle(),Toast.LENGTH_LONG).show();

                            mDrawerLayout.closeDrawers();

                            return true;
                        }
                    }
            );


            isNight=checkNightTime();
            if (status) {
                SupportMapFragment fm = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
                // Getting GoogleMap object from Map Fragment
                //googleMap = fm.getMapAsync(this); - YOU CANNOT ASSIGN A MAPFRAGMENT TO A GOOGLEMAP
                fm.getMapAsync(this);
                View mapView = fm.getView();
                //Moving the locatemebutton into a better
                if (mapView != null &&
                        mapView.findViewById(1) != null) {
                    // Get the button view
                    View locationButton = ((View) mapView.findViewById(1).getParent()).findViewById(2);
                    // and next place it, on bottom right (as Google Maps app)
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)
                            locationButton.getLayoutParams();
                    // position on right bottom
                    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
                    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
                    layoutParams.setMargins(100, 200, 30, 30);
                }

                //mapAsync calls onMapReady(), along with its subordinate build googleServices
                //ALL GOOGLEMAP RELATED CODE HAS TO BE IN ONMAPREADY

                //Set current view to current location



                //Get locationManager object from System Service
                locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                sharedPreferences = getSharedPreferences("location", 0);
                //In future versions, instead of the sharedPreferences, we should use a database server. but for demo version use SP

                //getting number of prox locations
                locationCount = sharedPreferences.getInt("locationCount", 0);




            }




            loadData =(Button)findViewById(R.id.buttonLoadData);
            loadData.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(MapActivity.this,"Infection Recorded, Data uploaded ", Toast.LENGTH_LONG).show();
                    googleMap.clear();
                    //TODO for SIF2018, createFakeHolder will work fine
                    testHolder = createFakeHolder2();
                    //testHolder=FetchAllQuestsFromRegion(cityCode);
                    PopulateMapWithMarkers(testHolder);
                }
            });

            toDetails =(Button)findViewById(R.id.detailButton) ;
            toDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            isUserInObjectiveArea(activeMarker,mLastLocation,questID);

                            showDetailDialog();
                        }
                    }, 150);
                    mDrawerLayout.closeDrawers();


                }
            });


        //Drawing custom mappointer
        resultEdit  =(EditText)findViewById(R.id.resultEditText);
        //TODO invisible for SIF
        resultEdit.setVisibility(View.INVISIBLE);

        //BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.icontransold);
        //Bitmap b = bitmapdraw.getBitmap();
        //int height = 100;
        //int width = 90;
        //user= Bitmap.createScaledBitmap(b, width, height, false);
        }

        //Required code for LocationListener, onConnected to Google Play Services, we will find
        @Override
        public void onConnected(Bundle bundle) {
            checkLocationPermission();
            mLocationRequest = LocationRequest.create();
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            mLocationRequest.setInterval(100);
            if (mGoogleApiClient == null || !mGoogleApiClient.isConnected()){

                buildGoogleApiClient();
                mGoogleApiClient.connect();

            }
            if (mGoogleApiClient.isConnected()){
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            }


            //TODO Upon connection, marker on current location, also used in checkArrival

            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (mLastLocation != null) {
                lat = mLastLocation.getLatitude();
                lon = mLastLocation.getLongitude();
                LatLng loc = new LatLng(lat, lon);
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
                /*

                */
            }





        }
        @Override
        public void onConnectionSuspended(int i){

        }

        @Override
        public void onLocationChanged(Location location){

            mLastLocation=location;
            lat  = mLastLocation.getLatitude();
            lon = mLastLocation.getLongitude();
            LatLng loc = new LatLng(lat,lon);
            //Code below is necessary in order to have zoom automatic on first load
            //googleMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
            //if(currentLocationMarker==null){
                //currentLocationMarker=googleMap.addMarker(new MarkerOptions().position(loc).icon(BitmapDescriptorFactory
                        //.fromBitmap(user)).title("CurLoc"));
            //}else{
                //currentLocationMarker.setPosition(loc);
            //}


            if(firstZoom ==false){
                googleMap.animateCamera(CameraUpdateFactory.zoomTo(16));
                firstZoom=true;
            }


        }

        public ArrayList<Quest> createFakeHolder(){
          ArrayList<Quest> questHolder = new ArrayList<Quest>();

            Quest q6 = new Quest("SG-006","Suspicious Case","Visited 09.03.2020 10AM","SG",60.194184,24.676905,"Cough/Fever/Pneumonia/Lethargy","lightbulb-bulb-light","Is there a lightbulb?-yes-yes","test3",false,0, "lol");




            questHolder.add(q6);

            return questHolder;
        }

        public ArrayList<Quest> createFakeHolder2(){
            ArrayList<Quest> questHolder = new ArrayList<Quest>();
            Quest q1 = new Quest("SG-001","Confirmed Case","Visited 12.03.2020 11AM","SG",60.193980,24.678507,"Cough/Fever/Pneumonia/Lethargy","test1","test2","test3",true,0,"lol");
            Quest q2 = new Quest("SG-002","Confirmed Case","Visited 12.03.2020 10PM","SG",60.191986,24.678143,"Cough/Fever/Pneumonia/Lethargy","test1","test2","test3",true,0, "lol");
            Quest q3 = new Quest("SG-003","Confirmed Case","Visited 12.03.2020 9PM","SG",60.193574,24.575131,"Cough/Fever/Pneumonia/Lethargy","test1","test2","test3",true,1, "lol");
            Quest q4 = new Quest("SG-004","Confirmed Case","Visited 11.03.2020 7AM","SG",60.193332,24.680404,"Cough/Fever/Pneumonia/Lethargy","lightbulb-bulb-light","Is there a lightbulb?-yes-yes","test3",true,0, "lol");
            Quest q5 = new Quest("SG-005","Confirmed Case","Visited 10.03.2020 12AM","SG",60.190690,24.680747,"Cough/Fever/Pneumonia/Lethargy","test1","test2","test3",true,1, "lol");
            Quest q6 = new Quest("SG-006","Suspicious Case","Visited 09.03.2020 10AM","SG",60.194184,24.676905,"Cough/Fever/Pneumonia/Lethargy","lightbulb-bulb-light","Is there a lightbulb?-yes-yes","test3",false,0, "lol");

            questHolder.add(q1);
            questHolder.add(q2);
            questHolder.add(q3);
            questHolder.add(q4);
            questHolder.add(q5);
            questHolder.add(q6);

            return questHolder;
        }




    public void onConnectionFailed(ConnectionResult connectionResult){
        buildGoogleApiClient();
    }

    synchronized void buildGoogleApiClient(){
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    };
    //Connect and disconnect to ApiClient during start/destroy
    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient == null || !mGoogleApiClient.isConnected()){

            buildGoogleApiClient();
            mGoogleApiClient.connect();

        }



    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        mGoogleApiClient.disconnect();
    }


    //Code required for getMapAsync, part of onMapReadyCallback
    //GetMapAsync needs hence this callback method, where you can immediately set stuff
    @Override
    public void onMapReady(GoogleMap map ) {


        this.googleMap = map;


        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            if(isNight==false){
                boolean success = googleMap.setMapStyle(
                        MapStyleOptions.loadRawResourceStyle(
                                this, R.raw.pastel));
                toolbar.setBackgroundColor(this.getResources().getColor(black_alpha_40));

                if (!success) {
                    Log.e("VoiVittu", "Style parsing failed.");
                }
            }else if(isNight==true){
                boolean success = googleMap.setMapStyle(
                        MapStyleOptions.loadRawResourceStyle(
                                this, R.raw.night));
                toolbar.setBackgroundColor(this.getResources().getColor(white));
                if (!success) {
                    Log.e("VoiVittu", "Style parsing failed.");
                }
            }

        } catch (Resources.NotFoundException e) {
            Log.e("VoiVittu", "Can't find style. Error: ", e);
        }
        //all of this code should be moved to setUpMap();
        map.setMyLocationEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);

        //Custom Map UI set up
        //disable zoom Controls
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setZoomGesturesEnabled(true);

        checkLocationPermission();


        googleMap.animateCamera(CameraUpdateFactory.zoomTo(16));


        //TODO 01082017 Removed onMapClickListener and LongClickListener code
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mDrawerLayout.closeDrawer(GravityCompat.END);
            }
        });

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

            @Override
            public boolean onMarkerClick(Marker arg0) {
                mDrawerLayout.openDrawer(GravityCompat.END);
                toDetails.setEnabled(true);
                if (arg0.getTitle().toString().equals("CurLoc")) {
                    resetDrawer();

                }else{

                    arrived = checkUserArrival(arg0,mLastLocation);
                    //Pass tag to variable in case we decide to view details
                    questID =(String)arg0.getTag();
                    activeMarker= arg0;
                    loadNavigationDrawerData((String)arg0.getTag());
                }


                googleMap.animateCamera(CameraUpdateFactory.newLatLng(arg0.getPosition()),1000,null);
                //map should be automatically centered on marker, move slowly


                if (doubleBackToExitPressedOnce) {

                    //1208 removed legacy codde

                } else {
                    doubleBackToExitPressedOnce = true;
                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            doubleBackToExitPressedOnce = false;
                        }
                    }, 2000);
                }

               /* TODO dont know if this is necessary. Anyhow, put a double click to select the quest
                TODO maybe save it in SharedPreferences under Active Quests. the ID should be shared here
                */
                return true;
            }

        });
    }

    private void drawCircle(LatLng point) {
        //CircleOptions needed
        CircleOptions circleOptions = new CircleOptions();
        //find center of circle
        circleOptions.center(point);
        //radius of the circle
        circleOptions.radius(20);
        //Border color of circle
        circleOptions.strokeColor(Color.BLACK);
        //fillcolor of circle
        circleOptions.fillColor(0x30ff0000);
        //Border width of circle
        circleOptions.strokeWidth(2);

        //add circle to googleMap
    }


    //TODO 2907 custom marker creation. Snippet should have descrip, frist line should be the one-line description
    //Add all markers
    private void drawQuestMarker(LatLng point, String name, String ID, String descrip, boolean completed, int questLvl){
        Marker temp;
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(point);
        markerOptions.title(name);
        markerOptions.snippet(descrip);


        //ID is required in order to use navigationDrawer and detailFragment
        //https://stackoverflow.com/questions/19076124/android-map-marker-color
        if(completed ==true){
            markerOptions.icon(BitmapDescriptorFactory
                    .defaultMarker(BitmapDescriptorFactory.HUE_RED));
        }
        //else if(questLvl> playerLevel){

            //markerOptions.icon(BitmapDescriptorFactory
                    //defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
        if(completed ==false){
            markerOptions.icon(BitmapDescriptorFactory
                    .defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
        }

        temp = googleMap.addMarker(markerOptions);
        temp.setTag(ID);
        allMarkers.add(temp);
    }

    private void modifyQuestMarker(Marker marker){
        marker.setIcon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

    }


    //responding to windowclick, TODO add the description show
    @Override
    public void onInfoWindowClick(Marker marker) {
        Toast.makeText(this, "Info window clicked",
                Toast.LENGTH_SHORT).show();
           /*Intent intent = new Intent(MapActivity.this,DetailActivity.class);
           String ID = (String) marker.getTag();
           intent.putString(ID);
                    startActivity(intent);*/
    }







    public boolean isGooglePlayServicesAvailable(Activity activity){
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int status= api.isGooglePlayServicesAvailable(this);
        //if we have a problem, return false
        if(status != ConnectionResult.SUCCESS) {
            if (api.isUserResolvableError(status)) {
                api.getErrorDialog(activity, status, 2404).show();
            }
            return false;

        }
        return true;
    }

    //Check if user allowed permission already
    public static final int MY_PERMISSIONS_REQUEST_LOCATION=99;
    public boolean checkLocationPermission(){
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
        {
            //No permission allowed, force user to give one
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.CAMERA, Manifest.permission.WRITE_SETTINGS, Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_LOCATION);
            return false;
        }else {
            return true;
        }

    }

    //callback from RequestPermissions() method
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int []grantResults){
        switch (requestCode){
            case MY_PERMISSIONS_REQUEST_LOCATION:{
                //if request is cancelled result arrays are empty
                if(grantResults.length>0 && grantResults[0] ==PackageManager.PERMISSION_GRANTED){
                    //permission granted, so do everything related to locations
                    if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED)
                    {
                        if (mGoogleApiClient == null){
                            buildGoogleApiClient();
                        }
                        googleMap.setMyLocationEnabled(true);
                    }
                }else{
                    //permission denied
                    Toast.makeText(this,"permission denied, app functionality disabled", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    //TODO 2608 Added nullchecking and visibility settings depending on number of objectives
    //https://stackoverflow.com/questions/33284812/android-change-navigation-drawer-menu-items-text-programatically
    public void updateObjectives(ArrayList<String>newobjectives, String questName,String questDescription){
        Menu menu = navigationView.getMenu();
        TextView name = (TextView)findViewById(R.id.headerView);
        name.setText(questName);
        MenuItem itemTitle = menu.findItem(R.id.questHeader);
        itemTitle.setTitle(questDescription);

        /* 2608 old code
        MenuItem itemOne= menu.findItem(R.id.itemOne);
        itemOne.setTitle(newobjectives.get(0));
        MenuItem itemTwo = menu.findItem(R.id.itemTwo);
        itemTwo.setTitle(newobjectives.get(1));
        MenuItem itemThree = menu.findItem(R.id.itemThree);
        itemThree.setTitle(newobjectives.get(2));
        MenuItem itemFour = menu.findItem(R.id.itemFour);
        itemThree.setTitle(newobjectives.get(4));

        */

        if(newobjectives.size()==0){
            MenuItem itemOne= menu.findItem(R.id.itemOne);
            //itemOne.setTitle("item one");
            itemOne.setVisible(false);
            // TODO need to move camera as well. Not sure if we need to pass id here, as theres no tag method
            //Alternatively onNavigationItemSelected can simply use .equals() to check against markers/quests?
            MenuItem itemTwo = menu.findItem(R.id.itemTwo);
            //itemTwo.setTitle("item two");set
            itemTwo.setVisible(false);
            MenuItem itemThree = menu.findItem(R.id.itemThree);
            //itemThree.setTitle("item three");
            itemThree.setVisible(false);
            MenuItem itemFour = menu.findItem(R.id.itemFour);
            //itemFour.setTitle("item four");
            itemFour.setVisible(false);
        }
        if (newobjectives.size() ==1){
            MenuItem itemOne= menu.findItem(R.id.itemOne);
            itemOne.setVisible(true);
            itemOne.setTitle(newobjectives.get(0));
            // TODO need to move camera as well. Not sure if we need to pass id here, as theres no tag method
            //Alternatively onNavigationItemSelected can simply use .equals() to check against markers/quests?
            MenuItem itemTwo = menu.findItem(R.id.itemTwo);
            //itemTwo.setTitle("item two");
            itemTwo.setVisible(false);
            MenuItem itemThree = menu.findItem(R.id.itemThree);
            //itemThree.setTitle("item three");
            itemThree.setVisible(false);
            MenuItem itemFour = menu.findItem(R.id.itemFour);
            //itemFour.setTitle("item four");
            itemFour.setVisible(false);
        } else if (newobjectives.size() == 2) {

            MenuItem itemOne= menu.findItem(R.id.itemOne);
            itemOne.setVisible(true);
            itemOne.setTitle(newobjectives.get(0));
            MenuItem itemTwo = menu.findItem(R.id.itemTwo);
            itemTwo.setVisible(true);
            itemTwo.setTitle(newobjectives.get(1));
            MenuItem itemThree = menu.findItem(R.id.itemThree);
            itemThree.setVisible(false);
            //itemThree.setTitle("item three");
            MenuItem itemFour = menu.findItem(R.id.itemFour);
            itemFour.setVisible(false);
            //itemFour.setTitle("item four");
        }else if (newobjectives.size()==3){
            MenuItem itemOne= menu.findItem(R.id.itemOne);
            itemOne.setVisible(true);
            itemOne.setTitle(newobjectives.get(0));
            MenuItem itemTwo = menu.findItem(R.id.itemTwo);
            itemTwo.setVisible(true);
            itemTwo.setTitle(newobjectives.get(1));
            MenuItem itemThree = menu.findItem(R.id.itemThree);
            itemThree.setVisible(true);
            itemThree.setTitle(newobjectives.get(2));
            MenuItem itemFour = menu.findItem(R.id.itemFour);
            itemFour.setVisible(false);
            //itemFour.setTitle("item four");
        }else if (newobjectives.size()==4){
            MenuItem itemOne= menu.findItem(R.id.itemOne);
            itemOne.setVisible(true);
            itemOne.setTitle(newobjectives.get(0));
            MenuItem itemTwo = menu.findItem(R.id.itemTwo);
            itemTwo.setVisible(true);
            itemTwo.setTitle(newobjectives.get(1));
            MenuItem itemThree = menu.findItem(R.id.itemThree);
            itemThree.setVisible(true);
            itemThree.setTitle(newobjectives.get(2));
            MenuItem itemFour = menu.findItem(R.id.itemFour);
            itemFour.setVisible(true);
            itemFour.setTitle(newobjectives.get(3));
        }

    }
    //TODO 0208 test method to update all of the player stats in the navigation header
    //https://stackoverflow.com/questions/33999407/how-to-set-text-to-view-from-drawer-header-layout-in-navigation-drawer-without-i
    public void updatePlayerStatus(ArrayList<String>newStats){
        View header = navigationView.getHeaderView(0);
        TextView name = (TextView)findViewById(R.id.headerView);
        name.setText("Adrian");

        //TODO add more details to this

    }
    //TODO ugly method for resetting all in navigation drawer
    public void resetDrawer(){

        Menu menu = navigationView.getMenu();
        View header = navigationView.getHeaderView(0);
        TextView navHeader = (TextView)findViewById(R.id.headerView);
        navHeader.setText("Reset Adrian");
        MenuItem nearbyHeader = menu.findItem(R.id.nearbyHeader);
        nearbyHeader.setTitle("Quests Nearby: ");

        MenuItem itemTitle = menu.findItem(R.id.questHeader);
        loadPlayerData();
        currentEXP = totalEXP%1000;
        int displayEXP=1000;



        //TODO default dashboard-click navigationbutton response

        itemTitle.setTitle("LVL: "+playerLevel +"      "+"EXP: "+currentEXP+"/"+displayEXP+"\n Nearby Quests");
        ArrayList<Marker> closest =returnClosestMarkers(allMarkers,mLastLocation);
        //Set the items in navigationmenu to show closest quests

        if(closest.size()==0){
            MenuItem itemOne= menu.findItem(R.id.itemOne);
            //itemOne.setTitle("item one");
            itemOne.setVisible(false);
            // TODO need to move camera as well. Not sure if we need to pass id here, as theres no tag method
            //Alternatively onNavigationItemSelected can simply use .equals() to check against markers/quests?
            MenuItem itemTwo = menu.findItem(R.id.itemTwo);
            //itemTwo.setTitle("item two");set
            itemTwo.setVisible(false);
            MenuItem itemThree = menu.findItem(R.id.itemThree);
            //itemThree.setTitle("item three");
            itemThree.setVisible(false);
            MenuItem itemFour = menu.findItem(R.id.itemFour);
            //itemFour.setTitle("item four");
            itemFour.setVisible(false);
        }
        if (closest.size() ==1){
            MenuItem itemOne= menu.findItem(R.id.itemOne);
            itemOne.setVisible(true);
            itemOne.setTitle(closest.get(0).getTitle());
            // TODO need to move camera as well. Not sure if we need to pass id here, as theres no tag method
            //Alternatively onNavigationItemSelected can simply use .equals() to check against markers/quests?
            MenuItem itemTwo = menu.findItem(R.id.itemTwo);
            //itemTwo.setTitle("item two");
            itemTwo.setVisible(false);
            MenuItem itemThree = menu.findItem(R.id.itemThree);
            //itemThree.setTitle("item three");
            itemThree.setVisible(false);
            MenuItem itemFour = menu.findItem(R.id.itemFour);
            //itemFour.setTitle("item four");
            itemFour.setVisible(false);
        } else if (closest.size() == 2) {

            MenuItem itemOne= menu.findItem(R.id.itemOne);
            itemOne.setVisible(true);
            itemOne.setTitle(closest.get(0).getTitle());
            MenuItem itemTwo = menu.findItem(R.id.itemTwo);
            itemTwo.setVisible(true);
            itemTwo.setTitle(closest.get(1).getTitle());
            MenuItem itemThree = menu.findItem(R.id.itemThree);
            itemThree.setVisible(false);
            //itemThree.setTitle("item three");
            MenuItem itemFour = menu.findItem(R.id.itemFour);
            itemFour.setVisible(false);
            //itemFour.setTitle("item four");
        }else if (closest.size()==3){
            MenuItem itemOne= menu.findItem(R.id.itemOne);
            itemOne.setVisible(true);
            itemOne.setTitle(closest.get(0).getTitle());
            MenuItem itemTwo = menu.findItem(R.id.itemTwo);
            itemTwo.setVisible(true);
            itemTwo.setTitle(closest.get(1).getTitle());
            MenuItem itemThree = menu.findItem(R.id.itemThree);
            itemThree.setVisible(true);
            itemThree.setTitle(closest.get(2).getTitle());
            MenuItem itemFour = menu.findItem(R.id.itemFour);
            itemFour.setVisible(false);
            //itemFour.setTitle("item four");
        }else if (closest.size()==4){
            MenuItem itemOne= menu.findItem(R.id.itemOne);
            itemOne.setVisible(true);
            itemOne.setTitle(closest.get(0).getTitle());
            MenuItem itemTwo = menu.findItem(R.id.itemTwo);
            itemTwo.setVisible(true);
            itemTwo.setTitle(closest.get(1).getTitle());
            MenuItem itemThree = menu.findItem(R.id.itemThree);
            itemThree.setVisible(true);
            itemThree.setTitle(closest.get(2).getTitle());
            MenuItem itemFour = menu.findItem(R.id.itemFour);
            itemFour.setVisible(true);
            itemFour.setTitle(closest.get(3).getTitle());
        }

    }

    //TODO method that checks if user is close to marker, used for arrived boolean
    //Note this isnt the world's most accurate method, esp at long distances, but it serves its role
    public boolean checkUserArrival(Marker mMarker, Location position){
        boolean arrival;
        Location user= new Location ("user");
        user.setLatitude(position.getLatitude());
        user.setLongitude(position.getLongitude());

        LatLng markerpos = mMarker.getPosition();
        Location marker = new Location("marker");
        marker.setLatitude(markerpos.latitude);
        marker.setLongitude(markerpos.longitude);


        float distance = user.distanceTo(marker);
        if(distance< 200.0f){
            arrival=true;
            Toast.makeText(MapActivity.this,"High Risk. Distance: "+distance,Toast.LENGTH_LONG).show();
        }else{
            arrival=false;
            Toast.makeText(MapActivity.this,"Low Risk. Distance: "+distance,Toast.LENGTH_LONG).show();
        }

        //Toast.makeText(MapActivity.this,"Distance: "+distance,Toast.LENGTH_LONG).show();

        return arrival;

    }








    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if (id == R.id.action_settings) {
            //TODO For SIF 2018, we now no longer launch dashboard, instead opening friendslist
            /*
            //open navigation drawer when you click the home button
            //This should be a default view, with no quest details but instead the basic information for the app
            //TODO 17082017 Modified resetDrawer method to return closest quests
            resetDrawer();
            toDetails.setEnabled(false);
            if(mDrawerLayout.isDrawerOpen(GravityCompat.END)) {
                //drawer is open
                toDetails.setEnabled(true);
                mDrawerLayout.closeDrawer(GravityCompat.END);
            }else{
                mDrawerLayout.openDrawer(GravityCompat.END);
            }

            return true;
            */
            //TODO launch the FriendFragment

            FragmentManager fm = getSupportFragmentManager();


            FriendFragment friendfrag = FriendFragment.newInstance("test","test2");
            friendfrag.show(fm, "FriendFragment");

        }

        if(id == R.id.action_profile){
            MapActivity.this.startActivity(new Intent(MapActivity.this, QRActivity.class));
        }



        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume(){
        super.onResume();
        //Required CONNECT CALL TO ACTUALLY START FUSED LOCATION API

        if (mGoogleApiClient == null || !mGoogleApiClient.isConnected()){

            buildGoogleApiClient();
            mGoogleApiClient.connect();

        }

        if (googleMap == null) {
            SupportMapFragment fm = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);

            fm.getMapAsync(this);
        }


        IntentFilter filter = new IntentFilter(PROX_ALERT_INTENT);
        registerReceiver(new QuestReceiver(), filter);
    }
    @Override
    public void onPause(){
        super.onPause();
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }

    }



    /*TODO i dont think this is necessary, but just in case storage and reading starts lagging
    public class FetchAllQuests extends AsyncTask<String, Void, Void>{
        dbHelper = new EmotionListOpenDBHelper(getApplicationContext());
            SQLiteDatabase db = dbHelper.getWritableDatabase();
    }
    */

    //TODO 2907 Master read method, fetches all quests with region code
    public ArrayList<Quest> FetchAllQuestsFromRegion(String regionID){
        ArrayList<Quest>selectedQuests = new ArrayList<Quest>();
        Quest temp;
        String questid=null;
        String questname=null;
        String details=null;
        String region= null;
        Double lat=null;
        Double lng=null;
        String object;
        String key1=null;
        String key2=null;
        String key3  = null;
        String completed;
        boolean realCompleted=false;
        Integer level= null;

        //Fetch cursor first
        Cursor cursor = db.query(true,"questlist",new String[]{
                        QuestListContract.QuestListEntry.COLUMN_ID,
                        QuestListContract.QuestListEntry.COLUMN_REGION,
                        QuestListContract.QuestListEntry.COLUMN_NAME,
                        QuestListContract.QuestListEntry.COLUMN_DETAILS,
                        QuestListContract.QuestListEntry.COLUMN_LATITUDE,
                        QuestListContract.QuestListEntry.COLUMN_LONGITUDE,
                        QuestListContract.QuestListEntry.COLUMN_OBJECTIVES,
                        QuestListContract.QuestListEntry.COLUMN_KEYWORD1,
                        QuestListContract.QuestListEntry.COLUMN_KEYWORD2,
                        QuestListContract.QuestListEntry.COLUMN_KEYWORD3,
                QuestListContract.QuestListEntry.COLUMN_COMPLETED,
                QuestListContract.QuestListEntry.COLUMN_LEVEL},
                QuestListContract.QuestListEntry.COLUMN_REGION + "=?",
                new String []{regionID}, null, null,QuestListContract.QuestListEntry.COLUMN_ID,
                null

        );
        //Iterate fields of cursor
        if (cursor != null) {
            cursor.moveToFirst();
        }
        while (cursor.isAfterLast()==false){
            //
            questid= cursor.getString(cursor.getColumnIndex(QuestListContract.QuestListEntry.COLUMN_ID));
            Toast.makeText(MapActivity.this,"questID is" +questID,Toast.LENGTH_LONG).show();
            questname =cursor.getString(cursor.getColumnIndex(QuestListContract.QuestListEntry.COLUMN_NAME));
            details = cursor.getString(cursor.getColumnIndex(QuestListContract.QuestListEntry.COLUMN_DETAILS));
            region =cursor.getString(cursor.getColumnIndex(QuestListContract.QuestListEntry.COLUMN_REGION));
            lat = Double.parseDouble(cursor.getString(cursor.getColumnIndex(QuestListContract.QuestListEntry.COLUMN_LATITUDE)));
            lng = Double.parseDouble(cursor.getString(cursor.getColumnIndex(QuestListContract.QuestListEntry.COLUMN_LONGITUDE)));
            object=cursor.getString(cursor.getColumnIndex(QuestListContract.QuestListEntry.COLUMN_OBJECTIVES));
            key1 =cursor.getString(cursor.getColumnIndex(QuestListContract.QuestListEntry.COLUMN_KEYWORD1));
            key2 =cursor.getString(cursor.getColumnIndex(QuestListContract.QuestListEntry.COLUMN_KEYWORD2));
            completed = cursor.getString(cursor.getColumnIndex(QuestListContract.QuestListEntry.COLUMN_COMPLETED));
            level = Integer.valueOf(cursor.getString(cursor.getColumnIndex(QuestListContract.QuestListEntry.COLUMN_LEVEL)));
            //TODO Implementing questcompleteion CHECKER
            if (completed.equals("true")){
                realCompleted=true;
            }

            //temp = new Quest(questid,questname,details,region,lat,lng,object,key1,key2,key3,realCompleted,level);
            //selectedQuests.add(temp);
            cursor.moveToNext();
        }


        return selectedQuests;
    }

    //TODO 05062017 main method that populates map, works with FetchAllQuestsFromRegion
    //TODO & drawQuestMarker . 10082017 WORKS

    public void PopulateMapWithMarkers(ArrayList<Quest> allquests){
        Quest temp;
        for(int i =0;i<allquests.size();i++){
            temp = allquests.get(i);
            drawQuestMarker(temp.point,temp.name,temp.ID,temp.description, temp.completed,temp.minLevel);
            //googleMap.clear();

        }

    }

    //TODO 05092017 added Filter method for filtering questType
    public void FilterOutMarkersBasedOnType(String type, ArrayList<Quest>allquests){
        ArrayList<Quest>selectedQuests = new ArrayList<>();
        ArrayList<Marker>selectedMarkers = new ArrayList<>();
        for (int i = 0;i<allquests.size();i++) {
            if (allquests.get(i).questType.equals(type)) {
                selectedQuests.add(allquests.get(i));
            }
        }

            //Once done, then find corresponding markersArray
            for (int j =0; j<selectedQuests.size();j++){
                //Select each quest, then run against allMakers
                for(int k=0;k< allMarkers.size();k++){
                    if(allMarkers.get(k).getId().equals(selectedQuests.get(j).ID)){
                        //Match found, add to SelectedMarkers
                        selectedMarkers.add(allMarkers.get(k));
                    }
                }
            }


        //Try setVisible
        //https://stackoverflow.com/questions/14507821/is-it-possible-to-show-hide-markers-in-android-google-maps-api-v2
        //TODO now set allMarkers to invisible and then iterate ones in Selected to Visible
        for (int x=0;x<allMarkers.size();x++){
            allMarkers.get(x).setVisible(false);
        }
        //TODO finally, setVisibility to true for all markers in selectedMarkers
        for (int y =0; y<selectedMarkers.size();y++){
            //Select each quest, then run against allMakers
            for(int z=0;z< allMarkers.size();z++){
                if(allMarkers.get(z).getId().equals(selectedMarkers.get(y).getId())){
                    //Match found, add to SelectedMarkers
                    allMarkers.get(z).setVisible(true);
                }
            }
        }


    }
    //Replacing the testMethod that loads fake data
    public void loadNavigationDrawerData(String questID){
        Quest activeQuest;

        //Cant make it invisible because it will make objectives also invisibile
        Menu menu = navigationView.getMenu();
        View header = navigationView.getHeaderView(0);

        MenuItem nearbyHeader = menu.findItem(R.id.nearbyHeader);
        nearbyHeader.setTitle(" ");


        /*Method below should not be necessary as we have loaded all into selectedQuests,
                May be useful for detailActivity
        Cursor cursor = db.query(true,"questlist",new String[]{
                        QuestListContract.QuestListEntry.COLUMN_ID,

                        QuestListContract.QuestListEntry.COLUMN_NAME,
                        QuestListContract.QuestListEntry.COLUMN_DETAILS,

                        QuestListContract.QuestListEntry.COLUMN_OBJECTIVES,
                        },
                QuestListContract.QuestListEntry.COLUMN_ID + "=?",
                new String []{questID}, null, null,null,
                null

        );
        */
        //TODO iterate through testHolder, is this faster than SQLite? If number of quests is small at least
        for (int i = 0;i<testHolder.size();i++){
            if(testHolder.get(i).ID.equals(questID)){
                //10082017 This string works
                activeQuest = testHolder.get(i);
                //Toast.makeText(this,activeQuest.objectives,Toast.LENGTH_LONG).show();
                //TODO we need to set global variables to correspond to activeQuest in order to pass them to detailActivity
                questName = activeQuest.name;
                questDescription=activeQuest.description;
                questID = activeQuest.ID;
                questKey1= activeQuest.key1;
                questKey2= activeQuest.key2;
                questKey3 = activeQuest.key3;
                questLevel =activeQuest.minLevel;


                objectives =separateTitleFromObjs(activeQuest.objectives,"/");
                //Toast.makeText(this,objectives.get(),Toast.LENGTH_LONG).show();
                updateObjectives(objectives, questName,questDescription );

                //TODO 1308 Following code checks for completion, disables detailsButton if already completed
                if(activeQuest.completed==true){
                    toDetails.setEnabled(false);
                }
                //TODO 01092017 Disable quest toDetails if our level isn't high enough
                if(questLevel >playerLevel){
                    toDetails.setEnabled(false);
                    Toast.makeText(MapActivity.this,"Your current level ("+playerLevel+") is not high enough to access this quest ("+questLevel+")",Toast.LENGTH_LONG).show();
                }

            }
        }



    }

    //TODO 12082017 Method for Checking if user is within MarkerDistance, without prox alert
    //May want to AUGMENT with radius method ---https://stackoverflow.com/questions/22102588/android-google-map-how-to-check-if-user-is-in-marker-circle-region

    public void isUserInObjectiveArea(Marker marker, Location currentLocation, String questID){

        float[] distance = new float[2];
        //distanceBetween(double startLatitude, double startLongitude, double endLatitude, double endLongitude, float[] results)
        Location.distanceBetween(currentLocation.getLatitude(),currentLocation.getLongitude(),
                marker.getPosition().latitude, marker.getPosition().longitude, distance);
        //TODO added in toast to check distance before we start messing around with it

        if (distance[0]<100){
            //TODO close to marker, mark objective completed
            obj1Completion= true;
            questEditor.putBoolean(questID+"obj1",obj1Completion);
            questEditor.commit();
            //TODO next, we automatically openDrawer
            Toast.makeText(MapActivity.this,String.valueOf(distance[0]),Toast.LENGTH_LONG).show();


        }else{
            //Nothing Happens, boolean's auto value is false so no need to put it.
            Toast.makeText(MapActivity.this,String.valueOf(distance[0]),Toast.LENGTH_LONG).show();
        }
    }

    //Support method for loadNavigationDrwer- separates objective string to array
    public ArrayList<String>separateTitleFromObjs(String input, String character){
        ArrayList<String>results= new ArrayList<String>();
        String[] splits = input.split(character);
        for (String s:splits){
            results.add(s);
        }

        return results;
    }



    //2103 Out of Date, replaced by createFakeHolder
    public void InsertQuestDataIntoDB () {
        addQuest("LDNC-001","TEST 1","This is a test quest","LDNC",60.193980,24.678507,"Objective1/Objective2/Objective3/Objective4","test1","test2","test3","false",1);
        addQuest("LDNC-002","TEST 2","This is a test quest","LDNC",60.191986,24.678143,"Objective1/Objective2/Objective3/Objective4","test1","test2","test3","false",1);
        addQuest("LDNC-003","TEST 3","This is a test quest","LDNC",60.193574,24.575131,"Objective1/Objective2/Objective3/Objective4","test1","test2","test3","false",2);
        addQuest("LDNC-004","TEST 4","This is a test quest","LDNC",60.193332,24.680404,"Objective1/Objective2/Objective3/Objective4","lightbulb-bulb-light","Is there a lightbulb?-yes-yes","test3","false",2);



    }
    //TODO AddQuest method, check if DB is initialized first!
    //TODO 08082017 addQuest modified with replace method in case of duplicate
    private long addQuest(String ID, String Name, String Description, String Region,
                          Double lat, Double lng,String Object, String key1, String key2,String key3, String completed, int level){
        ContentValues cv = new ContentValues();
        cv.put(QuestListContract.QuestListEntry.COLUMN_ID, ID);
        cv.put(QuestListContract.QuestListEntry.COLUMN_NAME, Name);
        cv.put(QuestListContract.QuestListEntry.COLUMN_DETAILS,Description);
        cv.put(QuestListContract.QuestListEntry.COLUMN_REGION, Region);
        cv.put(QuestListContract.QuestListEntry.COLUMN_LATITUDE,lat);
        cv.put(QuestListContract.QuestListEntry.COLUMN_LONGITUDE,lng);
        cv.put(QuestListContract.QuestListEntry.COLUMN_OBJECTIVES,Object);
        //We only use one objectives because we can fit everything there, at least for this version
        cv.put(QuestListContract.QuestListEntry.COLUMN_KEYWORD1,key1);
        cv.put(QuestListContract.QuestListEntry.COLUMN_KEYWORD2,key2);
        cv.put(QuestListContract.QuestListEntry.COLUMN_KEYWORD3,key3);
        cv.put(QuestListContract.QuestListEntry.COLUMN_COMPLETED,String.valueOf(completed));
        cv.put(QuestListContract.QuestListEntry.COLUMN_LEVEL,String.valueOf(level));

        return this.db.insertWithOnConflict("questlist", null, cv,SQLiteDatabase.CONFLICT_REPLACE);


    }

    //TODO 1108 Starting DialogFragment
    private void showDetailDialog() {
        FragmentManager fm = getSupportFragmentManager();

        if(objectives.size()!= 0){
            DetailFragment detfrag = DetailFragment.newInstance(questName,objectives,questID);
            detfrag.show(fm, "fragment_edit_name");
        }

    }
    //Method for handling DetailFragment dialog clicks, int type removed while debugging
    @Override
    public void onFragmentInteraction(int type){

        /* OLD TEST FOR QUEST COMPLETION- PASSED

        obj3Completion=true;
        obj1Completion=true;
        questEditor.putBoolean(questID+"obj1",obj1Completion);
        obj2Completion=true;
        questEditor.putBoolean(questID+"obj3",obj3Completion);


        questEditor.putBoolean(questID+"obj2",obj2Completion);
        questEditor.commit();

        */

        /*21082017 Testing Image recognition component
        obj1Completion=true;
        obj3Completion=true;
        questEditor.putBoolean(questID+"obj3",obj3Completion);
        questEditor.putBoolean(questID+"obj1",obj1Completion);
        questEditor.commit();
        Toast.makeText(MapActivity.this,"QR",Toast.LENGTH_LONG).show();
        */


        //24082017 Restored all methods for final testing

        if (type==0){
            Toast.makeText(MapActivity.this,"NAVIGATION",Toast.LENGTH_LONG).show();
            //Navigation check is already done before clicking onMarkerClickListener, hence we just wait for checkCompletion

        }
        if(type==1){
            Toast.makeText(MapActivity.this,"IMAGE",Toast.LENGTH_LONG).show();
            dispatchTakePictureIntent();
        }
        if(type==2){
            Toast.makeText(MapActivity.this,"QUESTION",Toast.LENGTH_LONG).show();
            //TODO Start QuestionFragment
            FragmentManager fm = getSupportFragmentManager();


                QuestionFragment qfrag = QuestionFragment.newInstance(questID,questKey2);
                qfrag.show(fm, "QuestionFragment");


        }
        //QRCode Activity, no selection in detailfragment
        if (type == 3 ){
            requestQRCodeScan();
        }

        //checkQuestCompletion should be called pretty much at any stage we do something with a quest
        checkQuestCompletion(questName);

        //SIF2018


        QuestCompleteFragment comfrag = QuestCompleteFragment.newInstance(questID,questName,totalEXP,250);
        comfrag.show(chatman, "QuestCompleteFragment");



    };
    //Method for handling QuestionFragment dialog clicks
    @Override
    public void onType2Interaction(boolean result){
        if(result==true){
            obj3Completion=true;
            questEditor.putBoolean(questID+"obj3",obj3Completion);
            questEditor.commit();
            checkQuestCompletion(questName);
        }

    }
    //Returned form QuestCompleteFragment. Note that checkQuestCompletion does not set activeQuest==true, but rather this is done after QuestCompleteFragment
    @Override
    public  void onQuestCompleteInteraction(String questID){
        Quest activeQuest;
        //Iterate through markers and  change id
        //1. First, change the marker using ID
        //2. Iterate through all quests for the particularID, set it's completion to true
        //3. Store in SQlite
        for (int i = 0;i<MapActivity.this.allMarkers.size();i++) {

            //modify questMarker to change color to green, also set as complete
            if (MapActivity.this.allMarkers.get(i).getTag().toString().equals(questID)) {
                //TODO Removal fucks up second time yu complete quest. Testing out modifying
                modifyQuestMarker(allMarkers.get(i));
                //TODO at the same time we modify  marker, also modify
                // TODO the corresponding testHolder completion status
                //TODO in case we need to save it at some point
                testHolder.get(i).completed=true;
                Toast.makeText(MapActivity.this,questID.toString(),Toast.LENGTH_LONG).show();

            }

        }


        //TODO redraw Marker
        for (int i = 0;i<MapActivity.this.testHolder.size();i++){
            if(MapActivity.this.testHolder.get(i).ID.equals(questID)){
                //TODO Change completed status, replace in arraylist
                activeQuest = MapActivity.this.testHolder.get(i);
                activeQuest.completed=true;
                MapActivity.this.testHolder.set(i,activeQuest);
                drawQuestMarker(activeQuest.point,activeQuest.name,activeQuest.ID,activeQuest.description, activeQuest.completed,activeQuest.minLevel);

            }
        }


        loadPlayerData();
    };

    public void checkQuestCompletion(String questNom){
        obj1Completion= questData.getBoolean(questID+"obj1",false);
        obj2Completion=questData.getBoolean(questID+"obj2",false);
        obj3Completion=questData.getBoolean(questID+"obj3",false);
        obj4Completion = questData.getBoolean(questID+"obj4",false);

        if (obj1Completion==true&&obj2Completion==true&&obj3Completion==true&&obj4Completion==true){
            //TODO launch QuestCompleteFragment if all conditions met!
            FragmentManager fm = getSupportFragmentManager();


            QuestCompleteFragment comfrag = QuestCompleteFragment.newInstance(questID,questName,totalEXP,250);
            comfrag.show(fm, "QuestCompleteFragment");
        }
    }

    public void loadPlayerData(){
        playerLevel = playerData.getInt("playerLevel",1);
        totalEXP = playerData.getInt("totalEXP",0);
        Toast.makeText(MapActivity.this, "Player Level is "+playerLevel,Toast.LENGTH_LONG).show();

    }
    //Save all data in Sqlite when quest gets destroyed
    //Check onQuestCompleteInteraction for corresponding update method
    @Override
    public void onStop(){
        Quest replaceQuest;
        super.onStop();
        //replaceData for sqlite, TODO but currently quests are not updated? or are they?
        for (int i=0;i<testHolder.size();i++){
            replaceQuest=testHolder.get(i);
            updateRowInDb(replaceQuest);
        }
        //Save player Data
        dataEditor.putInt("playerLevel",playerLevel);
        dataEditor.putInt("totalEXP",totalEXP);
        dataEditor.commit();

    }

    public long updateRowInDb(Quest quest){

        ContentValues cv = new ContentValues();
        cv.put(QuestListContract.QuestListEntry.COLUMN_ID, quest.ID);
        cv.put(QuestListContract.QuestListEntry.COLUMN_NAME, quest.name);
        cv.put(QuestListContract.QuestListEntry.COLUMN_DETAILS,quest.description);
        cv.put(QuestListContract.QuestListEntry.COLUMN_REGION, quest.region);
        cv.put(QuestListContract.QuestListEntry.COLUMN_LATITUDE,quest.lat);
        cv.put(QuestListContract.QuestListEntry.COLUMN_LONGITUDE,quest.lng);
        cv.put(QuestListContract.QuestListEntry.COLUMN_OBJECTIVES,quest.objectives);
        //We only use one objectives because we can fit everything there, at least for this version
        cv.put(QuestListContract.QuestListEntry.COLUMN_KEYWORD1,quest.key1);
        cv.put(QuestListContract.QuestListEntry.COLUMN_KEYWORD2,quest.key2);
        cv.put(QuestListContract.QuestListEntry.COLUMN_KEYWORD3,quest.key3);

        cv.put(QuestListContract.QuestListEntry.COLUMN_COMPLETED,String.valueOf(quest.completed));
        String[]args = new String[]{questID};
        return this.db.update("questlist",cv,QuestListContract.QuestListEntry.COLUMN_ID+"=?",args);
    }

    //Method to compare two georaphical points to each other, with reasonable accuracy
    public float distFrom (double lat1, double  lng1, double  lat2, double  lng2 )
    {
        double earthRadius = 3958.75;
        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng/2) * Math.sin(dLng/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double dist = earthRadius * c;

        int meterConversion = 1609;

        return new Float(dist * meterConversion).floatValue();
    }
    //Aims to return the 3 closest markers to a users current location
    public ArrayList<Marker> returnClosestMarkers(ArrayList<Marker>markers, Location loc){
        ArrayList<Marker>markerHolder = new ArrayList();
        ArrayList<Float>distanceHolder = new ArrayList();
        ArrayList<Marker> finalMarkers = new ArrayList();
        Double latitude =loc.getLatitude();
        Double longitude = loc.getLongitude();

        //Iterate through all markers, check that theyre closer than 300m
        for (int i = 0;i<markers.size();i++){
            Double markerlat = markers.get(i).getPosition().latitude;
            Double markerlng = markers.get(i).getPosition().longitude;

            float distance = distFrom(latitude,longitude,markerlat,markerlng);
            //Gotta check for quest completion too, you dont want finished quests being suggested after all
            boolean completed = checkCompletion(markers.get(i).getTag().toString(),testHolder);
            if(distance<=2000 &&completed ==false){
                markerHolder.add(markers.get(i));
                distanceHolder.add(distance);
            }
        }

        /* Dont think iterating through closest quests is necessary, just use as suggestions
        if (markerHolder.size() != 0 && markerHolder.size() >=2){
            for (int i=0;i<distanceHolder.size();i++){

            }
        }
        */

        return markerHolder;
        //TODO remember, the markers are linked to quests through their tags, so we do not need to return a quest arraylist


}

//Method to return correct ID from an objective title

    public String getIDFromTitle(String title, ArrayList<Quest>questHolder){
        String ID ="";

        for (int i =0;i<questHolder.size();i++){
            if (questHolder.get(i).name.equals(title)) {

                ID =(String) questHolder.get(i).ID;
            }
        }

        return ID;
    }



    //Continuing from the above, this method takes the returned ID, and returns the correct marker

    public Marker getMarkerFromID(String ID, ArrayList<Marker>markerHolder){
//TODO 19082017 Error was in ID, it's not the marker ID BUT THE TAG!
        Marker marker=null;


        for(int i =0;i<markerHolder.size();i++){
            if(markerHolder.get(i).getTag().toString().equals(ID)){
                marker = markerHolder.get(i);
                return marker;
            }
        }

        return marker;
    }

//Simple checkCompletion method for our closest marker method

    public boolean checkCompletion(String questid, ArrayList<Quest>questHolder){
        Boolean result = false;
        for (int i =0;i<questHolder.size();i++){
            if (questHolder.get(i).ID.equals(questid)){

                //positive match, now check quest compltion
                if (questHolder.get(i).completed==true){
                    result = true;
                }
            }
        }

        return result;
    }


    @Override
    public void onBackPressed()
    {

        if (mDrawerLayout.isDrawerOpen(GravityCompat.END)){
            mDrawerLayout.closeDrawers();
        }else{


        new AlertDialog.Builder(this)
            .setTitle("Exit LifeRPG")
            .setMessage("Are you sure you want to exit?")
            .setNegativeButton(android.R.string.no, null)
            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface arg0, int arg1) {
                    MapActivity.super.onBackPressed();
                }
            }).create().show();
        }

    }

    public boolean checkNightTime(){

        boolean isNight= false;
        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        if(hour < 6 || hour > 18){
            isNight = true;
        } else {
            isNight = false;
        }

        return isNight;
    }
    //20082017 Method for showing local city from the Latitude Longitude/location of user
    //https://stackoverflow.com/questions/2296377/how-to-get-city-name-from-latitude-and-longitude-coordinates-in-google-maps

    public static ArrayList<String> getAddress(Context context, double LATITUDE, double LONGITUDE) {

        ArrayList<String>locationDetails = new ArrayList();

        try {
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null && addresses.size() > 0) {



                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL

                locationDetails.add(country);
                locationDetails.add(city);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return locationDetails;
    }

    public String returnCityCode (ArrayList<String>locationDetails){
        //TODO returns the city code based on geolocation, also sets title

        String city = locationDetails.get(1);
        String country = locationDetails.get(0);
        String resultCode ="";
        toolbar.setTitle(city +", "+country);
        //From here on we check each location for a corresponding region

        if(country.equals("United Kingdom")&&city.equals("London")){
            resultCode ="LDNC";
        }

        //Singapore is just a city state

        if(country.equals("Singapore")){
            resultCode ="SG";
        }

        resultCode="LDNC";
        return resultCode;

    }




/*----------------------------------------------------------------------------------------------------------
Code for Microsoft Computer VisionAPI. Need to modify and hook up
https://github.com/Microsoft/Cognitive-Vision-Android/blob/master/Sample/app/src/main/java/com/microsoft/projectoxford/visionsample/AnalyzeActivity.java
 */



    private String process() throws VisionServiceException, IOException {
        Gson gson = new Gson();

        // Put the image into an input stream for detection.
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(output.toByteArray());

        AnalysisResult v = this.client.describe(inputStream, 1);

        String result = gson.toJson(v);
        Log.d("result", result);

        return result;
    }

    private class doRequest extends AsyncTask<String, String, String> {
        // Store error message
        private Exception e = null;

        public doRequest() {
        }

        @Override
        protected String doInBackground(String... args) {
            try {
                return process();
            } catch (Exception e) {
                this.e = e;    // Store error
            }

            return null;
        }

        @Override
        protected void onPostExecute(String data) {
            super.onPostExecute(data);
            // Display based on error existence

            resultTags = new ArrayList<>();
            if (e != null) {
                Log.e("Error: " , e.getMessage());
                Toast.makeText(MapActivity.this,"Computer Vision API doRequest failed "+ e.getMessage(),Toast.LENGTH_LONG).show();

                this.e = null;
            } else {
                Gson gson = new Gson();
                AnalysisResult result = gson.fromJson(data, AnalysisResult.class);

                /*
                mEditText.append("Image format: " + result.metadata.format + "\n");
                mEditText.append("Image width: " + result.metadata.width + ", height:" + result.metadata.height + "\n");
                mEditText.append("\n");


                */resultEdit.setText("");
                for (Caption caption: result.description.captions) {

                    //Toast.makeText(MapActivity.this,caption.text,Toast.LENGTH_LONG).show();

                    resultEdit.append(caption.text);
                }
                /*
                mEditText.append("\n");
                */

                for (String tag: result.description.tags) {
                    resultTags.add(tag);
                    //Toast.makeText(MapActivity.this,tag,Toast.LENGTH_LONG).show();

                    resultEdit.append(tag);
                }

            }

           compareTagswithkey(resultTags,questKey1);
        }
    }
    //Analysis comparison method for Computer Vision API
    public void compareTagswithkey(ArrayList<String>analysisTags, String questKey) {
        //1.Separate key into arraylist, and compare tags
        ArrayList<String>separatedkey= separateTitleFromObjs(questKey,"-");
        boolean match = false;
        //2. Go through your keys, try to match with tags
        for (int i = 0;i< separatedkey.size();i++){
            for(int j=0;j<analysisTags.size();j++){
                if(separatedkey.get(i).equals(analysisTags.get(j))){
                    match =true;
                }
            }

            if(match==true) {
                Toast.makeText(MapActivity.this,"Match found!",Toast.LENGTH_LONG).show();
                obj2Completion=true;
                questEditor.putBoolean(questID+"obj2",obj2Completion);
                questEditor.commit();
                checkQuestCompletion(questName);
            }



    }
    }


    //----------------------------Code for Taking photos----------------

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File f = new File(Environment.getExternalStorageDirectory(), "POST_IMAGE.jpg");
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
        mPhotoUri = Uri.fromFile(f);
        //mPhotoUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new ContentValues());
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    //And naturally onActivityResult, which is then connected to the Microsoft ComputerVisionApi
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_IMAGE_CAPTURE:
                if(resultCode == RESULT_OK) {
                    // If image is selected successfully, set the image URI and bitmap.
                    //mImageUri = data.getData();



                    if(mPhotoUri ==null){
                        resultEdit.clearComposingText();
                        resultEdit.append("Uri null");
                    }
                    //so mPhotURI is not null here,c ontinue on

                   mBitmap = ImageHelper.loadSizeLimitedBitmapFromUri(
                            mPhotoUri, getContentResolver());
                    //resultEdit.clearComposingText();
                    //resultEdit.append("Bitmap null");
                    if (mBitmap != null) {
                        // Show the image on screen.
                        resultEdit.clearComposingText();
                        resultEdit.append("Bitmap not null");

                        // Add detection log.
                        Log.d("AnalyzeActivity", "Image: " + mImageUri + " resized to " + mBitmap.getWidth()
                                + "x" + mBitmap.getHeight());

                        //Attempt to list the types of things that the image has
                        doDescribe();


                    }
                }
                break;
            //For QRCodeAnaylsis
            case QR_REQUEST:
                    if (resultCode == RESULT_OK) {
                        String qrData = data.getStringExtra(QRActivity.EXTRA_QR_RESULT);
                        Toast.makeText(MapActivity.this, "QR code is "+ qrData.toString(), Toast.LENGTH_LONG).show();
                        checkQRCode(qrData,questKey3);
                        //TODO while image analysis doDescribe has inbuilt checkQuestCompletion, for QR we need to call separately
                        checkQuestCompletion(questName);
                    } else {
                        Log.e("Error" , "Couldn't process QRcode analysis");
                        Toast.makeText(MapActivity.this,"QR_REQUEST analysis failed",Toast.LENGTH_LONG).show();
                    }
                    break;

            default:
                break;
        }

        }

    public void doDescribe() {
        try {
            new doRequest().execute();
        } catch (Exception e)
        {
            Log.e("Error" , e.toString());
            Toast.makeText(MapActivity.this,"Image analysis failed",Toast.LENGTH_LONG).show();
        }

    }


    //---------------------------------------------------------------------------------------------------------
        //Following code is for QRCode Analysis
        public void requestQRCodeScan() {
            Intent qrScanIntent = new Intent(this, QRActivity.class);
            startActivityForResult(qrScanIntent, QR_REQUEST);
        }
        //OnActivityResult is in its corresponding section

    public boolean checkQRCode(String code, String key){
        boolean result = false;
        if (code.equals(key)){
           result = true;
        }
        return result;
    }

    //Following is for friends fragment
    @Override
    public void onFriendChosen(String friend){
        launchChatFragment(friend);
    }

        //Launch ChatFragment
    public void launchChatFragment(String name){



        ChatFragment chatfrag = ChatFragment.newInstance(name);
        chatfrag.show(chatman, "ChatFragment");
    }
}




