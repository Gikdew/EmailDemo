package com.gikdew.BinaryOptionsSignals;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.gikdew.emaildemo.R;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {

	private ProgressDialog pDialog;

	JSONParser jsonParser = new JSONParser();
	EditText email;

	private static String url_new_mail = "http://gikdew.com/android/emails/db_newemail.php";

	// JSON Node names
	private static final String TAG_SUCCESS = "success";

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		ActionBar actionBar = getSupportActionBar();
		actionBar.hide();

		// General Variables
		SharedPreferences data = getSharedPreferences("data", 0);
		final Editor editor = data.edit();
		final EmailValidator validator = new EmailValidator();

		// Views
		email = (EditText) findViewById(R.id.editText1);
		Button validateBt = (Button) findViewById(R.id.button1);
		RelativeLayout rl = (RelativeLayout) findViewById(R.id.relativeMain);

		TextView tv = (TextView) findViewById(R.id.title);
		TextView tv2 = (TextView) findViewById(R.id.textView1);
		Typeface face = Typeface.createFromAsset(getAssets(),"fonts/Lato-Reg.ttf");

		Typeface face1 = Typeface.createFromAsset(getAssets(),
				"fonts/dense.otf");

		tv.setTypeface(face1);
		email.setTypeface(face);
		validateBt.setTypeface(face);
		tv2.setTypeface(face);

		// Buttons
		validateBt.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// Validate email format
				if (validator.validate(email.getText().toString())) {
					editor.putString("email", email.getText().toString());
					editor.commit();
					new SendMail().execute();
				} else {

					Toast.makeText(MainActivity.this, "Invalid email",
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		rl.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// Hide keyboard
				InputMethodManager inputManager = (InputMethodManager) MainActivity.this
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				inputManager.hideSoftInputFromWindow(MainActivity.this
						.getCurrentFocus().getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);
			}
		});

	}
	

	// Send email to the database
	class SendMail extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(MainActivity.this);
			pDialog.setMessage("Validating Email...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		/**
		 * Creating product
		 * */
		protected String doInBackground(String... args) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("email", email.getText()
					.toString()));

			// getting JSON Object
			// Note that create product url accepts POST method
			JSONObject json = jsonParser.makeHttpRequest(url_new_mail, "POST",
					params);

			// check log cat fro response
			Log.d("Create Response", json.toString());

			// check for success tag
			try {
				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {
					// successfully created product
					Intent i = new Intent(getApplicationContext(), App.class);
					startActivity(i);

					// closing this screen
					finish();
				} else {
					// failed to create product
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
			Toast.makeText(MainActivity.this, "App Activated",
					Toast.LENGTH_SHORT).show();
			pDialog.dismiss();
		}

	}

}