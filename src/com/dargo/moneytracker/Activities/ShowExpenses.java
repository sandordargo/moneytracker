package com.dargo.moneytracker.Activities;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.dargo.moneytracker.R;
import com.dargo.moneytracker.Adapters.ExpenseArrayAdapter;
import com.dargo.moneytracker.Common.Utilities;
import com.dargo.moneytracker.DataHandling.ExpenseData;
import com.dargo.moneytracker.DataHandling.MoneyDB;
import com.google.ads.AdRequest;
import com.google.ads.AdView;

public class ShowExpenses extends ListActivity {

	MoneyDB db;

	int aPeriodBeginYear = 0, aPeriodBeginMonth = 0, aPeriodBeginDay = 0;
	int aPeriodEndYear = 0, aPeriodEndMonth = 0, aPeriodEndDay = 0;
	String aStartDate = "01/01/1900";
	String aEndDate = "12/31/2099";
	ArrayList<ExpenseData> listData;

	Spinner categorySpinner;
	
	private AdView adView;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		db = new MoneyDB(this);
		listData = new ArrayList<ExpenseData>();
		initView();
		Utilities.showActionBar(this);
		categorySpinner = (Spinner) findViewById(R.id.selectCategory);
		categorySpinner
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {

						String aSelectedCategory = categorySpinner
								.getSelectedItem().toString();
						if (aSelectedCategory == "All") {
							showList(aStartDate, aEndDate, "%");
						} else {
							showList(aStartDate, aEndDate, aSelectedCategory);
						}

					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub

					}

				});
		// Loading spinner data from database
		loadSpinnerData();
		
		adView = (AdView) findViewById(R.id.show_expense_ad);
        adView.loadAd(new AdRequest());

	}

	private void loadSpinnerData() {
		// database handler
		Cursor cursor = db.getCategories(2);
		// Spinner Drop down elements
		List<String> labels = new ArrayList<String>();
		labels.add("All");
		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
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
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		// attaching data adapter to spinner
		categorySpinner.setAdapter(dataAdapter);

	}

	private void initView() {

		ListView listView = getListView();

		View header = getLayoutInflater().inflate(R.layout.show_expense_header,
				null);
		View footer = getLayoutInflater().inflate(R.layout.show_expense_footer,
				null);

		listView.addHeaderView(header);
		listView.addFooterView(footer);
		showList(aStartDate, aEndDate);
	}

	private void showList() 
	{
		showList("", "");
	}

	private void showList(String iStartDate, String iEndDate) 
	{
		showList(iStartDate, iEndDate, "%");
	}

	private void showList(String iStartDate, String iEndDate, String iCategory) {
		listData.clear();
		Cursor getDataCursor = db.getCategoryPeriodExpenses(iStartDate,
				iEndDate, iCategory);
		if (getDataCursor.moveToFirst()) 
		{
			try
			{
				while (getDataCursor.isAfterLast() == false) {
					ExpenseData temp = new ExpenseData();
					temp.id = getDataCursor.getInt(0);
					temp.itemName = getDataCursor.getString(1);
					temp.itemValue = getDataCursor.getDouble(2);
					temp.purchaseDate = getDataCursor.getString(3);
					listData.add(temp);
					temp = null;
					getDataCursor.moveToNext();
				}
			}
			finally
			{
				getDataCursor.close();
			}
		}

		setListAdapter(new ExpenseArrayAdapter(this,
				R.layout.show_expense_activity, listData));
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {

		// get selected items

	}

	public void showAllEntries(View v) 
	{
		TextView aTV = (TextView) findViewById(R.id.ShowWhatTV);
		aTV.setText("Show all");

		showList();
	}

	public void changeExpPeriod(View v) {

		Intent intentChangePeriod = new Intent(this, ChangeExpensePeriod.class);
		startActivityForResult(intentChangePeriod, 1);
	}

	public String getProperDate(int iDatePart) {
		String returnStr = "";

		if (iDatePart < 10) {
			returnStr = "0" + String.valueOf(iDatePart);
		} else {
			returnStr = String.valueOf(iDatePart);
		}

		return returnStr;

	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		int aPeriodBeginYear = 0, aPeriodBeginMonth = 0, aPeriodBeginDay = 0;
		int aPeriodEndYear = 0, aPeriodEndMonth = 0, aPeriodEndDay = 0;

		if (requestCode == 1) {

			if (resultCode == RESULT_OK) {
				aPeriodBeginYear = data.getIntExtra("aPeriodBeginYear", -1);
				aPeriodBeginMonth = data.getIntExtra("aPeriodBeginMonth", -1);
				aPeriodBeginDay = data.getIntExtra("aPeriodBeginDay", -1);
				aPeriodEndYear = data.getIntExtra("aPeriodEndYear", -1);
				aPeriodEndMonth = data.getIntExtra("aPeriodEndMonth", -1);
				aPeriodEndDay = data.getIntExtra("aPeriodEndDay", -1);

				TextView aTV = (TextView) findViewById(R.id.ShowWhatTV);

				aStartDate = String.valueOf(aPeriodBeginYear) + "-"
						+ getProperDate(aPeriodBeginMonth + 1) + "-"
						+ getProperDate(aPeriodBeginDay);
				aEndDate = String.valueOf(aPeriodEndYear) + "-"
						+ getProperDate(aPeriodEndMonth + 1) + "-"
						+ getProperDate(aPeriodEndDay);

				aTV.setText("Expenses from " + aStartDate + " to " + aEndDate);
				showList(aStartDate, aEndDate);

			}
			if (resultCode == RESULT_CANCELED) {
				// Write your code if there's no result
			}
		}
	}

	public void deleteExpenseFromList(View view) {
		// dummy method: handler in adapter class
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
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
