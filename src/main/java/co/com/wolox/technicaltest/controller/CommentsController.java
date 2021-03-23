package co.com.wolox.technicaltest.controller;

import co.com.wolox.technicaltest.model.comment.Comment;
import co.com.wolox.technicaltest.service.InterfaceCommentService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentsController {

    final
    InterfaceCommentService commentService;

    public CommentsController(InterfaceCommentService commentService) {
        this.commentService = commentService;
    }

    @Operation(summary = "Operation to search all comments")
    @GetMapping("/")
    public ResponseEntity<List<Comment>> comments() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(commentService.findAll());
    }

    @Operation(summary = "Operation to search by id a comment")
    @GetMapping("/{id}")
    public ResponseEntity<Comment> comment(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(commentService.findById(id));
    }

    @Operation(summary = "Operation to filter by name or userId")
    @GetMapping("/filter")
    public ResponseEntity<List<Comment>> filterComment(
            @RequestParam(required = false) String name, @RequestParam(required = false) Long userId) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(commentService
                        .findByUserOrName(userId, name));
    }
}
