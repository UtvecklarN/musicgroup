package com.david.musicgroup.rest;

import com.david.musicgroup.domain.Album;
import com.david.musicgroup.domain.converartarchives.CoverArtArchiveResponse;
import com.david.musicgroup.domain.musicbrainz.MusicBrainzResponse;
import com.david.musicgroup.domain.musicbrainz.Relation;
import com.david.musicgroup.domain.musicbrainz.ReleaseGroup;
import com.david.musicgroup.domain.wikipedia.WikipediaResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class ArtistDescriptionRestController {

    private final String WIKIPEDIA = "wikipedia";
    private RestTemplate restTemplate = new RestTemplate();

    private static final String MUSIC_BRAINZ_REQUEST_TEMPLATE = "http://musicbrainz.org/ws/2/artist/%s?" +
            "&fmt=json" +
            "&inc=url-rels+release-groups";

    private static final String WIKIPEDIA_REQUEST_TEMPLATE = "https://en.wikipedia.org/w/api.php" +
            "?action=query&format=json&prop=extracts&exintro=true&redirects=true&titles=%s";

    private static final String COVER_ART_ARCHIVE_REQUEST_TEMPLATE = "http://coverartarchive.org/release-group/%s";

    @RequestMapping(method= RequestMethod.GET, path = "/artist/{mbid}")
    public List<Album> getArtistDescription(@PathVariable String mbid) {
        MusicBrainzResponse musicBrainzResponse = getMusicBrainzResponse(mbid);
        WikipediaResponse wikipediaResponse = getWikipediaResponse(musicBrainzResponse);
        List<Album> albums = getAlbumList(musicBrainzResponse);

        return albums;
    }

    private List<Album> getAlbumList(MusicBrainzResponse musicBrainzResponse) {
        System.out.println(musicBrainzResponse.getReleaseGroups().size());
        return musicBrainzResponse.getReleaseGroups().parallelStream()
                .map(this::toAlbum)
                .collect(Collectors.toList());
    }

    private Album toAlbum(ReleaseGroup releaseGroup) {
        String url = getUrlFromReleaseGroupId(releaseGroup.getId());

        System.out.println(url);

        CoverArtArchiveResponse coverArtArchiveResponse = restTemplate.getForObject(url, CoverArtArchiveResponse.class);
        return isNotNull(coverArtArchiveResponse) ?
                Album.builder()
                        .withId(releaseGroup.getId())
                        .withTitle(releaseGroup.getTitle())
                        .withImage(coverArtArchiveResponse.getImage())
                        .build() : null;
    }

    private boolean isNotNull(CoverArtArchiveResponse coverArtArchiveResponse) {
        return coverArtArchiveResponse != null;
    }

    private WikipediaResponse getWikipediaResponse(MusicBrainzResponse musicBrainzResponse) {
        Optional<Relation> wikipediaRelation = musicBrainzResponse.getRelations().stream()
                .filter(x -> WIKIPEDIA.equals(x.getType()))
                .findFirst();
        if (wikipediaRelation.isPresent()) {
            String url = getUrlFromType(wikipediaRelation.get().getUrl().getWikipediaIdFromResource());
            WikipediaResponse response = restTemplate.getForObject(url, WikipediaResponse.class);
            return response;
        } else {
            return null;
        }
    }

    private MusicBrainzResponse getMusicBrainzResponse(String mbid) {
        String musicBrainzUrl = getMusicBrainzUrlForMbid(mbid);
        return restTemplate.getForObject(musicBrainzUrl, MusicBrainzResponse.class);
    }

    private String getMusicBrainzUrlForMbid(String mbid) {
        return String.format(MUSIC_BRAINZ_REQUEST_TEMPLATE, mbid);
    }

    private String getUrlFromType(String type) {
        return String.format(WIKIPEDIA_REQUEST_TEMPLATE, type);
    }

    private String getUrlFromReleaseGroupId(String id) {
        return String.format(COVER_ART_ARCHIVE_REQUEST_TEMPLATE, id);
    }
}
