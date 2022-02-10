package main.model.service;

import main.api.response.PostResponce;
import main.api.response.PostsResponce;
import main.api.response.UserResponse;
import main.model.User;
import main.model.repositories.CustomizedPostsImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PostService {

    PostsResponce postsResponce;
    CustomizedPostsImpl customizedPosts;
    List<PostResponce> postResponceList;
    UserResponse userResponse;
    User user;

    public PostsResponce getPost() {
        postsResponce = new PostsResponce();
        customizedPosts = new CustomizedPostsImpl();
        postResponceList = new ArrayList<>();
        customizedPosts.getActionCurrentNewPosts().stream().forEach(p -> {
            PostResponce postResponce = new PostResponce();
            postResponce.setId(p.getId());
            postResponce.setTimestamp(p.getTime());
            userResponse = new UserResponse();
            user = p.getUserId();
            userResponse.setId(user.getId());
            userResponse.setName(user.getName());
            postResponce.setUser(userResponse);
            postResponce.setTitle(p.getTitle());
            postResponce.setAnnounce(p.getText());
            postResponce.setLikeCount((int) p.getPostVotesList().stream().filter(u -> u.getValue() == 1).count());
            postResponce.setDislikeCount((int) p.getPostVotesList().stream().filter(u -> u.getValue() == -1).count());
            postResponce.setCommentCount(p.getPostComments().size());
            postResponce.setViewCount(p.getViewCount());
            postResponceList.add(postResponce);
        });
        postsResponce.setPostResponces(postResponceList);
        postsResponce.setCount(postResponceList.size());
        return postsResponce;
    }
}
