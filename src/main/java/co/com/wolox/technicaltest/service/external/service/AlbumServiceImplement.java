package co.com.wolox.technicaltest.service.external.service;

import co.com.wolox.technicaltest.model.album.Album;
import co.com.wolox.technicaltest.service.InterfaceAlbumService;
import net.logstash.logback.encoder.org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;

@Service
public class AlbumServiceImplement implements InterfaceAlbumService {

    Logger logger = LoggerFactory.getLogger(AlbumServiceImplement.class);

    private final RestTemplate clientRestTemplate;

    public AlbumServiceImplement(RestTemplate clientRestTemplate) {
        this.clientRestTemplate = clientRestTemplate;
    }

    @Override
    public List<Album> findAll() {
        return Arrays.asList(clientRestTemplate.getForObject("/albums", Album[].class));
    }

    @Override
    public Album findById(Long id) {
        try {
            return clientRestTemplate.getForObject("/albums/" + id, Album.class);
        } catch (HttpClientErrorException.NotFound e) {
            String message = String.format("Album with id: %s was not found", id);
            logger.error(message);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, message);
        } catch (HttpClientErrorException e) {
            String message = String.format("Error %s finding album with id: %s", e.getStatusCode().toString(), id);
            logger.error(message);
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, message);
        }
    }

    @Override
    public List<Album> findByUserId(Long userId) {
        try {
            Album[] result = clientRestTemplate.getForObject("/albums?userId=" + userId, Album[].class);
            if (ArrayUtils.isNotEmpty(result)) {
                return Arrays.asList(result);
            } else {
                String message = String.format("Albums for userId: %s were not found", userId);
                logger.error(message);
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, message);
            }
        } catch (HttpClientErrorException e) {
            String message =
                    String.format("Error %s finding albums for userId: %s", e.getStatusCode().toString(), userId);
            logger.error(message);
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, message);
        }
    }
}
