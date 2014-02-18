package com.dargo.moneytracker.Common;

import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.ViewConfiguration;
import android.widget.Toast;
import au.com.bytecode.opencsv.CSVWriter;

import com.dargo.moneytracker.R;
import com.dargo.moneytracker.Activities.AboutActivity;
import com.dargo.moneytracker.Activities.ExportTableSelector;
import com.dargo.moneytracker.Activities.InsertExpense;
import com.dargo.moneytracker.Activities.InsertIncome;
import com.dargo.moneytracker.Activities.ManageBudget;
import com.dargo.moneytracker.Activities.ManageCategories;
import com.dargo.moneytracker.Activities.ShowExpenses;
import com.dargo.moneytracker.Activities.ShowIncome;
import com.dargo.moneytracker.DataHandling.MoneyDB;


public class Utilities 
{

    static public void showActionBar(Context iContext)
    {
        try {
        	ViewConfiguration config = ViewConfiguration.get(iContext);
        	Field menuKeyField = ViewConfiguration.class
        	.getDeclaredField("sHasPermanentMenuKey");
        	if (menuKeyField != null) 
        	{
        		menuKeyField.setAccessible(true);
        		menuKeyField.setBoolean(config, false);
        	}
        } 
        catch (Exception e) {
        	e.printStackTrace();
        	}
    }
	
	
	static public boolean validateDate(String iDateString)
    {
    	if(iDateString.length() != 10)
    	{
    		return false;
    	}
    	Pattern aDatePattern = Pattern.compile("[\\d]{2}/[\\d]{2}/[\\d]{4}");
    	Matcher aDateMatcher = aDatePattern.matcher(iDateString);
    	if (!aDateMatcher.find())
    	{
    		return false;
    	}
    	return true;
    }
    
    static public Intent clickedOnMainMenu(MenuItem item, Context iContext) {
        switch (item.getItemId()) 
        {
	        case R.id.insert_expense_MI:
	        	return new Intent(iContext, InsertExpense.class);
	    	case R.id.insert_income_MI:
	    		return new Intent(iContext, InsertIncome.class);
	    	case R.id.category_manager_MI:
	    		return new Intent(iContext, ManageCategories.class);
	    	case R.id.export_MI:
	    		return new Intent(iContext, ExportTableSelector.class);
        	case R.id.show_incomes_MI:
	    		return new Intent(iContext, ShowIncome.class);
	    	case R.id.show_expenses_MI:
	    		return new Intent(iContext, ShowExpenses.class);
	    	case R.id.budget_manager_MI:
	    		return new Intent(iContext, ManageBudget.class);
	    	case R.id.about_MI:
	    		return new Intent(iContext, AboutActivity.class);
	    	case android.R.id.home:
	    		return null;
        }
        return null;
    }
    
    static public String getDateString(int iYear, int iMonth, char iDelimiter)
    {
    	
		String monthStr = null;
		String yearStr;
		
		switch (iMonth)
		{
			case 1:
					monthStr="Jan";
					break;
			case 2:
					monthStr="Feb";
					break;	
			case 3:
					monthStr="Mar";
					break;
			case 4:
					monthStr="Apr";
					break;
			case 5:
					monthStr="May";
					break;
			case 6:
					monthStr="Jun";
					break;
			case 7:
					monthStr="Jul";
					break;
			case 8:
					monthStr="Aug";
					break;
			case 9:
					monthStr="Sep";
					break;
			case 10:
					monthStr="Oct";
					break;
			case 11:
					monthStr="Nov";
					break;
			case 12:
					monthStr="Dec";
					break;
		};
		
			
		yearStr=String.valueOf(iYear);
		
		return monthStr + iDelimiter + yearStr;		
   	
    }
    
    static public String getDateString(int iYear, int iMonth, int iDay, char iDelimiter)
    {
    	String dayStr;
		String monthStr;
		String yearStr;
		
		
		if (iMonth<10)
		{
			monthStr="0"+String.valueOf(iMonth);
		}
		else
		{
			monthStr=String.valueOf(iMonth);
		}
		
		if (iDay<10)
		{
			dayStr="0"+String.valueOf(iDay);
		}
		else
		{
			dayStr=String.valueOf(iDay);
		}
		
		yearStr=String.valueOf(iYear);
		
		return monthStr + iDelimiter + dayStr + iDelimiter + yearStr;		
   	
    }
    
    
    static public File exportSQLiteTable2CSV(Context iContext, String iSqlSelection, String iTable2Export)
    {

    	MoneyDB dbhelper = new MoneyDB(iContext);
		File exportDir = new File(Environment.getExternalStorageDirectory(), "");        
		if (!exportDir.exists()) 
		{
		    exportDir.mkdirs();
		}

    	
		Calendar c = Calendar.getInstance();
		
		int aYear = c.get(Calendar.YEAR);
	    int aMonth = c.get(Calendar.MONTH) + 1;
	    int aDay = c.get(Calendar.DAY_OF_MONTH);
		
    	File file = new File(exportDir, iTable2Export + "_" + getDateString(aYear, aMonth, aDay, '-') +".csv");
		try 
		{
		    file.createNewFile();                
		    CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
		    SQLiteDatabase db = dbhelper.getReadableDatabase();
		    Cursor curCSV = db.rawQuery("SELECT export.Name, export.Value, export.Date, meta.CategoryName FROM " + iTable2Export 
		    		+ " export INNER JOIN Categories meta ON export.CategoryID=meta.ID " + iSqlSelection ,null);
		    csvWrite.writeNext(curCSV.getColumnNames());
		    while(curCSV.moveToNext())
		    {
		       //Which column you want to exprort
		        String arrStr[] ={curCSV.getString(0),curCSV.getString(1), curCSV.getString(2), curCSV.getString(3)};
		        csvWrite.writeNext(arrStr);
		    }
		    csvWrite.close();
		    curCSV.close();
		}
		catch(Exception sqlEx)
		{
		    Log.e("MainActivity", sqlEx.getMessage(), sqlEx);
		}
		
    	return file;
    }
    
    
    
    static public Intent sendExportExpensesMail(Context iContext, String iSqlSelection, ArrayList<String> iTables2Export){
		
		
		String ownerEmail = "";
		Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
		Account[] accounts = AccountManager.get(iContext).getAccounts();
		for (Account account : accounts) 
		{
		    if (emailPattern.matcher(account.name).matches()) 
		    {
		    	ownerEmail = account.name;
		        break;
		    }
		}
		
		
		
		
		
		

		Intent intent = new Intent(android.content.Intent.ACTION_SEND_MULTIPLE);
		//intent.setType("text/plain");
		intent.setType("message/rfc822"); 
		//intent.putExtra(Intent.EXTRA_EMAIL, new String[] {"sandor.dargo@gmail.com"});
		intent.putExtra(Intent.EXTRA_EMAIL, new String[] {ownerEmail});
		intent.putExtra(Intent.EXTRA_SUBJECT, "subject here");
		intent.putExtra(Intent.EXTRA_TEXT, "body text");

		ArrayList<Uri> uris = new ArrayList<Uri>();
		Iterator<String> itr = iTables2Export.iterator();
		while(itr.hasNext())
		{
			
			File file = exportSQLiteTable2CSV(iContext, iSqlSelection, itr.next());
			if (!file.exists() || !file.canRead()) {
			    Toast.makeText(iContext, "Attachment Error", Toast.LENGTH_SHORT).show();
			    return null;
			}
			Uri uri = Uri.parse("file://" + file);
			uris.add(uri);
			
		
		
		}   
		intent.putExtra(Intent.EXTRA_STREAM, uris);	
	
		return intent;
	}
      
}