package com.gikdew.BinaryOptionsSignals;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import android.annotation.SuppressLint;
import android.util.Log;
import android.widget.Toast;

public class ItemData {

	private String m_id;
	private String m_pair;
	private String m_expiry;
	private String m_result;
	private String m_createdat;

	public ItemData(String id, String pair, String expiry, String result, String createdat) {
		m_id = id;
		m_expiry = expiry;
		m_pair = pair;
		m_result = result;
		m_createdat = createdat;
	}

	public String getId() {
		return m_id;
	}

	public String getPair() {
		return m_pair;
	}

	public String getExpiry() {
		return m_expiry;
	}

	public String getResult() {
		return m_result;
	}
	
	@SuppressLint("SimpleDateFormat")
	public String getCreatedat() {
		String string = m_createdat;
		
		try {
			Date date = null;
			date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).parse(string);
			//long millisecondsFromNow = milliseconds - (new Date()).getTime();
			long time = System.currentTimeMillis();
			TimeZone pacificTimeZone = TimeZone.getTimeZone("America/Los_Angeles");
			
			long currentTime = time;
			//Log.d("Date", Long.toString(time) + " " + pacificTimeZone.getOffset(System.currentTimeMillis()));
			//Log.d("Date Converted", Long.toString(time-convertedTime));
			
			
			TimeZone mTimeZone = TimeZone.getDefault();
			int mGMTOffset = mTimeZone.getRawOffset(); 
			//Log.d("GMT", String.valueOf(mGMTOffset));
			Date finalTime = new Date(date.getTime() - pacificTimeZone.getOffset(currentTime) + mGMTOffset + 3600000);
			//System.out.printf("GMT offset is %s hours", TimeUnit.HOURS.convert(mGMTOffset, TimeUnit.MILLISECONDS)); 
						
			m_createdat = new SimpleDateFormat("MMM dd, HH:mm:ss ").format(finalTime);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return m_createdat;
	}

}
