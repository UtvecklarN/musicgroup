package com.david.musicgroup.service;

import com.david.musicgroup.domain.ArtistDescription;
import java.util.concurrent.ExecutionException;

public interface ArtistDescriptionService {

    /**
     * Service that fetches information specified in ArtistDescription from MusicBrainz, Wikipedia & CoverArtArchive
     * using the external identifier "mbid"
     * @param mbid The external identifier of an artist
     * @return Wrapper POJO of the information
     * @throws ExecutionException
     * @throws InterruptedException
     */
    ArtistDescription getArtistDescriptionByMbid(String mbid) throws ExecutionException, InterruptedException;
}
