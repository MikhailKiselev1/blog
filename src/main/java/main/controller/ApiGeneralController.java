package main.controller;

import main.api.response.*;
import main.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ApiGeneralController {

    private final InitResponse initResponse;
    private final SettingResponse globalSettingResponse;
    private final CheckService checkService;
    private final PostService postService;
    private final TagService tagService;
    private final CalendarService calendarService;

    @Autowired
    public ApiGeneralController(InitResponse initResponse, SettingResponse globalSettingResponse,
                                CheckService checkService, PostService postsService, TagService tagService, CalendarService calendarService) {
        this.initResponse = initResponse;
        this.globalSettingResponse = globalSettingResponse;
        this.checkService = checkService;
        this.postService = postsService;
        this.tagService = tagService;
        this.calendarService = calendarService;
    }

    @GetMapping("/init")
    public InitResponse init() {
        return initResponse;
    }


    @GetMapping("/settings")
    public SettingResponse setting() {
        return globalSettingResponse;
    }

    @PreAuthorize("hasAuthority('user:write')")
    @GetMapping("/auth/check")
    public CheckResponse check() {
        return checkService.getCheck();
    }

    @PreAuthorize("hasAuthority('user:write')")
    @GetMapping("/post")
    public ResponseEntity<PostsResponse> post(@RequestParam(defaultValue = "0", required = false) Integer offset, @RequestParam(defaultValue = "10", required = false)Integer limit,
                                              @RequestParam(defaultValue = "recent", required = false) String mode) {
        return new ResponseEntity<>(postService.getPost(offset,limit,mode), HttpStatus.OK);
    }

    // добавить @RequestParam(default value = 0) вместо Pathvariable
    @GetMapping("/post/search")
    public ResponseEntity<PostsResponse> postSearch(@RequestParam(defaultValue = "0", required = false) Integer offset, @RequestParam(defaultValue = "10", required = false)Integer limit,
                                                    @RequestParam(defaultValue = "recent", required = false) String mode, @RequestParam String query) {
        return new ResponseEntity<>(postService.getPostSearch(offset,limit,mode, query), HttpStatus.OK);
    }

    @GetMapping("/post/byDate")
    public ResponseEntity<PostsResponse> postByDate(@RequestParam(defaultValue = "0", required = false) Integer offset, @RequestParam(defaultValue = "10", required = false)Integer limit,
                                                    @RequestParam(defaultValue = "recent", required = false) String mode, @RequestParam String date) {
        return new ResponseEntity<>(postService.getPostByDate(offset,limit, mode, date), HttpStatus.OK);
    }

    @GetMapping("/post/byTag")
    public ResponseEntity<PostsResponse> postByTag(@RequestParam(defaultValue = "0", required = false) Integer offset, @RequestParam(defaultValue = "10", required = false)Integer limit,
                                                   @RequestParam(defaultValue = "recent", required = false) String mode, @RequestParam String tag) {
        return new ResponseEntity<>(postService.getPostByTag(offset, limit, mode, tag), HttpStatus.OK);
    }

    @GetMapping("/post/{ID}")
    public ResponseEntity<PostIdResponse> postById(@RequestParam Integer id) {
        return new ResponseEntity<>(postService.getPostById(id), HttpStatus.NOT_FOUND);
    }

    @GetMapping("/tag")
    public TagsResponse tag(@PathVariable @Nullable String query) {
        return tagService.getTag(query);
    }

    @GetMapping("/calendar")
    public CalendarResponse calendar(@RequestParam String year) {
        return calendarService.getCalendar(year);
    }

}
