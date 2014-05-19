package com.akshay.practiceapp;

import java.util.Calendar;

import com.akshay.practiceapp.util.MyBroadcastReceiver;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.FragmentManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;

/**
 * This is activity for calendar operation.
 * @author Akshay
 *
 */
public class CalendarActivity extends Activity {

	Button saveButton;
	Calendar eventDate;
	CalendarView calendarView;
	
	public static final String CALENDAR_ACTION = "com.akshay.practiceapp.CalendarActivity";
	
	AlarmManager alarm;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calendar);
		alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		calendarView = (CalendarView) findViewById(R.id.calendarView);
		eventDate = Calendar.getInstance();
		
		calendarView.setOnDateChangeListener(new OnDateChangeListener() {
			
			@Override
			public void onSelectedDayChange(CalendarView view, int year, int month,
					int dayOfMonth) {
				eventDate.set(year, month, dayOfMonth);
			}
		});
		

		saveButton = (Button) findViewById(R.id.save_button);
		saveButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onAddRemainderClick(v) ;
				
			}
		});
	}

	/**
	 * Open a dialog asking user for remainder description
	 * and time of remainder
	 * @param v
	 */
	public void onAddRemainderClick (View v) {
		FragmentManager fm = getFragmentManager();
		CalendarDialog calDialog = new CalendarDialog();
		calDialog.setEventDate(eventDate);

		calDialog.context = getApplicationContext();
		calDialog.show(fm, "fragment_calendar_dialog");

	}
	
	/**
	 * Data entered in CalendarDialog is available here 
	 * and it will schedule remainder.
	 * @param date of remainder scheduled
	 * @param description of remainder
	 */
	public void scheduleReminder (Calendar date, String desc) {
		Log.d("CalendarActivity", "date " + date.getTime().toString() + " desc " + desc);
		
		
		Intent in = new Intent(getApplicationContext(), MyBroadcastReceiver.class);
		in.setAction(CALENDAR_ACTION);
		in.putExtra("reminder_desc", desc);
		PendingIntent pi =  PendingIntent.getBroadcast(this, 0, in, PendingIntent.FLAG_UPDATE_CURRENT);
		alarm.set(AlarmManager.RTC, date.getTimeInMillis(), pi);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		int id = item.getItemId();
		if (id == R.id.action_home) {
			Intent intent = new Intent(getApplicationContext(), MainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;
		}
		return true;
	}
}
