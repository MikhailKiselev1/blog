package main.service;

import main.api.response.StatisticResponse;
import main.model.Post;
import main.model.PostVotes;
import main.repositories.PostRepository;
import main.repositories.PostVotesRepository;
import main.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Comparator;
import java.util.List;

@Service
public class StatisticService {

    private final PostRepository postRepository;
    private final PostVotesRepository postVotesRepository;

    @Autowired
    public StatisticService(PostRepository postRepository, PostVotesRepository postVotesRepository) {
        this.postRepository = postRepository;
        this.postVotesRepository = postVotesRepository;
    }

    public StatisticResponse getStatisticMy(Principal principal) {

        StatisticResponse statisticResponse = new StatisticResponse();
        List<PostVotes> myPostList = postVotesRepository.findByUserId(Integer.parseInt(principal.getName()));
        List<Post> posts = (List<Post>) postRepository.findByUserId(Integer.parseInt(principal.getName()));

        statisticResponse.setPostsCount(posts == null ? 0 : posts.size());
        statisticResponse.setLikesCount(myPostList == null ? 0
                : (int) myPostList.stream().filter(postVotes -> postVotes.getValue() == 1).count());
        statisticResponse.setDislikesCount(myPostList == null ? 0
                : (int) myPostList.stream().filter(postVotes -> postVotes.getValue() == -1).count());
        statisticResponse.setFirstPublication(myPostList.stream().min(Comparator.comparing(PostVotes::getTime))
                .orElseThrow().getTime().getNano());
        statisticResponse.setViewsCount(posts
                .stream().map(Post::getViewCount).reduce(Integer::sum).orElse(0));

        return statisticResponse;
    }

    public StatisticResponse getStatisticAll() {

        StatisticResponse statisticResponse = new StatisticResponse();
        List<PostVotes> myPostList = postVotesRepository.findAll();
        List<Post> posts = postRepository.findAll();

        statisticResponse.setPostsCount(posts == null ? 0 : posts.size());
        statisticResponse.setLikesCount(myPostList == null ? 0
                : (int) myPostList.stream().filter(postVotes -> postVotes.getValue() == 1).count());
        statisticResponse.setDislikesCount(myPostList == null ? 0
                : (int) myPostList.stream().filter(postVotes -> postVotes.getValue() == -1).count());
        statisticResponse.setFirstPublication(myPostList.stream().min(Comparator.comparing(PostVotes::getTime))
                .orElseThrow().getTime().getNano());
        statisticResponse.setViewsCount(postRepository.findAll()
                .stream().map(Post::getViewCount).reduce(Integer::sum).orElseThrow());

        return statisticResponse;
    }
}
