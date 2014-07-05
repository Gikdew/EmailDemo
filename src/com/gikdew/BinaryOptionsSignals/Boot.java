package com.gikdew.BinaryOptionsSignals;

import com.gikdew.BinaryOptionsSignals.DownloadService;
import com.gikdew.BinaryOptionsSignals.MainActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;

@SuppressLint("CommitPrefEdits")
public class Boot extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);			
		SharedPreferences data = getSharedPreferences("data", 0);	
		
		Intent i = new Intent(Boot.this, DownloadService.class);
		Boot.this.startService(i);

		
		//DELETE LATER
		Editor editor = data.edit();
		//editor.remove("email");
		//editor.commit();
		if(!data.contains("email")){
			 Intent intent = new Intent(this, MainActivity.class);
			 startActivity(intent);
			 Boot.this.finish();
		}else{
			Intent intent = new Intent(this, App.class);
			startActivity(intent);
			Boot.this.finish();
		}
	}
}
