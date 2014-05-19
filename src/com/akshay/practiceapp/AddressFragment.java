package com.akshay.practiceapp;

import java.util.List;

import com.akshay.practiceapp.util.ConnectionDetector;
import com.akshay.practiceapp.util.DialogManager;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;


/** 
 * This fragment is used to get Destination Address
 * from user 
 * @author Akshay
 *
 */
public class AddressFragment extends Fragment {

	private static final String TAG = "AddressFragment";
	EditText street, city,state,zip;
	Location currentLocation;
	Button findLocation;
	ConnectionDetector cd;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_address,
				container, false);
		
		cd = new ConnectionDetector(getActivity().getApplicationContext());
		currentLocation = LocationActivity.LOCATION_CLIENT.getLastLocation();
		street = (EditText) rootView.findViewById(R.id.editText_street);
		city = (EditText) rootView.findViewById(R.id.editText_city);
		state = (EditText) rootView.findViewById(R.id.editText_state);
		zip = (EditText) rootView.findViewById(R.id.editText_zip);
		
		findLocation = (Button) rootView.findViewById(R.id.button_find_location);
		findLocation.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onSearchLocationClick(v);
				
			}
		});
		
		return rootView;
	}

	/**
	 * This method will get parameters from user
	 * find location and send to map for plotting.
	 * @param view
	 */
	public void onSearchLocationClick (View v) {
		InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(
				  Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

		
		
		if (!cd.isConnectingToInternet()) {
			new DialogManager("Please check wifi/data network settings", 
					"No Network Connection", getActivity()).show(getFragmentManager(), "NetworkMissing");
		} else {
			StringBuilder add = new StringBuilder();
			add.append(street.getText().toString() + " ");
			add.append(city.getText().toString() + " ");
			add.append(state.getText().toString() + " ");
			add.append(zip.getText().toString());

			Geocoder coder = new Geocoder(getActivity().getApplicationContext());
			List<Address> address;

			try {
				address = coder.getFromLocationName(add.toString(),5);
				if (address == null || address.size() == 0) {
					new DialogManager("Please enter data in at least one field", 
							"Details Missing", getActivity()).show(getFragmentManager(), "MissingAddress");
				}
				Address location = address.get(0);


				Location loc = new Location("Location");
				loc.setLatitude(location.getLatitude());
				loc.setLongitude(location.getLongitude());
				
				Log.d(TAG, "Current " + currentLocation + " new Loc " + location);
	            Bundle bundle=new Bundle();
	            bundle.putParcelable("location", loc);
	            bundle.putParcelable("curr_location", currentLocation);
	            bundle.putString("option", "two");
	            bundle.putString("location_name", add.toString());
	            FragmentMap mapfrg = new FragmentMap();
	            mapfrg.setArguments(bundle);
	            
	            FragmentManager manager = getFragmentManager();
	            FragmentTransaction transaction = manager.beginTransaction();
	            transaction.replace(R.id.container, mapfrg);
	            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
	            transaction.addToBackStack(null);
	            transaction.commit();

			} catch (Exception e) {

			}
		}
	}

	
}
