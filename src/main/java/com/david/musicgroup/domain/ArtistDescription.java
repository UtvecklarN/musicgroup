package com.david.musicgroup.domain;

import java.util.List;

public class ArtistDescription {

    private String mbid;

    private String description;

    private List<Album> albums;

    public ArtistDescription() {
    }

    ArtistDescription(ArtistDescription.Builder builder) {
        this.mbid = builder.mbid;
        this.description = builder.description;
        this.albums = builder.albums;
    }

    public String getMbid() {
        return mbid;
    }

    public void setMbid(String mbid) {
        this.mbid = mbid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Album> getAlbums() {
        return albums;
    }

    public void setAlbums(List<Album> albums) {
        this.albums = albums;
    }

    public static ArtistDescription.Builder builder() {
        return new ArtistDescription.Builder();
    }

    public static class Builder {
        String mbid;
        String description;
        List<Album> albums;

        Builder() {
        }

        public ArtistDescription.Builder withMbid(String mbid) {
            this.mbid = mbid;
            return this;
        }

        public ArtistDescription.Builder withDescription(String description) {
            this.description = description;
            return this;
        }

        public ArtistDescription.Builder withAlbums(List<Album> albums) {
            this.albums = albums;
            return this;
        }

        public ArtistDescription build() {
            return new ArtistDescription(this);
        }
    }
}
