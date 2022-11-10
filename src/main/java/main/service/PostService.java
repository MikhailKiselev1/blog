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
import main.model.Tag2Post;
import main.model.User;
import main.model.enums.ModerationStatus;
import main.repositories.*;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
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
    private final Tag2PostRepository tag2PostRepository;

    @Autowired
    public PostService(TagRepository tagRepository, PostRepository postRepository,
                       UserRepository userRepository, Tag2PostRepository tag2PostRepository) {
        this.tagRepository = tagRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.tag2PostRepository = tag2PostRepository;
    }


    public PostGetResponse getPost(Integer offset, Integer limit, String mode) {
        PostGetResponse postsResponse = new PostGetResponse();
        List<PostDto> postDtoList = new ArrayList<>();
        List<Post> actionCurrentNewPosts = getSortedCollection(mode, offset, limit);
        if (!actionCurrentNewPosts.isEmpty()) {
            actionCurrentNewPosts.forEach(p -> postDtoList.add(addPostDto(p)));
        }
        postsResponse.setPostsDto(postDtoList);
        postsResponse.setCount(postDtoList.size());
        return postsResponse;
    }

    public PostGetResponse getPostSearch(Integer offset, Integer limit, String query) {
        if (query.matches("[\\s]") || query.equals("")) {
            return getPost(offset, limit, "recent");
        } else {
            PostGetResponse postsResponse = new PostGetResponse();
            List<PostDto> postDtoList = new ArrayList<>();
            getSortedCollection("recent", offset, limit).forEach(p -> {
                if (p.getText().toLowerCase(Locale.ROOT).contains(query)
                        || p.getTitle().toLowerCase(Locale.ROOT).contains(query)) {
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
        List<Post> filterDataList = getSortedCollection(mode, offset, limit).stream()
                .filter(post -> post.getTime().format(day).equals(date)).collect(Collectors.toList());
        if (!filterDataList.isEmpty()) {
            filterDataList.forEach(p -> postDtoList.add(addPostDto(p)));
        }
        postsResponse.setPostsDto(postDtoList);
        postsResponse.setCount(postDtoList.size());
        return postsResponse;
    }

    public PostGetResponse getPostByTag(Integer offset, Integer limit, String mode, String tag) {
        PostGetResponse postsResponse = new PostGetResponse();
        List<PostDto> postDtoList = new ArrayList<>();
        Tag searchTag = tagRepository.findByName(tag).orElseThrow();
        List<Post> filterDataList = getSortedCollection(mode, offset, limit).stream()
                .filter(post -> post.getTagsList().contains(searchTag)).collect(Collectors.toList());
        if (!filterDataList.isEmpty()) {
            filterDataList.forEach(p -> postDtoList.add(addPostDto(p)));
        }
        postsResponse.setPostsDto(postDtoList);
        postsResponse.setCount(postDtoList.size());
        return postsResponse;
    }

    public PostIdResponse getPostById(int id, Principal principal) {
        PostIdResponse postIdResponse = new PostIdResponse();
        User user = userRepository.findById(Integer.parseInt(principal.getName())).orElse(null);
        Post post = postRepository.findById(id).orElseThrow();

        if (user.getIsModerator() != 1 && !post.getUser().equals(user)) {
            post = postRepository.getActionCurrentNewPostsById(id).orElseThrow();
            post.setViewCount(post.getViewCount() + 1);
        }

        postIdResponse.setId(post.getId());
        postIdResponse.setTimestamp(post.getTime().atZone(ZoneOffset.UTC).toEpochSecond());
        PostUserDto postUserDto = new PostUserDto();
        User postUser = post.getUser();
        postUserDto.setId(postUser.getId());
        postUserDto.setName(postUser.getName());
        postIdResponse.setUser(postUserDto);
        postIdResponse.setTitle(post.getTitle());
        postIdResponse.setText(post.getText());
        postIdResponse.setLikeCount((int) post.getPostVotesList().stream().filter(u -> u.getValue() == 1).count());
        postIdResponse.setDislikeCount((int) post.getPostVotesList().stream().filter(u -> u.getValue() == -1).count());
        postIdResponse.setCommentCount(post.getPostComments().size());
        postIdResponse.setViewCount(post.getViewCount());
        //--------- Создание Списка дто комментариев
        List<PostCommentsDto> postCommentsDtoList = new ArrayList<>();
        post.getPostComments().forEach(postComments -> {
            PostCommentsDto postCommentsDto = new PostCommentsDto();
            postCommentsDto.setId(postComments.getId());
            postCommentsDto.setTimestamp(postComments.getTime().getNano());
            postCommentsDto.setText(postComments.getText());
            //----Создание дто пользователя
            PostCommentsUserDto userDto = new PostCommentsUserDto();
            User dtoUser = postComments.getUserId();
            userDto.setId(dtoUser.getId());
            userDto.setName(dtoUser.getName());
            userDto.setPhoto(dtoUser.getPhoto());
            postCommentsDto.setUser(userDto);
            postCommentsDtoList.add(postCommentsDto);
            //----пользователь
        });
        postIdResponse.setComments(postCommentsDtoList);
        //--------список дто комментов
        postIdResponse.setTags(post.getTagsList().stream().map(Tag::getName).collect(Collectors.toList()));
        postRepository.saveAndFlush(post);
        return postIdResponse;
    }

    public PostGetResponse getMyPosts(Integer offset, Integer limit, String status, Principal principal) {
        PostGetResponse postsResponse = new PostGetResponse();
        List<PostDto> postDtoList = new ArrayList<>();
        User user = userRepository.findById(Integer.parseInt(principal.getName())).orElseThrow();
        List<Post> allMyPosts = getMySortedCollection(status, offset, limit, user.getId());
        if (!allMyPosts.isEmpty()) {
            allMyPosts.forEach(p -> postDtoList.add(addPostDto(p)));
        }
        postsResponse.setPostsDto(postDtoList);
        postsResponse.setCount(postDtoList.size());
        return postsResponse;
    }

    public PostGetResponse getPostModeration(Integer offset, Integer limit, String status, Principal principal) {
        PostGetResponse postsResponse = new PostGetResponse();
        List<PostDto> postDtoList = new ArrayList<>();
        ArrayList<Post> allModerationPosts = (ArrayList<Post>) getModeratorSortedCollection(status, offset, limit, principal);
        if (!allModerationPosts.isEmpty()) {
            allModerationPosts
                    .forEach(p -> postDtoList.add(addPostDto(p)));
        }
        postsResponse.setPostsDto(postDtoList);
        postsResponse.setCount(postDtoList.size());
        return postsResponse;
    }

    public ErrorsResponse setPost(PostRequest postRequest, Principal principal) {

        HashMap<String, String> errors = new HashMap<>();
        if (postRequest.getTitle() == null) {
            errors.put("title", "Заголовок не установлен");
        } else if (postRequest.getTitle().length() < 3) {
            errors.put("title", "Заголовок слишком короткий");
        } else if (postRequest.getTitle().length() > 50) {
            errors.put("title", "Заголовок слишком длинный");
        }

        if (postRequest.getText() == null) {
            errors.put("text", "Текст не установлен");
        } else if (postRequest.getText().length() < 3) {
            errors.put("text", "Текст слишком короткий");
        }

        if (errors.isEmpty()) {
            Post post = new Post();
            post.setIsActive(postRequest.getActive());
            post.setModerationStatus(ModerationStatus.NEW);
            post.setUser(userRepository.findById(Integer.parseInt(principal.getName())).orElseThrow());
            //перевожу время из секунд в обьект класса LocalDateTime
            LocalDateTime postTime = LocalDateTime.ofEpochSecond
                    (postRequest.getTimestamp(), 0, ZoneOffset.UTC);
            post.setTime(LocalDateTime.now(ZoneOffset.UTC).isAfter(postTime) ?
                    LocalDateTime.now(ZoneOffset.UTC) : postTime);
            post.setTitle(postRequest.getTitle());
            post.setText(postRequest.getText());
            post.setViewCount(0);
            setTags(postRequest.getTags(), post);
            postRepository.saveAndFlush(post);
        }
        return ErrorsResponse.builder()
                .result(false)
                .errors(errors)
                .build();
    }

    public ErrorsResponse editPost(int id, PostRequest postRequest, Principal principal) {

        HashMap<String, String> errors = new HashMap<>();

        Post post = postRepository.findById(id).orElseThrow();
        User user = userRepository.findById(Integer.parseInt(principal.getName())).orElseThrow();


        if (postRequest.getTitle() == null) {
            errors.put("title", "Заголовок не установлен");
        } else if (postRequest.getTitle().length() < 3) {
            errors.put("title", "Заголовок слишком короткий");
        } else if (postRequest.getTitle().length() > 50) {
            errors.put("title", "Заголовок слишком длинный");
        }

        if (postRequest.getText() == null) {
            errors.put("text", "Текст не установлен");
        } else if (postRequest.getText().length() < 3) {
            errors.put("text", "Текст слишком короткий");
        }

        if (user.getIsModerator() != 1 && !post.getUser().equals(user)) {
            errors.put("user", "Автор поста другой пользователь");
        }

        if (!errors.isEmpty()) {
            return ErrorsResponse.builder()
                    .result(false)
                    .errors(errors)
                    .build();
        } else {
            post.setIsActive(postRequest.getActive());
            //перевожу время из секунд в обьект класса LocalDateTime
            LocalDateTime postTime = LocalDateTime.ofEpochSecond
                    (postRequest.getTimestamp(), 0, ZoneOffset.UTC);
            post.setTime(LocalDateTime.now(ZoneOffset.UTC).isAfter(postTime) ?
                    LocalDateTime.now(ZoneOffset.UTC) : postTime);
            post.setTitle(postRequest.getTitle());
            post.setText(postRequest.getText());
            post.setViewCount(0);
            post.setModerationStatus(user.getIsModerator() == 1 ?
                    post.getModerationStatus() : ModerationStatus.NEW);

            post.getTagsList().forEach(tag -> {
                if (!postRequest.getTags().contains(tag)) {
                    Tag2Post tag2Post = tag2PostRepository.findTag2Post(post.getId(), tag.getId()).orElseThrow();
                    tag2PostRepository.delete(tag2Post);
                }
            });
            setTags(postRequest.getTags(), post);
            postRequest.getTags().forEach(System.out::println);
            postRepository.saveAndFlush(post);
            return ErrorsResponse.builder()
                    .result(true)
                    .build();
        }
    }

    private PostDto addPostDto(Post p) {
        PostDto postDto = new PostDto();
        postDto.setId(p.getId());
        postDto.setTimestamp(p.getTime().atZone(ZoneOffset.UTC).toEpochSecond());
        PostUserDto postUserDto = new PostUserDto();
        User user = p.getUser();
        postUserDto.setId(user.getId());
        postUserDto.setName(user.getName());
        postDto.setUser(postUserDto);
        postDto.setTitle(p.getTitle());
        String parseText = Jsoup.clean(p.getText(), Safelist.none()).replaceAll("&nbsp;", " ");
        postDto.setAnnounce(parseText.length() > 147 ? parseText.substring(0, 147) + "..."
                : parseText);
        postDto.setLikeCount((int) p.getPostVotesList().stream().filter(u -> u.getValue() == 1).count());
        postDto.setDislikeCount((int) p.getPostVotesList().stream().filter(u -> u.getValue() == -1).count());
        postDto.setCommentCount(p.getPostComments().size());
        postDto.setViewCount(p.getViewCount());
        return postDto;
    }

    private void setTags(List<String> listTags, Post post) {
        listTags.forEach(tags -> {
            Tag repositoryTag = tagRepository.findByName((tags)).orElse(null);
            if (repositoryTag == null) {
                repositoryTag = new Tag();
                repositoryTag.setName(tags);
                List<Post> posts = new ArrayList<>();
                posts.add(post);
                repositoryTag.setPostsWithTags(posts);
                tagRepository.save(repositoryTag);
            } else {
                List<Post> posts = repositoryTag.getPostsWithTags();
                posts.add(post);
                repositoryTag.setPostsWithTags(posts);
                tagRepository.save(repositoryTag);
            }
        });
    }


    private List<Post> getSortedCollection(String mode, Integer offset, Integer limit) {
        List<Post> startList;

        switch (mode) {
            case "recent":
                startList = (List<Post>) postRepository.findAllPostOrderByRecent();
                break;
            case "popular":
                startList = (List<Post>) postRepository.findAllPostOrderByPopular();
                break;
            case "best":
                startList = (List<Post>) postRepository.findAllPostOrderByBest();
                break;
            case "early":
                startList = (List<Post>) postRepository.findAllPostOrderByEarly();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + mode);
        }

        return getFinishList(startList, offset, limit);
    }

    private List<Post> getMySortedCollection(String status, Integer offset,
                                             Integer limit, int userId) {
        List<Post> startList;


        switch (status) {
            case "inactive":
                startList = (List<Post>) postRepository.findMyPostOrderByInactive(userId);
                break;
            case "pending":
                startList = (List<Post>) postRepository.findMyPostOrderByPending(userId);
                break;
            case "declined":
                startList = (List<Post>) postRepository.findMyPostOrderByDeclined(userId);
                break;
            case "published":
                startList = (List<Post>) postRepository.findMyPostOrderByPublished(userId);;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + status);
        }

        return getFinishList(startList, offset, limit);
    }

    private List<Post> getModeratorSortedCollection(String status, Integer offset, Integer limit, Principal principal) {
        List<Post> startList;
        int adminId = Integer.parseInt(principal.getName());

        switch (status) {
            case "new":
                startList = (List<Post>) postRepository.findAllNewPostByModerate();
                break;
            case "declined":
                startList = (List<Post>) postRepository.findAllDeclinedPostByModerate(adminId);
                break;
            case "accepted":
                startList = (List<Post>) postRepository.findAllAcceptedPostByModerate(adminId);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + status);
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
