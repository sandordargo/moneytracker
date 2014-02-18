package com.dargo.moneytracker.Activities;

import java.util.ArrayList;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.dargo.moneytracker.R;
import com.dargo.moneytracker.Adapters.CategoryArrayAdapter;
import com.dargo.moneytracker.Common.Utilities;
import com.dargo.moneytracker.DataHandling.CategoriesData;
import com.dargo.moneytracker.DataHandling.MoneyDB;
import com.google.ads.AdRequest;
import com.google.ads.AdView;

public class ManageCategories extends ListActivity {
	 
	  MoneyDB db;
	  ArrayList<CategoriesData> listData;
	  CategoryArrayAdapter myAdapter;
	  private AdView adView;
	
	  @Override
	  	public void onCreate(Bundle savedInstanceState) 
	  	{
		  super.onCreate(savedInstanceState);
		  db = new MoneyDB(this);
		  listData = new ArrayList<CategoriesData>();
		  initView();
		  Utilities.showActionBar(this);
		  		  		  
	  	}
	  
	  	private void initView()
	  	{
	  		View header = getLayoutInflater().inflate(R.layout.manage_categories_header, null);
	        View footer = getLayoutInflater().inflate(R.layout.manage_categories_footer, null);
	        ListView listView = getListView();
	               
	        listView.addHeaderView(header);
	        listView.addFooterView(footer);
	        
	        adView = (AdView) findViewById(R.id.manage_categories_ad);
	        adView.loadAd(new AdRequest());
	      
	  		showList();
	  	}
	  
		private void showList()
		{
			listData.clear();
	        Cursor getCatCursor = db.getCategories(); 
	        if (getCatCursor.moveToFirst())
	        {
	        	while (getCatCursor.isAfterLast() == false)
	        	{
	        		CategoriesData temp = new CategoriesData();
		        	temp.id = getCatCursor.getInt(0);
		        	temp.catName = getCatCursor.getString(1);
		        	temp.expInc = getCatCursor.getInt(2);
		        	temp.isChanged = false;
		        	temp.origCatName = temp.catName;
		        	temp.origExpInc = temp.origExpInc;
		        	listData.add(temp);
		        	temp = null;
		        	getCatCursor.moveToNext();
	        	}
	        	getCatCursor.close();	
	        }
	        																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																		
	        
	        this.myAdapter = new CategoryArrayAdapter(this, R.layout.manage_categories, listData);
	        setListAdapter(myAdapter);
       
		}
		
		
		public void addNewCategory(View view)
		{
			EditText aNewCatET = (EditText) findViewById(R.id.newCategoryName);
			CheckBox aNewIncomeCatCB = (CheckBox) findViewById(R.id.newIncomeCategory);
			CheckBox aNewExpenseCatCB = (CheckBox) findViewById(R.id.newExpenseCategory);
			int aExpIncMeta = 0;
			
			if (aNewCatET.toString()=="")
			{
				Toast.makeText(this, "New category is not recorded! Please add a name!", Toast.LENGTH_SHORT).show();
	    		return;
			}
			if (!aNewExpenseCatCB.isChecked() && !aNewIncomeCatCB.isChecked())
			{
				Toast.makeText(this, "New category is not recorded! Is it an income or an expense cateogry?", Toast.LENGTH_SHORT).show();
	    		return;
			}
			
			if (aNewIncomeCatCB.isChecked())
			{
				aExpIncMeta += 1; 
			}
			
			if (aNewExpenseCatCB.isChecked())
			{
				aExpIncMeta += 2;
			}
			
			db.insertCategory(aNewCatET.getText().toString(), String.valueOf(aExpIncMeta));
			aNewCatET.setText("");
			aNewExpenseCatCB.setChecked(false);
			aNewIncomeCatCB.setChecked(false);
			showList();
		}
		
		
	    public void deleteCategory(View view)
	    {
	    	//dummy method: handler in adapter class
	    }
		
		public void onBackPressed()
		{
			myAdapter.onCloseEvent();
			finish();
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
	

