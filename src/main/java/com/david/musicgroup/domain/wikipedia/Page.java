package com.david.musicgroup.domain.wikipedia;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown=true)
public class Page {

    private String description;

    @JsonProperty(value = "extract")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Page{" +
                "description='" + description + '\'' +
                '}';
    }
}
