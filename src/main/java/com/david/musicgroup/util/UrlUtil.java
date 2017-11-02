package com.david.musicgroup.util;

public class UrlUtil {

    private static final String MUSIC_BRAINZ_REQUEST_TEMPLATE = "http://musicbrainz.org/ws/2/artist/%s?" +
            "&fmt=json" +
            "&inc=url-rels+release-groups";

    private static final String WIKIPEDIA_REQUEST_TEMPLATE = "https://en.wikipedia.org/w/api.php" +
            "?action=query&format=json&prop=extracts&exintro=true&redirects=true&titles=%s";

    private static final String COVER_ART_ARCHIVE_REQUEST_TEMPLATE = "http://coverartarchive.org/release-group/%s";

    public static String getMusicBrainzUrlFromMbid(String mbid) {
        return String.format(MUSIC_BRAINZ_REQUEST_TEMPLATE, mbid);
    }

    public static String getWikipediaUrlFromType(String type) {
        return String.format(WIKIPEDIA_REQUEST_TEMPLATE, type);
    }

    public static String getCoverArtArchiveUrlFromId(String id) {
        return String.format(COVER_ART_ARCHIVE_REQUEST_TEMPLATE, id);
    }
}
