package main.service;

import main.api.request.ModerationRequest;
import main.api.response.ModerationResponse;
import main.model.Post;
import main.model.enums.ModerationStatus;
import main.repositories.PostRepository;
import main.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public class ModerationService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Autowired
    public ModerationService(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public ModerationResponse postModeration(ModerationRequest request, Principal principal) {
        ModerationResponse moderationResponse = new ModerationResponse();
        try {
            Post post = postRepository.findById(request.getPostId()).orElseThrow();
            post.setModerationStatus(ModerationStatus.fromString(request.getDecision()));
            post.setModerator(userRepository.findById(Integer.parseInt(principal.getName())).orElseThrow());
            postRepository.save(post);
            moderationResponse.setResult(true);
        } catch (Exception ex) {
            moderationResponse.setResult(false);
            ex.printStackTrace();
        }

        return moderationResponse;
    }
}
