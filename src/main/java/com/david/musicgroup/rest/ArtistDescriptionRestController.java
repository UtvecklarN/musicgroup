package com.david.musicgroup.rest;

import com.david.musicgroup.domain.ArtistDescription;
import com.david.musicgroup.service.ArtistDescriptionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

@RestController
public class ArtistDescriptionRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ArtistDescriptionRestController.class);

    private static HashMap<String, ArtistDescription> cache = new HashMap<>();

    private final ArtistDescriptionService artistDescriptionService;

    @Autowired
    public ArtistDescriptionRestController(@NotNull ArtistDescriptionService artistDescriptionService) {
        this.artistDescriptionService = artistDescriptionService;
    }

    /**
     * Method that handles HTTP GET requests that fetches Wikipedia description and list of album information by an
     * external identifier
     * @param mbid The external identifier of an artist
     * @return ResponseEntity with the wrapper POJO class as json in body
     */

    @RequestMapping(method = RequestMethod.GET,
                    path = "/artist/{mbid}",
                    produces = "application/json")
    public ResponseEntity getArtistDescription(@PathVariable String mbid) {
        if (cache.containsKey(mbid))
            return ResponseEntity.ok(cache.get(mbid));
        try {
            ArtistDescription artistDescription = artistDescriptionService.getArtistDescriptionByMbid(mbid);
            cache.putIfAbsent(mbid, artistDescription);
            return ResponseEntity.ok(artistDescription);

        } catch (HttpClientErrorException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (HttpServerErrorException | InterruptedException | ExecutionException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
