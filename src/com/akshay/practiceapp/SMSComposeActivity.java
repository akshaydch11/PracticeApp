package com.akshay.practiceapp;

import java.util.ArrayList;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.provider.ContactsContract.CommonDataKinds.Phone;

/**
 * Message is composed and send to receiver
 * @author Akshay
 *
 */
public class SMSComposeActivity extends Activity {

	protected static final int RESULT_SPEECH = 1;
	protected static final int PICK_CONTACT_REQUEST = 2;
	String phoneNumber;
	EditText editPhoneNum, msgBody;
	Button sendSMS;
	ImageView imgSpeak,imgSend,imgContact;
	boolean flag ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_smscompose);
		flag = false;
		phoneNumber = "";
		editPhoneNum = (EditText) findViewById(R.id.editText_phone_num);

		msgBody = (EditText) findViewById(R.id.editText_msg_body);
		imgSend = (ImageView) findViewById(R.id.image_send);
		imgSend.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				sendSms();

			}
		});

		imgContact = (ImageView) findViewById(R.id.image_contact);
		imgContact.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent pickContactIntent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
				pickContactIntent.setType(Phone.CONTENT_TYPE); 
				startActivityForResult(pickContactIntent, PICK_CONTACT_REQUEST);

			}
		});
		imgSpeak = (ImageView) findViewById(R.id.image_micro);
		imgSpeak.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(
						RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

				intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");

				try {
					startActivityForResult(intent, RESULT_SPEECH);
					msgBody.setText("");
				} catch (ActivityNotFoundException a) {
					Toast t = Toast.makeText(getApplicationContext(),
							"Opps! Your device doesn't support Speech to Text",
							Toast.LENGTH_SHORT);
					t.show();
				}

			}
		});

	}

	/**
	 * Send sms code
	 */
	public void sendSms() {

		if (phoneNumber == "" && flag == false) {
			phoneNumber = editPhoneNum.getText().toString();
		}

		try {
			SmsManager smsManager = SmsManager.getDefault();
			Log.d("Compose", "phn " + phoneNumber);
			smsManager.sendTextMessage(phoneNumber,
					null, 
					msgBody.getText().toString(),
					null,
					null);
			Toast.makeText(getApplicationContext(), "Your sms has successfully sent!",
					Toast.LENGTH_SHORT).show();

		} catch (Exception ex) {
			if (phoneNumber.equals("")) {
				Toast.makeText(getApplicationContext(),"Enter valid Phone Number",
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(getApplicationContext(),"SMS sending failed",
						Toast.LENGTH_SHORT).show();				
			}
			ex.printStackTrace();
		} finally {
			phoneNumber = "";
			editPhoneNum.setText("");
			msgBody.setText("");
			flag = false;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case RESULT_SPEECH: 
			if (resultCode == RESULT_OK && null != data) {

				ArrayList<String> text = data
						.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

				msgBody.setText(text.get(0));
			}
			break;
		case PICK_CONTACT_REQUEST:

			if (resultCode == RESULT_OK && null != data) {
				Uri contactUri = data.getData();
				String[] projection = {Phone.NUMBER,Phone.DISPLAY_NAME};

				Cursor cursor = getContentResolver()
						.query(contactUri, projection, null, null, null);
				cursor.moveToFirst();

				int column = cursor.getColumnIndex(Phone.NUMBER);
				String number = cursor.getString(column);
				String name = cursor.getString(cursor.getColumnIndex(Phone.DISPLAY_NAME));
				editPhoneNum.setText(name);
				phoneNumber = number;
				flag = true;

			}
			break;

		}
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
