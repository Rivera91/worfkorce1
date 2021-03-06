package nyc.c4q.workforce1.splash;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import nyc.c4q.workforce1.R;
import nyc.c4q.workforce1.model.CenterLocations;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventMapFragment extends Fragment {

    private MapView mapView;
    private GoogleMap googleMap;
    private View rootView;
    private FusedLocationProviderClient fusedLocationProviderClient;

    public EventMapFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.event_map, container, false);
        mapView = (MapView) rootView.findViewById(R.id.event_detail_map);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {

                googleMap = mMap;

                if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1020);
                } else {

                    // For showing a move to my location button
                    googleMap.setMyLocationEnabled(true);
                    fusedLocationProviderClient.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                double lat = location.getLatitude();
                                double lng = location.getLongitude();
                                LatLng curr = new LatLng(lat, lng);

                                googleMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title("Current Location").icon(BitmapDescriptorFactory.fromResource(R.mipmap.current)));
                            }
                        }
                    });

                    try

                    {
                        JSONArray arr = CenterLocations.getCenters();
                        for (int i = 0; i < 1 ; i++) {
                            JSONObject obj = arr.getJSONObject(i);
                            String title = obj.getString("name");
                            String details = obj.getString("details");
                            Double lat = Double.parseDouble(obj.getString("lat"));
                            Double lng = Double.parseDouble(obj.getString("long"));
                            LatLng center = new LatLng(lat, lng);
                            CameraPosition cameraPosition = new CameraPosition.Builder().target(center).zoom(12).build();
                            googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                            googleMap.addMarker(new MarkerOptions()
                                    .position(center)
                                    .title(title)
                                    .snippet(obj.getString("Hours"))
                                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.center_icon)));

                        }
                    } catch (
                            JSONException e)

                    {
                        e.printStackTrace();
                    }

                    UiSettings uiSettings = mMap.getUiSettings();
                    uiSettings.setMapToolbarEnabled(true);
                    uiSettings.setZoomControlsEnabled(false);
                    uiSettings.setMyLocationButtonEnabled(true);
                }
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

}


