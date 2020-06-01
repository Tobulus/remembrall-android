package com.remembrall.model.database;

import androidx.room.RoomDatabase;
import com.remembrall.model.database.repository.GroceryListEntryRepository;
import com.remembrall.model.database.repository.GroceryListRepository;

@androidx.room.Database(entities = {GroceryList.class, GroceryListEntry.class}, version = 1)
public abstract class Database extends RoomDatabase {
    public abstract GroceryListRepository groceryListRepository();

    public abstract GroceryListEntryRepository groceryListEntryRepository();
}

