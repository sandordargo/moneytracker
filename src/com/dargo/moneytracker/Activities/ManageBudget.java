package com.dargo.moneytracker.Activities;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.FragmentManager;
import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.dargo.moneytracker.R;
import com.dargo.moneytracker.Adapters.BudgetArrayAdapter;
import com.dargo.moneytracker.Common.MonthAndYearPickerDialog;
import com.dargo.moneytracker.Common.MonthAndYearPickerDialog.EditNameDialogListener;
import com.dargo.moneytracker.Common.Utilities;
import com.dargo.moneytracker.DataHandling.BudgetData;
import com.dargo.moneytracker.DataHandling.MoneyDB;
import com.google.ads.AdRequest;
import com.google.ads.AdView;

public class ManageBudget extends ListActivity implements OnItemSelectedListener, EditNameDialogListener {
	 
	  MoneyDB db;
	  ArrayList<BudgetData> listData;
	  BudgetArrayAdapter myAdapter;
	  Spinner myNewBudgetSpinner;
	  Button myPeriodChooserButton;
	  int myYear;
	  int myMonth;
	  private AdView adView;
	
	  @Override
	  	public void onCreate(Bundle savedInstanceState) 
	  	{
		  super.onCreate(savedInstanceState);
		  db = new MoneyDB(this);
		  listData = new ArrayList<BudgetData>();
		  final Calendar aCal = Calendar.getInstance();
		  myYear = aCal.get(Calendar.YEAR);
		  myMonth = aCal.get(Calendar.MONTH) + 1;
		  
	      
	      initView();
	      myNewBudgetSpinner = (Spinner) findViewById(R.id.newBudgetItem);
		  myNewBudgetSpinner.setOnItemSelectedListener(this); // Spinner click listener
		  myPeriodChooserButton = (Button) findViewById(R.id.chooseBudgetMonth);
		  myPeriodChooserButton.setText(Utilities.getDateString(myYear, myMonth, '/'));
          // Loading spinner data from database
	      loadSpinnerData();
	      Utilities.showActionBar(this);
	  	}
	  
	  	private void initView()
	  	{
	  		View header = getLayoutInflater().inflate(R.layout.manage_budget_header, null);
	        View footer = getLayoutInflater().inflate(R.layout.manage_budget_footer, null);
	        ListView listView = getListView();
	               
	        listView.addHeaderView(header);
	        listView.addFooterView(footer);
	        adView = (AdView) findViewById(R.id.manage_budget_ad);
	        adView.loadAd(new AdRequest());
	      
	  		showList();
	  	}
	  
		private void showList()
		{
			listData.clear();
			String aPeriodStartDateStr = Utilities.getDateString(myYear, myMonth, 1, '/');
	        Cursor getBudgetCursor = db.getBudgetItems("2", aPeriodStartDateStr); 
	        if (getBudgetCursor.moveToFirst())
	        {
	        	try
	        	{
		        	while (getBudgetCursor.isAfterLast() == false)
		        	{
		        		BudgetData temp = new BudgetData();
			        	temp.id = getBudgetCursor.getInt(0);
			        	temp.budgetName = getBudgetCursor.getString(1);
			        	temp.budgetOrigValue = getBudgetCursor.getInt(2);
			        	temp.totalOutput = getBudgetCursor.getDouble(3);
			        	temp.isChanged = false;
			        	listData.add(temp);
			        	temp = null;
			        	getBudgetCursor.moveToNext();
		        	}
	        	}
	        	finally
	        	{
		        	getBudgetCursor.close();
	        	}
	        }
	        																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		
	        
	        this.myAdapter = new BudgetArrayAdapter(this, R.layout.manage_budget, listData);
	        setListAdapter(myAdapter);
       
		}
		
		private void loadSpinnerData() 
		{
	        Cursor cursor = db.getCategories(2);
	        List<String> labels = new ArrayList<String>();
	        // looping through all rows and adding to list
	        if (cursor.moveToFirst())
	        {
	        	try
	        	{
		        	while (cursor.isAfterLast() == false)
		        	{       
		        		labels.add(cursor.getString(0));
		        		cursor.moveToNext();
		        	}
	        	}
	        	finally
	        	{
	        		cursor.close();		
	        	}
	        }
	        // Creating adapter for spinner
	        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
	                android.R.layout.simple_spinner_item, labels);
	 
	        // Drop down layout style - list view with radio button
	        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	 
	        // attaching data adapter to spinner
	        myNewBudgetSpinner.setAdapter(dataAdapter);
	    }
	    
		
		public void addNewBudgetItem(View view)
		{
			
			EditText aBudgetItemValue = (EditText) findViewById(R.id.newBudgetItemValueFooter);
			
			String aBudgetItemNameStr = myNewBudgetSpinner.getSelectedItem().toString();
			String aBudgetItemValueStr = aBudgetItemValue.getText().toString();
			String aBudgetPeriodStartDate = Utilities.getDateString(myYear, myMonth, 1,'/');
			
			try
			{
				if (aBudgetItemValueStr=="" ||  Integer.parseInt(aBudgetItemValueStr) <= 0)
				{
					Toast.makeText(this, "New budget category cannot be added, value must be bigger than 0!", Toast.LENGTH_SHORT).show();
					return;
				}
			}
			catch(NumberFormatException ex)
			{
				Toast.makeText(this, "New budget category cannot be added, value must be bigger than 0!", Toast.LENGTH_SHORT).show();
				return;
			}
			
			db.insertBudgetItem(aBudgetItemNameStr, aBudgetItemValueStr, "2", aBudgetPeriodStartDate); //2 means this is an expense budget
			Toast.makeText(this, "New budget value: " + aBudgetItemValueStr + " from " + aBudgetPeriodStartDate , Toast.LENGTH_SHORT).show();
			aBudgetItemValue.setText("");
			showList();
		}
		
		
	    public void deleteCategory(View view)
	    {
	    	//dummy method: handler in adapter class
	    }
	    
	    public void chooseBudgetMonth(View view)
	    {
	    	//DialogFragment newFragment = new InnerDatePicker(0); //should use a const
		    //newFragment.show(getFragmentManager(), "datePicker");
		    
		        FragmentManager fm = getFragmentManager();
		        MonthAndYearPickerDialog helpDialog = new MonthAndYearPickerDialog();
		        helpDialog.show(fm, "fragment_help");
		    
	    }
	    
	    public void previousMonthBudget(View view)
	    {
	    	if (myMonth == 1)
	    	{
	    		--myYear;
	    		myMonth = 12;
	    	}
	    	else
	    	{
	    		--myMonth;
	    	}
	    	myPeriodChooserButton.setText(Utilities.getDateString(myYear, myMonth, '/'));
	    	showList();
	    }
	    
	    public void nextMonthBudget(View view)
	    {
	    	if (myMonth == 12)
	    	{
	    		++myYear;
	    		myMonth = 1;
	    	}
	    	else
	    	{
	    		++myMonth;
	    	}
	    	myPeriodChooserButton.setText(Utilities.getDateString(myYear, myMonth, '/'));
	    	showList();
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
	    
		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
			
		}
				

		@Override
		public void onFinishEditDialog(int iYear, int iMonth) {
			// TODO Auto-generated method stub

			myYear = iYear;
		    myMonth = iMonth;
		
			myPeriodChooserButton.setText(Utilities.getDateString(myYear, myMonth, '/'));
	    	showList();
		
		}

		
		
	    
		
}
	

