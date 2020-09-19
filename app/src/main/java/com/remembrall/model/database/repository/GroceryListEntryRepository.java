package com.remembrall.model.database.repository;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;
import com.remembrall.model.database.GroceryListEntry;

import java.util.List;

@Dao
public interface GroceryListEntryRepository extends BaseRepository<GroceryListEntry> {
    @Transaction
    @Query("SELECT * FROM GroceryListEntry WHERE groceryList = :groceryListId ORDER BY id DESC")
    List<GroceryListEntry> getGroceryListEntries(Long groceryListId);

    @Query("DELETE FROM GroceryListEntry")
    void deleteAll();
}
