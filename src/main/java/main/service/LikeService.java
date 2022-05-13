package main.service;

import main.api.request.LikeRequest;
import main.api.response.ErrorsResponse;
import main.model.Post;
import main.model.PostVotes;
import main.repositories.PostRepository;
import main.repositories.PostVotesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;

@Service
public class LikeService {

    private final PostVotesRepository postVotesRepository;
    private final PostRepository postRepository;

    @Autowired
    public LikeService(PostVotesRepository postVotesRepository, PostRepository postRepository) {
        this.postVotesRepository = postVotesRepository;
        this.postRepository = postRepository;
    }

    public ErrorsResponse setLike(LikeRequest request, Principal principal, int value) {

        ErrorsResponse errorsResponse = new ErrorsResponse();

        Post post = postRepository.findById(request.getPostId()).orElseThrow();
        PostVotes postVotes = post.getPostVotesList().stream()
                .filter(postVotes1 -> postVotes1.getUserId() == Integer.parseInt(principal.getName()))
                .findFirst().orElse(null);
        if (postVotes == null) {
            postVotes = new PostVotes();
            postVotes.setUserId(Integer.parseInt(principal.getName()));
            postVotes.setPostsId(post);
            postVotes.setTime(LocalDateTime.now());
            postVotes.setValue(value);
            errorsResponse.setResult(true);
            postVotesRepository.save(postVotes);
        } else if (postVotes.getValue() == value) {
            errorsResponse.setResult(false);
            return errorsResponse;
        } else {

            postVotes.setValue(value);
            errorsResponse.setResult(true);
        }
        postVotesRepository.save(postVotes);
        return errorsResponse;
    }
}
