package com.akshay.practiceapp;

import android.speech.tts.TextToSpeech;

import com.akshay.practiceapp.data.MessageCursorAdapter;
import com.akshay.practiceapp.util.MyBroadcastReceiver;
import com.akshay.practiceapp.util.SpeakOut;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

/**
 * Show inbox messages in list view
 * @author Akshay
 *
 */
public class SMSListActivity extends ListActivity  {

	public static final String ACTION_SMS_REFRESH = "com.akshay.practiceapp.ACTION_SMS_REFRESH";

	Cursor cursor;
	public MessageCursorAdapter mcAdapter;
	MyBroadcastReceiver receiver ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_smslist);
		new SpeakOut(getApplicationContext());
		fetchInbox();

		final Activity act = this;
		getListView().setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> adapter, View view,
					final int pos, long id) {
				Log.d("SMSListActivity","pos "+ pos + " id " + id );
				final Cursor temp = (Cursor) adapter.getItemAtPosition(pos);
				AlertDialog.Builder alert = new AlertDialog.Builder(act)
				.setTitle("Delete Message")
				.setMessage("Do you want to delete this message?")
				.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						deleteSMS_cur(temp, pos);
					}
				})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				});
				alert.create().show();
				return true;
			}
		});

	}




	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		IntentFilter inf = new IntentFilter(ACTION_SMS_REFRESH);
		receiver = new MyBroadcastReceiver();
		registerReceiver(receiver, inf);
	}

	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(receiver);

	}

	/**
	 * Fetch inbox messages
	 */
	public void fetchInbox()
	{
		Uri smsUri = Uri.parse("content://sms/inbox");
		CursorLoader cLoader = new CursorLoader(this, smsUri,
				new String[]{"_id", "address", "date", "body"},
				null,null,null);
		cursor = cLoader.loadInBackground(); 
		if (!cursor.moveToFirst()) {
			Toast.makeText(getApplicationContext(), "No Messages in Inbox", Toast.LENGTH_SHORT).show();
		} else {

			mcAdapter = new MessageCursorAdapter(this, cursor, 1);
			setListAdapter(mcAdapter);
		}
	}


	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		Cursor temp = (Cursor) l.getItemAtPosition(position);
		AlertDialog.Builder alert = new AlertDialog.Builder(this)
		.setTitle(temp.getString(1))
		.setMessage(temp.getString(3))
		.setPositiveButton("OK", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// DO Nothing
			}
		});
		alert.create().show();

	}


	public void deleteSMS_cur (Cursor c, int number) {
		if (c != null && c.moveToFirst()) {

			if (c.moveToPosition(number)) {
				long id = c.getLong(0);
				String address = c.getString(1);
				String body = c.getString(3);
				Log.d("SMSListActivity","id  " + id + " add " + address +" body" + body);
				getContentResolver().delete(
						Uri.parse("content://sms/" + id), null, null);
				Log.d("SMSListActivity","Deleted ");
			}
		}

		cursor = getContentResolver().query(Uri.parse("content://sms/inbox"), new String[]{"_id", "address", "date", "body"},null,null,null);
		mcAdapter.changeCursor(cursor);

	}


	@Override
	public void onDestroy() {
		TextToSpeech tts = SpeakOut.tts;
		if (tts != null) {
			tts.stop();
			tts.shutdown();
		}
		super.onDestroy();
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
