package main.controller;

import lombok.RequiredArgsConstructor;
import main.api.request.LikeRequest;
import main.api.request.PostRequest;
import main.api.response.ErrorsResponse;
import main.api.response.PostIdResponse;
import main.api.response.PostGetResponse;
import main.service.LikeService;
import main.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

/**
 * Controller class handling API endpoints related to posts, including retrieval, modification, and user interactions.
 */
@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class ApiPostController {

    private final PostService postService;
    private final LikeService likeService;

    /**
     * Retrieves a list of posts based on specified parameters.
     *
     * @param offset the offset for pagination
     * @param limit  the maximum number of posts to retrieve
     * @param mode   the sorting mode for posts
     * @return PostGetResponse containing the list of posts
     */
    @GetMapping
    public PostGetResponse post(@RequestParam(defaultValue = "0", required = false) Integer offset,
                                                @RequestParam(defaultValue = "10", required = false) Integer limit,
                                                @RequestParam(defaultValue = "recent", required = false) String mode) {
        return postService.getPost(offset, limit, mode);
    }

    /**
     * Searches for posts based on a query.
     *
     * @param offset the offset for pagination
     * @param limit  the maximum number of posts to retrieve
     * @param mode   the sorting mode for posts
     * @param query  the search query
     * @return PostGetResponse containing the list of posts matching the search query
     */
    @GetMapping("/search")
    public PostGetResponse postSearch(@RequestParam(defaultValue = "0", required = false) Integer offset, @RequestParam(defaultValue = "10", required = false) Integer limit,
                                                      @RequestParam(defaultValue = "recent", required = false) String mode, @RequestParam String query) {
        return postService.getPostSearch(offset, limit, query);
    }

    /**
     * Retrieves posts based on a specified date.
     *
     * @param offset the offset for pagination
     * @param limit  the maximum number of posts to retrieve
     * @param mode   the sorting mode for posts
     * @param date   the date to filter posts
     * @return PostGetResponse containing the list of posts for the specified date
     */
    @GetMapping("/byDate")
    public PostGetResponse postByDate(@RequestParam(defaultValue = "0", required = false) Integer offset, @RequestParam(defaultValue = "10", required = false) Integer limit,
                                                      @RequestParam(defaultValue = "recent", required = false) String mode, @RequestParam String date) {
        return postService.getPostByDate(offset, limit, mode, date);
    }

    /**
     * Retrieves posts based on a specified tag.
     *
     * @param offset the offset for pagination
     * @param limit  the maximum number of posts to retrieve
     * @param mode   the sorting mode for posts
     * @param tag    the tag to filter posts
     * @return PostGetResponse containing the list of posts for the specified tag
     */
    @GetMapping("/byTag")
    public PostGetResponse postByTag(@RequestParam(defaultValue = "0", required = false) Integer offset, @RequestParam(defaultValue = "10", required = false) Integer limit,
                                                     @RequestParam(defaultValue = "recent", required = false) String mode, @RequestParam String tag) {
        return postService.getPostByTag(offset, limit, mode, tag);
    }

    /**
     * Retrieves information about a specific post.
     *
     * @param id        the ID of the post
     * @param principal the authenticated user principal
     * @return PostIdResponse containing details of the specified post
     */
    @PreAuthorize("hasAuthority('user:write')")
    @GetMapping("/{id}")
    public PostIdResponse postById(@PathVariable long id, Principal principal) {
        return postService.getPostById((int) id, principal);
    }

    /**
     * Retrieves posts authored by the current user.
     *
     * @param offset   the offset for pagination
     * @param limit    the maximum number of posts to retrieve
     * @param status   the status of the posts to filter (e.g., "inactive")
     * @param principal the authenticated user principal
     * @return PostGetResponse containing the list of posts authored by the current user
     */
    @PreAuthorize("hasAuthority('user:write')")
    @GetMapping("/my")
    public PostGetResponse postMy(@RequestParam(defaultValue = "0", required = false) Integer offset,
                                                  @RequestParam(defaultValue = "10", required = false) Integer limit,
                                                  @RequestParam(defaultValue = "inactive", required = false) String status,
                                                    Principal principal) {
        return postService.getMyPosts(offset, limit, status, principal);
    }

    /**
     * Retrieves posts for moderation.
     *
     * @param offset    the offset for pagination
     * @param limit     the maximum number of posts to retrieve
     * @param status    the status of the posts to filter (e.g., "new")
     * @param principal the authenticated user principal
     * @return PostGetResponse containing the list of posts for moderation
     */
    @PreAuthorize("hasAuthority('user:moderate')")
    @GetMapping("/moderation")
    public PostGetResponse postModeration(@RequestParam(defaultValue = "0", required = false) Integer offset,
                                                          @RequestParam(defaultValue = "10", required = false) Integer limit,
                                                          @RequestParam(defaultValue = "new", required = false) String status,
                                                          Principal principal) {
        return postService.getPostModeration(offset, limit, status, principal);
    }

    /**
     * Adds a new post.
     *
     * @param postRequest the PostRequest containing the details of the new post
     * @param principal   the authenticated user principal
     * @return ErrorsResponse indicating the status of the post addition
     */
    @PreAuthorize("hasAuthority('user:write')")
    @PostMapping
    public ErrorsResponse addPost(@RequestBody PostRequest postRequest, Principal principal) {
        postRequest.getTags().forEach(System.out::println);
        return postService.setPost(postRequest, principal);
    }

    /**
     * Edits an existing post.
     *
     * @param id          the ID of the post to be edited
     * @param postRequest the PostRequest containing the updated details of the post
     * @param principal   the authenticated user principal
     * @return ErrorsResponse indicating the status of the post editing
     */
    @PreAuthorize("hasAuthority('user:write')")
    @PutMapping("/{id}")
    public ErrorsResponse editPost(@PathVariable int id,
                                                   @RequestBody PostRequest postRequest, Principal principal) {
        return postService.editPost(id, postRequest, principal);
    }

    /**
     * Likes a specific post.
     *
     * @param request   the LikeRequest containing information about the liked post
     * @param principal the authenticated user principal
     * @return ErrorsResponse indicating the status of the like action
     */
    @PreAuthorize("hasAuthority('user:write')")
    @PostMapping("/like")
    public ErrorsResponse likePost(@RequestBody LikeRequest request, Principal principal) {
        return likeService.setLike(request, principal, 1);
    }

    /**
     * Dislikes a specific post.
     *
     * @param request   the LikeRequest containing information about the disliked post
     * @param principal the authenticated user principal
     * @return ErrorsResponse indicating the status of the dislike action
     */
    @PreAuthorize("hasAuthority('user:write')")
    @PostMapping("/dislike")
    public ErrorsResponse dislikePost(@RequestBody LikeRequest request, Principal principal) {
        return likeService.setLike(request, principal, -1);
    }

}
