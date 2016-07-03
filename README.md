SOURCE CODE
==============

ROOT PACKAGE
-------------

Root package of all source code is package **com.tw.retailstore**. 
All packages that are mentioned further are to be interpreted relative to this package.

PACKAGES
-------------

Retail Store Application is further split across below packages.

1. **ui** - package for implementation of application UI.
   
   1.1. **cart** - package for Cart UI.
   
   1.2. **products** - package for Products UI.
   
   UI logic in above packages follow the below pattern:
   
   **Activity** (say *CartMainActivity.java*) is responsible for UI presentation only
   and interacts with **Presenter** (say *CartPresenter.java*) which functions as a delegate for fetching 
   data and performing other business operations using implementations available in
   **businesslogic** package.
   
   **Activity** and **Presenter** are linked to each other by a **Contract** (say *CartViewContract.java*)
   which binds them appropriately.

   Below is the list of contracts in current **ui** package:
   
     a. *CartViewContract* linking *CartMainActivity* and *CartPresenter*.
   
     b. *ProductListViewContract* linking *ProductsListActivity* and *ProductsListPresenter*.
   
     c. *ProductDetailViewContract* linking *ProductDetailActivity* and *ProductDetailPresenter*.
    	
2. **businesslogic** - package for implementation of Business Logic. 
   Business Logic includes handling of Store Products, functionality of User Carts and interaction
   with **storage** package for persistence of Carts to a database.
	
3. **models** - package for business domain models of a Retail Store.
   Includes Product, CartProduct (Product in a Cart i.e. a Product with shopped quantity) and a 
   CartKey (Identifier for a Cart).
   
4. **storage** - package for implementation of Database Logic.
   Database logic includes creation of tables for Carts and Products. Also includes 
   basic CRUD utilities used by **businesslogic** layer to achieve its functionality. 

5. **utils** - package for utilities and base components.
   Current utilities include bitmap, currency utilities and base classes for activities/presenters.


UNIT TESTS
-------------
Basic Unit tests are available under location **src.test.java.com.tw.retailstore.businesslogic**.


