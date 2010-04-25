package com.googlecode.chartdroid.calendar;

import com.googlecode.chartdroid.calendar.container.CalendarDay;
import com.googlecode.chartdroid.calendar.container.SimpleEvent;

import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

public class CalendarUtils {

    public static class ViewHolderCalendarDay {
    	
    	public TextView title, datum;
    	public ImageView thumb;
    }
    
    public static int generate_days(GregorianCalendar cal, List<CalendarDay> day_list, List<SimpleEvent> events) {

    	final int active_month = cal.get(GregorianCalendar.MONTH);

    	cal.set(GregorianCalendar.DAY_OF_MONTH, 1);

    	
    	
    	
		GregorianCalendar cal_min_upper_limit = (GregorianCalendar) cal.clone();
		cal_min_upper_limit.add(GregorianCalendar.MONTH, 1);
//		Log.i(TAG, "Minimum upper limit: " + cal_min_upper_limit.getTime());

//		GregorianCalendar cal_max_upper_limit = (GregorianCalendar) cal_min_upper_limit.clone();
//		cal_max_upper_limit.add(GregorianCalendar.DATE, 7);
//    	Log.i(TAG, "Maximum upper limit: " + cal_max_upper_limit.getTime());
    	
    	



    	int first_day_of_week = cal.getFirstDayOfWeek();
//    	Log.e(TAG, "first day of week: " + first_day_of_week);
    	
    	int daydiff = cal.get(GregorianCalendar.DAY_OF_WEEK) - first_day_of_week;
		cal.add(GregorianCalendar.DATE, -daydiff);
    	
//    	Log.i(TAG, "Month of starting day in calendar: " + cal.get(GregorianCalendar.MONTH));
		
//		int maximum_possible_day_of_month = cal.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
//		Log.d(TAG, "Days in this month: " + maximum_possible_day_of_month);
    	
		

//		Log.d(TAG, "Size of day_list: " + day_list.size());
//		Log.d(TAG, "Size of events: " + events.size());
//		Log.d(TAG, "active_month: " + active_month);
		
		int event_index = 0;
    	while ( cal.before(cal_min_upper_limit)
    			|| cal.get(GregorianCalendar.DAY_OF_WEEK) > first_day_of_week) {

//    		Log.i(TAG, "Cal date: " + cal.getTime());
//    		Log.d(TAG, "Before min end date? " + cal.before(cal_min_upper_limit));
//    		Log.w(TAG, "After first day of week? " + (cal.get(GregorianCalendar.DAY_OF_WEEK) > first_day_of_week));
//    		Log.e(TAG, "Before uppper limit? " + cal.before(cal_max_upper_limit));
    		
    		
    		CalendarDay cd = new CalendarDay();
    		cd.date = cal.getTime();
    		cd.day_events = new ArrayList<SimpleEvent>();
    		
    		
    		// Catch up the event list with the current date
    		while ( event_index < events.size() && events.get(event_index).timestamp.compareTo( cal.getTime() ) <= 0 ) {
    			event_index++;
    		}

    		
    		// Advance calendar to the next day
    		cal.add(GregorianCalendar.DATE, 1);

    		
    		// Add all the events that occur before the next day
    		if (event_index < events.size()) {
	    		SimpleEvent scan_event = events.get(event_index);
				while ( scan_event.timestamp.compareTo( cal.getTime() ) <= 0 ) {
					
					cd.day_events.add( scan_event );

	    			event_index++;
	    			if (event_index < events.size())
	    				scan_event = events.get(event_index);
	    			else
	    				break;
				}
    		}
    		
    		day_list.add(cd);
    	}
    	
    	// Reset the moth so we can access it later...
    	cal.set(GregorianCalendar.MONTH, active_month);
    	
    	return active_month;
    }

}
