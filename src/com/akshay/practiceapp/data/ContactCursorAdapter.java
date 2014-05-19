package com.akshay.practiceapp.data;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import com.akshay.practiceapp.R;

import android.content.ContentUris;
import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Adapter class to contacts and set in list view
 * @author Akshay
 *
 */
public class ContactCursorAdapter extends CursorAdapter{

	private LayoutInflater mLayoutInflator ;
	public ContactCursorAdapter(Context context, Cursor c, int flags) {
		super(context, c, flags);
		mLayoutInflator = LayoutInflater.from(context);

	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {

		ViewHolder holder  =   (ViewHolder)    view.getTag();
		String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));;
		String name=cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

		if (name == "" || name == null) {
			holder.cName.setText("NO NAME");
		} else 
			holder.cName.setText(name);

		Bitmap bitmap= BitmapFactory.decodeStream(openPhoto(context,Long.parseLong(id)));
		holder.cPhoto.setImageBitmap(bitmap);


	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		View view = mLayoutInflator.inflate(R.layout.contacts_list_item, parent, false);

		ViewHolder holder  =   new ViewHolder();
		holder.cName    =   (TextView)  view.findViewById(R.id.text_name);
		holder.cPhoto = (ImageView) view.findViewById(R.id.image_contact);
		view.setTag(holder);


		return view;	
	}

	/**
	 * This method to get photo thumbnail from database
	 * @param context
	 * @param contactId
	 * @return inputstream
	 */
	public InputStream openPhoto(Context context,long contactId) {
		Uri contactUri = ContentUris.withAppendedId(Contacts.CONTENT_URI, contactId);
		Uri photoUri = Uri.withAppendedPath(contactUri, Contacts.Photo.CONTENT_DIRECTORY);

		CursorLoader cLoader = new CursorLoader(context, photoUri, 
				new String[] {Contacts.Photo.PHOTO},null,null,null);

		Cursor cursor =cLoader.loadInBackground();

		if (cursor == null) {
			return null;
		}
		try {
			if (cursor.moveToFirst()) {
				byte[] data = cursor.getBlob(0);
				if (data != null) {
					return new ByteArrayInputStream(data);
				}
			}
		} finally {
			cursor.close();
		}
		return null;
	}

	private  class ViewHolder {
		TextView cName;
		ImageView cPhoto;

	}
}
