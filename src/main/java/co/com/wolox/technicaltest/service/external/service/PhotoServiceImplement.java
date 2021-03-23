package co.com.wolox.technicaltest.service.external.service;

import co.com.wolox.technicaltest.model.album.Album;
import co.com.wolox.technicaltest.model.photo.Photo;
import co.com.wolox.technicaltest.service.InterfaceAlbumService;
import co.com.wolox.technicaltest.service.InterfacePhotoService;
import net.logstash.logback.encoder.org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class PhotoServiceImplement implements InterfacePhotoService {

    Logger logger = LoggerFactory.getLogger(PhotoServiceImplement.class);

    private final RestTemplate clientRestTemplate;

    final
    InterfaceAlbumService albumService;

    public PhotoServiceImplement(RestTemplate clientRestTemplate, InterfaceAlbumService albumService) {
        this.clientRestTemplate = clientRestTemplate;
        this.albumService = albumService;
    }

    @Override
    public List<Photo> findAll() {
        return Arrays.asList(clientRestTemplate.getForObject("/photos", Photo[].class));
    }

    @Override
    public Photo findById(Long id) {
        try {
            return clientRestTemplate.getForObject("/photos/" + id, Photo.class);
        } catch (HttpClientErrorException.NotFound e) {
            String message = String.format("Photo with id: %s was not found", id);
            logger.error(message);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, message);
        } catch (HttpClientErrorException e) {
            String message = String.format("Error %s finding photo with id: %s", e.getStatusCode().toString(), id);
            logger.error(message);
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, message);
        }

    }

    @Override
    public List<Photo> findByUserId(Long userId) {
        try {
            List<Album> albumsByUser = albumService.findByUserId(userId);
            List<Photo> photosByUser = new ArrayList<>();
            albumsByUser.forEach(album -> photosByUser.addAll(findByAlbumId(album.getId())));
            if (!photosByUser.isEmpty()) {
                return photosByUser;
            } else {
                String message = String.format("Photos for userId: %s were not found", userId);
                logger.error(message);
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, message);
            }
        } catch (HttpClientErrorException e) {
            String message =
                    String.format("Error %s finding photos with userId: %s", e.getStatusCode().toString(), userId);
            logger.error(message);
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, message);
        }
    }

    @Override
    public List<Photo> findByAlbumId(Long albumId) {
        try {
            Photo[] result = clientRestTemplate.getForObject("/photos?albumId=" + albumId, Photo[].class);
            if (ArrayUtils.isNotEmpty(result)) {
                return Arrays.asList(result);
            } else {
                String message = String.format("Photos with albumId: %s were not found", albumId);
                logger.error(message);
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, message);
            }
        } catch (HttpClientErrorException e) {
            String message =
                    String.format("Error %s finding photos with albumId: %s", e.getStatusCode().toString(), albumId);
            logger.error(message);
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, message);
        }
    }
}
