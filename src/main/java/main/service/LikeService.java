package main.service;

import lombok.RequiredArgsConstructor;
import main.api.request.LikeRequest;
import main.api.response.ErrorsResponse;
import main.model.Post;
import main.model.PostVotes;
import main.repositories.PostRepository;
import main.repositories.PostVotesRepository;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final PostVotesRepository postVotesRepository;
    private final PostRepository postRepository;


    public ErrorsResponse setLike(LikeRequest request, Principal principal, int value) {

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
            postVotesRepository.save(postVotes);
            return ErrorsResponse.builder()
                    .result(true)
                    .build();
        } else if (postVotes.getValue() == value) {
            return ErrorsResponse.builder().result(false).build();
        } else {
            postVotes.setValue(value);
            postVotesRepository.save(postVotes);
            return ErrorsResponse.builder().result(true).build();
        }
    }
}
