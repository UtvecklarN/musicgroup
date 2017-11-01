package com.david.musicgroup.domain;

public class Album {

    private String title;

    private String id;

    private String image;

    Album(Builder builder) {
        this.title = builder.title;
        this.id = builder.id;
        this.image = builder.image;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        String title;
        String id;
        String image;

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

        public Builder withImage(String image) {
            this.image = image;
            return this;
        }

        public Album build() {
            return new Album(this);
        }
    }
}

