package com.remembrall.api.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GroceryListData {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("numberOfEntries")
    private Integer numberOfEntries;

    @JsonProperty("numberOfCheckedEntries")
    private Integer numberOfCheckedEntries;

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

    public Integer getNumberOfEntries() {
        return numberOfEntries;
    }

    public void setNumberOfEntries(Integer numberOfEntries) {
        this.numberOfEntries = numberOfEntries;
    }

    public Integer getNumberOfCheckedEntries() {
        return numberOfCheckedEntries;
    }

    public void setNumberOfCheckedEntries(Integer numberOfCheckedEntries) {
        this.numberOfCheckedEntries = numberOfCheckedEntries;
    }

    public Map<String, String> toMap() {
        HashMap<String, String> map = new HashMap<>();
        map.put("id", id.toString());
        map.put("name", name);
        return map;
    }

    public com.remembrall.model.database.GroceryList toEntity() {
        com.remembrall.model.database.GroceryList entity =
                new com.remembrall.model.database.GroceryList();
        entity.setId(id);
        entity.setName(name);
        entity.setNumberOfCheckedEntries(numberOfCheckedEntries);
        entity.setNumberOfEntries(numberOfEntries);
        return entity;
    }
}
