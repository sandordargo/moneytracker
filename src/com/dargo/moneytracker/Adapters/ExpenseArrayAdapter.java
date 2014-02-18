package com.dargo.moneytracker.Adapters;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.dargo.moneytracker.R;
import com.dargo.moneytracker.DataHandling.ExpenseData;
import com.dargo.moneytracker.DataHandling.MoneyDB;

public class ExpenseArrayAdapter extends ArrayAdapter<ExpenseData> {
   	private ArrayList<ExpenseData> items;
    private Context context;
    MoneyDB db;

    public ExpenseArrayAdapter(Context context, int textViewResourceId,
    		ArrayList<ExpenseData>  items) {
        super(context, textViewResourceId, items);
        this.context = context;
        this.items = items;
        db = new MoneyDB(super.getContext());
      }

    @SuppressLint("UseValueOf")
	public View getView(final int position, View convertView,
            ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.show_expense_activity, null);
            																						
        }																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																												
        TextView itemView = (TextView) view.findViewById(R.id.showItem);
        TextView itemValue = (TextView) view.findViewById(R.id.showValue);
        TextView purchaseDate = (TextView) view.findViewById(R.id.showDate);
        
        itemView.setText(items.get(position).itemName);
        itemValue.setText(new Double(items.get(position).itemValue).toString());
        purchaseDate.setText(items.get(position).purchaseDate);
        
        final Button aDelButton = (Button) view.findViewById(R.id.deleteExpenseFromList);
        aDelButton.setOnClickListener(new OnClickListener() 
        {
   		
	   		@Override
	   		public void onClick(View v) 
	   		{
	   			// TODO Auto-generated method stub
	   			db.deleteExpense(String.valueOf(items.get(position).id));
	   			remove(items.get(position));
	   			notifyDataSetChanged();
	   		}
        }
        );
        
        
        
    return view;
    }
}