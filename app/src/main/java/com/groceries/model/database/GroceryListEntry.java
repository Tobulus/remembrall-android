package com.groceries.model.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class GroceryListEntry {

    @PrimaryKey
    private Long id;

    private String name;

    private boolean checked;

    private Long groceryList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public Long getGroceryList() {
        return groceryList;
    }

    public void setGroceryList(Long groceryList) {
        this.groceryList = groceryList;
    }

}
