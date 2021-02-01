package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.dbh;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Set;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentMemoryAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentMemoryTransactionDAO;

public class DatabaseHandler extends SQLiteOpenHelper {


    public DatabaseHandler(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    // Called on the first time the database is accessed
    @Override
    public void onCreate(SQLiteDatabase db) {
        String accountTable = "CREATE TABLE account (account_no TEXT PRIMARY KEY, bank_name TEXT NOT NULL, account_holder_name TEXT NOT NULL, balance REAL NOT NULL)";
        String transactionTable = "CREATE TABLE trans (id INTEGER PRIMARY KEY AUTOINCREMENT, date TEXT NOT NULL, account_no TEXT NOT NULL, expense_type TEXT NOT NULL, amount REAL NOT NULL, FOREIGN KEY(account_no) REFERENCES account(account_no))";
        db.execSQL(accountTable);
        db.execSQL(transactionTable);
    }

    // Called when the database version number changes
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        String deleteAccountTable = "DROP TABLE IF EXISTS account";
        String deleteTransactionTable = "DROP TABLE IF EXISTS trans";
        db.execSQL(deleteAccountTable);
        db.execSQL(deleteTransactionTable);
        onCreate(db);
    }

    // Add one record
    public void addOne(String tableName, ContentValues values) {
        SQLiteDatabase db = this.getWritableDatabase();
        long insert = db.insert(tableName, null, values);
        if (insert == -1) {
            //unsuccessful insertion
        } else {
            //successful insertion
        }
    }

    // View records
    public Cursor getRecords(String tableName, ArrayList<String> requiredColumns, ContentValues conditions, Integer limit) {
        //start query build
        String queryString = "SELECT ";

        //add required columns to query
        String[] subQuery1 = new String[requiredColumns.size()];
        Integer index1 = 0;
        for (String column : requiredColumns) {
            subQuery1[index1] = column;
            index1 += 1;
        }
        queryString += TextUtils.join(" , ", subQuery1);

        //add table name to query
        queryString += " FROM " + tableName;

        String[] placeholderValues = new String[0];
        if (conditions.keySet().size() != 0) {
            queryString += " WHERE ";

            //add where conditions to query
            Set<String> keys = conditions.keySet();
            placeholderValues = new String[keys.size()];
            String[] subQuery2 = new String[keys.size()];
            Integer index2 = 0;
            for (String key : keys) {
                subQuery2[index2] = key + " = ?";
                placeholderValues[index2] = conditions.get(key).toString();
                index2 += 1;
            }
            queryString += TextUtils.join(" AND ", subQuery2);
        }

        if (limit != -1) {
            //add limit to query
            queryString += " LIMIT " + limit.toString();
        }

        //end query build
        queryString += ";";

        //query database
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(queryString, placeholderValues);

        return cursor;
    }

    // View records
    public Cursor getRecords(String tableName, ArrayList<String> requiredColumns, ContentValues conditions) {
        return getRecords(tableName, requiredColumns, conditions, -1);
    }

    // Update records
    public Cursor updateRecords(String tableName, ContentValues values, ContentValues conditions) {
        //start query build
        String queryString = "UPDATE " + tableName + " SET ";

        //add column values that are to be updated to query
        Set<String> keys1 = values.keySet();
        String[] subQuery1 = new String[keys1.size()];
        String[] placeholderValues = new String[keys1.size()];
        Integer index1 = 0;
        for (String key : keys1) {
            subQuery1[index1] = key + " = ?";
            placeholderValues[index1] = values.get(key).toString();
            index1 += 1;
        }
        queryString += TextUtils.join(" , ", subQuery1);

        queryString += " WHERE ";

        //add WHERE conditions to query
        Set<String> keys2 = values.keySet();
        String[] subQuery2 = new String[keys2.size()];
        Integer index2 = 0;
        for (String key : keys2) {
            subQuery1[index2] = key + " = ?";
            placeholderValues[index2] = values.get(key).toString();
            index2 += 1;
        }
        queryString += TextUtils.join(" AND ", subQuery2);

        //end query build
        queryString += ";";

        //query database
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(queryString, placeholderValues);

        return cursor;
    }

    // Delete records
    public int deleteRecords(String tableName, ContentValues conditions) {
        String whereClause = null;
        //build where clause
        Set<String> keys = conditions.keySet();
        String[] placeholderValues = new String[0];
        String[] subQuery = new String[keys.size()];
        Integer index = 0;
        for (String key : keys) {
            subQuery[index] = key + " = ?";
            placeholderValues[index] = conditions.get(key).toString();
            index += 1;
        }
        if (index > 0) {
            whereClause = TextUtils.join(" AND ", subQuery);
        }
        //query database
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(tableName, whereClause, placeholderValues);
    }
}
