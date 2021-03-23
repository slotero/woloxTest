package co.com.wolox.technicaltest.service;

import co.com.wolox.technicaltest.model.comment.Comment;

import java.util.List;

public interface InterfaceCommentService {

    public List<Comment> findAll();

    public Comment findById(Long id);

    public List<Comment> findByUserOrName(Long userId, String name);

    public List<Comment> findCommentByPostId(Long postId);

}
