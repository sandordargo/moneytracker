package com.dargo.moneytracker.DataHandling;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class MoneyDB extends SQLiteAssetHelper {

	private static final String DATABASE_NAME = "moneyDB";
	private static final int DATABASE_VERSION = 3;
	
	public MoneyDB(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);	
		
		// you can use an alternate constructor to specify a database location
		// (such as a folder on the sd card)
		// you must ensure that this folder is available and you have permission
		// to write to it
		//super(context, DATABASE_NAME, context.getExternalFilesDir(null).getAbsolutePath(), null, DATABASE_VERSION);
		
		// call this method to force a database overwrite if the version number 
		// is below a certain threshold
		//setForcedUpgradeVersion(2);
	}

	public Cursor getExpenses() 
	{
		SQLiteDatabase aDb = getReadableDatabase();
		SQLiteQueryBuilder aQb = new SQLiteQueryBuilder();

		String [] aSqlSelect = {"ID, Name, Value, Date"}; 
		String aSqlTables = "Expenses";
		String aOrderBy = "Date ASC";

		aQb.setTables(aSqlTables);
		Cursor aCursor = aQb.query(aDb, aSqlSelect, null, null,
				null, null, aOrderBy);

		aCursor.moveToFirst();
		return aCursor;
	}
	
	
	public Cursor getPeriodExpenses(String iStartDate, String iEndDate) 
	{
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

	
		String [] sqlSelect = {"ID, Name, Value, Date"}; 
		String sqlTables = "Expenses";
		String orderBy = "Date ASC";
		String sqlSelection = "Date > '" + iStartDate + "' AND Date < '" + iEndDate + "'";
		qb.setTables(sqlTables);
		Cursor c = qb.query(db, sqlSelect, sqlSelection, null,
				null, null, orderBy);

		c.moveToFirst();
		return c;
	}
	
	public Cursor getCategoryPeriodExpenses(String iStartDate, String iEndDate, String iCategory) 
	{
		SQLiteDatabase db = getReadableDatabase();
		String aQuery =   " SELECT exp.ID, exp.Name, exp.Value, exp.DATE "
						+ " FROM Expenses exp INNER JOIN Categories cat ON exp.CategoryID=cat.ID "
						+ " WHERE exp.Date > '" + iStartDate + "' AND exp.Date < '" + iEndDate + "' AND cat.CategoryName LIKE '" + iCategory + "'";
				
		Cursor c = db.rawQuery(aQuery, null);
		c.moveToFirst();
		return c;

	}

	public Cursor getCategoryPeriodIncome(String iStartDate, String iEndDate, String iCategory) 
	{
		SQLiteDatabase db = getReadableDatabase();
		String aQuery =   " SELECT inc.ID, inc.Name, inc.Value, inc.DATE "
				+ " FROM Income inc INNER JOIN Categories cat ON inc.CategoryID=cat.ID "
				+ " WHERE inc.Date > '" + iStartDate + "' AND inc.Date < '" + iEndDate + "' AND cat.CategoryName LIKE '" + iCategory + "'";
		
		Cursor c = db.rawQuery(aQuery, null);
		
		c.moveToFirst();
		return c;
	}

	
	public Cursor getCategories() 
	{
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

		String [] sqlSelect = {"ID, CategoryName, ExpInc"}; 
		String sqlTables = "Categories";
		String orderBy = "CategoryName ASC";

		qb.setTables(sqlTables);
		Cursor c = qb.query(db, sqlSelect, null, null,
				null, null, orderBy);

		c.moveToFirst();
		return c;
	}
	
	
	public Cursor getTables2Export() 
	{
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

		String [] sqlSelect = {"ID, TableName"};
		String sqlSelection = "Exported = 1";
		String sqlTables = "TablesMeta";
		String orderBy = "TableName ASC";

		qb.setTables(sqlTables);
		Cursor c = qb.query(db, sqlSelect, sqlSelection, null,
				null, null, orderBy);

		c.moveToFirst();
		return c;
	}


	public Cursor getPopularExpenseNames()
	{
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		String [] sqlSelect = {"Name, count(Name) as counter"}; 
		String sqlTables = "Expenses";
		String groupBy = "Name";
		String orderBy = "counter desc";
		String limit = "50";
		qb.setTables(sqlTables);
		Cursor c = qb.query(db, sqlSelect, null, null,
				groupBy, null, orderBy, limit); 
		c.moveToFirst();
		
		return c;
	}
	
	public Cursor getPopularIncomeNames()
	{
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		String [] sqlSelect = {"Name, count(Name) as counter"}; 
		String sqlTables = "Income";
		String groupBy = "Name";
		String orderBy = "counter desc";
		String limit = "50";
		qb.setTables(sqlTables);
		Cursor c = qb.query(db, sqlSelect, null, null,
				groupBy, null, orderBy, limit); 
		c.moveToFirst();
		
		return c;
	}


	public Cursor getCategories(int iCategoryType) 
	{
		//include where clause to differentiate between exp/inc tables
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		String [] sqlSelect = {"CategoryName"}; 
		String sqlTables = "Categories";
		String sqlSelection = "";
		String orderBy = "";

		switch(iCategoryType)
		{
		case 1:
				sqlSelection="ExpInc = 1 OR ExpInc = 3"; //income
				orderBy = "IncomeCounter DESC, CategoryName ASC";
				break;
		case 2:
				sqlSelection="ExpInc = 2 OR ExpInc = 3"; //expenses
				orderBy = "ExpensesCounter DESC, CategoryName ASC";
				break;
		default:
				break;
		
		}		
		qb.setTables(sqlTables);
		Cursor c = qb.query(db, sqlSelect, sqlSelection, null,
				null, null, orderBy); 
		c.moveToFirst();
		return c;
	}
	

	public Cursor getBudgetItems(String iExpInc, String iPeriodStartDate) 
	{
		String aTable = null;
		switch (Integer.parseInt(iExpInc))
		{
		case 1:
				aTable = "Income";
				break;
		case 2:
				aTable = "Expenses";
				break;
		};
		
		SQLiteDatabase db = getReadableDatabase();
		String aQuery =   " SELECT bud.ID, cat.CategoryName, bud.Budget, "
						+ "		(SELECT sum(items.Value) "
						+ "		 FROM " + aTable + " items "
						+ "	 	 WHERE bud.CategoryID = items.CategoryID) as totalOutput "
						+ " FROM Budget bud INNER JOIN Categories cat ON bud.CategoryID=cat.ID "
						+ " WHERE bud.PeriodStart = '" + iPeriodStartDate + "' AND bud.ExpInc = " + iExpInc;
				
		Cursor c = db.rawQuery(aQuery, null);
		c.moveToFirst();
		return c;
	}

	
	public Cursor getIncomes() 
	{
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

		String [] sqlSelect = {"ID, Name, Value, Date"}; 
		String sqlTables = "Income";
		String orderBy = "Date ASC";

		qb.setTables(sqlTables);
		Cursor c = qb.query(db, sqlSelect, null, null,
				null, null, orderBy);

		c.moveToFirst();
		return c;
	}

	
	public void insertNewExpense(String itemName, String itemValue, String purchaseDate, String expenseCategory)
	{
		SQLiteDatabase db = getWritableDatabase();
		String sql = "INSERT INTO Expenses (Name, Value, Date, CategoryID) SELECT "
					+ "\""+ itemName + "\"," + itemValue + ",\"" + purchaseDate +  "\", ID FROM Categories"
					+ " WHERE categoryName = \""+ expenseCategory + "\"";
		db.execSQL(sql);
	}
	
	public void insertIncome(String incomeName, String incomeValue, String incomeDate, String incomeCategory)
	{
		SQLiteDatabase db = getWritableDatabase();
		String sql = "INSERT INTO Income (Name, Value, Date, CategoryID) SELECT "
				+ "\""+ incomeName + "\"," + incomeValue + ",\"" + incomeDate + "\", ID FROM Categories "
						+ " WHERE categoryName = \""+ incomeCategory + "\"";
		db.execSQL(sql);
	}
	
	public void insertCategory(String iCategoryName, String iExpInc)
	{
		SQLiteDatabase db = getWritableDatabase();
		String sql = "INSERT INTO Categories (CategoryName, ExpInc) VALUES "
				+ "(\""+ iCategoryName + "\",\"" + iExpInc + "\")";
		db.execSQL(sql);
	}
	
	
	public void insertBudgetItem(String iBudgetItemName, String iBudgetValue, String iExpInc, String iPeriodStart)
	{
		SQLiteDatabase db = getWritableDatabase();
		String sql = "INSERT INTO Budget (CategoryID, ExpInc, PeriodStart, Budget) SELECT ID, "
						+ ""+ iExpInc + ",\"" + iPeriodStart + "\"," + iBudgetValue + " FROM Categories "
						+ " WHERE categoryName = \""+ iBudgetItemName + "\"";
		db.execSQL(sql);
	}
	

	public void deleteCategory(String iCategoryName)
	{
		SQLiteDatabase db = getWritableDatabase();
		String sql = "DELETE FROM Categories WHERE CategoryName = "
				+ "(\""+ iCategoryName + "\")";
		db.execSQL(sql);
	}

	public void deleteBudgetItem(String iID)
	{
		SQLiteDatabase db = getWritableDatabase();
		String sql = "DELETE FROM Budget WHERE ID = "
				+ iID;
		db.execSQL(sql);
	}

	
	public void deleteExpense(String iID)
	{
		SQLiteDatabase db = getWritableDatabase();
		String sql = "DELETE FROM Expenses WHERE ID = "
					+  iID;
		db.execSQL(sql);
	}


	public void deleteIncome(String iID)
	{
		SQLiteDatabase db = getWritableDatabase();
		String sql = "DELETE FROM Income WHERE ID = "
					+ "("+ iID + ")";
		db.execSQL(sql);
	}

	public void updateCategory(String iCatID, String iCatExpInc)
	{
		SQLiteDatabase db = getWritableDatabase();
		String sql = "UPDATE Categories SET ExpInc = " + iCatExpInc + " WHERE ID = " + iCatID;
		db.execSQL(sql);
	}
	
	public void updateCategoryName(String iCatID, String iCatName)
	{
		SQLiteDatabase db = getWritableDatabase();
		String sql = "UPDATE Categories SET CategoryName = '" + iCatName + "' WHERE ID = " + iCatID;
		db.execSQL(sql);
	}

	public void updateBudgetItemValue(String iBudgetID, String iBudgetValue)
	{
		SQLiteDatabase db = getWritableDatabase();
		String sql = "UPDATE Budget SET Budget = " + iBudgetValue + " WHERE ID = " + iBudgetID;
		db.execSQL(sql);
	}
	
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
	{
		try{
		db.execSQL("DROP TRIGGER IF EXISTS insert_new_expense");
		db.execSQL("DROP TRIGGER IF EXISTS insert_new_income");
		db.execSQL("ALTER TABLE Income RENAME TO TmpIncome");
		db.execSQL("ALTER TABLE Expenses RENAME TO TmpExpenses");
				
		db.execSQL("CREATE TABLE Income (ID INTEGER PRIMARY KEY, Name TEXT NOT NULL DEFAULT 'something', "
				+ "Value NUMERIC NOT NULL DEFAULT '0', Date DATE NOT NULL DEFAULT (date()), CategoryID Integer, "
				+ "FOREIGN KEY (CategoryID) REFERENCES Categories(ID) );");
		
		db.execSQL("CREATE TRIGGER insert_new_income INSERT ON Income " 
				+ "BEGIN UPDATE Categories SET IncomeCounter = IncomeCounter + 1  "
				+ "WHERE Categories.ID = new.CategoryID; END;");
		
		db.execSQL("CREATE TABLE Expenses (ID INTEGER PRIMARY KEY, Name TEXT NOT NULL DEFAULT 'something', "
				+ "Value NUMERIC NOT NULL DEFAULT '0', Date DATE NOT NULL DEFAULT (date()), "
				+ "CategoryID INTEGER, FOREIGN KEY (CategoryID) REFERENCES Categories(ID));");
		
		
		db.execSQL("CREATE TRIGGER insert_new_expense INSERT ON Expenses"
				+ " BEGIN UPDATE Categories SET ExpensesCounter = ExpensesCounter + 1 "
				+ " WHERE Categories.ID = new.CategoryID; END;  ");
		
		db.execSQL("INSERT INTO Income (Name, Value, Date, CategoryID) SELECT Name, Value, Date, cat.ID FROM TmpIncome INNER JOIN Categories cat ON TmpIncome.Category=cat.CategoryName ");
		db.execSQL("INSERT INTO Expenses (Name, Value, Date, CategoryID) SELECT Name, Value, Date FROM, cat.ID TmpExpenses INNER JOIN Categories cat ON TmpExpenses.Category=cat.CategoryName ");

		db.execSQL("DROP TABLE IF EXISTS TmpIncome");
		db.execSQL("DROP TABLE IF EXISTS TmpExpenses");
		}
		catch(Exception ex)
		{
			
		}
	}


}


	

