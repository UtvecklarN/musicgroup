package com.david.musicgroup.domain.wikipedia;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Iterator;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown=true)
public class WikipediaResponse {

    private Query query;

    public Query getQuery() {
        return query;
    }

    public void setQuery(Query query) {
        this.query = query;
    }

    @JsonIgnore
    public String getDescription() {
        if (getQuery().getPages() != null){
            Iterator<Map.Entry<String, Page>> iterator = getQuery().getPages().entrySet().iterator();
            return iterator.hasNext() ? iterator.next().getValue().getDescription() : null;
        } else {
            return null;
        }
    }

    @Override
    public String toString() {
        return "WikipediaResponse{" +
                "query=" + query +
                '}';
    }
}
