package main.service;

import main.api.request.PostRequest;
import main.api.response.ErrorsResponse;
import main.api.response.PostIdResponse;
import main.api.response.dto.PostCommentsDto;
import main.api.response.dto.PostCommentsUserDto;
import main.api.response.dto.PostDto;
import main.api.response.PostGetResponse;
import main.api.response.dto.PostUserDto;
import main.model.Post;
import main.model.Tag;
import main.model.User;
import main.model.enums.ModerationStatus;
import main.repositories.PostRepository;
import main.repositories.TagRepository;
import main.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PostService {

    private final DateTimeFormatter day = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final TagRepository tagRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Autowired
    public PostService(TagRepository tagRepository, PostRepository postRepository,
                       UserRepository userRepository) {
        this.tagRepository = tagRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }


    public PostGetResponse getPost(Integer offset, Integer limit, String mode) {

        PostGetResponse postsResponse = new PostGetResponse();
        List<PostDto> postDtoList = new ArrayList<>();
        ArrayList<Post> actionCurrentNewPosts = (ArrayList<Post>) postRepository.getActionCurrentNewPosts();
        if (!actionCurrentNewPosts.isEmpty()) {
            getSortedCollection(mode, actionCurrentNewPosts, offset, limit)
                    .stream().forEach(p -> postDtoList.add(addPostDto(p)));
        }
        postsResponse.setPostsDto(postDtoList);
        postsResponse.setCount(postDtoList.size());
        return postsResponse;
    }

    public PostGetResponse getPostSearch(Integer offset, Integer limit, String mode, String query) {
        if (query.matches("[\\s]") || query.equals("")) {
            return getPost(offset, limit, mode);
        } else {
            PostGetResponse postsResponse = new PostGetResponse();
            ArrayList<Post> actionCurrentNewPosts = (ArrayList<Post>) postRepository.findAll();
            List<PostDto> postDtoList = new ArrayList<>();
            getSortedCollection(mode, actionCurrentNewPosts, offset, limit).stream().forEach(p -> {
                if (p.getText().matches(query)) {
                    postDtoList.add(addPostDto(p));
                }
            });
            postsResponse.setPostsDto(postDtoList);
            postsResponse.setCount(postDtoList.size());
            return postsResponse;
        }
    }

    public PostGetResponse getPostByDate(Integer offset, Integer limit, String mode, String date) {
        PostGetResponse postsResponse = new PostGetResponse();
        List<PostDto> postDtoList = new ArrayList<>();
        List<Post> filterDataList = postRepository.findAll().stream()
                .filter(post -> post.getTime().format(day).equals(date)).collect(Collectors.toList());
        if (!filterDataList.isEmpty()) {
            getSortedCollection(mode, filterDataList, offset, limit)
                    .stream().forEach(p -> postDtoList.add(addPostDto(p)));
        }
        postsResponse.setPostsDto(postDtoList);
        postsResponse.setCount(postDtoList.size());
        return postsResponse;
    }

    public PostGetResponse getPostByTag(Integer offset, Integer limit, String mode, String tag) {
        PostGetResponse postsResponse = new PostGetResponse();
        List<PostDto> postDtoList = new ArrayList<>();
        List<Tag> tagList = tagRepository.findAll();
        Tag searchTag = tagList.stream().filter(t -> t.getName().equals(tag)).findFirst().get();
        List<Post> filterDataList = postRepository.findAll().stream()
                .filter(post -> post.getTagsList().contains(searchTag)).collect(Collectors.toList());
        if (!filterDataList.isEmpty()) {
            getSortedCollection(mode, filterDataList, offset, limit)
                    .forEach(p -> postDtoList.add(addPostDto(p)));
        }
        postsResponse.setPostsDto(postDtoList);
        postsResponse.setCount(postDtoList.size());
        return postsResponse;
    }

    public PostIdResponse getPostById(int id) {
        PostIdResponse postIdResponse = new PostIdResponse();
        Post post = postRepository.findById(id).get();
        post.setViewCount(post.getViewCount() + 1);
        // добавляю просмотр
        postIdResponse.setId(post.getId());
        postIdResponse.setTimestamp(post.getTime().getNano());
        PostUserDto postUserDto = new PostUserDto();
        User user = post.getUserId();
        postUserDto.setId(user.getId());
        postUserDto.setName(user.getName());
        postIdResponse.setUser(postUserDto);
        postIdResponse.setTitle(post.getTitle());
        postIdResponse.setAnnounce(post.getText());
        postIdResponse.setLikeCount((int) post.getPostVotesList().stream().filter(u -> u.getValue() == 1).count());
        postIdResponse.setDislikeCount((int) post.getPostVotesList().stream().filter(u -> u.getValue() == -1).count());
        postIdResponse.setCommentCount(post.getPostComments().size());
        postIdResponse.setViewCount(post.getViewCount());
        //--------- Создание Списка дто комментариев
        List<PostCommentsDto> postCommentsDtoList = new ArrayList<>();
        post.getPostComments().forEach(postComments -> {
            PostCommentsDto postCommentsDto = new PostCommentsDto();
            postCommentsDto.setId(postComments.getId());
            postCommentsDto.setTimestamp(postComments.getTime().getTime());
            postCommentsDto.setText(postComments.getText());
            //----Создание дто пользователя
            PostCommentsUserDto userDto = new PostCommentsUserDto();
            User currentUser = postComments.getUserId();
            userDto.setId(currentUser.getId());
            userDto.setName(currentUser.getName());
            userDto.setPhoto(currentUser.getPhoto());
            postCommentsDto.setUser(userDto);
            postCommentsDtoList.add(postCommentsDto);
            //----пользователь
        });
        postIdResponse.setComments(postCommentsDtoList);
        //--------список дто комментов
        postIdResponse.setTags(post.getTagsList().stream().map(tag -> tag.getName()).collect(Collectors.toList()));
        return postIdResponse;
    }

    public PostGetResponse getMyPosts(Integer offset, Integer limit, String status) {
        PostGetResponse postsResponse = new PostGetResponse();
        List<PostDto> postDtoList = new ArrayList<>();
        ArrayList<Post> allPosts = (ArrayList<Post>) postRepository.findAll();
        if (!allPosts.isEmpty()) {
            getMySortedCollection(status, allPosts, offset, limit)
                    .stream().forEach(p -> postDtoList.add(addPostDto(p)));
        }
        postsResponse.setPostsDto(postDtoList);
        postsResponse.setCount(postDtoList.size());
        return postsResponse;
    }

    public PostGetResponse getPostModeration(Integer offset, Integer limit, String status, Principal principal) {
        PostGetResponse postsResponse = new PostGetResponse();
        List<PostDto> postDtoList = new ArrayList<>();
        ArrayList<Post> allPosts = (ArrayList<Post>) postRepository.findAll();
        if (!allPosts.isEmpty()) {
            getModeratorSortedCollection(status, allPosts, offset, limit, principal)
                    .stream().forEach(p -> postDtoList.add(addPostDto(p)));
        }
        postsResponse.setPostsDto(postDtoList);
        postsResponse.setCount(postDtoList.size());
        return postsResponse;
    }

    public ErrorsResponse postPost(PostRequest postRequest, Principal principal) {
        ErrorsResponse postAddResponse = new ErrorsResponse();
        HashMap<String, String> errors = new HashMap<>();
        if (postRequest.getTitle() == null) {
            errors.put("title", "Заголовок не установлен");
        }
        else if (postRequest.getTitle().length() < 3) {
            errors.put("title", "Заголовок слишком короткий");
        }

        if (postRequest.getText() == null) {
            errors.put("text", "Текст не установлен");
        }
        else if (postRequest.getText().length() < 3) {
            errors.put("text", "Текст слишком короткий");
        }

        if (!errors.isEmpty()) {
            postAddResponse.setResult(false);
            postAddResponse.setErrors(errors);
        }
        else {
            postAddResponse.setResult(true);
            Post post = new Post();
            post.setIsActive(postRequest.getActive());
            post.setModerationStatus(ModerationStatus.NEW);
            post.setUserId(userRepository.findByEmail(principal.getName()).get());
            //перевожу время из секунд в обьект класса LocalDateTime
            LocalDateTime postTime = LocalDateTime.ofEpochSecond
                    (postRequest.getTimestamp(), 0, ZoneOffset.UTC);
            post.setTime(LocalDateTime.now(ZoneOffset.UTC).isAfter(postTime) ?
                    LocalDateTime.now(ZoneOffset.UTC) : postTime);
            post.setTitle(postRequest.getTitle());
            post.setText(postRequest.getText());
            post.setViewCount(0);
            postRepository.saveAndFlush(post);
        }

            return postAddResponse;
    }

    public ErrorsResponse editPost(int id, PostRequest postRequest, Principal principal) {
        ErrorsResponse postAddResponse = new ErrorsResponse();
        HashMap<String, String> errors = new HashMap<>();
        Post post = postRepository.findById(id).get();
        if (postRequest.getTitle() == null) {
            errors.put("title", "Заголовок не установлен");
        }
        else if (postRequest.getTitle().length() < 3) {
            errors.put("title", "Заголовок слишком короткий");
        }

        if (postRequest.getText() == null) {
            errors.put("text", "Текст не установлен");
        }
        else if (postRequest.getText().length() < 3) {
            errors.put("text", "Текст слишком короткий");
        }

        if(!principal.getName().equals(post.getUserId().getEmail())) {
            errors.put("user", "Автор поста другой пользователь");
        }

        if (!errors.isEmpty()) {
            postAddResponse.setResult(false);
            postAddResponse.setErrors(errors);
        }
        else {
            postAddResponse.setResult(true);
            post.setIsActive(postRequest.getActive());
            //перевожу время из секунд в обьект класса LocalDateTime
            LocalDateTime postTime = LocalDateTime.ofEpochSecond
                    (postRequest.getTimestamp(), 0, ZoneOffset.UTC);
            post.setTime(LocalDateTime.now(ZoneOffset.UTC).isAfter(postTime) ?
                    LocalDateTime.now(ZoneOffset.UTC) : postTime);
            post.setTitle(postRequest.getTitle());
            post.setText(postRequest.getText());
            post.setViewCount(0);
            postRepository.saveAndFlush(post);
        }

        return postAddResponse;
    }

    private PostDto addPostDto(Post p) {
        PostDto postDto = new PostDto();
        postDto.setId(p.getId());
        postDto.setTimestamp(p.getTime().getNano());
        PostUserDto postUserDto = new PostUserDto();
        User user = p.getUserId();
        postUserDto.setId(user.getId());
        postUserDto.setName(user.getName());
        postDto.setUser(postUserDto);
        postDto.setTitle(p.getTitle());
        postDto.setAnnounce(p.getText());
        postDto.setLikeCount((int) p.getPostVotesList().stream().filter(u -> u.getValue() == 1).count());
        postDto.setDislikeCount((int) p.getPostVotesList().stream().filter(u -> u.getValue() == -1).count());
        postDto.setCommentCount(p.getPostComments().size());
        postDto.setViewCount(p.getViewCount());
        return postDto;
    }

    private List<Post> getSortedCollection(String mode, List<Post> postList, Integer offset, Integer limit) {
        List<Post> startList;
        List<Post> finishList;


        if (mode.equals("popular")) {
            startList = postList.stream().filter(p -> p.getPostComments().size() > 0)
                    .sorted(Comparator.comparing(p -> p.getPostComments().size()))
                    .sorted(Collections.reverseOrder())
                    .collect(Collectors.toList());
        } else if (mode.equals("best")) {
            startList = postList.stream().filter(p -> p.getPostVotesList().size() > 0)
                    .sorted(Comparator.comparing(p -> p.getPostVotesList().size()))
                    .sorted(Collections.reverseOrder()).collect(Collectors.toList());
        } else if (mode.equals("early")) {
            startList = postList.stream().sorted(Comparator.comparing(Post::getTime)).collect(Collectors.toList());
        } else {
            startList = postList.stream().sorted(Comparator.comparing(Post::getTime))
                    .sorted(Collections.reverseOrder()).collect(Collectors.toList());
        }
        finishList = new ArrayList<>();
        for (int i = offset; i <= limit; i++) {
            finishList.add(startList.get(i));
        }
        return finishList;
    }

    private List<Post> getMySortedCollection(String status, List<Post> postList, Integer offset, Integer limit) {
        List<Post> startList;
        List<Post> finishList;


        if (status.equals("inactive")) {
            startList = postList.stream().filter(p -> p.getIsActive() == 0)
                    .sorted(Comparator.comparing(p -> p.getTime()))
                    .sorted(Collections.reverseOrder())
                    .collect(Collectors.toList());
        } else if (status.equals("pending")) {
            startList = postList.stream().filter(p -> p.getIsActive() == 1 && p.getModerationStatus() == ModerationStatus.NEW)
                    .sorted(Comparator.comparing(p -> p.getTime()))
                    .sorted(Collections.reverseOrder()).collect(Collectors.toList());
        } else if (status.equals("declined")) {
            startList = postList.stream().filter(p -> p.getIsActive() == 1 && p.getModerationStatus() == ModerationStatus.DECLINED)
                    .sorted(Comparator.comparing(p -> p.getTime()))
                    .sorted(Collections.reverseOrder()).collect(Collectors.toList());
        } else {
            startList = postList.stream().filter(p -> p.getIsActive() == 1 && p.getModerationStatus() == ModerationStatus.ACCEPTED)
                    .sorted(Comparator.comparing(p -> p.getTime()))
                    .sorted(Collections.reverseOrder()).collect(Collectors.toList());
        }

        return getFinishList(startList, offset, limit);
    }

    private List<Post> getModeratorSortedCollection(String status, List<Post> postList, Integer offset, Integer limit, Principal principal) {
        List<Post> startList;
        List<Post> finishList;

        if (status.equals("new")) {
            startList = postList.stream().filter(p -> p.getModerationStatus() == ModerationStatus.NEW)
                    .sorted(Comparator.comparing(p -> p.getTime()))
                    .sorted(Collections.reverseOrder())
                    .collect(Collectors.toList());
        } else if (status.equals("declined")) {
            startList = postList.stream().filter(p -> p.getModerator().getEmail().equals(principal.getName())
                            && p.getModerationStatus() == ModerationStatus.DECLINED)
                    .sorted(Comparator.comparing(p -> p.getTime()))
                    .sorted(Collections.reverseOrder()).collect(Collectors.toList());
        } else {
            startList = postList.stream().filter(p -> p.getModerator().getEmail().equals(principal.getName())
                            && p.getModerationStatus() == ModerationStatus.ACCEPTED)
                    .sorted(Comparator.comparing(p -> p.getTime()))
                    .sorted(Collections.reverseOrder()).collect(Collectors.toList());
        }

        return getFinishList(startList, offset, limit);
    }

    //Данный метод выдает посты, согласно настройкам
    private List<Post> getFinishList(List<Post> startList, int offset, int limit) {
        if (startList.size() < limit) {
            limit = startList.size();
        }
        ArrayList<Post> finishList = new ArrayList<>();
        for (int i = offset; i < limit; i++) {
            finishList.add(startList.get(i));
        }
        return finishList;
    }
}
