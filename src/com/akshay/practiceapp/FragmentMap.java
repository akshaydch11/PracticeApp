package com.akshay.practiceapp;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import com.akshay.practiceapp.util.DirectionsJSONParser;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This fragment do all operations to plot the location
 * on Google Maps also calculates route to destination
 * @author Akshay
 *
 */
public class FragmentMap extends Fragment implements GoogleMap.OnInfoWindowClickListener {

	String TAG = "MapsFragment";
	private GoogleMap googleMap;
	private Location loc, currentLocation;
	TextView distDur;
	private String locationName,option,distDuration;
	boolean isInfoWindowClicked = false;
	FragmentTransaction ft ;
	Fragment fragment;
	PolylineOptions options;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_map,
				container, false);
		googleMap = ((MapFragment) getFragmentManager().findFragmentById(
				R.id.map)).getMap();
		distDur =(TextView) rootView.findViewById(R.id.distance_duration);
		distDur.setText("");
		if (savedInstanceState == null) {
			Bundle bundle = this.getArguments();
			if (bundle != null) {
				loc = bundle.getParcelable("location");
				currentLocation = bundle.getParcelable("curr_location");
				locationName = bundle.getString("location_name");
				option = bundle.getString("option");
				try {
					initilizeMap();

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} else {
			loc = savedInstanceState.getParcelable("location");
			currentLocation = savedInstanceState.getParcelable("curr_location");
			locationName = savedInstanceState.getString("location_name");
			option = savedInstanceState.getString("option");
			isInfoWindowClicked = savedInstanceState.getBoolean("info_window");
			if (!isInfoWindowClicked) {
				initilizeMap();
			} else {
				getDirections();
				if (savedInstanceState.containsKey("line_options")) {
					options = savedInstanceState.getParcelable("line_options");
					distDuration = savedInstanceState.getString("distance");
					distDur.setText(distDuration);
					distDur.setBackgroundColor(Color.GRAY);
					distDur.setTextColor(Color.RED);
					googleMap.addPolyline(options);		
				}
				
			}
		}
		return rootView;
	}




	@Override
	public void onStop() {
		super.onStop();
		fragment = (getFragmentManager().findFragmentById(R.id.map));
		ft = getActivity().getFragmentManager().beginTransaction();
		ft.remove(fragment);
		ft.commitAllowingStateLoss();

	}




	@Override
	public void onSaveInstanceState(Bundle outState) {
		Log.d(TAG, "onSaveInstanceState");

		outState.putParcelable("location", loc);
		outState.putParcelable("curr_location", currentLocation);
		outState.putString("location_name", locationName);
		outState.putString("option", option);
		outState.putBoolean("info_window", isInfoWindowClicked);

		if (options != null) {
			outState.putParcelable("line_options", options);
			outState.putString("distance", distDuration);
		}
		super.onSaveInstanceState(outState);
	}



	/**
	 * Initializes google map
	 */
	private void initilizeMap() {


		if (googleMap != null) {


			googleMap.setOnInfoWindowClickListener(this);

			MarkerOptions markerCurrent = new MarkerOptions().position(
					new LatLng(loc.getLatitude(),loc.getLongitude() ))
					.title(locationName);

			markerCurrent.icon(BitmapDescriptorFactory
					.defaultMarker(BitmapDescriptorFactory.HUE_RED));
			googleMap.addMarker(markerCurrent);


			CameraPosition cameraPosition = new CameraPosition.Builder()
			.target(new LatLng(loc.getLatitude(),
					loc.getLongitude())).zoom(12).build();

			googleMap.animateCamera(CameraUpdateFactory
					.newCameraPosition(cameraPosition));
		}
	}

	@Override
	public void onInfoWindowClick(Marker marker) {

		if (option.equals("two")) {
			getDirections();
			isInfoWindowClicked = true;
			String url = getDirectionsUrl(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
					new LatLng(loc.getLatitude(), loc.getLongitude()));				

			DownloadTask downloadTask = new DownloadTask();

			// Start downloading json data from Google Directions API
			downloadTask.execute(url);
			CameraPosition cameraPosition = new CameraPosition.Builder()
			.target(new LatLng(currentLocation.getLatitude(),
					currentLocation.getLongitude())).zoom(12).build();

			googleMap.animateCamera(CameraUpdateFactory
					.newCameraPosition(cameraPosition));
		}


	}

	/**
	 * If Location is other than current location
	 * this method will calculates and show route on map
	 */
	private void getDirections() {

		if (googleMap != null) {

			googleMap.clear();
			googleMap.setOnInfoWindowClickListener(null);


			MarkerOptions markerStart = new MarkerOptions().position(
					new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude() ))
					.title("Current Location");

			markerStart.icon(BitmapDescriptorFactory
					.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
			googleMap.addMarker(markerStart);


			MarkerOptions markerEnd = new MarkerOptions().position(
					new LatLng(loc.getLatitude(),loc.getLongitude() ))
					.title(locationName);

			markerEnd.icon(BitmapDescriptorFactory
					.defaultMarker(BitmapDescriptorFactory.HUE_RED));
			googleMap.addMarker(markerEnd);			





		}
	}

	/**
	 * Create URL required for calculating route.
	 * @param origin location
	 * @param destination location
	 * @return url as string
	 */
	private String getDirectionsUrl(LatLng origin,LatLng dest){
		String str_origin = "origin="+origin.latitude+","+origin.longitude;
		String str_dest = "destination="+dest.latitude+","+dest.longitude;		
		String sensor = "sensor=false";			
		String parameters = str_origin+"&"+str_dest+"&"+sensor;
		String output = "json";
		String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;
		return url;
	}

	/** 
	 * A method to download json data from directions URL 
	 */
	private String downloadUrl(String strUrl) throws IOException{
		String data = "";
		InputStream iStream = null;
		HttpURLConnection urlConnection = null;
		try{
			URL url = new URL(strUrl);
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.connect();
			iStream = urlConnection.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

			StringBuffer sb  = new StringBuffer();

			String line = "";
			while( ( line = br.readLine())  != null){
				sb.append(line);
			}

			data = sb.toString();

			br.close();

		}catch(Exception e){
			Log.d("Exception while downloading url", e.toString());
		}finally{
			iStream.close();
			urlConnection.disconnect();
		}
		return data;
	}




	/**
	 * AsyncTask for downloading data from url
	 *
	 */
	private class DownloadTask extends AsyncTask<String, Void, String>{			
		@Override
		protected String doInBackground(String... url) {
			String data = "";

			try{
				data = downloadUrl(url[0]);
			}catch(Exception e){
				Log.d("Background Task",e.toString());
			}
			return data;		
		}

		@Override
		protected void onPostExecute(String result) {			
			super.onPostExecute(result);			

			ParserTask parserTask = new ParserTask();
			parserTask.execute(result);

		}		
	}
	/** 
	 * A class to parse the Google Places in JSON format 
	 */
	private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{

		@Override
		protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

			JSONObject jObject;	
			List<List<HashMap<String, String>>> routes = null;			           

			try{
				jObject = new JSONObject(jsonData[0]);
				DirectionsJSONParser parser = new DirectionsJSONParser();

				routes = parser.parse(jObject);    
			}catch(Exception e){
				e.printStackTrace();
			}
			return routes;
		}

		@Override
		protected void onPostExecute(List<List<HashMap<String, String>>> result) {
			ArrayList<LatLng> points = null;
			PolylineOptions lineOptions = null;
			String distance = "";
			String duration = "";


			if(result.size()<1){
				Toast.makeText(getActivity().getApplicationContext(), "No Points", Toast.LENGTH_SHORT).show();
				return;
			}
			for(int i=0;i<result.size();i++){
				points = new ArrayList<LatLng>();
				lineOptions = new PolylineOptions();
				List<HashMap<String, String>> path = result.get(i);

				for(int j=0;j<path.size();j++){
					HashMap<String,String> point = path.get(j);	

					if(j==0) {	
						distance = (String)point.get("distance");						
						continue;
					} else if(j==1) { 
						duration = (String)point.get("duration");
						continue;
					}

					double lat = Double.parseDouble(point.get("lat"));
					double lng = Double.parseDouble(point.get("lng"));
					LatLng position = new LatLng(lat, lng);	

					points.add(position);						
				}

				lineOptions.addAll(points);
				lineOptions.width(5);
				lineOptions.color(Color.RED);	

			}

			Log.d(TAG, "dist : " + distance + " duration : " + duration);
			distDuration = "Distance: "+distance + ", Duration: "+duration;
			distDur.setText("Distance: "+distance + ", Duration: "+duration);
			distDur.setBackgroundColor(Color.GRAY);
			distDur.setTextColor(Color.RED);
			options = lineOptions;
			googleMap.addPolyline(lineOptions);							
		}			
	}

}
