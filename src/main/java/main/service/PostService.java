package main.service;

import main.api.response.PostIdResponse;
import main.api.response.dto.PostCommentsDto;
import main.api.response.dto.PostCommentsUserDto;
import main.api.response.dto.PostDto;
import main.api.response.PostsResponse;
import main.api.response.dto.PostUserDto;
import main.model.Post;
import main.model.Tag;
import main.model.User;
import main.model.enums.ModerationStatus;
import main.repositories.CustomizedPostsImpl;
import main.repositories.PostRepository;
import main.repositories.TagRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PostService {

    private PostsResponse postsResponse;
    private CustomizedPostsImpl customizedPosts;
    private List<PostDto> postDtoList;
    private PostUserDto postUserDto;
    private User user;
    private int count;
    private final DateTimeFormatter day = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private TagRepository tagRepository;
    private PostRepository postRepository;

    public PostsResponse getPost(Integer offset, Integer limit, String mode) {
        postsResponse = new PostsResponse();
        postDtoList = new ArrayList<>();
        PostDto postDto = new PostDto();
        postDto.setId(345);
        postDto.setTimestamp(LocalDateTime.now());
        postUserDto = new PostUserDto();
        postUserDto.setId(88);
        postUserDto.setName("Дмитрий петров");
        postDto.setTitle("Заголовок поста");
        postDto.setAnnounce("Тект анонса поста");
        postDto.setLikeCount(35);
        postDto.setDislikeCount(3);
        postDto.setCommentCount(15);
        postDto.setViewCount(55);
        postDtoList.add(postDto);
        postsResponse.setCount(postDtoList.size());
        postsResponse.setPostsDto(postDtoList);


        return postsResponse;
    }

//    public PostsResponse getPost(Integer offset, Integer limit, String mode) {
//
//        postsResponse = new PostsResponse();
//        customizedPosts = new CustomizedPostsImpl();
//        postDtoList = new ArrayList<>();
//        getSortedCollection(mode, customizedPosts.getActionCurrentNewPosts(), offset, limit)
//            .stream().forEach(p -> postDtoList.add(addPostDto(p)));
//        postsResponse.setPostsDto(postDtoList);
//        postsResponse.setCount(count);
//        return postsResponse;
//    }
//
//    public PostsResponse getPostSearch(Integer offset, Integer limit, String mode, String query) {
//        if (query.matches("[\\s]") || query.equals("")) {
//            return getPost(offset, limit, mode);
//        }
//        else {
//            postsResponse = new PostsResponse();
//            customizedPosts = new CustomizedPostsImpl();
//            postDtoList = new ArrayList<>();
//            getSortedCollection(mode, customizedPosts.getActionCurrentNewPosts(), offset, limit).stream().forEach(p -> {
//                if(p.getText().matches(query)) {
//                    postDtoList.add(addPostDto(p));
//                }
//            });
//            postsResponse.setPostsDto(postDtoList);
//            postsResponse.setCount(postDtoList.size());
//            return postsResponse;
//        }
//    }
//
//    public PostsResponse getPostByDate (Integer offset, Integer limit, String mode, String date) {
//        postsResponse = new PostsResponse();
//        customizedPosts = new CustomizedPostsImpl();
//        postDtoList = new ArrayList<>();
//        List<Post> filterDataList = customizedPosts.getActionCurrentNewPosts().stream()
//                .filter(post -> post.getTime().format(day).equals(date)).collect(Collectors.toList());
//        getSortedCollection(mode, filterDataList, offset, limit)
//                .stream().forEach(p -> postDtoList.add(addPostDto(p)));
//        postsResponse.setPostsDto(postDtoList);
//        postsResponse.setCount(count);
//        return postsResponse;
//    }
//
//    public PostsResponse getPostByTag (Integer offset, Integer limit, String mode, String tag) {
//        postsResponse = new PostsResponse();
//        customizedPosts = new CustomizedPostsImpl();
//        postDtoList = new ArrayList<>();
//        List<Tag> tagList = (List<Tag>) tagRepository.findAll();
//        Tag searchTag = tagList.stream().filter(t -> t.getName().equals(tag)).findFirst().get();
//        List<Post> filterDataList = customizedPosts.getActionCurrentNewPosts().stream()
//                .filter(post -> post.getTagsList().contains(searchTag)).collect(Collectors.toList());
//        getSortedCollection(mode, filterDataList, offset, limit)
//                .forEach(p -> postDtoList.add(addPostDto(p)));
//        postsResponse.setPostsDto(postDtoList);
//        postsResponse.setCount(count);
//        return postsResponse;
//    }
//
//    public PostIdResponse getPostById (int id) {
//        PostIdResponse postIdResponse = new PostIdResponse();
//        Post post = postRepository.findById((long) id).get();
//        post.setViewCount(post.getViewCount() + 1);
//        // добавляю просмотр
//        postIdResponse.setId(post.getId());
//        postIdResponse.setTimestamp(post.getTime());
//        postUserDto = new PostUserDto();
//        user = post.getUserId();
//        postUserDto.setId(user.getId());
//        postUserDto.setName(user.getName());
//        postIdResponse.setUser(postUserDto);
//        postIdResponse.setTitle(post.getTitle());
//        postIdResponse.setAnnounce(post.getText());
//        postIdResponse.setLikeCount((int) post.getPostVotesList().stream().filter(u -> u.getValue() == 1).count());
//        postIdResponse.setDislikeCount((int) post.getPostVotesList().stream().filter(u -> u.getValue() == -1).count());
//        postIdResponse.setCommentCount(post.getPostComments().size());
//        postIdResponse.setViewCount(post.getViewCount());
//        //--------- Создание Списка дто комментариев
//        List<PostCommentsDto> postCommentsDtoList = new ArrayList<>();
//        post.getPostComments().forEach( postComments -> {
//            PostCommentsDto postCommentsDto = new PostCommentsDto();
//            postCommentsDto.setId(postComments.getId());
//            postCommentsDto.setTimestamp(postComments.getTime().getTime());
//            postCommentsDto.setText(postComments.getText());
//            //----Создание дто пользователя
//            PostCommentsUserDto userDto = new PostCommentsUserDto();
//            User user = postComments.getUserId();
//            userDto.setId(user.getId());
//            userDto.setName(user.getName());
//            userDto.setPhoto(user.getPhoto());
//            postCommentsDto.setUser(userDto);
//            postCommentsDtoList.add(postCommentsDto);
//            //----
//        });
//        postIdResponse.setComments(postCommentsDtoList);
//        //--------
//        postIdResponse.setTags(post.getTagsList().stream().map(tag -> tag.getName()).collect(Collectors.toList()));
//        return postIdResponse;
//    }
//
//    private PostDto addPostDto(Post p) {
//        PostDto postDto = new PostDto();
//        postDto.setId(p.getId());
//        postDto.setTimestamp(p.getTime());
//        postUserDto = new PostUserDto();
//        user = p.getUserId();
//        postUserDto.setId(user.getId());
//        postUserDto.setName(user.getName());
//        postDto.setUser(postUserDto);
//        postDto.setTitle(p.getTitle());
//        postDto.setAnnounce(p.getText());
//        postDto.setLikeCount((int) p.getPostVotesList().stream().filter(u -> u.getValue() == 1).count());
//        postDto.setDislikeCount((int) p.getPostVotesList().stream().filter(u -> u.getValue() == -1).count());
//        postDto.setCommentCount(p.getPostComments().size());
//        postDto.setViewCount(p.getViewCount());
//        return postDto;
//    }
//
//    private List<Post> getSortedCollection(String mode, List<Post> postList, Integer offset, Integer limit) {
//        List<Post> startList;
//        List<Post> finishList;
//
//        if (limit == null) {
//            limit = 10;
//        }
//        if (offset == null) {
//            offset = 0;
//        }
//
//        if (mode.equals("popular")) {
//            startList = postList.stream().filter(p -> p.getPostComments().size() > 0)
//                    .sorted(Comparator.comparing(p -> p.getPostComments().size()))
//                    .sorted(Collections.reverseOrder())
//                    .collect(Collectors.toList());
//        }
//        else if (mode.equals("best")) {
//            startList = postList.stream().filter(p -> p.getPostVotesList().size() > 0)
//                    .sorted(Comparator.comparing(p -> p.getPostVotesList().size()))
//                    .sorted(Collections.reverseOrder()).collect(Collectors.toList());
//        }
//        else if (mode.equals("early")) {
//            startList = postList.stream().sorted(Comparator.comparing(Post::getTime)).collect(Collectors.toList());
//        }
//        else {
//            mode = "recent";
//            startList = postList.stream().sorted(Comparator.comparing(Post::getTime))
//                    .sorted(Collections.reverseOrder()).collect(Collectors.toList());
//        }
//        finishList = new ArrayList<>();
//        for (int i = offset; i <= limit; i++) {
//            finishList.add(startList.get(i));
//        }
//        count = startList.size();
//        return finishList;
//
//    }
//
//    private List<Post> getRandomPost() {
//        List<Post> postList = new ArrayList<>();
//        for (int i = 0; i < 50; i++) {
//            Post post = new Post();
//            post.setId(i);
//            post.setIsActive(i % 2);
//            post.setModerationStatus(ModerationStatus.NEW);
//            post.setModeratorId(110);
//            User user = new User();
//            user.setId(i + 100);
//            user.setName("User " + (i + 100));
//            if(i == 10) {
//                user.setIsModerator(1);
//            }
//            user.setIsModerator(0);
//            LocalDateTime localDateTime = LocalDateTime.now();
//            user.setRegTime(localDateTime);
//            user.setEmail("user@test.ru");
//            user.setPassword("password");
//            post.setUserId(user);
//            post.setTime(localDateTime);
//            post.setTitle("Пост номер " + (i + 1));
//            post.setText("Текст Поста");
//            post.setViewCount(i);
//            postList.add(post);
//        }
//
//        return postList;
//    }
}
