package com.gikdew.emaildemo;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.ListView;

public class DownloadService extends Service {

		
	// Creating JSON Parser object
	JSONParser jParser = new JSONParser();

	ArrayList<HashMap<String, String>> productsList;

	// url to get all products list
	private static String url_all_products = "http://gikdew.com/android/emails/get_last_signal.php";

	// JSON Node names
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_PRODUCTS = "signals";
	private static final String TAG_ID = "id";
	private static final String TAG_pair = "pair";
	private static final String TAG_expiry = "expiry";
	private static final String TAG_result = "result";
	private static final String TAG_created_at = "created_at";
	String pair, expiry, result;

	ListView list;

	// products JSONArray
	JSONArray products = null;
	
	@
	Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		//Log.i("Service", "Started");
		new DownloaderTask().execute();
		return START_STICKY;
	}

	@
	Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	private class DownloaderTask extends AsyncTask < URL, Void, String > {
		final SharedPreferences signalsPref = getSharedPreferences("signals", Context.MODE_PRIVATE);
		final Editor editor = signalsPref.edit();

		@
		Override
		protected void onPreExecute() {
		}

		@
		Override
		protected String doInBackground(URL...url) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			// getting JSON string from URL
			JSONObject json = jParser.makeHttpRequest(url_all_products, "GET",
					params);

			// Check your log cat for JSON reponse
			Log.d("All Products: ", json.toString());

			try {
				// Checking for SUCCESS TAG
				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {
					// products found
					// Getting Array of Products
					products = json.getJSONArray(TAG_PRODUCTS);

					// looping through All Products
					for (int i = 0; i < products.length(); i++) {
						JSONObject c = products.getJSONObject(i);

						// Storing each json item in variable
						String id = c.getString(TAG_ID);

						pair = c.getString(TAG_pair);
						expiry = c.getString(TAG_expiry);
						result = c.getString(TAG_result);
						//createat = c.getString(TAG_created_at);

						if (i == 0) {
							SharedPreferences signalsPref = getSharedPreferences(
									"signals", 0);
							Editor editor = signalsPref.edit();

							editor.putString("lastSignal1", id);
							editor.commit();

							Log.d("lastSignal",
									signalsPref.getString("lastSignal1", "0"));
						}
					}
				} else {
					Log.d("Error", "DownloadServiceError");
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		@
		Override
		protected void onPostExecute(String result) {
			if(!signalsPref.getString("lastSignal1", "0").equals(signalsPref.getString("lastSignal", "0"))) {
				
				editor.putString("lastSignal", signalsPref.getString("lastSignal1", "0"));
				editor.commit();
				
				sendNotification();

			} else {
				
			}
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					new DownloaderTask().execute();
				}
			}, 7000);
		}

		public void sendNotification() {
			Intent intent = new Intent(DownloadService.this, App.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
			PendingIntent pIntent = PendingIntent.getActivity(DownloadService.this, 0, intent, 0);

			// build notification
			// the addAction re-use the same intent to keep the example short
			NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(DownloadService.this);

			mBuilder.setContentTitle(pair)
				.setContentText(expiry + " " + result)
				.setTicker(pair + " " + expiry + " " + result)
				.setSmallIcon(R.drawable.ic_launcher)
				.setContentIntent(pIntent)
				.setAutoCancel(true)
				.setDefaults(Notification.DEFAULT_ALL);

			NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

			notificationManager.notify(0, mBuilder.build());

		}
	}

}