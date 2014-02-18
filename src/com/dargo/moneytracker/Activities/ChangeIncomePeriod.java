package com.dargo.moneytracker.Activities;

import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.dargo.moneytracker.R;
import com.dargo.moneytracker.Common.DatePicker;
import com.dargo.moneytracker.Common.Utilities;
import com.google.ads.AdRequest;
import com.google.ads.AdView;


public class ChangeIncomePeriod extends Activity
{

	int myPeriodBeginYear = 1900, myPeriodBeginMonth = 1 , myPeriodBeginDay = 1;
	int myPeriodEndYear = 2099, myPeriodEndMonth = 12 , myPeriodEndDay = 31;
	private AdView adView;
	
	@Override
	protected void onCreate(Bundle iSavedInstanceState) 
	{
		super.onCreate(iSavedInstanceState);
		setContentView(R.layout.change_income_period);
		Utilities.showActionBar(this);
		adView = (AdView) findViewById(R.id.change_inc_period_ad);
        adView.loadAd(new AdRequest());
	}
	
	@SuppressLint("NewApi")
	public void showDatePickerDialogFrom(View iView) 
	{
	    DialogFragment aFragment = new InnerDatePicker(0);
	    aFragment.show(getFragmentManager(), "datePicker");
	}

	@SuppressLint("NewApi")
	public void showDatePickerDialogTo(View iView) 
	{
	    DialogFragment aFragment = new InnerDatePicker(1); //should use a const
	    aFragment.show(getFragmentManager(), "datePicker");
	}
	
	public void doOK(View iView)
	{
		Intent aReturnIntent = new Intent();
		aReturnIntent.putExtra("aPeriodBeginYear",myPeriodBeginYear);
		aReturnIntent.putExtra("aPeriodBeginMonth",myPeriodBeginMonth);
		aReturnIntent.putExtra("aPeriodBeginDay",myPeriodBeginDay);
		aReturnIntent.putExtra("aPeriodEndYear",myPeriodEndYear);
		aReturnIntent.putExtra("aPeriodEndMonth",myPeriodEndMonth);
		aReturnIntent.putExtra("aPeriodEndDay",myPeriodEndDay);
		
		setResult(RESULT_OK,aReturnIntent);     
		finish();
	}
	
	public void doCancel(View iView)
	{
		Intent aReturnIntent = new Intent();
		setResult(RESULT_CANCELED, aReturnIntent);        
		finish();
	}
	
	public void setDate(int iYear, int iMonth, int iDay, int iStartOrEnd)
	{
		switch(iStartOrEnd)
		{
		case 0: //we set a start date
			myPeriodBeginYear = iYear;
			myPeriodBeginMonth = iMonth;
			myPeriodBeginDay = iDay;
			Button aButton = (Button) findViewById(R.id.ShowIncFromBut);
			aButton.setText(String.valueOf(iMonth + 1 ) + "/" +   String.valueOf(iDay) + "/" + String.valueOf(iYear));
			break;
		case 1: //we set an end date
			myPeriodEndYear = iYear;
			myPeriodEndMonth = iMonth;
			myPeriodEndDay = iDay;
			Button bButton = (Button) findViewById(R.id.ShowIncToBut);
			bButton.setText(String.valueOf(iMonth + 1 ) + "/" +   String.valueOf(iDay) + "/" + String.valueOf(iYear));
			break;
		default:
			break;
		}
	}
	
	
	@Override
    public boolean onCreateOptionsMenu(Menu iMenu) 
    {
        MenuInflater aInflater = getMenuInflater();
        aInflater.inflate(R.menu.main, iMenu);
        return true;
    }
    
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) 
    {
    	try
    	{
    		startActivity(Utilities.clickedOnMainMenu(item, this));
            return super.onOptionsItemSelected(item);	
    	}
    	catch (NullPointerException ex)
    	{
    		super.onBackPressed();
    		return false;
    	}
    }
	
	@SuppressLint({ "ValidFragment", "NewApi" })
	public class InnerDatePicker extends DialogFragment	implements DatePickerDialog.OnDateSetListener 
	{
		int _startOrEnd; //0 if start, 1 if end 

		public InnerDatePicker(int iStartOrEnd) 
		{
			_startOrEnd=iStartOrEnd;
		}	
			
		@SuppressLint("NewApi")
		@Override
		public Dialog onCreateDialog(Bundle iSavedInstanceState) 
		{
			// Use the current date as the default date in the picker
			final Calendar c = Calendar.getInstance();
			int aYear = c.get(Calendar.YEAR);
			int aMonth = c.get(Calendar.MONTH);
			int aDay = c.get(Calendar.DAY_OF_MONTH);
			// Create a new instance of DatePickerDialog and return it
			return new DatePickerDialog(getActivity(), this, aYear, aMonth, aDay);
		}
	
		public void onDateSet(DatePicker view, int ioYear, int ioMonth, int ioDay) 
		{
		// Do something with the date chosen by the user
		}
	
		@Override
		public void onDateSet(android.widget.DatePicker view, int iYear, int iMonth, int iDay) 
		{
			// TODO Auto-generated method stub
			setDate(iYear, iMonth, iDay, _startOrEnd);
			
		}
	}
	
	
}
