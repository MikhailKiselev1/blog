package main.service;

import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class PostCommentsService {

    private final PostRepository postRepository;
    private final PostCommentsRepository postCommentsRepository;
    private final UserRepository userRepository;


    public PostCommentResponse postComments(PostCommentRequest request, Principal principal) {
        if (request.getText().length() < 3) {
            HashMap<String, String> errors = new HashMap<>();
            errors.put("text", "Текст комментария не задан или слишком короткий");

            return PostCommentResponse.builder()
                    .result(false)
                    .errors(errors)
                    .build();
        }
        PostComments postComments = new PostComments();

        if (request.getParentId() == 0) {
            postComments.setParentId(null);
        } else {
            postComments.setParentId(request.getParentId());
        }

        postComments.setPostId(postRepository.findById(request.getPostId()).orElseThrow());
        postComments.setUserId(userRepository.findById(Integer.parseInt(principal.getName())).orElseThrow());
        postComments.setTime(LocalDateTime.now());
        postComments.setText(request.getText());
        postCommentsRepository.save(postComments);

        return PostCommentResponse.builder()
                .id(postComments.getId())
                .build();
    }
}
