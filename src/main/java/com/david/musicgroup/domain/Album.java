package com.david.musicgroup.domain;

import java.util.List;

public class Album {

    private String title;

    private String id;

    private List<String> images;

    public Album() {
    }

    Album(Builder builder) {
        this.title = builder.title;
        this.id = builder.id;
        this.images = builder.images;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getImage() {
        return images;
    }

    public void setImage(List<String> image) {
        this.images = image;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        String title;
        String id;
        List<String> images;

        Builder() {
        }

        public Builder withTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder withId(String id) {
            this.id = id;
            return this;
        }

        public Builder withImages(List<String> images) {
            this.images = images;
            return this;
        }

        public Album build() {
            return new Album(this);
        }
    }
}

