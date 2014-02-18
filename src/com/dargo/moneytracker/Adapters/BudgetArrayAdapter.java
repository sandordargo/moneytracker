package com.dargo.moneytracker.Adapters;

import java.text.DecimalFormat;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dargo.moneytracker.R;
import com.dargo.moneytracker.DataHandling.BudgetData;
import com.dargo.moneytracker.DataHandling.MoneyDB;

public class BudgetArrayAdapter extends ArrayAdapter<BudgetData> {
   	private ArrayList<BudgetData> myBudgetItems;
    private Context context;
    MoneyDB db;

    public BudgetArrayAdapter(Context context, int textViewResourceId, ArrayList<BudgetData>  iBudgetItems) 
    {
        super(context, textViewResourceId, iBudgetItems);
        this.context = context;
        this.myBudgetItems = iBudgetItems;
        db = new MoneyDB(super.getContext());
    }

    @SuppressLint("UseValueOf")
	public View getView(final int position, View convertView, ViewGroup parent) 
    {
    	View view = convertView;
        if (view == null) 
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.manage_budget, null);
        }
        
        TextView itemName = (TextView) view.findViewById(R.id.showBudgetItemName);
        final EditText itemValue = (EditText) view.findViewById(R.id.showBudgetItemValue);
        TextView totalOutputET = (TextView) view.findViewById(R.id.showBudgetItemSpentPerc);
        
        itemName.setText(myBudgetItems.get(position).budgetName);
        itemValue.setText(String.valueOf(myBudgetItems.get(position).budgetOrigValue)); 
        //handle decimals, and infinity
        double totalOutputPercentage = 0;
        if (myBudgetItems.get(position).budgetOrigValue != 0)
        {
        	totalOutputPercentage = myBudgetItems.get(position).totalOutput/myBudgetItems.get(position).budgetOrigValue*100;
        	DecimalFormat aDecimalFormat = new DecimalFormat("#.##");
            totalOutputET.setText(String.valueOf(aDecimalFormat.format(totalOutputPercentage))+"%");
        }
        else
        {
        	
        	totalOutputET.setText("-");
        }
        
        itemValue.setOnFocusChangeListener(new OnFocusChangeListener() 
        {          
            public void onFocusChange(View v, boolean hasFocus) 
            {
                if(!hasFocus) 
                {
                    //SAVE THE DATA 
                	db.updateBudgetItemValue(String.valueOf(myBudgetItems.get(position).id), itemValue.getText().toString());
                	myBudgetItems.get(position).budgetOrigValue = Integer.parseInt(itemValue.getText().toString());
                	notifyDataSetChanged();
                }  
            }
        });
        
        
                
        final Button aDelButton = (Button) view.findViewById(R.id.deleteBudgetItem);
        aDelButton.setOnClickListener(new OnClickListener() 
        {
   		
	   		@Override
	   		public void onClick(View v) 
	   		{
	   			// TODO Auto-generated method stub
	   			db.deleteBudgetItem(String.valueOf(myBudgetItems.get(position).id));
	   			remove(myBudgetItems.get(position));
	   			notifyDataSetChanged();
	   		}
        }
        );
        
        
     return view;
    }
    
     
}