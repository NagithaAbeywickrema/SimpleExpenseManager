package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.database.Cursor;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.dbh.DatabaseHandler;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class PersistentMemoryTransactionDAO implements TransactionDAO {
    public static final String ID_COLUMN = "id";
    public static final String DATE_COLUMN = "Date";
    public static final String ACCOUNT_NO_COLUMN = "account_no";
    public static final String EXPENSE_TYPE_COLUMN = "expense_type";
    public static final String AMOUNT_COLUMN = "amount";
    public static final String TRANSACTION_TABLE = "trans";
    private DatabaseHandler dbh;

    public PersistentMemoryTransactionDAO(DatabaseHandler dbh) {
        this.dbh = dbh;
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        ContentValues transactionDetails = new ContentValues();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        String strDate = dateFormat.format(date);
        transactionDetails.put(DATE_COLUMN, strDate);
        transactionDetails.put(ACCOUNT_NO_COLUMN, accountNo);
        transactionDetails.put(EXPENSE_TYPE_COLUMN, expenseType.toString());
        transactionDetails.put(AMOUNT_COLUMN, amount);
        this.dbh.addOne(TRANSACTION_TABLE, transactionDetails);
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        ArrayList<String> requiredColumns = new ArrayList<>();
        requiredColumns.add(DATE_COLUMN);
        requiredColumns.add(ACCOUNT_NO_COLUMN);
        requiredColumns.add(EXPENSE_TYPE_COLUMN);
        requiredColumns.add(AMOUNT_COLUMN);
        Cursor cursor = this.dbh.getRecords(TRANSACTION_TABLE, requiredColumns, new ContentValues());

        ArrayList<Transaction> transactions = new ArrayList<Transaction>();
        if (cursor.moveToFirst()) {
            do {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
                Date date = null;
                try {
                    date = formatter.parse(cursor.getString(0));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                transactions.add(new Transaction(date, cursor.getString(1), ExpenseType.valueOf(cursor.getString(2)), cursor.getFloat(3)));
            } while (cursor.moveToNext());
        }
        return transactions;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {

        ArrayList<String> requiredColumns = new ArrayList<>();
        requiredColumns.add(DATE_COLUMN);
        requiredColumns.add(ACCOUNT_NO_COLUMN);
        requiredColumns.add(EXPENSE_TYPE_COLUMN);
        requiredColumns.add(AMOUNT_COLUMN);
        Cursor cursor = this.dbh.getRecords(TRANSACTION_TABLE, requiredColumns, new ContentValues(), limit);

        ArrayList<Transaction> transactions = new ArrayList<Transaction>();
        if (cursor.moveToFirst()) {
            do {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
                Date date = null;
                try {
                    date = formatter.parse(cursor.getString(0));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                transactions.add(new Transaction(date, cursor.getString(1), ExpenseType.valueOf(cursor.getString(2)), cursor.getFloat(3)));
            } while (cursor.moveToNext());
        }
        return transactions;
    }

}
