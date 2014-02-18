package com.dargo.moneytracker.Common;

import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.Button;

import com.dargo.moneytracker.DataHandling.EntryDate;

@SuppressLint("ValidFragment")
public class DatePicker extends DialogFragment
implements DatePickerDialog.OnDateSetListener {

	Button myButton;
	EntryDate myStartDate;
	EntryDate myEndDate;
	
public DatePicker() 
{
    
}	
	
public DatePicker(Button button) 
{
	myButton = button;
	
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
public void onDateSet(android.widget.DatePicker view, int year, int month,
					  int day) {
	// TODO Auto-generated method stub
	myButton.setText(String.valueOf(month + 1 ) + "/" +   String.valueOf(day) + "/" + String.valueOf(year));
	
}
}