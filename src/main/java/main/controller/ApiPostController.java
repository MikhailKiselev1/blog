package main.controller;

import main.api.request.PostRequest;
import main.api.response.ErrorsResponse;
import main.api.response.PostIdResponse;
import main.api.response.PostGetResponse;
import main.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/post")
public class ApiPostController {

    private final PostService postService;

    @Autowired
    public ApiPostController(PostService postsService) {
        this.postService = postsService;
           }

    @GetMapping
    public ResponseEntity<PostGetResponse> post(@RequestParam(defaultValue = "0", required = false) Integer offset,
                                                @RequestParam(defaultValue = "10", required = false) Integer limit,
                                                @RequestParam(defaultValue = "recent", required = false) String mode) {
        return ResponseEntity.ok(postService.getPost(offset, limit, mode));
    }

    @GetMapping("/search")
    public ResponseEntity<PostGetResponse> postSearch(@RequestParam(defaultValue = "0", required = false) Integer offset, @RequestParam(defaultValue = "10", required = false) Integer limit,
                                                      @RequestParam(defaultValue = "recent", required = false) String mode, @RequestParam String query) {
        return ResponseEntity.ok(postService.getPostSearch(offset, limit, mode, query));
    }

    @GetMapping("/byDate")
    public ResponseEntity<PostGetResponse> postByDate(@RequestParam(defaultValue = "0", required = false) Integer offset, @RequestParam(defaultValue = "10", required = false) Integer limit,
                                                      @RequestParam(defaultValue = "recent", required = false) String mode, @RequestParam String date) {
        return ResponseEntity.ok(postService.getPostByDate(offset, limit, mode, date));
    }

    @GetMapping("/byTag")
    public ResponseEntity<PostGetResponse> postByTag(@RequestParam(defaultValue = "0", required = false) Integer offset, @RequestParam(defaultValue = "10", required = false) Integer limit,
                                                     @RequestParam(defaultValue = "recent", required = false) String mode, @RequestParam String tag) {
        return ResponseEntity.ok(postService.getPostByTag(offset, limit, mode, tag));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostIdResponse> postById(@PathVariable long id) {
        System.out.println(id);
        return ResponseEntity.ok(postService.getPostById((int) id));
    }

    @PreAuthorize("hasAuthority('user:write')")
    @GetMapping("/my")
    public ResponseEntity<PostGetResponse> postMy(@RequestParam(defaultValue = "0", required = false) Integer offset,
                                                  @RequestParam(defaultValue = "10", required = false) Integer limit,
                                                  @RequestParam(defaultValue = "inactive", required = false) String status) {
        return ResponseEntity.ok(postService.getMyPosts(offset, limit, status));
    }

    @PreAuthorize("hasAuthority('user:moderate')")
    @GetMapping("/moderation")
    public ResponseEntity<PostGetResponse> postModeration(@RequestParam(defaultValue = "0", required = false) Integer offset,
                                                          @RequestParam(defaultValue = "10", required = false) Integer limit,
                                                          @RequestParam(defaultValue = "new", required = false) String status,
                                                          Principal principal) {
        return ResponseEntity.ok(postService.getPostModeration(offset, limit, status, principal));
    }

    @PreAuthorize("hasAuthority('user:write')")
    @PostMapping
    public ResponseEntity<ErrorsResponse> addPost(@RequestBody PostRequest postRequest, Principal principal) {
        return ResponseEntity.ok(postService.postPost(postRequest, principal));
    }

    @PreAuthorize("hasAuthority('user:write')")
    @PutMapping("/{id}")
    public ResponseEntity<ErrorsResponse> editPost (@PathVariable int id,
                                                     @RequestBody PostRequest postRequest, Principal principal) {
        return ResponseEntity.ok(postService.editPost(id, postRequest, principal));
    }
}
