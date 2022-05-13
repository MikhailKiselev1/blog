package main.service;

import main.api.request.PostCommentRequest;
import main.api.response.PostCommentResponse;
import main.model.PostComments;
import main.repositories.PostCommentsRepository;
import main.repositories.PostRepository;
import main.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.HashMap;

@Service
public class PostCommentsService {

    private final PostRepository postRepository;
    private final PostCommentsRepository postCommentsRepository;
    private final UserRepository userRepository;

    @Autowired
    public PostCommentsService(PostRepository postRepository, PostCommentsRepository postCommentsRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.postCommentsRepository = postCommentsRepository;
        this.userRepository = userRepository;
    }

    public PostCommentResponse postComments(PostCommentRequest request, Principal principal) {
        PostCommentResponse postCommentResponse = new PostCommentResponse();
        if (request.getText().length() < 3) {
            postCommentResponse.setResult(false);
            HashMap<String, String> errors = new HashMap<>();
            errors.put("text", "Текст комментария не задан или слишком короткий");
            postCommentResponse.setErrors(errors);
            return postCommentResponse;
        }
        PostComments postComments = new PostComments();
        postCommentResponse.setId(postComments.getId());

        if (request.getParentId() == 0) {
            postComments.setParentId(null);
        } else {
            postComments.setParentId(request.getParentId());
        }

        postComments.setPostId(postRepository.findById(request.getPostId()).get());
        postComments.setUserId(userRepository.findById(Integer.parseInt(principal.getName())).get());
        postComments.setTime(LocalDateTime.now());
        postComments.setText(request.getText());
        postCommentsRepository.save(postComments);

        return postCommentResponse;
    }
}
