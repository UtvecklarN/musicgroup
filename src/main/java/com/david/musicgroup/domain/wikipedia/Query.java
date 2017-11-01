package com.david.musicgroup.domain.wikipedia;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown=true)
public class Query {

    private Map<String, Page> pages;

    public void setPages(Map<String, Page> pages) {
        this.pages = pages;
    }

    Map<String, Page> getPages() {
        return pages;
    }
}
