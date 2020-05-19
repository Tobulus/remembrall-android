package com.groceries.model.database.repository;

import androidx.room.Dao;
import androidx.room.Query;

import com.groceries.model.database.GroceryListEntry;

import java.util.List;

@Dao
public interface GroceryListEntryRepository extends BaseRepository<GroceryListEntry> {
    @Query("SELECT * FROM GroceryListEntry WHERE groceryList = :groceryListId")
    List<GroceryListEntry> getGroceryListEntries(Long groceryListId);
}
