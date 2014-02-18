package com.dargo.moneytracker.Activities;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.dargo.moneytracker.R;
import com.dargo.moneytracker.Common.DatePicker;
import com.dargo.moneytracker.Common.Utilities;
import com.dargo.moneytracker.DataHandling.MoneyDB;
import com.google.ads.AdRequest;
import com.google.ads.AdView;

public class InsertExpense extends Activity implements
OnItemSelectedListener{
	
	private AdView adView;	
	MoneyDB db;
	Spinner spinner;
	Button expDate;
	AutoCompleteTextView expenseNameText;
    @SuppressLint({ "NewApi", "SimpleDateFormat" })
    
    
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new MoneyDB(this);
        setContentView(R.layout.insert_expense_activity);
        
        // Make sure we're running on Honeycomb or higher to use ActionBar APIs
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // Show the Up button in the action bar.
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
        
        Calendar c = Calendar.getInstance();
    	SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
    	String formattedDate = df.format(c.getTime());
    	expDate = (Button) findViewById(R.id.expenseDateButton);
    	expDate.setText(formattedDate);
    	spinner = (Spinner) findViewById(R.id.itemCategory);
    	
    	 // Spinner click listener
        spinner.setOnItemSelectedListener(this);
 
        // Loading spinner data from database
        loadSpinnerData();
        
        expenseNameText = (AutoCompleteTextView) findViewById(R.id.newExpenseNameText);
        loadExpenseNameAutoCompleteData();
        Utilities.showActionBar(this);
        adView = (AdView) findViewById(R.id.insert_expense_ad);
        adView.loadAd(new AdRequest());
        
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
    

    
    public void insert(View view){
       	
    	EditText itemValueText = (EditText) findViewById(R.id.newItemPriceText);
    	String purchaseDateMsg = expDate.getText().toString();
    	String itemNameMsg = expenseNameText.getText().toString();
    	String itemValueMsg = itemValueText.getText().toString();
    	String purchaseCategoryMsg = spinner.getSelectedItem().toString();
    	if (!Utilities.validateDate(purchaseDateMsg))
    	{
    		Toast.makeText(this, "Date is not correct: " + purchaseDateMsg + "! Expense not recorded!", Toast.LENGTH_SHORT).show();
    		return;
    	}
    	
    	db.insertNewExpense(itemNameMsg, itemValueMsg, purchaseDateMsg, purchaseCategoryMsg);
    	
    	Toast.makeText(this, "New expense recorded", Toast.LENGTH_SHORT).show();
    	
    	expenseNameText.setText("");
    	itemValueText.setText("");
    	
    }
    
    public void cancel(View view){
    	db.close();
    	onBackPressed();
    }
    
    private void loadSpinnerData() {
        // database handler
        Cursor cursor = db.getCategories(2);
        // Spinner Drop down elements
        List<String> labels = new ArrayList<String>();
        try
        {
	        // looping through all rows and adding to list
	        if (cursor.moveToFirst())
	        {
	        	while (cursor.isAfterLast() == false)
	        	{       
	        		labels.add(cursor.getString(0));
	        		cursor.moveToNext();
	        	}
	        }
        }
        finally
        {
        	cursor.close();
        }
              
 
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, labels);
 
        // Drop down layout style - list view with radio button
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
 
 
        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
    }
    
    
    private void loadExpenseNameAutoCompleteData() {
        // database handler
        Cursor cursor = db.getPopularExpenseNames();
        // Spinner Drop down elements
        List<String> labels = new ArrayList<String>();
        try
        {
		    // looping through all rows and adding to list
		    if (cursor.moveToFirst())
		    {
		    	while (cursor.isAfterLast() == false)
		    	{       
		    		labels.add(cursor.getString(0));
		    		cursor.moveToNext();
		    	}
		    }
        }
        finally
        {
		    cursor.close();
        }               
         // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.select_dialog_item, labels);
 
        expenseNameText.setAdapter(dataAdapter);
    }

    
    
    
    @Override
	protected void onDestroy() 
    {
    	if (adView != null) 
    	{
    	      adView.destroy();
    	}
    	super.onDestroy();
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) 
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) 
	{
		// TODO Auto-generated method stub
	}
	
	
	@SuppressLint("NewApi")
	public void showDatePickerDialogInsertExpense(View v) 
	{
		//pass date int variables instead of button
	    DialogFragment newFragment = new InnerDatePicker(0); //should use a const
	    newFragment.show(getFragmentManager(), "datePicker");
	}
		
	
	
	@SuppressLint({ "ValidFragment", "NewApi" })
	public class InnerDatePicker extends DialogFragment
	implements DatePickerDialog.OnDateSetListener {
		
		int _startOrEnd; //0 if start, 1 if end 

		public InnerDatePicker(int iStartOrEnd) 
		{
			_startOrEnd=iStartOrEnd;
		}	
	
			
		@SuppressLint("NewApi")
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the current date as the default date in the picker
			final Calendar c = Calendar.getInstance();
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH);
			int day = c.get(Calendar.DAY_OF_MONTH);
			
			// Create a new instance of DatePickerDialog and return it
			return new DatePickerDialog(getActivity(), this, year, month, day);
		}
	
		public void onDateSet(DatePicker view, int ioYear, int ioMonth, int ioDay) {
		// Do something with the date chosen by the user
		}
	
		@Override
		public void onDateSet(android.widget.DatePicker view, int year, int month, int day) 
		{
			expDate.setText(Utilities.getDateString(year, month+1, day, '/'));
		}
	}

	
		

}
