package com.remembrall.model.database.repository;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;
import com.remembrall.model.database.GroceryList;

import java.util.List;

@Dao
public interface GroceryListRepository extends BaseRepository<GroceryList> {
    @Transaction
    @Query("SELECT * FROM GroceryList WHERE archived = :archived ORDER BY id DESC")
    List<GroceryList> getGroceryLists(boolean archived);

    @Query("DELETE FROM GroceryList WHERE archived = :archived")
    void deleteAll(boolean archived);
}
