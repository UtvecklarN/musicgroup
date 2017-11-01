package com.david.musicgroup.domain.musicbrainz;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class Url {

    private String resource;

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    @JsonIgnore
    public String getWikipediaIdFromResource() {
        return getResource().substring(getResource().lastIndexOf("/") + 1, getResource().length());
    }
}
