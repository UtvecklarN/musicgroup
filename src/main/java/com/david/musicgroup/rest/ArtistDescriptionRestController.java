package com.david.musicgroup.rest;

import com.david.musicgroup.domain.GreetingResponse;
import com.david.musicgroup.domain.musicbrainz.MusicBrainzResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.atomic.AtomicLong;

@RestController
public class ArtistDescriptionRestController {

    /**
     *  LEK
     */
    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    private RestTemplate restTemplate = new RestTemplate();
    private static final String MUSIC_BRAINZ_REQUEST_TEMPLATE = "http://musicbrainz.org/ws/2/artist/%s?" +
            "&fmt=json" +
            "&inc=url-rels+release-groups";

    @RequestMapping(method= RequestMethod.GET, path = "/hello-world")
    public GreetingResponse sayHello(@RequestParam(value="name", required=false, defaultValue="Stranger") String name) {
        return new GreetingResponse(counter.incrementAndGet(), String.format(template, name));
    }

    @RequestMapping(method= RequestMethod.GET, path = "/artist/{mbid}")
    public MusicBrainzResponse getArtistDescription(@PathVariable String mbid) {
        String musicBrainzUrl = getMusicBrainzUrlForMbid(mbid);
        return restTemplate.getForObject(musicBrainzUrl, MusicBrainzResponse.class);

    }

    private String getMusicBrainzUrlForMbid(String mbid) {
        return String.format(MUSIC_BRAINZ_REQUEST_TEMPLATE, mbid);
    }
}
