package com.akshay.practiceapp.util;

import java.util.Locale;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;

public class SpeakOut implements TextToSpeech.OnInitListener{

	public static TextToSpeech tts;
	
	public SpeakOut() {

	}
	public SpeakOut(Context context) {
		tts = new TextToSpeech(context,this);

	}

	@Override
	public void onInit(int status) {
		Log.d("SpeakOut", "onInit");
		if (status == TextToSpeech.SUCCESS) {

			int result = tts.setLanguage(Locale.US);

			if (result == TextToSpeech.LANG_MISSING_DATA
					|| result == TextToSpeech.LANG_NOT_SUPPORTED) {
				Log.e("TTS", "This Language is not supported");
			} else {
				//tts.speak(message, TextToSpeech.QUEUE_FLUSH, null);
			}

		} else {
			Log.e("TTS", "Initilization Failed!");
		}

	}




}
