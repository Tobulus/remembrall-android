package com.remembrall.model.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.remembrall.api.data.GroceryListData;

@Entity
public class GroceryList {

    @PrimaryKey
    private Long id;

    private String name;

    private boolean archived;

    private Integer numberOfEntries;

    private Integer numberOfCheckedEntries;

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

    public boolean isArchived() {
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

    public GroceryListData toData() {
        GroceryListData data = new GroceryListData();
        data.setId(id);
        data.setName(name);
        data.setArchived(archived);
        return data;
    }
}
