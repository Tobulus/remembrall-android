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

    @JsonProperty("archived")
    private boolean archived;

    @JsonProperty("numberOfEntries")
    private Integer numberOfEntries;

    @JsonProperty("numberOfCheckedEntries")
    private Integer numberOfCheckedEntries;

    @JsonProperty("participants")
    private String participants;

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

    public boolean getArchived() {
        return archived;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }

    public String getParticipants() {
        return participants;
    }

    public void setParticipants(String participants) {
        this.participants = participants;
    }

    public Map<String, String> toMap() {
        HashMap<String, String> map = new HashMap<>();
        map.put("id", id.toString());
        map.put("name", name);
        map.put("archived", Boolean.toString(archived));
        return map;
    }

    public com.remembrall.model.database.GroceryList toEntity() {
        com.remembrall.model.database.GroceryList entity =
                new com.remembrall.model.database.GroceryList();
        entity.setId(id);
        entity.setName(name);
        entity.setArchived(archived);
        entity.setNumberOfCheckedEntries(numberOfCheckedEntries);
        entity.setNumberOfEntries(numberOfEntries);
        entity.setParticipants(participants);
        return entity;
    }
}
