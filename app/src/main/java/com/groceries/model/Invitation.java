package com.groceries.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Invitation {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("title")
    private String title;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
