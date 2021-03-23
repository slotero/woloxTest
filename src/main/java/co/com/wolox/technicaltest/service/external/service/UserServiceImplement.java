package co.com.wolox.technicaltest.service.external.service;

import co.com.wolox.technicaltest.model.user.User;
import co.com.wolox.technicaltest.service.InterfaceUserService;
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
public class UserServiceImplement implements InterfaceUserService {

    Logger logger = LoggerFactory.getLogger(UserServiceImplement.class);

    private final RestTemplate clientRestTemplate;

    public UserServiceImplement(RestTemplate clientRestTemplate) {
        this.clientRestTemplate = clientRestTemplate;
    }

    @Override
    public User findById(Long id) {
        try {
            return clientRestTemplate.getForObject("/users/ " + id, User.class);
        } catch (HttpClientErrorException.NotFound e) {
            String message = String.format("User with id: %s was not found", id);
            logger.error(message);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, message);
        } catch (HttpClientErrorException e) {
            String message = String.format("Error %s finding user with id: %s", e.getStatusCode().toString(), id);
            logger.error(message);
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, message);
        }
    }

    @Override
    public List<User> findAll() {
        return Arrays.asList(clientRestTemplate.getForObject("/users", User[].class));
    }
}
