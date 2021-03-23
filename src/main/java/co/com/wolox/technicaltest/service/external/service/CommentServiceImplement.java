package co.com.wolox.technicaltest.service.external.service;

import co.com.wolox.technicaltest.model.comment.Comment;
import co.com.wolox.technicaltest.model.comment.Post;
import co.com.wolox.technicaltest.service.InterfaceCommentService;
import net.logstash.logback.encoder.org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
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
public class CommentServiceImplement implements InterfaceCommentService {

    Logger logger = LoggerFactory.getLogger(CommentServiceImplement.class);

    private final RestTemplate clientRestTemplate;

    public CommentServiceImplement(RestTemplate clientRestTemplate) {
        this.clientRestTemplate = clientRestTemplate;
    }

    @Override
    public List<Comment> findAll() {
        return Arrays.asList(clientRestTemplate.getForObject("/comments", Comment[].class));
    }

    @Override
    public Comment findById(Long id) {
        try {
            return clientRestTemplate.getForObject("/comments/" + id, Comment.class);
        } catch (HttpClientErrorException.NotFound e) {
            String message = String.format("Comment with id: %s was not found", id);
            logger.error(message);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, message);
        } catch (HttpClientErrorException e) {
            String message = String.format("Error %s finding comment with id: %s", e.getStatusCode().toString(), id);
            logger.error(message);
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, message);
        }
    }

    @Override
    public List<Comment> findByUserOrName(Long userId, String name) {
        try {
            if (userId != null) {
                List<Post> posts =
                        Arrays.asList(clientRestTemplate.getForObject("/posts?userId=" + userId, Post[].class));
                List<Comment> commentsByUser = new ArrayList<>();
                posts.forEach(post -> commentsByUser.addAll(findCommentByPostId(post.getId())));
                if (!commentsByUser.isEmpty()) {
                    return commentsByUser;
                } else {
                    String message = String.format("Comments for userId: %s were not found", userId);
                    logger.error(message);
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, message);
                }
            } else if (StringUtils.isNoneBlank(name)) {
                Comment[] result = clientRestTemplate.getForObject("/comments?name=" + name, Comment[].class);
                if (ArrayUtils.isNotEmpty(result)) {
                    return Arrays.asList(result);
                } else {
                    String message = String.format("Comments with name: %s were not found", name);
                    logger.error(message);
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, message);
                }
            }
            return findAll();
        } catch (HttpClientErrorException e) {
            String message =
                    String.format("Error %s finding comments", e.getStatusCode().toString());
            logger.error(message);
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, message);
        }
    }

    @Override
    public List<Comment> findCommentByPostId(Long postId) {
        try {
            Comment[] result = clientRestTemplate.getForObject("/comments?postId=" + postId, Comment[].class);
            if (ArrayUtils.isNotEmpty(result)) {
                return Arrays.asList(result);
            } else {
                String message = String.format("Comments with postId: %s were not found", postId);
                logger.error(message);
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, message);
            }
        } catch (HttpClientErrorException e) {
            String message =
                    String.format("Error %s finding comments with postId: %s", e.getStatusCode().toString(), postId);
            logger.error(message);
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, message);
        }
    }
}
