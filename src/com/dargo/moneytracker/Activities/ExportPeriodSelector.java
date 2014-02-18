package com.dargo.moneytracker.Activities;


import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;

import com.dargo.moneytracker.R;
import com.dargo.moneytracker.Common.Utilities;
import com.google.ads.AdRequest;
import com.google.ads.AdView;

public class ExportPeriodSelector extends Activity {
	
	String sqlSelection = " WHERE ";
	Calendar c;
	private AdView adView;
	
	public void onCreate(Bundle savedInstanceState) 
  	{
	  super.onCreate(savedInstanceState);
	  setContentView(R.layout.export_select_period);
	  Utilities.showActionBar(this);
	  c = Calendar.getInstance();
	  adView = (AdView) findViewById(R.id.export_select_period_ad);
      adView.loadAd(new AdRequest());

  	}
	
	
	public void onExpPeriodRadioButtonClicked(View view) {
		sqlSelection = " WHERE ";
		// Is the button now checked?
	    boolean checked = ((RadioButton) view).isChecked();
	    int aYear = c.get(Calendar.YEAR);
	    int aMonth = c.get(Calendar.MONTH) + 1;
	    //int aDay = c.get(Calendar.DAY_OF_MONTH);
	    // Check which radio button was clicked
	    switch(view.getId()) {
	        case R.id.expradio_this_month:
	            if (checked)
	            {	          	
	            	sqlSelection += " export.Date >= '" + Utilities.getDateString(aYear, aMonth, 1, '/') + "'";
	            	sqlSelection += " AND export.Date < '" + Utilities.getDateString(aYear, aMonth+1, 1, '/') + "'";
	            	afterRadioClicked();
	            }
	            break;
	        case R.id.expradio_last_month:
	            if (checked)
	            {	          	
	            	sqlSelection += " export.Date >= '" + Utilities.getDateString(aYear, aMonth-1, 1, '/') + "'";
	            	sqlSelection += " AND export.Date < '" + Utilities.getDateString(aYear, aMonth, 1, '/') + "'";
	            	afterRadioClicked();
	            }
	            break;
	        case R.id.expradio_this_year:
	            if (checked)
	            {	          	
	            	sqlSelection += " export.Date >= '" + Utilities.getDateString(aYear, 1, 1, '/') + "'";
	            	sqlSelection += " AND export.Date < '" + Utilities.getDateString(aYear+1, 1, 1, '/') + "'";
	            	afterRadioClicked();
	            }
	            break;
	        case R.id.expradio_all:
	            if (checked)
	            {	          	
	            	sqlSelection += " export.Date >= '" + Utilities.getDateString(1900, 1, 1, '/') + "'";
	            	sqlSelection += " AND export.Date < '" + Utilities.getDateString(2100, 1, 1, '/') + "'";
	            	afterRadioClicked();
	            }
	            break;
	    }
	}
	
	public void afterRadioClicked()
	{
		Intent intent = Utilities.sendExportExpensesMail(this, sqlSelection, this.getIntent().getStringArrayListExtra("tables2Export"));
		intent.putStringArrayListExtra("tables2Export", this.getIntent().getStringArrayListExtra("tables2Export"));
		startActivity(intent);
	
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
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
}
