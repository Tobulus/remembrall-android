package com.groceries.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GroceryList {

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
}
