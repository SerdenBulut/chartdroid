package com.googlecode.chartdroid.calendar.activity;

import com.googlecode.chartdroid.R;
import com.googlecode.chartdroid.calendar.MiniMonthDrawable;
import com.googlecode.chartdroid.calendar.container.CalendarDay;
import com.googlecode.chartdroid.calendar.container.SimpleEvent;
import com.googlecode.chartdroid.calendar.view.DayView;
import com.googlecode.chartdroid.calendar.view.MonthLayout;
import com.googlecode.chartdroid.core.ContentSchemaOld;
import com.googlecode.chartdroid.core.IntentConstants;
import com.googlecode.chartdroid.core.ContentSchemaOld.CalendarEvent;

import org.achartengine.activity.GraphicalActivity;
import org.achartengine.activity.GraphicalActivity.DataSeriesAttributes;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class NewCalendarActivity extends Activity {


    final static public String TAG = "Calendar";

	static final int REQUEST_CODE_EVENT_SELECTION = 1;

	
    private LayoutInflater mInflater;
    
    ImageView mini_calendar_prev, mini_calendar_curr, mini_calendar_next;
    
    
    List<SimpleEvent> events = new ArrayList<SimpleEvent>();
    
    // ========================================================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_LEFT_ICON);
        setContentView(R.layout.experimental_calendar_month);
        getWindow().setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.titlebar_icon);


        
        
        Uri intent_data = getIntent().getData();
//    	Log.d(TAG, "Intent data: " + intent_data);
//    	Log.d(TAG, "Intent type: " + getIntent().getType());
//
//    	Log.d(TAG, "Intent action: " + getIntent().getAction());
//    	
    	
        // Zip the events
        if (intent_data != null) {
        	// We have been passed a cursor to the data via a content provider.
        	
        	Log.d(TAG, "Querying content provider for: " + intent_data);

        	String key_event_title = ContentSchemaOld.CalendarEvent.COLUMN_EVENT_TITLE;
 			Cursor cursor = managedQuery(intent_data,
 					new String[] {BaseColumns._ID, CalendarEvent.COLUMN_EVENT_TIMESTAMP, CalendarEvent.COLUMN_EVENT_TITLE},
 					null, null, null);

 			int id_column = cursor.getColumnIndex(BaseColumns._ID);
 			int timestamp_column = cursor.getColumnIndex(ContentSchemaOld.CalendarEvent.COLUMN_EVENT_TIMESTAMP);
 			
// 			Log.e(TAG, "In calendar - rowcount: " + cursor.getCount());
// 			Log.e(TAG, "In calendar - colcount: " + cursor.getColumnCount());
 			
 			if (cursor.moveToFirst()) {
	 			do {
	 				long timestamp = cursor.getLong(timestamp_column)*1000;
	 				Log.d(TAG, "Adding event with timestamp: " + timestamp);
	 				Log.d(TAG, "Timestamp date is: " + new Date(timestamp));
	
		        	events.add(
		        		new SimpleEvent(
		        			cursor.getLong(id_column),
		        			timestamp) );
		        	
	 			} while (cursor.moveToNext());
 			}
        } else {
        	// We have been passed the data directly.

//        	Log.d(TAG, "We have been passed the data directly.");
        	
	        long[] event_ids = getIntent().getLongArrayExtra(IntentConstants.EXTRA_EVENT_IDS);
	        long[] event_timestamps = getIntent().getLongArrayExtra(IntentConstants.EXTRA_EVENT_TIMESTAMPS);
	        for (int i=0; i<event_timestamps.length; i++)
	        	events.add( new SimpleEvent(event_ids[i], event_timestamps[i]) );
	        
//	        Log.d(TAG, "Added " + event_timestamps.length + " timestamps.");
        }
        Collections.sort(events);
        
        
        
        mInflater = LayoutInflater.from(this);
        

        
        MonthLayout month_layout = (MonthLayout) findViewById(R.id.full_month);
        month_layout.setMonth(new GregorianCalendar());

        
        
        
        
        
        
        
        
        
        
        
        mini_calendar_prev = (ImageView) findViewById(R.id.mini_calendar_prev);
        mini_calendar_curr = (ImageView) findViewById(R.id.mini_calendar_curr);
        mini_calendar_next = (ImageView) findViewById(R.id.mini_calendar_next);

        

		GregorianCalendar cal_prev = new GregorianCalendar();
		cal_prev.add(GregorianCalendar.MONTH, -1);
		
		GregorianCalendar cal_curr = new GregorianCalendar();
		
		GregorianCalendar cal_next = new GregorianCalendar();
		cal_next.add(GregorianCalendar.MONTH, 1);
		
//		Log.d(TAG, "Previous month...");
        mini_calendar_prev.setImageDrawable(new MiniMonthDrawable(this, mini_calendar_prev, cal_prev));
        
//        Log.d(TAG, "Current month...");
        mini_calendar_curr.setImageDrawable(new MiniMonthDrawable(this, mini_calendar_curr, cal_curr));
        
//        Log.d(TAG, "Next month...");
        mini_calendar_next.setImageDrawable(new MiniMonthDrawable(this, mini_calendar_next, cal_next));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != RESULT_OK) {
            Log.i(TAG, "==> result " + resultCode + " from subactivity!  Ignoring...");
//            Toast t = Toast.makeText(this, "Action cancelled!", Toast.LENGTH_SHORT);
//            t.show();
            return;
        }
        
  	   	switch (requestCode) {
   		case REQUEST_CODE_EVENT_SELECTION:
   		{
   			
   			long id = data.getLongExtra(IntentConstants.INTENT_EXTRA_CALENDAR_SELECTION_ID, -1);

			Intent i = new Intent();
			i.putExtra(IntentConstants.INTENT_EXTRA_CALENDAR_SELECTION_ID, id);
	        setResult(Activity.RESULT_OK, i);
			finish();

//   			Toast.makeText(this, "Result: " + id, Toast.LENGTH_SHORT).show();
            break;
        }
  	   	}
    }
    
}
