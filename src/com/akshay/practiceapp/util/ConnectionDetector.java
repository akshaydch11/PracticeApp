package com.akshay.practiceapp.util;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;

public class ConnectionDetector {
	private static final String TAG = "ConnectionDetector";
	private Context context;
	FragmentManager fManager;
	public ConnectionDetector(Context paramContext) {
		this.context = paramContext;
	}

	public boolean isConnectingToInternet() {
		ConnectivityManager localConnectivityManager = (ConnectivityManager)this.context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] arrayOfNetworkInfo = null;
		if (localConnectivityManager != null)
		{
			arrayOfNetworkInfo = localConnectivityManager.getAllNetworkInfo();
			if (arrayOfNetworkInfo == null);
		}
		for (int i = 0; ; i++)
		{
			if (i >= arrayOfNetworkInfo.length)
				return false;
			if (arrayOfNetworkInfo[i].getState() == NetworkInfo.State.CONNECTED)
				return true;
		}
	}

	public boolean isGplayServicesConnected(Activity act) {

		int resultCode =
				GooglePlayServicesUtil.isGooglePlayServicesAvailable(context.getApplicationContext());

		if (ConnectionResult.SUCCESS == resultCode) {
			Log.d(TAG, "Play service available");
			return true;
		} else {
			Dialog dialog = GooglePlayServicesUtil.getErrorDialog(resultCode, act, 0);
			if (dialog != null) {
				ErrorDialogFragment errorFragment = new ErrorDialogFragment();
				errorFragment.setDialog(dialog);
				errorFragment.show(act.getFragmentManager(), "LocationSample");
			}
			return false;
		}
	}


	public static class ErrorDialogFragment extends DialogFragment {

		private Dialog mDialog;
		public ErrorDialogFragment() {
			super();
			mDialog = null;
		}

		public void setDialog(Dialog dialog) {
			mDialog = dialog;
		}

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			return mDialog;
		}
	}
}
