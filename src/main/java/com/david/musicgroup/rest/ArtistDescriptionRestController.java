package com.david.musicgroup.rest;

import com.david.musicgroup.domain.musicbrainz.MusicBrainzResponse;
import com.david.musicgroup.domain.musicbrainz.Relation;
import com.david.musicgroup.domain.wikipedia.WikipediaResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@RestController
public class ArtistDescriptionRestController {

    private final String WIKIPEDIA = "wikipedia";

    private RestTemplate restTemplate = new RestTemplate();
    private static final String MUSIC_BRAINZ_REQUEST_TEMPLATE = "http://musicbrainz.org/ws/2/artist/%s?" +
            "&fmt=json" +
            "&inc=url-rels+release-groups";

    private static final String WIKIPEDIA_REQUEST_TEMPLATE = "https://en.wikipedia.org/w/api.php" +
            "?action=query&format=json&prop=extracts&exintro=true&redirects=true&titles=%s";

    @RequestMapping(method= RequestMethod.GET, path = "/artist/{mbid}")
    public WikipediaResponse getArtistDescription(@PathVariable String mbid) {
        MusicBrainzResponse musicBrainzResponse = getMusicBrainzResponse(mbid);
        WikipediaResponse wikipediaResponse = getWikipediaResponse(musicBrainzResponse);
        System.out.println(wikipediaResponse);

        return wikipediaResponse;
    }

    private WikipediaResponse getWikipediaResponse(MusicBrainzResponse musicBrainzResponse) {
        Optional<Relation> wikipediaRelation = musicBrainzResponse.getRelations().stream()
                .filter(x -> WIKIPEDIA.equals(x.getType()))
                .findFirst();
        if (wikipediaRelation.isPresent()) {
            String url = getUrlFromType(wikipediaRelation.get().getType());
            return restTemplate.getForObject(url, WikipediaResponse.class);
        } else {
            return null;
        }
    }

    private String getUrlFromType(String type) {
        return String.format(WIKIPEDIA_REQUEST_TEMPLATE, type);
    }

    private MusicBrainzResponse getMusicBrainzResponse(String mbid) {
        String musicBrainzUrl = getMusicBrainzUrlForMbid(mbid);
        return restTemplate.getForObject(musicBrainzUrl, MusicBrainzResponse.class);
    }

    private String getMusicBrainzUrlForMbid(String mbid) {
        return String.format(MUSIC_BRAINZ_REQUEST_TEMPLATE, mbid);
    }
}
