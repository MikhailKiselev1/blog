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
        List<PostVotes> myPostList = (List<PostVotes>) postVotesRepository.findAllByUserId(Integer.parseInt(principal.getName()));

        statisticResponse.setPostsCount(postRepository.findAllByUserId(Integer.parseInt(principal.getName())).size());
        statisticResponse.setLikesCount((int) myPostList.stream().filter(postVotes -> postVotes.getValue() == 1).count());
        statisticResponse.setDislikesCount((int) myPostList.stream().filter(postVotes -> postVotes.getValue() == -1).count());
        statisticResponse.setFirstPublication(myPostList.stream().min(Comparator.comparing(PostVotes::getTime))
                .orElseThrow().getTime().getNano());
        statisticResponse.setViewsCount(postRepository.findAllByUserId(Integer.parseInt(principal.getName()))
                .stream().map(Post::getViewCount).reduce(Integer::sum).orElseThrow());

        return statisticResponse;
    }

    public StatisticResponse getStatisticAll() {

        StatisticResponse statisticResponse = new StatisticResponse();
        List<PostVotes> myPostList = postVotesRepository.findAll();

        statisticResponse.setPostsCount(postRepository.findAll().size());
        statisticResponse.setLikesCount((int) myPostList.stream().filter(postVotes -> postVotes.getValue() == 1).count());
        statisticResponse.setDislikesCount((int) myPostList.stream().filter(postVotes -> postVotes.getValue() == -1).count());
        statisticResponse.setFirstPublication(myPostList.stream().min(Comparator.comparing(PostVotes::getTime))
                .orElseThrow().getTime().getNano());
        statisticResponse.setViewsCount(postRepository.findAll()
                .stream().map(Post::getViewCount).reduce(Integer::sum).orElseThrow());

        return statisticResponse;
    }
}
