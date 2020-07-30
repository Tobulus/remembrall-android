package com.remembrall.model.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.remembrall.api.data.GroceryListData;
import com.remembrall.api.data.GroceryListEntryData;

@Entity
public class GroceryListEntry {

    @PrimaryKey
    private Long id;

    private String name;

    private boolean checked;

    private Long groceryList;

    private Double quantity;

    private String quantityUnit;

    private Boolean unseen;

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

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public String getQuantityUnit() {
        return quantityUnit;
    }

    public void setQuantityUnit(String quantityUnit) {
        this.quantityUnit = quantityUnit;
    }

    public Boolean isUnseen() {
        return unseen;
    }

    public void setUnseen(Boolean unseen) {
        this.unseen = unseen;
    }

    public GroceryListEntryData toData() {
        GroceryListEntryData data = new GroceryListEntryData();
        data.setId(id);
        data.setName(name);
        data.setChecked(checked);
        data.setQuantity(quantity);
        data.setQuantityUnit(quantityUnit);

        GroceryListData list = new GroceryListData();
        list.setId(groceryList);
        data.setGroceryList(list);

        return data;
    }
}
