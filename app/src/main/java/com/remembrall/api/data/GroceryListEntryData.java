package com.remembrall.api.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GroceryListEntryData {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("checked")
    private boolean checked;

    @JsonProperty("groceryList")
    private GroceryListData groceryList;

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

    public GroceryListData getGroceryList() {
        return groceryList;
    }

    public void setGroceryList(GroceryListData groceryList) {
        this.groceryList = groceryList;
    }

    public Map<String, String> toMap() {
        HashMap<String, String> map = new HashMap<>();
        map.put("id", id.toString());
        map.put("name", name);
        map.put("checked", Boolean.toString(checked));
        return map;
    }

    public com.remembrall.model.database.GroceryListEntry toEntity() {
        com.remembrall.model.database.GroceryListEntry entity =
                new com.remembrall.model.database.GroceryListEntry();
        entity.setId(id);
        entity.setName(name);
        entity.setChecked(checked);
        entity.setGroceryList(getGroceryList().getId());
        return entity;
    }
}
