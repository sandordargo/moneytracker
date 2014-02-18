package com.dargo.moneytracker.DataHandling;

import java.io.Serializable;

public class EntryDate implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -231004440213031161L;
	public int _year;
	public int _month;
	public int _day;
	
	public String getFormattedDate()
	{
		String aFormattedDate, aFormattedMonth;
		
		switch(_month)
		{
		case 1:
				aFormattedMonth = "Jan";
				break;
		case 2:
				aFormattedMonth = "Feb";
				break;
		case 3:
				aFormattedMonth = "Mar";
				break;
		case 4:
				aFormattedMonth = "Apr";
				break;
		case 5:
				aFormattedMonth = "May";
				break;
		case 6:
				aFormattedMonth = "Jun";
				break;
		case 7:
				aFormattedMonth = "Jul";
				break;
		case 8:
				aFormattedMonth = "Aug";
				break;
		case 9:
				aFormattedMonth = "Sep";
				break;
		case 10:
				aFormattedMonth = "Oct";
				break;
		case 11:
				aFormattedMonth = "Nov";
				break;
		case 12:
				aFormattedMonth = "Dec";
				break;
		default:
			aFormattedMonth = "INV";
		}
		
		aFormattedDate = Integer.toString(_year) + "-" + aFormattedMonth + "-" + Integer.toString(_day);
		return aFormattedDate;
		
	}
}
