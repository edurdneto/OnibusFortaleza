package br.ufc.onibusfortaleza.onibusfortaleza;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleMap mMap;
    private GoogleApiClient googleApiClient;
    private RouteDAO routeDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        routeDAO = new RouteDAO(this);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        GoogleApiClient.Builder builder = new GoogleApiClient.Builder(this);
        builder.addApi(LocationServices.API);
        builder.addConnectionCallbacks(this);
        builder.addOnConnectionFailedListener(this);
        googleApiClient = builder.build();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney2"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();

    }

    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());

        System.out.print("entrou");
        mMap.addMarker(new MarkerOptions().position(latLng).title("Im here"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public void calcularRota(View view){
        EditText origin=(EditText)findViewById(R.id.editTextOrigin);
        EditText dest=(EditText)findViewById(R.id.editTextDestiny);

        GetRouteAsyncTask getRouteAsyncTask = new GetRouteAsyncTask();
        getRouteAsyncTask.execute(origin.getText().toString(),dest.getText().toString());

        mMap.addPolyline(new PolylineOptions().addAll(PolyUtil.decode("b_xUbn~iFg@|Bw@bEoEaAy@Q_A`EOh@o@rCaAvE_AbEERzGxAvBd@dDt@~GzAnE~@nCl@j@NbFhAtBd@p@N~D|@tA\\RDfB`@t@NxIlBi@`CUjAeAzEcAtES`Ao@vCa@nBi@|BGZy@rDaAtEcArE[tAe@|B_AjEcArEaArEm@pCU~@gA~ECPw@vDEPk@lC{@xDg@hCWfAS~@w@rDrCn@x@RzCr@q@tCa@dBi@hC_A~DXFYGgCrIuCeCgCoBc@pBm@lCw@rDGT{@xDCPxIhBcAnEaAzEeBhEcAtEYpAQr@o@`C{Cl@aD\\]BFb@lArJN~@JdA^pCNRVjBXtBAn@Fd@r@zF^|C`@bD`@dD\\bDNPThB@FRzAAr@vAvKd@hDLTJz@@JJz@A`@@Lj@xEl@~EdAlIP|ALJPrA")));


    }


    public void history(View view){
        List<Route> routes = routeDAO.list();
        if(routes != null) {
            Intent i = new Intent();
            i.setAction("br.ufc.dc.dspm.action.history");
            i.setComponent(null);
            i.addCategory("br.ufc.dc.dspm.category.Categoria");
            i.setComponent(null);
            startActivity(i);
        } else {
            Toast.makeText(getApplicationContext(), "0 Notes", Toast.LENGTH_SHORT).show();
        }
    }

}
