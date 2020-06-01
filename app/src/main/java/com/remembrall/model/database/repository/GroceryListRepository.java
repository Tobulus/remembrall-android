package com.remembrall.model.database.repository;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;
import com.remembrall.model.database.GroceryList;

import java.util.List;

@Dao
public interface GroceryListRepository extends BaseRepository<GroceryList> {
    @Transaction
    @Query("SELECT * FROM GroceryList")
    List<GroceryList> getGroceryLists();

    @Query("DELETE FROM GroceryList")
    void deleteAll();
}
