package com.remembrall.api.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class InvitationData {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("sender")
    private UserData sender;

    @JsonProperty("groceryList")
    private GroceryListData groceryList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserData getSender() {
        return sender;
    }

    public void setSender(UserData sender) {
        this.sender = sender;
    }

    public GroceryListData getGroceryList() {
        return groceryList;
    }

    public void setGroceryList(GroceryListData groceryList) {
        this.groceryList = groceryList;
    }
}
