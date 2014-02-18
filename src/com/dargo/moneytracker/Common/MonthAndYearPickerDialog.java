package com.dargo.moneytracker.Common;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.dargo.moneytracker.R;

public class MonthAndYearPickerDialog extends DialogFragment {

	
Button myOKButton;
Button myCancelButton;

Spinner myYearSpinner;
Spinner myMonthSpinner;

EditNameDialogListener activity;

public interface EditNameDialogListener {
    void onFinishEditDialog(int iYear, int iMonth);
}

public MonthAndYearPickerDialog(){
    // Empty constructor required for DialogFragment
}

@Override
public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    
	activity = (EditNameDialogListener) getActivity();
	
	//Inflate the XML view for the help dialog fragment
    View view = inflater.inflate(R.layout.month_and_year_picker_dialog, container);
    
    
    //get the OK button and add a Listener
    myOKButton = (Button) view.findViewById(R.id.confirmationDialogButton);
    myOKButton.setOnClickListener(new OnClickListener() {
        public void onClick(View v) {
             // When button is clicked, call up to owning activity.
        	int aYear = Integer.parseInt(myYearSpinner.getSelectedItem().toString());
        	int aMonth = convertMonth2Int(myMonthSpinner.getSelectedItem().toString());
        	activity.onFinishEditDialog(aYear, aMonth);
        	MonthAndYearPickerDialog.this.dismiss();
         }

	
     });
    
    myCancelButton = (Button) view.findViewById(R.id.cancelDialogButton);
    myCancelButton.setOnClickListener(new OnClickListener() {
        public void onClick(View v) {
             // When button is clicked, call up to owning activity.
        	MonthAndYearPickerDialog.this.dismiss();
         }

	
     });
    
    myYearSpinner = (Spinner) view.findViewById(R.id.budgetPeriodYearSpinner);
	
	 // Spinner click listener
    myYearSpinner.setOnItemSelectedListener(null);

   // Loading spinner data from database
    loadYearSpinnerData();

   myMonthSpinner = (Spinner) view.findViewById(R.id.budgetPeriodMonthSpinner);
	
	 // Spinner click listener
   myMonthSpinner.setOnItemSelectedListener(null);

  // Loading spinner data from database
  loadMonthSpinnerData();


    

    return view;
}



@Override
public Dialog onCreateDialog(Bundle savedInstanceState) {
  Dialog dialog = super.onCreateDialog(savedInstanceState);

  // request a window without the title
  dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
  return dialog;
}

private int convertMonth2Int(String iMonth)
{
	int aResult = 0;
	if (iMonth == "Jan") {aResult=1;}
	if (iMonth == "Feb") {aResult=2;}
	if (iMonth == "Mar") {aResult=3;}
	if (iMonth == "Apr") {aResult=4;}
	if (iMonth == "May") {aResult=5;}
	if (iMonth == "Jun") {aResult=6;}
	if (iMonth == "Jul") {aResult=7;}
	if (iMonth == "Aug") {aResult=8;}
	if (iMonth == "Sep") {aResult=9;}
	if (iMonth == "Oct") {aResult=10;}
	if (iMonth == "Nov") {aResult=11;}
	if (iMonth == "Dec") {aResult=12;}
	return aResult;
	
}


private void loadMonthSpinnerData() {
    // database handler
    
    // Spinner Drop down elements
    List<String> aMonthLabelList = new ArrayList<String>();
    // looping through all rows and adding to list
    
    aMonthLabelList.add("Jan");
    aMonthLabelList.add("Feb");
    aMonthLabelList.add("Mar");
    aMonthLabelList.add("Apr");
    aMonthLabelList.add("May");
    aMonthLabelList.add("Jun");
    aMonthLabelList.add("Jul");
    aMonthLabelList.add("Aug");
    aMonthLabelList.add("Sep");
    aMonthLabelList.add("Oct");
    aMonthLabelList.add("Nov");
    aMonthLabelList.add("Dec");
          

    // Creating adapter for spinner
    ArrayAdapter<String> aMonthDataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, aMonthLabelList);

    // Drop down layout style - list view with radio button
    aMonthDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    // attaching data adapter to spinner
    myMonthSpinner.setAdapter(aMonthDataAdapter);
}


private void loadYearSpinnerData() {
    // database handler
    
    // Spinner Drop down elements
    List<String> aYearLabelList = new ArrayList<String>();
    // looping through all rows and adding to list
   
    
    
    for (int i = 0; i<100; i++)
    {
    	final Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		aYearLabelList.add(String.valueOf(year+1-i));
    	
    }
          

    // Creating adapter for spinner
    ArrayAdapter<String> aYearDataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, aYearLabelList);

    // Drop down layout style - list view with radio button
    aYearDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    // attaching data adapter to spinner
    myYearSpinner.setAdapter(aYearDataAdapter);
}


}
