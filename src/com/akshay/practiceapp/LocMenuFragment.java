package com.akshay.practiceapp;

import com.akshay.practiceapp.util.ConnectionDetector;
import com.akshay.practiceapp.util.DialogManager;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Give two options to user 
 * 1. Current Location
 * 2. Enter Address
 * @author Akshay
 *
 */
public class LocMenuFragment extends Fragment {
	
	//private static final String TAG = "LocMenuFragment";
	Button currLocation,enterAddress;
    
	
	ConnectionDetector cd ;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_location,
				container, false);
		
		cd = new ConnectionDetector(getActivity().getApplicationContext());

		currLocation = (Button) rootView.findViewById(R.id.current_location);
		currLocation.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onShowCurrentLocation(v);
				
			}
		});
		enterAddress = (Button) rootView.findViewById(R.id.enter_address);
		enterAddress.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onEnterAddress(v);
			}
		});
		
		return rootView;
	}


	
	/**
	 * Show current location functionality implemented
	 * 
	 * @param view
	 */
	public void onShowCurrentLocation (View v) {
        if (cd.isConnectingToInternet() && cd.isGplayServicesConnected(getActivity()) ) {
            // Get the current location
            Location currentLocation = LocationActivity.LOCATION_CLIENT.getLastLocation();
            Bundle bundle=new Bundle();
            bundle.putParcelable("location", currentLocation);
            bundle.putString("option", "one");
            bundle.putString("location_name", "Current Location");
            FragmentMap mapfrg = new FragmentMap();
            mapfrg.setArguments(bundle);
            
            FragmentManager manager = getFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.container, mapfrg);
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            transaction.addToBackStack(null);
            transaction.commit();

        } else {
			new DialogManager("Please check wifi/data network settings", 
					"No Network Connection", getActivity()).show(getFragmentManager(), "NetworkMissing");
        }
	}
	
	/**
	 * Enter address fragment is called.
	 * @param view
	 */
	public void onEnterAddress (View v) {
		
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.container, new AddressFragment());
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.addToBackStack(null);
        transaction.commit();
	}
	

}


