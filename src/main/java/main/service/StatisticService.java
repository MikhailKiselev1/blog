package main.service;

import lombok.RequiredArgsConstructor;
import main.api.response.StatisticResponse;
import main.model.Post;
import main.model.PostVotes;
import main.repositories.PostRepository;
import main.repositories.PostVotesRepository;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.ZoneOffset;
import java.util.Comparator;
import java.util.List;

/**
 * Service responsible for providing statistical information about posts and votes.
 */
@Service
@RequiredArgsConstructor
public class StatisticService {

    private final PostRepository postRepository;
    private final PostVotesRepository postVotesRepository;

    /**
     * Retrieves the statistical information for the authenticated user's posts and votes.
     *
     * @param principal the authenticated user principal
     * @return StatisticResponse containing the statistical information
     */
    public StatisticResponse getStatisticMy(Principal principal) {

        StatisticResponse statisticResponse = new StatisticResponse();
        List<PostVotes> myPostVotesList = postVotesRepository.findByUserId(Integer.parseInt(principal.getName()));
        List<Post> posts = (List<Post>) postRepository.findByUserId(Integer.parseInt(principal.getName()));
        Post firstPost = posts.stream().min(Comparator.comparing(Post::getTime)).orElse(null);

        statisticResponse.setPostsCount(posts == null ? 0 : posts.size());
        statisticResponse.setLikesCount(myPostVotesList == null ? 0
                : (int) myPostVotesList.stream().filter(postVotes -> postVotes.getValue() == 1).count());
        statisticResponse.setDislikesCount(myPostVotesList == null ? 0
                : (int) myPostVotesList.stream().filter(postVotes -> postVotes.getValue() == -1).count());
        statisticResponse.setFirstPublication(firstPost == null ? 0 : posts.stream().min(Comparator.comparing(Post::getTime))
                .orElseThrow().getTime().atZone(ZoneOffset.UTC).toEpochSecond());
        statisticResponse.setViewsCount(posts
                .stream().map(Post::getViewCount).reduce(Integer::sum).orElse(0));

        return statisticResponse;
    }

    /**
     * Retrieves the overall statistical information for all posts and votes.
     *
     * @return StatisticResponse containing the overall statistical information
     */
    public StatisticResponse getStatisticAll() {

        StatisticResponse statisticResponse = new StatisticResponse();
        List<PostVotes> myPostList = postVotesRepository.findAll();
        List<Post> posts = postRepository.findAll();

        statisticResponse.setPostsCount(posts == null ? 0 : posts.size());
        statisticResponse.setLikesCount(myPostList == null ? 0
                : (int) myPostList.stream().filter(postVotes -> postVotes.getValue() == 1).count());
        statisticResponse.setDislikesCount(myPostList == null ? 0
                : (int) myPostList.stream().filter(postVotes -> postVotes.getValue() == -1).count());
        statisticResponse.setFirstPublication(posts.stream().min(Comparator.comparing(Post::getTime))
                .orElseThrow().getTime().atZone(ZoneOffset.UTC).toEpochSecond());
        statisticResponse.setViewsCount(postRepository.findAll()
                .stream().map(Post::getViewCount).reduce(Integer::sum).orElseThrow());

        return statisticResponse;
    }
}
