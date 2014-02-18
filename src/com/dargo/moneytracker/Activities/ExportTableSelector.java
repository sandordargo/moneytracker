package com.dargo.moneytracker.Activities;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.dargo.moneytracker.R;
import com.dargo.moneytracker.Adapters.TableArrayAdapter;
import com.dargo.moneytracker.Common.Utilities;
import com.dargo.moneytracker.DataHandling.MoneyDB;
import com.dargo.moneytracker.DataHandling.TableMetaData;
import com.google.ads.AdRequest;
import com.google.ads.AdView;

public class ExportTableSelector extends ListActivity {
	 
	  MoneyDB db;
	  ArrayList<TableMetaData> listData;
	  TableArrayAdapter myAdapter;
	  Button nextBtn;
	  private AdView adView;
	
	  @Override
	  	public void onCreate(Bundle savedInstanceState) 
	  	{
		  super.onCreate(savedInstanceState);
		  nextBtn = (Button) findViewById(R.id.ExportSelectTablesNext);
		  db = new MoneyDB(this);
		  listData = new ArrayList<TableMetaData>();
		  showList();
		  Utilities.showActionBar(this);
		  adView = (AdView) findViewById(R.id.export_table_selector_ad);
	      adView.loadAd(new AdRequest());
		  		  		  
		 // addListenerOnChkImCat();
	  	}
	  
		private void showList()
		{
	        Cursor aCursor = db.getTables2Export();
	        if (aCursor.moveToFirst())
	        {
	        	try
	        	{
		        	while (aCursor.isAfterLast() == false)
		        	{
		        		TableMetaData temp = new TableMetaData();
			        	temp.Selected2Export = false;
			        	temp.id = aCursor.getInt(0);
		        		temp.tablename = aCursor.getString(1);
			        	listData.add(temp);
			        	temp = null; 
			        	aCursor.moveToNext();
		        	}
	        	}
	        	finally
	        	{
	        		aCursor.close();
	        	}
	        }
	        																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		
	        View header = getLayoutInflater().inflate(R.layout.export_select_tables_header, null);
	        View footer = getLayoutInflater().inflate(R.layout.export_select_tables_footer, null);
	        ListView listView = getListView();
	               
	        listView.addHeaderView(header);
	        listView.addFooterView(footer);
	      
	        this.myAdapter = new TableArrayAdapter(this, R.layout.export_select_tables, listData);
	        setListAdapter(myAdapter);
       
		}
		
		
		public void onBackPressed()
		{
			
			myAdapter.onCloseEvent();
			finish();
		}
		
		public void exportSelectTablesNext(View v)
		{
						
			ArrayList<String> tables2Export = myAdapter.onNextClicked();
			
			
			Intent intent = new Intent(this, ExportPeriodSelector.class);
			intent.putStringArrayListExtra("tables2Export", tables2Export);
			startActivity(intent);
		}
		
		public void onPause()
		{
			super.onPause();
			myAdapter.onCloseEvent();
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
	

