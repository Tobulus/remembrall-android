package com.groceries.model.database.repository;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;
import com.groceries.model.database.GroceryListEntry;

import java.util.List;

@Dao
public interface GroceryListEntryRepository extends BaseRepository<GroceryListEntry> {
    @Transaction
    @Query("SELECT * FROM GroceryListEntry WHERE groceryList = :groceryListId")
    List<GroceryListEntry> getGroceryListEntries(Long groceryListId);
}
