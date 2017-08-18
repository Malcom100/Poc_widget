package adneom.poc_widget.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import adneom.poc_widget.R;
import adneom.poc_widget.interfaces.ISnapHotGoogleMap;


public class FragmentMap extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnInfoWindowClickListener,
        GoogleMap.OnMapLongClickListener,
        GoogleMap.OnMapClickListener,
        GoogleMap.OnMarkerClickListener {

    MapView mMapView;
    private GoogleMap myGoogleMap;
    private final int REQUEST_LOCATION = 896;
    private GoogleApiClient mGoogleApiClient;
    private ImageView snapshot;
    private ISnapHotGoogleMap interfaceGoogle;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View v = inflater.inflate(R.layout.fragment_map,container,false);
        // Gets the MapView from the XML layout and creates it
        mMapView = (MapView) v.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        // Needs to call MapsInitializer before doing any CameraUpdateFactory calls
        MapsInitializer.initialize(this.getActivity());
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);

        mGoogleApiClient = new GoogleApiClient.Builder( getActivity() )
                .addConnectionCallbacks( this )
                .addOnConnectionFailedListener( this )
                .addApi( LocationServices.API )
                .build();

        snapshot = (ImageView)getActivity().findViewById(R.id.snapshot);
        mMapView = (MapView)getActivity().findViewById(R.id.mapView);
        //when map is ready it will be initialize
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                myGoogleMap = googleMap;

                checkPermissionsMap();
            }
        });
    }

    private void initListeners() {
        myGoogleMap.setOnMarkerClickListener(this);
        myGoogleMap.setOnMapLongClickListener(this);
        myGoogleMap.setOnInfoWindowClickListener( this );
        myGoogleMap.setOnMapClickListener(this);
    }

    private void checkPermissionsMap(){
        if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(),Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        }else{
            initListeners();

            // For showing a move to my location button
            myGoogleMap.setMyLocationEnabled(true);

            myGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);

            // For dropping a marker at a point on the Map
            LatLng sydney = new LatLng(50.8537638, 4.3147165);
            myGoogleMap.addMarker(new MarkerOptions().position(sydney).title("Brussels").snippet("Brussels ma belle :-) "));


            // For zooming automatically to the location of the marker
            CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(12).build();
            myGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            hh.post(rn);
        }
    }


    Handler hh = new Handler();
    Runnable rn = new Runnable() {
        @Override
        public void run() {
            snapshot();
        }
    };
    //take snapshot when map is loaded
    private void snapshot(){
        if(myGoogleMap != null){
            myGoogleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                @Override
                public void onMapLoaded() {
                    myGoogleMap.snapshot(new GoogleMap.SnapshotReadyCallback() {
                        @Override
                        public void onSnapshotReady(Bitmap bitmap) {
                            final Bitmap bm = bitmap;
                            //snapshot.setImageBitmap(bm);
                            interfaceGoogle.senSnapshot(bm);
                        }
                    });
                }
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case REQUEST_LOCATION:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                    checkPermissionsMap();
                }
                break;
        }
    }

    @Override
    public void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            interfaceGoogle = (ISnapHotGoogleMap) getActivity();
        }catch (ClassCastException e) {
            e.printStackTrace();
            Log.i("Test",e.getMessage()+" -> "+getActivity().toString()+" must implement ISnapshotGoogleMap interface ");
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onInfoWindowClick(Marker marker) {

    }

    @Override
    public void onMapClick(LatLng latLng) {

    }

    @Override
    public void onMapLongClick(LatLng latLng) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }
}
