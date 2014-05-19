package com.akshay.practiceapp.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

@SuppressLint("ValidFragment")
public class DialogManager extends DialogFragment{

	String message;
	String title;
	Activity activity;
	
	public DialogManager(String message, String title, Activity activity) {
		super();
		this.message = message;
		this.title = title;
		this.activity = activity;
	}



	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		 super.onCreateDialog(savedInstanceState);
		AlertDialog.Builder alert = new AlertDialog.Builder(activity);
		alert.setTitle(title);
		alert.setMessage(message);
		alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		return alert.create();
	}
	
}
