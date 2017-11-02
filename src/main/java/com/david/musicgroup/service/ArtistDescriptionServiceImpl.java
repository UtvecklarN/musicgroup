package com.david.musicgroup.service;

import com.david.musicgroup.domain.Album;
import com.david.musicgroup.domain.ArtistDescription;
import com.david.musicgroup.domain.converartarchives.CoverArtArchiveResponse;
import com.david.musicgroup.domain.converartarchives.Image;
import com.david.musicgroup.domain.musicbrainz.MusicBrainzResponse;
import com.david.musicgroup.domain.musicbrainz.Relation;
import com.david.musicgroup.domain.musicbrainz.ReleaseGroup;
import com.david.musicgroup.domain.wikipedia.WikipediaResponse;
import com.david.musicgroup.util.UrlUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Service
public class ArtistDescriptionServiceImpl implements ArtistDescriptionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ArtistDescriptionServiceImpl.class);

    /**
     * A proxy to make http call and return jackson parsed POJO
     */
    private RestTemplate restTemplate = new RestTemplate();

    private final String WIKIPEDIA = "wikipedia";

    private static ExecutorService executorService = Executors.newCachedThreadPool();


    @Override
    public ArtistDescription getArtistDescriptionByMbid(String mbid) throws ExecutionException, InterruptedException {
            MusicBrainzResponse musicBrainzResponse = getMusicBrainzResponse(mbid);
            Future<String> description = executorService.submit(getWikipediaDescriptionCallable(musicBrainzResponse));
            Future<List<Album>> albums = executorService.submit(getAlbumListCallable(musicBrainzResponse));

            return ArtistDescription.builder()
                    .withMbid(mbid)
                    .withDescription(description.get())
                    .withAlbums(albums.get())
                    .build();
    }

    private Callable<String> getWikipediaDescriptionCallable(MusicBrainzResponse musicBrainzResponse) {
        return () -> getWikipediaDescription(musicBrainzResponse);
    }

    private Callable<List<Album>> getAlbumListCallable(MusicBrainzResponse musicBrainzResponse) {
        return () -> getAlbumList(musicBrainzResponse);
    }

    private List<Album> getAlbumList(MusicBrainzResponse musicBrainzResponse) {
        return musicBrainzResponse.getReleaseGroups().parallelStream()
                .map(this::toAlbum)
                .collect(Collectors.toList());
    }

    private String getWikipediaDescription(MusicBrainzResponse musicBrainzResponse) {
        Optional<Relation> wikipediaRelation = musicBrainzResponse.getRelations().stream()
                .filter(x -> WIKIPEDIA.equals(x.getType()))
                .findFirst();
        if (wikipediaRelation.isPresent()) {
            String url = UrlUtil.getWikipediaUrlFromType(wikipediaRelation.get().getUrl().getWikipediaIdFromResource());
            try {
                WikipediaResponse response = restTemplate.getForObject(url, WikipediaResponse.class);
                return response.getDescription();
            } catch (Exception e) {
                LOGGER.info("Could not retrieve wikipedia description with id '{}'",
                        wikipediaRelation.get().getUrl().getWikipediaIdFromResource());
                return null;
            }
        } else {
            return null;
        }
    }

    private Album toAlbum(ReleaseGroup releaseGroup) {
        String url = UrlUtil.getCoverArtArchiveUrlFromId(releaseGroup.getId());
        List<Image> images = null;

        try {
            images = restTemplate.getForObject(url, CoverArtArchiveResponse.class).getImages();
        } catch (HttpClientErrorException e) {
            LOGGER.info("Cover image not found for album with id '{}' and title '{}'",
                    releaseGroup.getId(), releaseGroup.getTitle());
        } catch (Exception e) {
            LOGGER.info("Could not retrieve image with id '{}' and title '{}'",
                    releaseGroup.getId(), releaseGroup.getTitle());
        }

        return Album.builder()
                .withId(releaseGroup.getId())
                .withTitle(releaseGroup.getTitle())
                .withImages(getImagesString(images))
                .build();
    }

    private List<String> getImagesString(List<Image> images) {
        return images != null ? images.stream()
                .map(Image::toString)
                .collect(Collectors.toList()) : null;
    }

    private MusicBrainzResponse getMusicBrainzResponse(String mbid) {
        String musicBrainzUrl = UrlUtil.getMusicBrainzUrlFromMbid(mbid);
        return restTemplate.getForObject(musicBrainzUrl, MusicBrainzResponse.class);
    }
}
