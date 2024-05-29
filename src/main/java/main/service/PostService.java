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

/**
 * Service responsible for handling posts.
 */
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


    /**
     * Gets posts based on the specified parameters.
     *
     * @param offset the offset for pagination
     * @param limit  the limit for pagination
     * @param mode   the mode for sorting (recent, popular, best, early)
     * @return PostGetResponse containing the list of posts and count
     */
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

    /**
     * Gets posts based on search query.
     *
     * @param offset the offset for pagination
     * @param limit  the limit for pagination
     * @param query  the search query
     * @return PostGetResponse containing the list of posts and count
     */
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

    /**
     * Gets posts based on date.
     *
     * @param offset the offset for pagination
     * @param limit  the limit for pagination
     * @param mode   the mode for sorting (recent, popular, best, early)
     * @param date   the date to filter posts
     * @return PostGetResponse containing the list of posts and count
     */
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

    /**
     * Gets posts based on tag.
     *
     * @param offset the offset for pagination
     * @param limit  the limit for pagination
     * @param mode   the mode for sorting (recent, popular, best, early)
     * @param tag    the tag to filter posts
     * @return PostGetResponse containing the list of posts and count
     */
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

    /**
     * Gets a specific post by ID.
     *
     * @param id        the ID of the post
     * @param principal the authenticated user principal
     * @return PostIdResponse containing the details of the post
     */
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

    /**
     * Gets posts authored by the authenticated user.
     *
     * @param offset    the offset for pagination
     * @param limit     the limit for pagination
     * @param status    the status of the posts (inactive, pending, declined, published)
     * @param principal the authenticated user principal
     * @return PostGetResponse containing the list of user's posts and count
     */
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

    /**
     * Gets posts for moderation.
     *
     * @param offset    the offset for pagination
     * @param limit     the limit for pagination
     * @param status    the status of the posts (new, declined, accepted)
     * @param principal the authenticated user principal
     * @return PostGetResponse containing the list of posts for moderation and count
     */
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

    /**
     * Handles setting a post.
     *
     * @param postRequest the PostRequest containing details of the post
     * @param principal   the authenticated user principal
     * @return ErrorsResponse indicating the status of setting the post
     */
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

    /**
     * Handles editing a post.
     *
     * @param id           the ID of the post to edit
     * @param postRequest  the PostRequest containing details of the edited post
     * @param principal    the authenticated user principal
     * @return ErrorsResponse indicating the status of editing the post
     */
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

    /**
     * Helper method to create a PostDto from a Post entity.
     *
     * @param p the Post entity
     * @return PostDto containing details of the post
     */
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

    /**
     * Helper method to set tags for a post.
     *
     * @param listTags the list of tags
     * @param post     the post entity
     */
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

    /**
     * Helper method to get a sorted collection of posts based on mode, offset, and limit.
     *
     * @param mode   the mode for sorting (recent, popular, best, early)
     * @param offset the offset for pagination
     * @param limit  the limit for pagination
     * @return List of sorted posts
     */
    private List<Post> getSortedCollection(String mode, Integer offset, Integer limit) {
        List<Post> postList;

        switch (mode) {
            case "recent":
                postList = (List<Post>) postRepository.findPostOrderByRecent(offset, limit);
                break;
            case "popular":
                postList = (List<Post>) postRepository.findPostOrderByPopular(offset, limit);
                break;
            case "best":
                postList = (List<Post>) postRepository.findPostOrderByBest(offset, limit);
                break;
            case "early":
                postList = (List<Post>) postRepository.findAllPostOrderByEarly(offset, limit);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + mode);
        }

        return postList;
    }

    /**
     * Helper method to get a sorted collection of user's posts based on status, offset, and limit.
     *
     * @param status the status of the posts (inactive, pending, declined, published)
     * @param offset the offset for pagination
     * @param limit  the limit for pagination
     * @param userId the ID of the user
     * @return List of sorted user's posts
     */
    private List<Post> getMySortedCollection(String status, Integer offset,
                                             Integer limit, int userId) {
        List<Post> postList;


        switch (status) {
            case "inactive":
                postList = (List<Post>) postRepository.findMyPostOrderByInactive(userId, offset, limit);
                break;
            case "pending":
                postList = (List<Post>) postRepository.findMyPostOrderByPending(userId, offset, limit);
                break;
            case "declined":
                postList = (List<Post>) postRepository.findMyPostOrderByDeclined(userId, offset, limit);
                break;
            case "published":
                postList = (List<Post>) postRepository.findMyPostOrderByPublished(userId, offset, limit);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + status);
        }

        return postList;
    }

    /**
     * Helper method to get a sorted collection of moderator's posts based on status, offset, and limit.
     *
     * @param status    the status of the posts (new, declined, accepted)
     * @param offset    the offset for pagination
     * @param limit     the limit for pagination
     * @param principal the authenticated user principal
     * @return List of sorted moderator's posts
     */
    private List<Post> getModeratorSortedCollection(String status, Integer offset, Integer limit, Principal principal) {
        List<Post> postList;
        int adminId = Integer.parseInt(principal.getName());

        switch (status) {
            case "new":
                postList = (List<Post>) postRepository.findNewPostByModerate(offset, limit);
                break;
            case "declined":
                postList = (List<Post>) postRepository.findDeclinedPostByModerate(adminId, offset, limit);
                break;
            case "accepted":
                postList = (List<Post>) postRepository.findAcceptedPostByModerate(adminId, offset, limit);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + status);
        }

        return postList;
    }
}
