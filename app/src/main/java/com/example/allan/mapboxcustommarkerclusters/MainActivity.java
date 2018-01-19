package com.example.allan.mapboxcustommarkerclusters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.style.layers.Filter;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonOptions;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import java.net.MalformedURLException;
import java.net.URL;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textField;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.textSize;

public class MainActivity extends AppCompatActivity {

    MapView mapView;
    MapboxMap mapboxMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, "pk.eyJ1IjoiYWxsYW5zYWxlaDk1IiwiYSI6ImNqYWpvN2MyYjJia3gycW9pcDloanJpNHoifQ.PkYjXHgB6hrj8vWROjVnYQ");
        setContentView(R.layout.activity_main);
        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap map) {

                mapboxMap = map;

                mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(12.099, -79.045), 3));

                addClusteredGeoJsonSource();
            }
        });
    }
    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
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
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
    private void addClusteredGeoJsonSource() {

        // Add a new source from the GeoJSON data and set the 'cluster' option to true.
        try {
            mapboxMap.addSource(
                    // Point to GeoJSON data. This example visualizes all M1.0+ earthquakes from
                    // 12/22/15 to 1/21/16 as logged by USGS' Earthquake hazards program.
                    new GeoJsonSource("earthquakes",
                            new URL("https://www.mapbox.com/mapbox-gl-js/assets/earthquakes.geojson"),
                            new GeoJsonOptions()
                                    .withCluster(true)
                                    .withClusterMaxZoom(16)
                                    .withClusterRadius(70)
                    )
            );
        } catch (MalformedURLException malformedUrlException) {
            Log.e("dataClusterActivity", "Check the URL " + malformedUrlException.getMessage());
        }

        /*
        // Use the earthquakes GeoJSON source to create three layers: One layer for each cluster category.
        // Each point range gets a different fill color.
        int[][] layers = new int[][] {
                new int[] {150, ContextCompat.getColor(this, R.color.mapbox_red)},
                new int[] {20, ContextCompat.getColor(this, R.color.mapbox_green)},
                new int[] {0, ContextCompat.getColor(this, R.color.mapbox_blue)}
        };*/


        Bitmap clusterBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.blue_feather_with_shadow);
        Bitmap unclusterBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.marker);

        mapboxMap.addImage("UNCLUSTER_ICON", unclusterBitmap);
        mapboxMap.addImage("CLUSTER_ICON", clusterBitmap);

        //Creating a marker layer for single data points
        SymbolLayer unclustered = new SymbolLayer("unclustered-points", "earthquakes");
        unclustered.setProperties(iconImage("UNCLUSTER_ICON"));
        mapboxMap.addLayer(unclustered);


        SymbolLayer clustered = new SymbolLayer("cluster-1" , "earthquakes");
        clustered.setProperties(iconImage("CLUSTER_ICON"),
                textField("{point_count}"),
                textSize(15f),
                textColor(Color.WHITE));

        clustered.setFilter(Filter.gt("point_count", 0));
        mapboxMap.addLayer(clustered);

        /*        for (int i = 0; i < layers.length; i++) {
            //Add clusters' circles
            CircleLayer circles = new CircleLayer("cluster-" + i, "earthquakes");
            circles.setProperties(
                    circleColor(layers[i][1]),
                    circleRadius(18f)
            );

            // Add a filter to the cluster layer that hides the circles based on "point_count"
            circles.setFilter(
                    i == 0
                            ? gte("point_count", layers[i][0]) :
                            all(gte("point_count", layers[i][0]), lt("point_count", layers[i - 1][0]))
            );
            mapboxMap.addLayer(circles);
        }

        //Add the count labels
        SymbolLayer count = new SymbolLayer("count", "earthquakes");
        count.setProperties(
                textField("{point_count}"),
                textSize(12f),
                textColor(Color.WHITE)
        );
        mapboxMap.addLayer(count);*/
    }
}
