package com.akshay.practiceapp.data;

import com.akshay.practiceapp.R;
import com.akshay.practiceapp.util.SpeakOut;

import android.content.Context;
import android.database.Cursor;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Adapter class to get inbox messages and set in list view
 * @author Akshay
 *
 */
public class MessageCursorAdapter extends CursorAdapter {

	private LayoutInflater mLayoutInflator ;
	public MessageCursorAdapter(Context context, Cursor c, int flags) {
		super(context, c, flags);
		mLayoutInflator = LayoutInflater.from(context);
	}

	@Override
	public void bindView(View view, Context context,  Cursor cursor) {
		
		ViewHolder holder  =   (ViewHolder)    view.getTag();
		holder.name.setText(cursor.getString(1));
		final String tempStr = cursor.getString(3);
		holder.message.setText(tempStr);
		holder.speakOut.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				SpeakOut.tts.speak(tempStr, TextToSpeech.QUEUE_FLUSH, null);

			}
		});
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		View view = mLayoutInflator.inflate(R.layout.message_list_item, parent, false);

		ViewHolder holder  =   new ViewHolder();
		holder.name    =   (TextView)  view.findViewById(R.id.name_text);
		holder.message    =   (TextView)  view.findViewById(R.id.msg_text);
		holder.speakOut = (ImageView) view.findViewById(R.id.speak_out);
		view.setTag(holder);


		return view;	


	}

	private  class ViewHolder {
		TextView name;
		TextView message;
		ImageView speakOut;
	}


}
