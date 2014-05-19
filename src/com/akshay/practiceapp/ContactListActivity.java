package com.akshay.practiceapp;

import com.akshay.practiceapp.data.ContactCursorAdapter;

import android.app.ListActivity;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


/**
 * Displays contacts in list view 
 * @author Akshay
 *
 */
public class ContactListActivity extends ListActivity {

	ContactCursorAdapter contactCursorAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact_list);
		fetchContacts();
	}


	/**
	 * Fetches contact from database
	 */
	public void fetchContacts () {
		Uri uri=ContactsContract.Contacts.CONTENT_URI;
		String sortOrder = ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC";

		CursorLoader cLoader = new CursorLoader(this, uri,
				null,null,null,sortOrder);
		Cursor cur = cLoader.loadInBackground();
		if (!cur.moveToFirst()) {
			Toast.makeText(getApplicationContext(), "No Contacts in phone", Toast.LENGTH_SHORT).show();
		} else {
			contactCursorAdapter = new ContactCursorAdapter(getApplicationContext(), cur, 1);
			setListAdapter(contactCursorAdapter);
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
