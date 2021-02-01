package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.Context;
import android.provider.ContactsContract;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.exception.ExpenseManagerException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.dbh.DatabaseHandler;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentMemoryAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentMemoryTransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.ui.MainActivity;

public class PersistentExpenseManager extends ExpenseManager{
    public static final String DB_FILE = "180009k.db";
    private Context context;
    private DatabaseHandler dbh;
    public PersistentExpenseManager(Context context) {
        this.context = context;
        this.dbh = new DatabaseHandler(this.context, DB_FILE, null, 2);
        try {
            this.setup();
        } catch (ExpenseManagerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setup() throws ExpenseManagerException {
        TransactionDAO persistentMemoryTransactionDAO = new PersistentMemoryTransactionDAO(this.dbh);
        setTransactionsDAO(persistentMemoryTransactionDAO);

        AccountDAO persistentMemoryAccountDAO = new PersistentMemoryAccountDAO(this.dbh);
        setAccountsDAO(persistentMemoryAccountDAO);
    }
}
