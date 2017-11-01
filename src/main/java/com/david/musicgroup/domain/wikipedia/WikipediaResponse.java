package com.david.musicgroup.domain.wikipedia;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class WikipediaResponse {

    private Query query;

    public Query getQuery() {
        return query;
    }

    public void setQuery(Query query) {
        this.query = query;
    }

    @Override
    public String toString() {
        return "WikipediaResponse{" +
                "query=" + query +
                '}';
    }
}
