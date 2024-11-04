package com.nighthawk.spring_portfolio.mvc.messages;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentApiController {
    @Autowired
    private CommentJpaRepository commentRepository;

    @Autowired
    private MessageJpaRepository messageRepository;

    @GetMapping
    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }

    @PostMapping("/{messageId}")
    public ResponseEntity<Comment> createComment(@PathVariable Long messageId, @RequestBody Comment comment) {
        return messageRepository.findById(messageId).map(message -> {
            comment.setMessage(message);
            return ResponseEntity.ok(commentRepository.save(comment));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        if (commentRepository.existsById(id)) {
            commentRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
