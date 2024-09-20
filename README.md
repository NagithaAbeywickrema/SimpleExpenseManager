# SimpleExpenseManager
This is an android based basic expense manager application which is a lab assignment for CS3042 - Database Systems course module.

## Description
During this assignment we will be self-learning how to use an embedded database in an android application. This project is an android application that act as a skeleton.

The current implementation is non-persistent. Therefore all the account information and transactions are stored in the memory and once the application is closed, the information is lost.

Your task is to make the storage of account information and transactions persistent, using an embedded database such as SQLite as the persistent storage.

In order to achieve this you should implement the two interfaces, [`AccountDAO`](/app/src/main/java/lk/ac/mrt/cse/dbs/simpleexpensemanager/data/AccountDAO.java) and [`TransactionDAO`](/app/src/main/java/lk/ac/mrt/cse/dbs/simpleexpensemanager/data/TransactionDAO.java). These two interfaces follow the “Data Access Object” Design pattern [1](http://www.oracle.com/technetwork/java/dataaccessobject-138824.html) [2](http://www.tutorialspoint.com/design_pattern/data_access_object_pattern.htm). You can refer the current In-Memory implementation ([`InMemoryAccountDAO`](/app/src/main/java/lk/ac/mrt/cse/dbs/simpleexpensemanager/data/impl/InMemoryAccountDAO.java), [`InMemoryTransactionDAO`](/app/src/main/java/lk/ac/mrt/cse/dbs/simpleexpensemanager/data/impl/InMemoryTransactionDAO.java)) to get an idea of the current implementation.

After you have implemented the two interfaces, you can use these implementations to setup the application to use the persistent storage instead of the existing in-memory storage. In order to do that you should implement the `setup()` method of the abstract class [`ExpenseManager`](/app/src/main/java/lk/ac/mrt/cse/dbs/simpleexpensemanager/control/ExpenseManager.java). You can refer the current concrete implementation ([`InMemoryDemoExpenseManager`](/app/src/main/java/lk/ac/mrt/cse/dbs/simpleexpensemanager/control/InMemoryDemoExpenseManager.java)) of this class to get an idea.

After you have completed implementation of the [`ExpenseManager`](/app/src/main/java/lk/ac/mrt/cse/dbs/simpleexpensemanager/control/ExpenseManager.java) class, go to [`MainActivity`](/app/src/main/java/lk/ac/mrt/cse/dbs/simpleexpensemanager/ui/MainActivity.java) class in the ui package and change the existing implementation to your implementation.

eg:

Non-persistent implementation
``` Java
/***  Begin generating dummy data for In-Memory implementation  ***/
expenseManager = new InMemoryDemoExpenseManager();
/*** END ***/
```

Persistent implementation
``` Java
/***  Setup the persistent storage implementation  ***/
expenseManager = new PersistentExpenseManager(context);
/*** END ***/
```

You can make improvements to the project as you require. However this project is designed to act as a skeleton and minimize involvement in other components such as the UI. Therefore your main effort should be focused on implementing the persistent storage using an embedded database.

## Instructions
1. Fork the GitHub project - https://github.com/GayashanNA/SimpleExpenseManager
2. Clone your fork of the above project into your android studio IDE.
3. Implement the two interfaces
  * [`AccountDAO`](/app/src/main/java/lk/ac/mrt/cse/dbs/simpleexpensemanager/data/AccountDAO.java)
  * [`TransactionDAO`](/app/src/main/java/lk/ac/mrt/cse/dbs/simpleexpensemanager/data/TransactionDAO.java)
4. Extend and implement the `setup()` method of the abstract class [`ExpenseManager`](/app/src/main/java/lk/ac/mrt/cse/dbs/simpleexpensemanager/control/ExpenseManager.java).
5. Change current implementation to your implementation in the [`MainActivity`](/app/src/main/java/lk/ac/mrt/cse/dbs/simpleexpensemanager/ui/MainActivity.java) ui class.

  Non-persistent implementation
  ```Java
  /***  Begin generating dummy data for In-Memory implementation  ***/
  expenseManager = new InMemoryDemoExpenseManager();
  /*** END ***/
  ```
  Persistent implementation
  ```Java
  /***  Setup the persistent storage implementation  ***/
  expenseManager = new PersistentExpenseManager(context);
  /*** END ***/
  ```
6. Commit your code and push to your forked repository in GitHub.
7. Download your project as a Zip from GitHub and submit as the completed assignment.

