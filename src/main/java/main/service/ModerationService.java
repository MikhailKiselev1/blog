package main.service;

import lombok.RequiredArgsConstructor;
import main.api.request.ModerationRequest;
import main.api.response.ModerationResponse;
import main.model.Post;
import main.model.enums.ModerationStatus;
import main.repositories.PostRepository;
import main.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.security.Principal;

/**
 * Service responsible for handling post moderation operations.
 */
@Service
@RequiredArgsConstructor
public class ModerationService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    /**
     * Handles the moderation decision for a post.
     *
     * @param request   the ModerationRequest containing the post ID and decision
     * @param principal the authenticated user principal
     * @return ModerationResponse indicating the status of the moderation decision
     */
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
