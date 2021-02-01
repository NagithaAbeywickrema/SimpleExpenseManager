package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.animation.ObjectAnimator;
import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.dbh.DatabaseHandler;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

public class PersistentMemoryAccountDAO implements AccountDAO {
    public static final String ACCOUNT_TABLE = "account";
    public static final String ACCOUNT_NO_COLUMN = "account_no";
    public static final String BANK_NAME_COLUMN = "bank_name";
    public static final String ACCOUNT_HOLDER_NAME_COLUMN = "account_holder_name";
    public static final String BALANCE_COLUMN = "balance";
    private DatabaseHandler dbh;

    public PersistentMemoryAccountDAO(DatabaseHandler dbh) {
        this.dbh = dbh;
    }

    @Override
    public List<String> getAccountNumbersList() {
        ArrayList<String> requiredColumns = new ArrayList<>();
        requiredColumns.add(ACCOUNT_NO_COLUMN);
        Cursor cursor = this.dbh.getRecords(ACCOUNT_TABLE, requiredColumns, new ContentValues());

        ArrayList<String> accountNumberList = new ArrayList<String>();
        if (cursor.moveToFirst()) {
            do {
                accountNumberList.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        return accountNumberList;
    }

    @Override
    public List<Account> getAccountsList() {
        ArrayList<String> requiredColumns = new ArrayList<>();
        requiredColumns.add(ACCOUNT_NO_COLUMN);
        requiredColumns.add(BANK_NAME_COLUMN);
        requiredColumns.add(ACCOUNT_HOLDER_NAME_COLUMN);
        requiredColumns.add(BALANCE_COLUMN);
        Cursor cursor = this.dbh.getRecords(ACCOUNT_TABLE, requiredColumns, new ContentValues());

        ArrayList<Account> accounts = new ArrayList<Account>();
        if (cursor.moveToFirst()) {
            do {
                accounts.add(new Account(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getFloat(3)));
            } while (cursor.moveToNext());
        }
        return accounts;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        ArrayList<String> requiredColumns = new ArrayList<>();
        requiredColumns.add(ACCOUNT_NO_COLUMN);
        requiredColumns.add(BANK_NAME_COLUMN);
        requiredColumns.add(ACCOUNT_HOLDER_NAME_COLUMN);
        requiredColumns.add(BALANCE_COLUMN);
        ContentValues conditions = new ContentValues();
        conditions.put(ACCOUNT_NO_COLUMN, accountNo);
        Cursor cursor = this.dbh.getRecords(ACCOUNT_TABLE, requiredColumns, conditions);

        ArrayList<Account> accounts = new ArrayList<Account>();
        if (cursor.moveToFirst()) {
            return new Account(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getFloat(3));
        }

        String msg = "Account " + accountNo + " is invalid.";
        throw new InvalidAccountException(msg);
    }

    @Override
    public void addAccount(Account account) {
        ContentValues accountDetails = new ContentValues();
        accountDetails.put(ACCOUNT_NO_COLUMN, account.getAccountNo());
        accountDetails.put(BANK_NAME_COLUMN, account.getBankName());
        accountDetails.put(ACCOUNT_HOLDER_NAME_COLUMN, account.getAccountHolderName());
        accountDetails.put(BALANCE_COLUMN, account.getBalance());
        this.dbh.addOne(ACCOUNT_TABLE, accountDetails);
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        ContentValues conditions = new ContentValues();
        conditions.put(ACCOUNT_NO_COLUMN, accountNo);
        if (this.dbh.deleteRecords(ACCOUNT_TABLE, conditions) == 0) {
            String msg = "Account " + accountNo + " is invalid.";
            throw new InvalidAccountException(msg);
        }
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {

        Account account = this.getAccount(accountNo);
        // specific implementation based on the transaction type
        switch (expenseType) {
            case EXPENSE:
                account.setBalance(account.getBalance() - amount);
                break;
            case INCOME:
                account.setBalance(account.getBalance() + amount);
                break;
        }

        ContentValues values = new ContentValues();
        values.put(BALANCE_COLUMN, account.getBalance());
        ContentValues conditions = new ContentValues();
        conditions.put(ACCOUNT_NO_COLUMN, accountNo);
        this.dbh.updateRecords(ACCOUNT_TABLE, values, conditions);
    }
}
