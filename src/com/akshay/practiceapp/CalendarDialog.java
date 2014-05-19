package com.akshay.practiceapp;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

/**
 * Custom dialog asking user to enter 
 * remainder description and time
 * @author Akshay
 *
 */
public class CalendarDialog extends DialogFragment {

	Context context;
	EditText reminderDesc;
	Button  timeButton,saveButton;
	Calendar eventDate;
	private SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a", Locale.US); 

	public CalendarDialog() {

	}
		
	

	public void setEventDate(Calendar eventDate) {
		this.eventDate = eventDate;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.dialog_calendar, container);
		reminderDesc = (EditText) view.findViewById(R.id.reminder_desc);
		getDialog().setTitle("Remainder Details");
		timeButton = (Button) view.findViewById(R.id.time_button);
		timeButton.setText(timeFormat.format(Calendar.getInstance().getTime()));
		timeButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				onTimeClick(v);
			}
		});

		saveButton = (Button) view.findViewById(R.id.save_button);

		
		saveButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				CalendarActivity calActivity = (CalendarActivity) getActivity();
				calActivity.scheduleReminder(eventDate, reminderDesc.getText().toString());
				getDialog().dismiss();

			}
		});

		return view;
	}


	/**
	 * Sets the time from timepicker in event date
	 * @param view
	 */
	public void onTimeClick (View v) {

		DateFormat.is24HourFormat(getActivity());
		
		
		TimePickerDialog tpd = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {

			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
				eventDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
				eventDate.set(Calendar.MINUTE, minute);
				eventDate.set(Calendar.SECOND, 0);
				timeButton.setText(timeFormat.format(eventDate.getTime()));
			}
		}, 
		eventDate.get(Calendar.HOUR_OF_DAY), eventDate.get(Calendar.MINUTE), 
			DateFormat.is24HourFormat(getActivity()));
		tpd.show();
	}


}
