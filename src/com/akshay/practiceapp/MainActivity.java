package com.akshay.practiceapp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


/**
 * Launcher Activity giving user options
 * to navigate different functionality 
 * @author Akshay
 *
 */
public class MainActivity extends Activity {

    private Button makeCallButton, showCalendarButton,openBrowserButton,
    addressBookButton,locationButton,smsButton;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
        makeCallButton = (Button) findViewById(R.id.button_make_call);
        makeCallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                startActivity(intent);
            }
        });
        showCalendarButton = (Button) findViewById(R.id.button_calendar);
        showCalendarButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent in = new Intent(getApplicationContext(),CalendarActivity.class);
				startActivity(in);
				
			}
		});
        openBrowserButton = (Button) findViewById(R.id.button_browser);
        openBrowserButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent launchIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("about:blank"));
                startActivity(launchIntent);
            }
        });
        addressBookButton = (Button) findViewById(R.id.button_address_book);
        addressBookButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent in = new Intent(getApplicationContext(), ContactListActivity.class);
				startActivity(in);				
				
			}
		});
        locationButton = (Button) findViewById(R.id.button_location);
        locationButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent in = new Intent(getApplicationContext(), LocationActivity.class);
				startActivity(in);				
			}
		});
        smsButton = (Button) findViewById(R.id.button_sms);
        smsButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent in = new Intent(getApplicationContext(), SMSActivity.class);
				startActivity(in);
			}
		});

	}


}
