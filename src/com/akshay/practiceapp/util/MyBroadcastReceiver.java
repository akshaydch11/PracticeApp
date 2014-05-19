package com.akshay.practiceapp.util;




import com.akshay.practiceapp.CalendarActivity;
import com.akshay.practiceapp.MainActivity;
import com.akshay.practiceapp.R;
import com.akshay.practiceapp.SMSListActivity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

public class MyBroadcastReceiver extends BroadcastReceiver {

	public final int notificationID = 123;
	public static final String TAG = "MyBroadcastReceiver";
	
	@SuppressWarnings("deprecation")
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d(TAG, "onReceive " + intent.getAction() );
		if (intent.getAction().equals(CalendarActivity.CALENDAR_ACTION)){
			Log.d(TAG, "Desc " + intent.getExtras().getString("reminder_desc"));
			
			Intent i = new Intent(context, MainActivity.class);
			i.putExtra("notificationID", notificationID);
			
			PendingIntent pi = PendingIntent.getActivity(context, 0, i, 0);
			
			NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
			
			Notification notif = new Notification(R.drawable.ic_launcher,
					"PracticeApp",System.currentTimeMillis());
			
			notif.setLatestEventInfo(context, "Reminder", 
					intent.getExtras().getString("reminder_desc"), pi);
			notif.vibrate = new long[] {100,250,100,500};
			
			nm.notify(notificationID,notif);
			
		}
		if (intent.getAction().equals(SMSListActivity.ACTION_SMS_REFRESH)) {
			SMSListActivity smsList = new SMSListActivity();
			Cursor cursor = context.getContentResolver().query(Uri.parse("content://sms/inbox"), 
					new String[]{"_id", "address", "date", "body"},null,null,null);
			smsList.mcAdapter.changeCursor(cursor);
		}
		
	}

}
