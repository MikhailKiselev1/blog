package main.controller;

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

@RestController
@RequestMapping("/api/post")
public class ApiPostController {

    private final PostService postService;
    private final LikeService likeService;

    @Autowired
    public ApiPostController(PostService postsService, LikeService likeService) {
        this.postService = postsService;
        this.likeService = likeService;
    }

    @GetMapping
    public PostGetResponse post(@RequestParam(defaultValue = "0", required = false) Integer offset,
                                                @RequestParam(defaultValue = "10", required = false) Integer limit,
                                                @RequestParam(defaultValue = "recent", required = false) String mode) {
        return postService.getPost(offset, limit, mode);
    }

    @GetMapping("/search")
    public PostGetResponse postSearch(@RequestParam(defaultValue = "0", required = false) Integer offset, @RequestParam(defaultValue = "10", required = false) Integer limit,
                                                      @RequestParam(defaultValue = "recent", required = false) String mode, @RequestParam String query) {
        return postService.getPostSearch(offset, limit, query);
    }

    @GetMapping("/byDate")
    public PostGetResponse postByDate(@RequestParam(defaultValue = "0", required = false) Integer offset, @RequestParam(defaultValue = "10", required = false) Integer limit,
                                                      @RequestParam(defaultValue = "recent", required = false) String mode, @RequestParam String date) {
        return postService.getPostByDate(offset, limit, mode, date);
    }

    @GetMapping("/byTag")
    public PostGetResponse postByTag(@RequestParam(defaultValue = "0", required = false) Integer offset, @RequestParam(defaultValue = "10", required = false) Integer limit,
                                                     @RequestParam(defaultValue = "recent", required = false) String mode, @RequestParam String tag) {
        return postService.getPostByTag(offset, limit, mode, tag);
    }

    @PreAuthorize("hasAuthority('user:write')")
    @GetMapping("/{id}")
    public PostIdResponse postById(@PathVariable long id, Principal principal) {
        return postService.getPostById((int) id, principal);
    }

    @PreAuthorize("hasAuthority('user:write')")
    @GetMapping("/my")
    public PostGetResponse postMy(@RequestParam(defaultValue = "0", required = false) Integer offset,
                                                  @RequestParam(defaultValue = "10", required = false) Integer limit,
                                                  @RequestParam(defaultValue = "inactive", required = false) String status,
                                                    Principal principal) {
        return postService.getMyPosts(offset, limit, status, principal);
    }

    @PreAuthorize("hasAuthority('user:moderate')")
    @GetMapping("/moderation")
    public PostGetResponse postModeration(@RequestParam(defaultValue = "0", required = false) Integer offset,
                                                          @RequestParam(defaultValue = "10", required = false) Integer limit,
                                                          @RequestParam(defaultValue = "new", required = false) String status,
                                                          Principal principal) {
        return postService.getPostModeration(offset, limit, status, principal);
    }

    @PreAuthorize("hasAuthority('user:write')")
    @PostMapping
    public ErrorsResponse addPost(@RequestBody PostRequest postRequest, Principal principal) {
        postRequest.getTags().forEach(System.out::println);
        return postService.setPost(postRequest, principal);
    }

    @PreAuthorize("hasAuthority('user:write')")
    @PutMapping("/{id}")
    public ErrorsResponse editPost(@PathVariable int id,
                                                   @RequestBody PostRequest postRequest, Principal principal) {
        return postService.editPost(id, postRequest, principal);
    }

    @PreAuthorize("hasAuthority('user:write')")
    @PostMapping("/like")
    public ErrorsResponse likePost(@RequestBody LikeRequest request, Principal principal) {
        return likeService.setLike(request, principal, 1);
    }

    @PreAuthorize("hasAuthority('user:write')")
    @PostMapping("/dislike")
    public ErrorsResponse dislikePost(@RequestBody LikeRequest request, Principal principal) {
        return likeService.setLike(request, principal, -1);
    }

}
