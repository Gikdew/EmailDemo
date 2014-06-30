package com.gikdew.emaildemo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

public class App extends ActionBarActivity {

	private ProgressDialog pDialog;

	// Creating JSON Parser object
	JSONParser jParser = new JSONParser();

	ArrayList<HashMap<String, String>> productsList;

	// url to get all products list
	private static String url_all_products = "http://gikdew.com/android/emails/get_all_signals.php";

	// JSON Node names
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_PRODUCTS = "signals";
	private static final String TAG_ID = "id";
	private static final String TAG_pair = "pair";
	private static final String TAG_expiry = "expiry";
	private static final String TAG_result = "result";
	private static final String TAG_created_at = "created_at";

	ListView list;

	// products JSONArray
	JSONArray products = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.app);

		list = (ListView) findViewById(R.id.listview1);
		list.addHeaderView(new View(this));
		list.addFooterView(new View(this));

		productsList = new ArrayList<HashMap<String, String>>();

		// Loading products in Background Thread
		new LoadAllProducts().execute();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.action_refresh:
			new LoadAllProducts().execute();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	class LoadAllProducts extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */

		BaseInflaterAdapter<ItemData> adapter;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(App.this);
			pDialog.setMessage("Loading signals...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
			adapter = new BaseInflaterAdapter<ItemData>(new CardInflater());
		}

		/**
		 * getting All products from url
		 * */
		protected String doInBackground(String... args) {
			// Building Parameters
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
						String pair = c.getString(TAG_pair);
						String expiry = c.getString(TAG_expiry);
						String result = c.getString(TAG_result);
						String createat = c.getString(TAG_created_at);

						ItemData data = new ItemData(id, pair, expiry, result,
								createat);
						adapter.addItem(data, false);
						adapter.notifyDataSetChanged();

						if (i == 0) {
							SharedPreferences signalsPref = getSharedPreferences(
									"signals", 0);
							Editor editor = signalsPref.edit();

							editor.putString("lastSignal", id);
							editor.commit();

							Log.d("lastSignal",
									signalsPref.getString("lastSignal", "0"));
						}
					}
				} else {
					// no products found
					// Launch Add New product Activity
					Intent i = new Intent(getApplicationContext(), App.class);
					// Closing all previous activities
					i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(i);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			// dismiss the dialog after getting all products
			pDialog.dismiss();
			// updating UI from Background Thread
			runOnUiThread(new Runnable() {
				public void run() {

					list.setAdapter(adapter);
				}
			});

		}
	}
}
